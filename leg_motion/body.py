import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from scipy.spatial.transform import Rotation as R

# 读取数据（你自己造好的 Excel）
hip_df = pd.read_excel("le_hip.xlsx")
knee_df = pd.read_excel("le_knee.xlsx")
ankle_df = pd.read_excel("le_ankle.xlsx")

# 获取四元数（注意列名要与文件一致）
hip_quat = hip_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
knee_quat = knee_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
ankle_quat = ankle_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values

# 旋转矩阵
hip_rot = [R.from_quat(q).as_matrix() for q in hip_quat]
knee_rot = [R.from_quat(q).as_matrix() for q in knee_quat]
ankle_rot = [R.from_quat(q).as_matrix() for q in ankle_quat]

# 关键点定义
def get_skeleton(frame):
    # 髋部作为原点
    hip_center = np.array([0, 0, 0])

    # 左腿（用传感器旋转矩阵乘以骨骼方向）
    l_hip = hip_center
    l_knee = l_hip + hip_rot[frame].dot([0, 0, -0.4])
    l_ankle = l_knee + knee_rot[frame].dot([0, 0, -0.4])

    # 右腿：用左腿镜像（左右颠倒 Y轴）
    r_hip = hip_center
    r_knee = r_hip + hip_rot[frame].dot([0, 0, -0.4]) * np.array([1, -1, 1])
    r_ankle = r_knee + knee_rot[frame].dot([0, 0, -0.4]) * np.array([1, -1, 1])

    # 上半身 + 头部（先简单垂直上升）
    chest = hip_center + np.array([0, 0, 0.3])
    neck = chest + np.array([0, 0, 0.2])
    head = neck + np.array([0, 0, 0.2])

    return {
        'hip': hip_center,
        'l_knee': l_knee, 'l_ankle': l_ankle,
        'r_knee': r_knee, 'r_ankle': r_ankle,
        'chest': chest, 'neck': neck, 'head': head
    }

# 动画绘图
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

def update(frame):
    ax.clear()
    skel = get_skeleton(frame)

    # 设置范围
    ax.set_xlim([-1, 1])
    ax.set_ylim([-1, 1])
    ax.set_zlim([-1, 1])
    ax.set_title(f"Frame {frame}")

    def draw_line(a, b, color='black'):
        ax.plot([a[0], b[0]], [a[1], b[1]], [a[2], b[2]], color=color, lw=3)

    # 躯干
    draw_line(skel['hip'], skel['chest'], 'gray')
    draw_line(skel['chest'], skel['neck'], 'gray')
    draw_line(skel['neck'], skel['head'], 'gray')

    # 左腿（蓝色）
    draw_line(skel['hip'], skel['l_knee'], 'blue')
    draw_line(skel['l_knee'], skel['l_ankle'], 'blue')

    # 右腿（红色）
    draw_line(skel['hip'], skel['r_knee'], 'red')
    draw_line(skel['r_knee'], skel['r_ankle'], 'red')

    # 关键点（可选）
    for point in skel.values():
        ax.scatter(point[0], point[1], point[2], color='black', s=20)

ani = FuncAnimation(fig, update, frames=len(hip_rot), interval=100)
plt.show()
