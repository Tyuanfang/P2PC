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
    // Project Panama FFM，Java 22 才在正式版里可用，所以这里统一抬到 22。
    afterEvaluate {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_22
            targetCompatibility = JavaVersion.VERSION_22
            toolchain.languageVersion.set(JavaLanguageVersion.of(25))
        }
        tasks.withType<JavaCompile>().configureEach {
            options.release.set(22)
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
