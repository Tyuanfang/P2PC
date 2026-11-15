import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
}

modSettings {
    clientOptions {
        fov = 90
        guiScale = 3
        narrator = false
        darkBackground = true
        musicVolume = 0.0
    }

    variableReplacements =
        mapOf(
            "minecraftVersionVirtual" to stonecutter.current.version,
            "forgeLoaderVersion" to
                if (project.mod.isForge) {
                    project.mod
                        .prop("forge_version")
                        .substringAfter("-")
                        .substringBefore(".")
                } else {
                    ""
                },
        )
}

publishMods {
    modrinth {
        if (mod.isFabric) requires("fabric-api")
        environment.set(CLIENT_OR_SERVER_PREFERS_BOTH)
    }

    curseforge {
        client = true
        server = true
        if (mod.isFabric) requires("fabric-api")
    }
}

// ---------------------------------------------------------------------------
// P2PC 专属构建配置
// 注意：这个 build 脚本会被应用到每一个版本-加载器子项目（Stonecutter 的
// includeBuild 模型），所以这里的配置直接作用于「当前项目」，不要再套 subprojects {}。
// ---------------------------------------------------------------------------

plugins.withId("java") {
    // Stonecraft 按 MC 版本给的是 Java 21（1.21.1），但生成的 UniFFI 绑定用的是
    // Project Panama FFM，Java 22 才在正式版里可用，所以 1.21.x 统一抬到 22；
    // 26.x 的原版和加载器构件本身就是按 Java 25 编译的，只能跟着用 25。
    val p2pcJavaTarget = if (stonecutter.current.version.startsWith("26.")) 25 else 22

    // maven.neoforged.net 在国内常被 DNS 污染（解析到 199.59.x）直接超时，
    // 官方 CDN 镜像 neoforged.forgecdn.net 提供同路径构件，这里统一改写仓库地址。
    // 必须在配置阶段就注册（configureEach 对之后加入的仓库也生效）：加载器插件会在
    // 自己的 afterEvaluate 里解析 neoForge 依赖，放到我们的 afterEvaluate 里就太晚了。
    repositories.withType<MavenArtifactRepository>().configureEach {
        val base = url.toString()
        if (base.startsWith("https://maven.neoforged.net")) {
            setUrl(
                "https://neoforged.forgecdn.net" +
                    base.removePrefix("https://maven.neoforged.net"),
            )
        }
    }

    afterEvaluate {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.toVersion(p2pcJavaTarget)
            targetCompatibility = JavaVersion.toVersion(p2pcJavaTarget)
            // 1.21.x 的构建插件默认用 Java 21 工具链，但 FFM 需要 22+：抬到 25。
            // forge 项目在配置阶段可能已经把这个属性 finalize（"languageVersion is final"），
            // 那种情况下只保留 release 目标，编译用 Gradle 自身的 JDK。
            try {
                toolchain.languageVersion.set(JavaLanguageVersion.of(25))
            } catch (error: IllegalStateException) {
                logger.warn("[p2pc] toolchain 已被加载器插件固定，改用 release 目标继续：{}", error.message)
            }
        }
        tasks.withType<JavaCompile>().configureEach {
            options.release.set(p2pcJavaTarget)
        }
    }

    // Java 22/23 上 FFM 受限方法需要显式放开；24+ 只是警告，带上无害。
    tasks.withType<JavaExec>().configureEach {
        jvmArgs("--enable-native-access=ALL-UNNAMED")
    }

    // 本项目没有单元测试。Stonecraft 默认会往 test 源集里塞 junit-jupiter 和
    // fabric-loader-junit，于是每次 build 都会为了一个空测试源集去解析 Fabric 仓库
    // （maven.fabricmc.net）。网络抖动/被墙时这个仓库会被 Gradle 标记为
    // "Repository Fabric is disabled due to earlier error"，整个 test 配置跟着失败。
    // 既然没有测试，就把这些依赖清空，test 任务会保持 NO-SOURCE。
    configurations.matching { it.name.startsWith("test") }.configureEach {
        withDependencies { clear() }
    }
    tasks.withType<Test>().configureEach { enabled = false }

    // sculk 原生库的平台标识与文件名（与 SculkClient.platformToken() 对应）
    val nativePlatforms =
        mapOf(
            "windows-x86_64" to "sculk_ffi.dll",
            "linux-x86_64" to "libsculk_ffi.so",
            "linux-aarch64" to "libsculk_ffi.so",
            "macos-x86_64" to "libsculk_ffi.dylib",
            "macos-aarch64" to "libsculk_ffi.dylib",
        )
    val preparedNatives = rootProject.file("natives")
    val localNativeTarget = rootProject.file("sculk/sculk-main/target/release")

    // 原生库统一走 processResources：jar 与 dev 运行（runClient 读 build/resources/main）
    // 用同一份资源，避免「jar 里有、dev 找不到」。
    tasks.withType<ProcessResources>().configureEach {
        from(preparedNatives) { into("natives") }

        val missingPlatforms = mutableListOf<String>()
        nativePlatforms.forEach { (platform, libraryName) ->
            val staged = preparedNatives.resolve("$platform/$libraryName")
            val local = localNativeTarget.resolve(libraryName)
            if (!staged.exists()) {
                if (local.exists()) {
                    from(local) { into("natives/$platform") }
                } else {
                    missingPlatforms.add(platform)
                }
            }
        }
        doFirst {
            if (missingPlatforms.isNotEmpty()) {
                println("[p2pc] 缺少这些平台的原生库（可接受：CI 会为其他平台补上）：$missingPlatforms")
            }
        }
    }
}
