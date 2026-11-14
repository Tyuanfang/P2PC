
# P2PC-Mod

> 基于 P2P 打洞的 Minecraft 联机模组——无需公网 IP、无需内网穿透，邀请码一发即可和朋友直连。

## 项目简介

P2PC 是一个 Fabric 模组，目标是让 Minecraft 联机摆脱对公网服务器和内网穿透工具的依赖。房主在游戏内创建房间后，模组自动完成 NAT 打洞；好友输入邀请码，客户端自动建立 P2P 隧道并连接进房主的世界。整个过程对玩家透明，不需要手动填写 IP、不需要配置端口映射、不需要任何第三方穿透服务。

底层网络能力基于 Rust 生态的 [sculk](https://github.com/KercyDing/sculk)（一个面向 Minecraft 联机的 P2P 隧道核心库，构建在 [iroh](https://github.com/n0-computer/iroh) / QUIC 之上）。模组通过 UniFFI 生成 Java 绑定，以动态库形式加载 Rust 核心，Java 侧只负责游戏内交互、连接触发和状态展示。

## 核心特性

- **一键开房**：房主在游戏内创建房间，生成邀请码，分享给好友即可
- **邀请码加入**：好友输入邀请码，客户端自动完成隧道建立并连接世界
- **直连优先，中继兜底**：优先尝试 NAT 打洞直连；失败时自动回退到中继，保证尽量能连上
- **连接状态可见**：界面展示当前是「直连」还是「中继」，以及 RTT
- **房主审核**：新玩家加入时需房主批准，未批准玩家被拦截在进入世界之前
- **多人支持**：房间支持多名玩家加入，人数上限可配置

## 工作原理（简述）

1. 房主创建房间，Rust 核心启动监听，生成邀请码。
2. 好友输入邀请码，双方通过信令交换地址信息并尝试 NAT 打洞。
3. 打洞成功则建立直连；失败则自动走中继。
4. 隧道建立后，加入者客户端拿到本地端点地址，模组自动发起 MC 连接。
5. 房主侧在玩家进入世界前拦截未审批玩家，推送审批请求给房主。

## 技术栈

| 层 | 技术 |
|---|---|
| 模组平台 | Fabric（Minecraft Java Edition） |
| 模组语言 | Java |
| P2P 核心 | Rust + sculk + iroh（QUIC） |
| 绑定层 | UniFFI（生成 Java 绑定） |
| 构建 | Gradle（Java 侧）+ Cargo（Rust 侧） |

## 开发者
1. A90PN：构思，主要代码编写者
2. ZhgnNo：提供部分ui
3. DeepSeek：协助编写了你能看见的99%中文（Release，Package，Actions，Commit，注释，Wiki）
## 当前状态

端到端联机链路已经跑通，处在「功能可用、界面打磨中」的阶段：

- **Rust 核心 → UniFFI → Java**：`sculk-ffi` crate 暴露 sculk 的全量公开 API（隧道服务、多服务节点、密钥/令牌/邀请码、持久化、LAN 扫描与广播），`uniffi-bindgen-java` 生成 Panama FFM 绑定，模组侧统一门面为 `a90pn.p2pc.SculkClient`。
- **房主侧**：暂停菜单的「对局域网开放」被换成 P2PC 房间设置页——游戏模式 / 允许作弊 / 端口 / 允许离线玩家 / 启用 PvP / 服务器描述（MOTD）；创建后进入房间页，显示邀请码与在线人数，可复制邀请码、停止房间。
- **加入侧**：输入邀请码后播放下界传送门搭建动画，隧道握手成功（收到「已连接」）后自动连接 `127.0.0.1:25565` 进入世界；任何错误则播放 TNT 爆炸动画，展示错误原因并提供返回主界面的按钮。
- **多平台**：CI 用矩阵分别构建 Windows x86_64 / Linux x86_64 / Linux aarch64 / macOS x86_64 / macOS aarch64 原生库，合并进同一个模组 jar（`natives/<platform>/`），客户端按系统自动选择加载。
- **收尾**：离开服务器或世界时自动关闭隧道；创建/加入两端的设置持久化在 `config/p2pc-client.properties`。

接下来重点：房主审批 UI、多人场景下的中继表现测试、中继服务器可配置。

## 路线图

- [x] Rust 核心最小可用：开房 / 加入 / 关闭 + 事件回调
- [x] UniFFI Java 绑定生成与模组内加载
- [x] 房间创建与加入流程跑通
- [x] 加入者自动连接 MC 世界
- [x] 直连/中继状态展示
- [ ] 房主审批功能
- [ ] 多人场景测试与中继配置
- [x] 多平台（Windows / Linux / macOS）native 库打包

## 致谢

- [sculk](https://github.com/KercyDing/sculk)：P2P 隧道核心
- [iroh](https://github.com/n0-computer/iroh)：QUIC / 打洞 / 中继底层
- [Fabric](https://fabricmc.net/)：模组加载器与 API


