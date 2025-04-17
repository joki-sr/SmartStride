import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from scipy.spatial.transform import Rotation as R
import imageio.v2 as imageio
import os

# 读取数据
hip_df = pd.read_excel("le_hip.xlsx")
knee_df = pd.read_excel("le_knee.xlsx")
ankle_df = pd.read_excel("le_ankle.xlsx")

hip_quat = hip_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
knee_quat = knee_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
ankle_quat = ankle_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values

hip_rot = [R.from_quat(q).as_matrix() for q in hip_quat]
knee_rot = [R.from_quat(q).as_matrix() for q in knee_quat]
ankle_rot = [R.from_quat(q).as_matrix() for q in ankle_quat]

min_frames = min(len(hip_rot), len(knee_rot), len(ankle_rot))

def get_skeleton(frame):
    hip_center = np.array([0, 0, 0])

    l_hip = hip_center
    l_knee = l_hip + hip_rot[frame].dot([0, 0, -0.4])
    l_ankle = l_knee + knee_rot[frame].dot([0, 0, -0.4])

    r_hip = hip_center
    r_knee = r_hip + hip_rot[frame].dot([0, 0, -0.4]) * np.array([1, -1, 1])
    r_ankle = r_knee + knee_rot[frame].dot([0, 0, -0.4]) * np.array([1, -1, 1])

    chest = hip_center + np.array([0, 0, 0.3])
    neck = chest + np.array([0, 0, 0.2])
    head = neck + np.array([0, 0, 0.2])

    return {
        'hip': hip_center,
        'l_knee': l_knee, 'l_ankle': l_ankle,
        'r_knee': r_knee, 'r_ankle': r_ankle,
        'chest': chest, 'neck': neck, 'head': head
    }

# 保存骨架数据为CSV
all_frames_data = []
for frame in range(min_frames):
    skel = get_skeleton(frame)
    row = {'frame': frame}
    for joint, pos in skel.items():
        row[f'{joint}_x'] = pos[0]
        row[f'{joint}_y'] = pos[1]
        row[f'{joint}_z'] = pos[2]
    all_frames_data.append(row)
df_out = pd.DataFrame(all_frames_data)
df_out.to_csv("skeleton_data.csv", index=False)

# 准备绘图与保存路径
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
os.makedirs("frames", exist_ok=True)
saved_images = []

def update(frame):
    ax.clear()
    skel = get_skeleton(frame)

    ax.set_xlim([-1, 1])
    ax.set_ylim([-1, 1])
    ax.set_zlim([-1, 1])
    ax.set_title(f"Frame {frame}")

    def draw_line(a, b, color='black'):
        ax.plot([a[0], b[0]], [a[1], b[1]], [a[2], b[2]], color=color, lw=3)

    draw_line(skel['hip'], skel['chest'], 'gray')
    draw_line(skel['chest'], skel['neck'], 'gray')
    draw_line(skel['neck'], skel['head'], 'gray')

    draw_line(skel['hip'], skel['l_knee'], 'blue')
    draw_line(skel['l_knee'], skel['l_ankle'], 'blue')

    draw_line(skel['hip'], skel['r_knee'], 'red')
    draw_line(skel['r_knee'], skel['r_ankle'], 'red')

    for point in skel.values():
        ax.scatter(point[0], point[1], point[2], color='black', s=20)

    # 保存当前帧为图片
    fname = f"frames/frame_{frame:03d}.png"
    plt.savefig(fname)
    saved_images.append(fname)

# 渲染所有帧
for frame in range(min_frames):
    update(frame)

# 合成GIF
images = [imageio.imread(fname) for fname in saved_images]
imageio.mimsave("skeleton.gif", images, duration=0.1)

print("GIF 生成完成：skeleton.gif")
