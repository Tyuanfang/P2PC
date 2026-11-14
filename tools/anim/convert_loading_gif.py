"""
把 textures/gui/loading.gif 转成 Minecraft 能当贴图用的横向 sprite sheet。

用法（Windows 上用带 Pillow 的 Python）：
    python tools/anim/convert_loading_gif.py
"""

from PIL import Image
import os

ROOT = r"E:\mods_for_mc\p2pc\p2pc-template-1.21.1"
SRC = os.path.join(ROOT, r"src\main\resources\assets\p2pc\textures\gui\loading.gif")
DST = os.path.join(ROOT, r"src\main\resources\assets\p2pc\textures\gui\loading_frames.png")


def main() -> None:
    im = Image.open(SRC)
    frames = getattr(im, "n_frames", 1)
    width, height = im.size

    sheet = Image.new("RGBA", (width * frames, height), (0, 0, 0, 0))
    for index in range(frames):
        im.seek(index)
        sheet.paste(im.convert("RGBA"), (index * width, 0))

    sheet.save(DST)
    duration = im.info.get("duration")
    print(f"{frames} frames, {width}x{height} each, {duration} ms/frame -> {DST}")


if __name__ == "__main__":
    main()
