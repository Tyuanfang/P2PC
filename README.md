# P2PC

> 基于 P2P 打洞的 Minecraft 联机模组——无需公网 IP、无需内网穿透，邀请码一发即可和朋友直连。

## 项目简介

P2PC 的目标是让 Minecraft 联机摆脱对公网服务器和内网穿透工具的依赖。房主在游戏内创建房间后，模组自动完成 NAT 打洞；好友输入邀请码，客户端自动建立 P2P 隧道并连接进房主的世界。整个过程对玩家透明，不需要手动填写 IP、不需要配置端口映射、不需要任何第三方穿透服务。

底层网络能力基于 Rust 生态的 [sculk](https://github.com/KercyDing/sculk)（一个面向 Minecraft 联机的 P2P 隧道核心库，构建在 [iroh](https://github.com/n0-computer/iroh) / QUIC 之上）。模组通过 UniFFI 生成 Java 绑定，以动态库形式加载 Rust 核心，Java 侧只负责游戏内交互、连接触发和状态展示。

## 核心特性

- **一键开房**：房主在游戏内创建房间，生成邀请码，分享给好友即可
- **邀请码加入**：好友输入邀请码，客户端自动完成隧道建立并连接世界
- **直连优先，中继兜底**：优先尝试 NAT 打洞直连；失败时自动回退到中继，保证尽量能连上
- **连接状态可见**：界面展示当前是「直连」还是「中继」，以及 RTT
- **多人支持**：房间支持多名玩家加入，人数上限可配置

## 当前状态

端到端联机链路已跑通，处在「功能可用、界面打磨中」的阶段：

- **Rust 核心 → UniFFI → Java**：`sculk/` 工作区里的 `sculk-ffi` crate 暴露 sculk 的全量公开 API（隧道服务、多服务节点、密钥/令牌/邀请码、持久化、LAN 扫描与广播），`uniffi-bindgen-java` 生成 Panama FFM 绑定，模组侧统一门面为 `a90pn.p2pc.SculkClient`。
- **房主侧**：暂停菜单的「对局域网开放」被换成 P2PC 房间设置页——游戏模式 / 允许作弊 / 端口 / 允许离线玩家 / 启用 PvP / 服务器描述（MOTD）；创建后进入房间页，显示邀请码与在线人数，可复制邀请码、停止房间。
- **加入侧**：输入邀请码后播放下界传送门搭建动画，隧道握手成功后自动连接本地隧道端点进入世界；任何错误则播放 TNT 爆炸动画，展示错误原因并提供返回主界面的按钮。
- **多平台**：`natives/<platform>/` 里带 Windows x86_64 / Linux x86_64 / Linux aarch64 / macOS x86_64 / macOS aarch64 五个平台的原生库，客户端按系统自动选择加载。
- **收尾**：离开服务器或世界时自动关闭隧道；创建/加入两端的设置持久化在 `config/p2pc-client.properties`。

接下来重点：多人场景下的中继表现测试、中继服务器可配置、更多 Minecraft 版本。

## 技术栈

| 层 | 技术 |
|---|---|
| 模组平台 | Fabric（Minecraft Java Edition） |
| 模组语言 | Java |
| P2P 核心 | Rust + sculk + iroh（QUIC） |
| 绑定层 | UniFFI（生成 Java 绑定，Project Panama FFM） |
| 构建 | Gradle + Stonecraft / Stonecutter（多版本骨架）+ Cargo（Rust 侧） |

## 构建

要求：**JDK 25**（生成的 FFM 绑定需要 Java 22+ 的字节码，构建脚本统一 `release = 22`）。

```shell
# 当前激活版本（1.21.1-fabric）
./gradlew buildActive

# 所有已配置的版本-加载器对，产物收集到 build/libs
./gradlew buildAndCollect

# 切到某个版本-加载器（提交前记得 Reset active project）
./gradlew "Set active project to 1.21.1-fabric"
./gradlew "Reset active project"
```

原生库打包在 `processResources` 里完成：把 `natives/<platform>/` 与本机 `sculk/sculk-main/target/release/` 的产物写进 jar 的 `natives/<platform>/`，这样 jar 运行与 dev 运行（`runClient` 读 `build/resources/main`）用的是同一份资源。

重新生成 Java 绑定（改动 UDL 之后）：

```shell
cd sculk/sculk-main
cargo build -p sculk-ffi --release
uniffi-bindgen-java generate ffi/src/sculk_ffi.udl -o ../../src/main/java -c ffi/bindgen-global.toml --metadata-no-deps
```

## 多版本说明

项目骨架用 [Stonecraft](https://stonecraft.meza.gg) + Stonecutter，可同时构建 Fabric / Forge / NeoForge 多个 Minecraft 版本。**当前只启用了 `1.21.1-fabric`**（`settings.gradle.kts` 里其余版本-加载器组合先注释保留）：26.x 的 API 迁移（`ResourceLocation` → `Identifier`、`Minecraft.setScreen` 移除、事件与 UI 层重写）尚未开始。

> 注意：Stonecutter 的 `/*? if ... {*/ ... /*?}*/` 注释是预处理指令，不能当死代码删掉。

## 开发者

1. A90PN：构思，主要代码编写者
2. ZhgnNo：提供部分 UI
3. DeepSeek：协助编写了你能看见的 99% 中文（Release、Package、Actions、Commit、注释、Wiki）

## 致谢

- [sculk](https://github.com/KercyDing/sculk)：Rust 侧的 P2P 隧道核心
- [iroh](https://github.com/n0-computer/iroh)：QUIC / 打洞 / 中继基础设施
- [UniFFI](https://mozilla.github.io/uniffi-rs/) 与 [uniffi-bindgen-java](https://github.com/IronCoreLabs/uniffi-bindgen-java)：Rust ↔ Java 绑定
