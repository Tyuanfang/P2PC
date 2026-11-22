package a90pn.p2pc.client.ui;

import a90pn.p2pc.Sculk.SculkClient;
import a90pn.p2pc.compat.BaseScreen;
import a90pn.p2pc.compat.Gfx;
import a90pn.p2pc.compat.Time;
import a90pn.p2pc.ffi.JoinUri;
import a90pn.p2pc.ffi.ScuLkException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * 标题界面里的「加入世界」页：粘贴邀请码 → 本地校验 → 进传送门过场页。
 */
public class JoinUI extends BaseScreen {
    private static final int WIDGET_WIDTH = 220;

    /** 静态文案：drawContents 每帧都会跑，别在里面 new Component。 */
    private static final Component TITLE_TEXT = Component.literal("加入世界");
    private static final Component HINT_TEXT = Component.literal("粘贴好友分享的邀请码");
    private static final Component TRUST_TEXT = Component.literal("只连接你信任的人分享的邀请码");

    private final Screen parent;
    private EditBox codeBox;
    private String errorText;
    private Component errorComponent;
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
    protected void drawContents(Object guiGraphics, int mouseX, int mouseY, float partialTick) {
        long now = Time.millis();
        Gfx.centeredText(guiGraphics, this.font, TITLE_TEXT, this.width / 2, 36, 0xFFFFFF);
        Gfx.centeredText(guiGraphics, this.font, HINT_TEXT, this.width / 2, this.height / 2 - 34, 0xA0A0A0);
        if (this.errorText != null) {
            if (now < this.errorUntil) {
                Gfx.centeredText(guiGraphics, this.font, this.errorComponent,
                        this.width / 2, this.height / 2 + 84, 0xFF5555);
            } else {
                this.errorText = null;
                this.errorComponent = null;
            }
        }
        Gfx.centeredText(guiGraphics, this.font, TRUST_TEXT, this.width / 2, this.height - 30, 0x808080);
    }

    //? if <1.21.11 {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter / 小键盘 Enter
            this.submit();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    //?} else {
    /*@Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        if (event.key() == 257 || event.key() == 335) { // Enter / 小键盘 Enter
            this.submit();
            return true;
        }
        return super.keyPressed(event);
    }
    *///?}

    private void submit() {
        String code = this.codeBox.getValue().replaceAll("\\s+", "");
        if (code.isEmpty()) {
            this.showError("请先输入邀请码", 1200L);
            return;
        }
        try (JoinUri parsed = SculkClient.parseJoinUri(code)) {
            // 只做一次本地校验，解析成功就能进传送门页
        } catch (ScuLkException error) {
            this.showError("邀请码无效：" + error.getMessage(), 1600L);
            return;
        } catch (Throwable ignored) {
            // 原生库问题留给传送门页与错误页处理
        }
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            a90pn.p2pc.compat.ScreenSwap.to(minecraft, new JoinPortalScreen(code, this.parent));
        }
    }

    private void showError(String text, long durationMs) {
        this.errorText = text;
        this.errorComponent = Component.literal(text);
        this.errorUntil = Time.millis() + durationMs;
    }

    @Override
    public void onClose() {
        Minecraft minecraft = this.minecraft;
        if (minecraft != null) {
            a90pn.p2pc.compat.ScreenSwap.to(minecraft, this.parent);
        }
    }
}
