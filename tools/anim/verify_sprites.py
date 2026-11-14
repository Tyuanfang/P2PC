"""自检：sprite sheet 尺寸 / alpha / 帧数 / manifest 一致性，并输出放大预览图。"""

import json
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
ANIM = ROOT / "src" / "main" / "resources" / "assets" / "p2pc" / "textures" / "anim"
PREVIEW = Path(__file__).resolve().parent / "preview"

CHECKED_ALPHA = (0, 32, 64, 96, 128, 160, 192, 224, 255)


def main() -> None:
    manifest = json.loads((ANIM / "manifest.json").read_text(encoding="utf-8"))
    PREVIEW.mkdir(exist_ok=True)
    ok = True

    for entry in manifest["sheets"]:
        path = ANIM / entry["file"]
        image = Image.open(path).convert("RGBA")
        expected = (entry["frameWidth"] * entry["frames"], entry["frameHeight"])
        alpha = image.getchannel("A")
        low, high = alpha.getextrema()
        pixels = alpha.get_flattened_data() if hasattr(alpha, "get_flattened_data") else alpha.getdata()
        used = sorted({value for value in pixels if value > 0})
        size_ok = image.size == expected
        counts_ok = len(used) > 0 and all(value in CHECKED_ALPHA for value in used)
        # portal_swirl 是整块铺满的门内表面（没有“背景”），其余三张必须带透明背景
        full_bleed = entry["file"] == "portal_swirl.png"
        background_ok = low == 0 or full_bleed
        print(
            f"{entry['file']:16s} size={image.size} expect={expected} size_ok={size_ok} "
            f"frames={entry['frames']} alpha=[{low},{high}] used_alpha={used[:6]}{'...' if len(used) > 6 else ''} "
            f"quantized={counts_ok} background_ok={background_ok}"
        )
        ok = ok and size_ok and background_ok and high == 255

        # 每帧单独裁出来放大 4 倍，拼成一张便于用眼睛检查的预览
        frame_w, frame_h = entry["frameWidth"], entry["frameHeight"]
        scale = 4 if frame_w * entry["frames"] <= 2048 else 3
        cells = []
        for index in range(entry["frames"]):
            box = (index * frame_w, 0, (index + 1) * frame_w, frame_h)
            cell = image.crop(box).resize((frame_w * scale, frame_h * scale), Image.NEAREST)
            cells.append(cell)
        cell_w, cell_h = frame_w * scale, frame_h * scale
        preview = Image.new("RGBA", (cell_w * min(len(cells), 8), cell_h * ((len(cells) + 7) // 8)), (24, 24, 28, 255))
        for index, cell in enumerate(cells):
            preview.alpha_composite(cell, ((index % 8) * cell_w, (index // 8) * cell_h))
        preview_path = PREVIEW / f"preview_{path.stem}.png"
        preview.save(preview_path)
        print(f"    preview -> {preview_path}")

    print("ALL CHECKS", "PASS" if ok else "FAIL")


if __name__ == "__main__":
    main()
