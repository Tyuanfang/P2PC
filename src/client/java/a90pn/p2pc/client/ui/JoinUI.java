package a90pn.p2pc.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class JoinUI extends Screen {

    final Screen parent;
    private EditBox nameBox;

    protected JoinUI(Component component, Screen parent) {
        super(component);
        this.parent = parent;
    }
    @Override
    protected void init() {
        super.init();

        //bottom
        LinearLayout column = LinearLayout.vertical().spacing(8);
        column.addChild(new StringWidget(Component.literal("请输入邀请码"), this.font));

        this.nameBox = new EditBox(this.font, 0, 0, 200, 20, Component.literal("邀请码"));
        this.nameBox.setMaxLength(32);
        column.addChild(this.nameBox);

        column.addChild(new StringWidget(Component.literal("注意！不要填入不受信任的邀请码"), this.font));

        column.addChild(Button.builder(Component.literal("确定"), this::onDone)
                .bounds(0, 0, 200, 20).build());

        column.arrangeElements();
        column.setX(this.width / 2 - column.getWidth() / 2);
        column.setY(60);

        column.visitWidgets(this::addRenderableWidget);
    }
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        var pose = context.pose();

        //title
        Component title = Component.literal("加入世界");
        pose.pushPose();
        pose.translate(this.width / 2f, 20, 0);
        pose.scale(2.0F, 2.0F, 1.0F);
        context.drawCenteredString(
                this.font,
                title,
                this.width/2,
                20,
                0xFFFFFF
        );
        pose.popPose();

    }
    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().setScreen(parent);
    }
    public void onDone(Button b) {
        String text = this.nameBox.getValue();
        if (text.isBlank()) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("请输入内容！"), false);
            }
        }
    }
}
