package a90pn.p2pc.compat;

import net.minecraft.client.gui.components.EditBox;

/**
 * 输入框的输入约束。
 *
 * <p>26.3 起 {@code EditBox#setFilter} 被移除，只剩下用于显示的 {@code addFormatter}，
 * 所以新版本改用 responder 把非数字字符过滤掉。</p>
 */
public final class Fields {
    private Fields() {}

    /** 只允许输入数字，且最多 {@code maxDigits} 位。 */
    public static void digitsOnly(EditBox box, int maxDigits) {
        //? if <26.3 {
        box.setFilter(value -> value.isEmpty() || value.matches("\\d{0," + maxDigits + "}"));
        //?} else {
        /*box.setResponder(value -> {
            String digits = value.replaceAll("[^0-9]", "");
            if (digits.length() > maxDigits) {
                digits = digits.substring(0, maxDigits);
            }
            if (!digits.equals(value)) {
                box.setValue(digits);
            }
        });
        *///?}
    }
}
