package a90pn.p2pc.client;

import java.util.Locale;

/**
 * 界面文案工具：把 Rust / Java 侧抛上来的英文错误翻译成中文。
 *
 * <p>sculk 的错误分类（{@code ScuLkError}）在 Java 侧是 {@code ScuLkException} 的静态子类，
 * 其 message 来自 Rust 的 {@code Display} 实现，全是英文；底层 iroh / 标准库的 I/O 文案
 * 也会原样透传。这里做一次集中翻译，查不到的保持原样（至少不会丢信息）。</p>
 */
public final class UiText {

    /** 帮助群号：任何界面都可以拼 {@link #HELP_LINE} 展示。 */
    public static final String QQ_GROUP = "1136281217";

    /** 一行求助提示（不含换行）。 */
    public static final String HELP_LINE = "遇到问题可加 QQ 群 " + QQ_GROUP + " 寻求帮助";

    private UiText() {
    }

    /**
     * 翻译一条错误信息；查不到时原样返回（空值返回「未知错误」）。
     */
    public static String error(String raw) {
        if (raw == null || raw.isBlank()) {
            return "未知错误";
        }
        String text = raw.trim();
        String translated = exact(text);
        if (translated != null) {
            return translated;
        }
        translated = contains(text.toLowerCase(Locale.ROOT));
        return translated != null ? translated : text;
    }

    /**
     * 把「中文摘要 + 英文细节」拼成一句可读的提示。
     */
    public static String detail(String message, String reason) {
        if (reason == null || reason.isBlank()) {
            return message == null || message.isBlank() ? "未知错误" : message;
        }
        String translatedReason = error(reason);
        if (message == null || message.isBlank()) {
            return translatedReason;
        }
        return message + "（" + translatedReason + "）";
    }

    private static String exact(String text) {
        return switch (text) {
            // sculk 隧道服务错误
            case "a tunnel is already starting, active, or stopping" -> "已有隧道正在运行，请先停止";
            case "no tunnel is running" -> "当前没有正在运行的隧道";
            case "port must be non-zero" -> "端口不能为 0";
            case "max players must be greater than zero" -> "最大玩家数必须大于 0";
            case "I/O error" -> "网络读写失败（I/O 错误）";
            case "invalid join uri" -> "邀请码无效";
            case "invalid endpoint or relay url" -> "连接节点或中继地址无效";
            case "authorization denied" -> "鉴权失败：邀请码可能已失效";
            case "host unreachable" -> "连不上房主（对方可能不在线）";
            case "target unavailable" -> "房主的世界暂时不可用";
            case "local port unavailable" -> "本地端口被占用";
            case "identity unavailable" -> "节点身份不可用";
            case "operation conflict" -> "操作冲突，请稍后再试";
            case "resource limit reached" -> "已达到资源上限";
            case "invalid configuration" -> "配置无效";
            case "internal error" -> "内部错误";
            // 多服务节点错误
            case "target address must be a loopback address" -> "目标地址必须是本机回环地址";
            case "target port must be non-zero" -> "目标端口不能为 0";
            case "a service with this service id is already published" -> "该服务已经在发布中";
            case "service is not published" -> "服务尚未发布";
            case "service token rotation is already in progress" -> "令牌正在轮换，请稍后再试";
            case "token refresh period must be greater than zero" -> "令牌刷新周期必须大于 0";
            case "service session generation space exhausted" -> "会话代数已耗尽，请重建房间";
            case "failed to bind node endpoint" -> "节点端口绑定失败";
            // 事件兜底
            case "unknown tunnel update variant", "unknown tunnel event variant" -> "收到未知的隧道事件";
            default -> null;
        };
    }

    private static String contains(String text) {
        if (text.contains("connection refused")) {
            return "连接被拒绝（对方端口没有在监听）";
        }
        if (text.contains("connection reset")) {
            return "连接被对方重置";
        }
        if (text.contains("timed out") || text.contains("timeout")) {
            return "连接超时";
        }
        if (text.contains("no route to host")) {
            return "无法路由到目标主机";
        }
        if (text.contains("network is unreachable")) {
            return "网络不可达";
        }
        if (text.contains("broken pipe") || text.contains("connection aborted")) {
            return "连接中断";
        }
        if (text.contains("permission denied")) {
            return "权限不足";
        }
        if (text.contains("invalid join uri")) {
            return "邀请码无效";
        }
        if (text.contains("authorization denied") || text.contains("unauthorized")) {
            return "鉴权失败：邀请码可能已失效";
        }
        return null;
    }
}
