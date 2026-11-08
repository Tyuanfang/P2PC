plugins {
	id("net.fabricmc.fabric-loom-remap")
	`maven-publish`
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("p2pc") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 22
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_22
	targetCompatibility = JavaVersion.VERSION_22
}

// 生成的 UniFFI Java 绑定（Panama FFM，需 Java 22+）。
sourceSets {
	main {
		java {
			srcDir("src/generated/java")
		}
	}
}

// Java 22/23 上 FFM 受限方法需要显式放开；24+ 只是警告，带上无害。
tasks.withType<JavaExec>().configureEach {
	jvmArgs("--enable-native-access=ALL-UNNAMED")
}

// sculk 原生库的平台标识与文件名（与 SculkClient.platformToken() 对应）
val nativePlatforms = mapOf(
	"windows-x86_64" to "sculk_ffi.dll",
	"linux-x86_64" to "libsculk_ffi.so",
	"linux-aarch64" to "libsculk_ffi.so",
	"macos-x86_64" to "libsculk_ffi.dylib",
	"macos-aarch64" to "libsculk_ffi.dylib",
)
// 已准备好的多平台原生库目录（CI 合并各平台产物、或本机手动放置）
val preparedNatives = file("natives")
// 本机 cargo 构建产物（cargo build -p sculk-ffi --release 的输出）
val localNativeTarget = file("sculk/sculk-main/target/release")

// 原生库统一走 processResources：
// - 打包进 jar（jar 默认包含 build/resources/main）
// - 开发环境 runClient 直接读 build/resources/main，两边是同一份资源，
//   不会再出现「jar 里有、dev 运行找不到」的差异。
tasks.processResources {
	// 1) 多平台目录：natives/<平台标识>/<库文件> 原样进 resources
	from(preparedNatives) {
		into("natives")
	}

	// 2) 缺哪个平台就用本机构建产物补上（本地 Windows 开发时零拷贝）
	val missingPlatforms = mutableListOf<String>()
	nativePlatforms.forEach { (platform, libraryName) ->
		val staged = preparedNatives.resolve("$platform/$libraryName")
		val local = localNativeTarget.resolve(libraryName)
		if (!staged.exists()) {
			if (local.exists()) {
				from(local) {
					into("natives/$platform")
				}
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

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
