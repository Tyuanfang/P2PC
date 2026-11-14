package a90pn.p2pc.client.ui;

import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.ffi.ScuLkException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;

/**
 * 加入世界：输入好友分享的邀请码。
 *
 * <p>保持原版界面风格：原版背景、原版输入框与按钮，标题用普通白色字体居中。
 */
public class JoinUI extends Screen {
    private static final int WIDGET_WIDTH = 220;

    private final Screen parent;
    private EditBox codeBox;
    private String errorText;
    private long errorUntil;

    public JoinUI(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - WIDGET_WIDTH / 2;
        int y = this.height / 2 - 18;

        this.codeBox = new EditBox(this.font, x, y, WIDGET_WIDTH, 20, Component.literal("邀请码"));
        this.codeBox.setMaxLength(128);
        this.codeBox.setHint(Component.literal("在这里粘贴邀请码").withStyle(ChatFormatting.GRAY));
        this.addRenderableWidget(this.codeBox);

        this.addRenderableWidget(Button.builder(Component.literal("加入世界"), button -> this.submit())
                .bounds(x, y + 26, WIDGET_WIDTH, 20)
                .build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose())
                .bounds(x, y + 52, WIDGET_WIDTH, 20)
                .build());

        this.setInitialFocus(this.codeBox);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 注意：Screen.render 内部会先调用 renderBackground（标题界面全景 + 模糊层 + 暗色压暗），
        // 再画输入框和按钮。所以我们的文字必须放在 super.render 之后，否则会被背景重新盖住。
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        long now = Util.getMillis();
        guiGraphics.drawCenteredString(this.font, Component.literal("加入世界"), this.width / 2, 36, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.literal("粘贴好友分享的邀请码"),
                this.width / 2, this.height / 2 - 34, 0xA0A0A0);
        if (this.errorText != null) {
            if (now < this.errorUntil) {
                guiGraphics.drawCenteredString(this.font, Component.literal(this.errorText),
                        this.width / 2, this.height / 2 + 84, 0xFF5555);
            } else {
                this.errorText = null;
            }
        }
        guiGraphics.drawCenteredString(this.font, Component.literal("只连接你信任的人分享的邀请码"),
                this.width / 2, this.height - 30, 0x808080);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter / 小键盘 Enter
            this.submit();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void submit() {
        String raw = this.codeBox.getValue();
        String code = raw == null ? "" : raw.replaceAll("\\s+", "");
        if (code.isEmpty()) {
            this.showError("请先输入邀请码", 1200L);
            return;
        }
        try {
            SculkClient.parseJoinUri(code);
        } catch (ScuLkException error) {
            this.showError("邀请码无效：" + error.getMessage(), 1600L);
            return;
        } catch (Throwable ignored) {
            // 原生库问题留给传送门页与错误页处理
        }
        this.minecraft.setScreen(new JoinPortalScreen(code, this.parent));
    }

    private void showError(String text, long durationMs) {
        this.errorText = text;
        this.errorUntil = Util.getMillis() + durationMs;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
