"""生成 P2PC 模组 GUI 用的像素动画 sprite sheet。

产物（横向排列、无外边距、透明背景、硬边像素画）：
  portal_build.png  16 帧 x 64x80   黑曜石逐块搭建（底行→右列→顶行→左列）
  portal_swirl.png   8 帧 x 32x48   传送门内部漩涡循环（首尾可无缝衔接）
  tnt_fuse.png      10 帧 x 32x32   TNT 点燃下坠（引线火花 + 拉伸压缩）
  tnt_blast.png     14 帧 x 128x128 白闪 → 火球外扩 → 火花飞散 → 烟与余烬

用法：
  python tools/anim/generate_sprites.py
  python tools/anim/verify_sprites.py     # 自检尺寸 / alpha / 帧数 / manifest

脚本是确定性的：同样的输入永远得到同样的像素，方便以后微调重出图。
"""

from __future__ import annotations

import json
import math
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
OUT_DIR = ROOT / "src" / "main" / "resources" / "assets" / "p2pc" / "textures" / "anim"

# ---------------------------------------------------------------- 调色板（MC 原版色系）

OBSIDIAN = ("#1b0f26", "#2a1a3a", "#3d2a52", "#4a3560")
PORTAL = ("#2a0d5e", "#4a1a8c", "#6f24c9", "#9a4dff", "#c98bff")
TNT_RED = ("#7a1018", "#b81c29", "#db2334", "#e8464f")
TNT_WHITE = ("#c9c9c9", "#f3f3f3")
FIRE = ("#5a1610", "#8e2419", "#e8481f", "#ffc861", "#fff3c4")
SMOKE = ("#3a3a3a", "#5a5a5a", "#7d7d7d")
DUST = ("#3d2a52", "#4a3560", "#6a6270")


def rgba(hex_color: str, alpha: int = 255) -> tuple[int, int, int, int]:
    value = hex_color.lstrip("#")
    return (
        int(value[0:2], 16),
        int(value[2:4], 16),
        int(value[4:6], 16),
        quant_alpha(alpha),
    )


