pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
    }
}
plugins {
    id("gg.meza.stonecraft") version "1.15.1"
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    shared {
        fun mc(
            version: String,
            vararg loaders: String,
        ) {
            // Make the relevant version directories named "1.20.2-fabric", "1.20.2-forge", etc.
            for (it in loaders) version("$version-$it", version)
        }

        // 目前只做 Fabric 多版本：P2PC 的客户端 UI 深度依赖 Fabric API
        // （ScreenEvents / HudRenderCallback / ClientTickEvents / ClientPlayConnectionEvents），
        // Forge/NeoForge 需要另写一套事件与界面层，等 Fabric 版本稳定后再逐版本补。
        mc("1.21.1", "fabric")



        vcsVersion = "1.21.1-fabric"
    }
    create(rootProject)
}

rootProject.name = "P2PC"
