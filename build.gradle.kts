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

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}

	// 把 Rust 侧构建出的 sculk_ffi.dll 打包进模组
	val dll = file("sculk/sculk-main/target/release/sculk_ffi.dll")
	if (dll.exists()) {
		from(dll) {
			into("natives/windows-x86_64")
		}
	} else {
		println("[p2pc] 未找到 sculk_ffi.dll（${dll.path}），跳过原生库打包。先构建 Rust 侧：cargo build -p sculk-ffi --release")
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