def quant_alpha(alpha: int) -> int:
    """把 alpha 量化成硬档（0/32/64/.../224/255），保持像素画的块状质感。"""
    if alpha >= 250:
        return 255
    if alpha <= 0:
        return 0
    return (alpha // 32) * 32


def noise(x: int, y: int, seed: int) -> float:
    """确定性伪随机：0..1，用来做原版方块贴图那种噪点。"""
    h = (x * 374761393 + y * 668265263 + seed * 2147483647) & 0xFFFFFFFF
    h = ((h ^ (h >> 13)) * 1274126177) & 0xFFFFFFFF
    h = h ^ (h >> 16)
    return (h & 0xFFFF) / 65535.0


def new_frame(width: int, height: int) -> Image.Image:
    return Image.new("RGBA", (width, height), (0, 0, 0, 0))


def put(img: Image.Image, x: int, y: int, color: tuple[int, int, int, int]) -> None:
    if 0 <= x < img.width and 0 <= y < img.height:
        img.putpixel((x, y), color)


def sheet(frames: list[Image.Image], width: int, height: int) -> Image.Image:
    out = Image.new("RGBA", (width * len(frames), height), (0, 0, 0, 0))
    for index, frame in enumerate(frames):
        # 不带 mask 直接覆盖：RGBA→RGBA 保留原始 alpha（带 mask 会把 alpha 再乘一次）
        out.paste(frame, (index * width, 0))
    return out


# ---------------------------------------------------------------- 传送门表面的配色函数

def portal_color(dx: float, dy: float, half_w: float, half_h: float, phase: float, seed: int) -> str:
    """dx/dy 为相对门内区域中心的像素偏移，返回该点颜色（整块区域都填满）。

    三条旋臂随半径扭转：8 帧循环时 phase 前进 2π/8，首尾能无缝接上。
    """
    u = dx / half_w
    v = dy / half_h
    radius = math.hypot(u, v) / 1.4142  # 四角处约为 1.0
    angle = math.atan2(v, u)
    swirl = angle + 3.4 * radius - phase
    arm = math.sin(swirl * 2.0)
    if radius < 0.13:
        color = PORTAL[4]  # 中心亮核
    elif arm > 0.72:
        color = PORTAL[4]
    elif arm > 0.22:
        color = PORTAL[3]
    elif arm > -0.35:
        color = PORTAL[2]
    else:
        color = PORTAL[1]
    if radius > 0.88:
        # 靠近门框的一圈压暗，让门内看起来有纵深
        color = PORTAL[0] if arm < 0.2 else PORTAL[1]
    if noise(int(dx) + 64, int(dy) + 64, seed) > 0.955:
        color = PORTAL[4]
    return color


# ---------------------------------------------------------------- 1. 黑曜石逐块搭建

BUILD_COLS, BUILD_ROWS, CELL = 4, 5, 16
BUILD_W, BUILD_H = BUILD_COLS * CELL, BUILD_ROWS * CELL
BUILD_ORDER = [16, 17, 18, 19, 15, 11, 7, 3, 2, 1, 0, 4, 8, 12]
BUILD_FRAMES = 16
# 门内 2x3 的区域：x 16..47（列 1..2），y 16..63（行 1..3）
INTERIOR_X, INTERIOR_Y = CELL, CELL
INTERIOR_W, INTERIOR_H = 2 * CELL, 3 * CELL


def obsidian_block(img: Image.Image, x: int, y: int, seed: int, squash: bool) -> None:
    """画一块 16x16 黑曜石；squash=True 表示刚落地的压实姿态（矮 1 px、下沉 1 px）。"""
    height = 15 if squash else 16
    top = y + (1 if squash else 0)
    for ly in range(height):
        for lx in range(CELL):
            n = noise(lx, ly, seed)
            if lx == 0 or ly == 0:
                color = OBSIDIAN[3] if (lx == 0 and ly == 0) else OBSIDIAN[2]
            elif lx == 1 and ly <= 2:
                color = OBSIDIAN[2]
            elif n > 0.78:
                color = OBSIDIAN[2]
            elif n > 0.40:
                color = OBSIDIAN[1]
            else:
                color = OBSIDIAN[0]
            put(img, x + lx, top + ly, rgba(color))
    # 底部一条更暗的阴影，让方块落地有重量感
    for lx in range(CELL):
        put(img, x + lx, top + height - 1, rgba(OBSIDIAN[0]))


def build_block(cell: int) -> tuple[int, int]:
    return (cell % BUILD_COLS) * CELL, (cell // BUILD_COLS) * CELL


def portal_build_frames() -> list[Image.Image]:
    frames = []
    done = len(BUILD_ORDER)
    for k in range(BUILD_FRAMES):
        img = new_frame(BUILD_W, BUILD_H)
        for index, cell in enumerate(BUILD_ORDER):
            if index > k:
                break
            bx, by = build_block(cell)
            squash = index == k and k < done - 1
            obsidian_block(img, bx, by, seed=cell * 7 + 1, squash=squash)
            if squash:
                # 落地扬起的灰尘：方块下方 2~3 个亮点，下一帧就散掉
                for i in range(5):
                    dx = bx + 1 + int(noise(i, k, 3) * 14)
                    dy = by + CELL + int(noise(i, k, 4) * 2)
                    put(img, dx, dy, rgba(DUST[i % len(DUST)], 170))
        # 门框合拢后，内部从中心向外亮起来（最后 3 帧）
        if k >= done - 3:
            grow = (k - (done - 3) + 1) / 3.0  # 0.33 / 0.66 / 1.0
            cx = INTERIOR_X + INTERIOR_W / 2.0
            cy = INTERIOR_Y + INTERIOR_H / 2.0
            max_radius = math.hypot(INTERIOR_W / 2.0, INTERIOR_H / 2.0)
            for py in range(INTERIOR_H):
                for px in range(INTERIOR_W):
                    dx = INTERIOR_X + px + 0.5 - cx
                    dy = INTERIOR_Y + py + 0.5 - cy
                    radius = math.hypot(dx, dy) / max_radius
                    if radius > grow * 1.06:
                        continue
                    color = portal_color(dx, dy, INTERIOR_W / 2.0, INTERIOR_H / 2.0, 0.0, 11 + k)
                    put(img, INTERIOR_X + px, INTERIOR_Y + py, rgba(color))
        frames.append(img)
    return frames


# ---------------------------------------------------------------- 2. 门内漩涡循环

SWIRL_W, SWIRL_H = 32, 48
SWIRL_FRAMES = 8


def portal_swirl_frames() -> list[Image.Image]:
    frames = []
    for k in range(SWIRL_FRAMES):
        img = new_frame(SWIRL_W, SWIRL_H)
        phase = k * (2.0 * math.pi / SWIRL_FRAMES)
        for y in range(SWIRL_H):
            for x in range(SWIRL_W):
                dx = x + 0.5 - SWIRL_W / 2.0
                dy = y + 0.5 - SWIRL_H / 2.0
                color = portal_color(dx, dy, SWIRL_W / 2.0, SWIRL_H / 2.0, phase, 40 + k)
                put(img, x, y, rgba(color))
        frames.append(img)
    return frames


# ---------------------------------------------------------------- 3. TNT 点燃下坠

FUSE_W, FUSE_H = 32, 32
FUSE_FRAMES = 10


def draw_tnt(img: Image.Image, x: int, y: int, height: int, fuse: int) -> None:
    """16 宽、height 高的 TNT；fuse 控制火花相位。"""
    width = 16
    for ly in range(height):
        for lx in range(width):
            n = noise(lx, ly, 51)
            if 5 <= ly <= 9 and height >= 14:
                color = TNT_WHITE[1] if n > 0.3 else TNT_WHITE[0]
            elif ly >= height - 2:
                color = TNT_RED[0]
            elif lx == 0 or ly == 0:
                color = TNT_RED[3]
            elif n > 0.80:
                color = TNT_RED[3]
            elif n > 0.45:
                color = TNT_RED[2]
            elif n > 0.18:
                color = TNT_RED[1]
            else:
                color = TNT_RED[0]
            put(img, x + lx, y + ly, rgba(color))
    # 引线：顶部中间一根短芯 + 闪烁火花
    for i in range(3):
        put(img, x + 8, y - 1 - i, rgba("#c9a227"))
    for i in range(4):
        sx = x + 6 + int(noise(i, fuse, 61) * 5)
        sy = y - 5 - int(noise(i, fuse, 62) * 3)
        put(img, sx, sy, rgba(FIRE[4] if i % 2 == 0 else FIRE[3]))
    if fuse % 3 == 0:
        put(img, x + 9, y - 4, rgba(FIRE[3]))
    if fuse % 4 == 1:
        put(img, x + 6, y - 2, rgba("#ffe9a8"))


def tnt_fuse_frames() -> list[Image.Image]:
    frames = []
    for k in range(FUSE_FRAMES):
        img = new_frame(FUSE_W, FUSE_H)
        # 下坠 + 拉伸压缩：偶数帧拉长、奇数帧压扁，幅度 1px，保证对齐像素
        stretch = [1, -1, 1, 1, -1, 0, 1, -1, 1, 0][k]
        height = 16 + stretch
        side = 16 - stretch if stretch > 0 else 16
        x = (FUSE_W - side) // 2
        y = 8 + (16 - height) // 2 + (1 if k in (3, 7) else 0)
        draw_tnt(img, x, y, height, k)
        # 下坠尾迹：脚下 1~2 条斜向的速度线
        if 1 <= k <= 7:
            for i in range(2):
                put(img, x + 2 + i * 5, y + height + 1 + i, rgba(TNT_WHITE[0], 150))
        frames.append(img)
    return frames


# ---------------------------------------------------------------- 4. TNT 爆炸

BLAST_SIZE = 128
BLAST_FRAMES = 14


def blast_frame(k: int) -> Image.Image:
    img = new_frame(BLAST_SIZE, BLAST_SIZE)
    cx = cy = BLAST_SIZE / 2.0

    # 第 0~1 帧：白闪
    if k <= 1:
        flash_radius = 24.0 if k == 0 else 38.0
        for y in range(BLAST_SIZE):
            for x in range(BLAST_SIZE):
                dx = x + 0.5 - cx
                dy = y + 0.5 - cy
                dist = math.hypot(dx, dy)
                edge = flash_radius * (0.90 + 0.18 * noise(x // 3, y // 3, 80 + k))
                if dist > edge:
                    continue
                color = "#ffffff" if dist < edge * 0.62 else FIRE[4]
                put(img, x, y, rgba(color))
        # 白闪边缘炸出的短射线
        for i in range(12):
            angle = i * math.tau / 12 + noise(i, k, 71) * 0.3
            length = int(flash_radius) + 6 + int(noise(i, k, 72) * 10)
            for step in range(6, length):
                px = int(cx + math.cos(angle) * step)
                py = int(cy + math.sin(angle) * step)
                put(img, px, py, rgba(FIRE[4], 200))
        return img

    # 第 2 帧起：火球外扩 + 分层配色 + 破碎消散
    growth = min(1.0, (k - 2) / 6.0)
    # 火球在第 8 帧长到最大，之后靠“半径回收 + 整体变淡”消散，而不是戳成噪点
    settle = max(0.0, (k - 8) / 5.0)  # 0 → 1
    radius = (24.0 + 56.0 * growth) * (1.0 - 0.30 * settle)
    fire_alpha = int(255 - 210 * settle)
    bands = (FIRE[4], FIRE[3], FIRE[2], FIRE[1], FIRE[0])

    for y in range(BLAST_SIZE):
        for x in range(BLAST_SIZE):
            dx = x + 0.5 - cx
            dy = y + 0.5 - cy
            dist = math.hypot(dx, dy)
            edge = radius * (0.88 + 0.24 * noise(x // 3, y // 3, 90 + k))
            if dist > edge:
                continue
            ratio = dist / max(edge, 1.0)
            color = bands[min(len(bands) - 1, int(ratio * 4.8))]
            if settle > 0.30:
                # 熄灭过程：火焰转成烟灰（里亮外暗），避免半透明火色发灰发脏
                color = SMOKE[2] if ratio < 0.45 else (SMOKE[1] if ratio < 0.80 else SMOKE[0])
            if settle > 0.55 and ratio > 0.55:
                # 末尾只留下外缘的几块碎火，主体已经化成烟
                if noise(x // 2, y // 2, 120 + k) < (settle - 0.55) * 1.8:
                    continue
            put(img, x, y, rgba(color, fire_alpha))

    # 火花：从第 2 帧起飞出，受重力下坠，后段变暗
    for i in range(60):
        angle = noise(i, 1, 91) * math.tau
        speed = 0.45 + noise(i, 2, 92) * 0.95
        travel = (k - 1) * 9.0 * speed
        px = cx + math.cos(angle) * travel
        py = cy + math.sin(angle) * travel + 0.010 * travel * travel
        alpha = 255 if k < 10 else max(0, 220 - (k - 10) * 60)
        if alpha <= 0:
            continue
        color = FIRE[4] if speed > 0.95 else FIRE[3]
        size = 3 if speed > 1.15 else 2
        for sy in range(size):
            for sx in range(size):
                put(img, int(px) + sx, int(py) + sy, rgba(color, alpha))

    # 烟与余烬：第 5 帧起上升、变淡（成团上升，不做逐像素噪点）
    if k >= 5:
        for i in range(26):
            angle = noise(i, 3, 93) * math.tau
            rise = (k - 5) * (1.6 + noise(i, 4, 94) * 2.0)
            sx = cx + math.cos(angle) * (10 + noise(i, 5, 95) * 34)
            sy = cy + math.sin(angle) * (8 + noise(i, 6, 96) * 26) - rise
            alpha = max(0, 224 - (k - 5) * 34)
            if alpha <= 0:
                continue
            blob = 4 if i % 3 == 0 else 3
            shade = SMOKE[i % len(SMOKE)]
            for by in range(blob):
                for bx in range(blob):
                    if noise(int(sx) + bx, int(sy) + by, 130 + i) < 0.18:
                        continue
                    put(img, int(sx) + bx, int(sy) + by, rgba(shade, alpha))

    # 最后两帧：只剩零星余烬
    if k >= BLAST_FRAMES - 2:
        for i in range(10):
            sx = cx + (noise(i, 7, 97) - 0.5) * 76
            sy = cy + (noise(i, 8, 98) - 0.5) * 66 + 10
            put(img, int(sx), int(sy), rgba(FIRE[3], 96))

    return img


# ---------------------------------------------------------------- 输出

def main() -> None:
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    build = portal_build_frames()
    swirl = portal_swirl_frames()
    fuse = tnt_fuse_frames()
    blast = [blast_frame(k) for k in range(BLAST_FRAMES)]

    outputs = (
        ("portal_build.png", build, BUILD_W, BUILD_H, 70, False),
        ("portal_swirl.png", swirl, SWIRL_W, SWIRL_H, 80, True),
        ("tnt_fuse.png", fuse, FUSE_W, FUSE_H, 60, False),
        ("tnt_blast.png", blast, BLAST_SIZE, BLAST_SIZE, 55, False),
    )

    manifest = {"sheets": []}
    for name, frames, width, height, frame_ms, loop in outputs:
        image = sheet(frames, width, height)
        image.save(OUT_DIR / name)
        manifest["sheets"].append(
            {
                "file": name,
                "frameWidth": width,
                "frameHeight": height,
                "frames": len(frames),
                "frameMs": frame_ms,
                "loop": loop,
            }
        )
        print(f"{name}: {len(frames)} frames, sheet {image.width}x{image.height}")

    (OUT_DIR / "manifest.json").write_text(
        json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )
    print(f"manifest: {OUT_DIR / 'manifest.json'}")


if __name__ == "__main__":
    main()
