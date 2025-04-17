import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from scipy.spatial.transform import Rotation as R

# 读取 文件
hip_df = pd.read_excel("le_hip.xlsx")
knee_df = pd.read_excel("le_knee.xlsx")
ankle_df = pd.read_excel("le_ankle.xlsx")

# 提取四元数数据
hip_quats = hip_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
knee_quats = knee_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values
ankle_quats = ankle_df[['四元数0()', '四元数1()', '四元数2()', '四元数3()']].values

# 计算旋转矩阵
hip_rot = [R.from_quat(q).as_matrix() for q in hip_quats]
knee_rot = [R.from_quat(q).as_matrix() for q in knee_quats]
ankle_rot = [R.from_quat(q).as_matrix() for q in ankle_quats]

# 创建动画
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.set_xlim([-1, 1])
ax.set_ylim([-1, 1])
ax.set_zlim([-1, 1])


# 创建腿部部位的连接
def update(num):
    ax.clear()

    # 上段 (大腿)
    hip = hip_rot[num].dot([0, 0, 0.5])  # 模拟大腿长度
    knee = knee_rot[num].dot([0, 0, 0.5])  # 模拟膝盖到脚踝的长度
    ankle = ankle_rot[num].dot([0, 0, 0.5])  # 模拟小腿长度

    # 绘制三部分线段（大腿、小腿）
    ax.plot([0, hip[0]], [0, hip[1]], [0, hip[2]], color='blue', lw=3)
    ax.plot([hip[0], knee[0]], [hip[1], knee[1]], [hip[2], knee[2]], color='green', lw=3)
    ax.plot([knee[0], ankle[0]], [knee[1], ankle[1]], [knee[2], ankle[2]], color='red', lw=3)

    ax.set_xlim([-1, 1])
    ax.set_ylim([-1, 1])
    ax.set_zlim([0, 1])
    ax.set_title(f'Frame: {num}')


# 动画
ani = FuncAnimation(fig, update, frames=len(hip_df), interval=100)

# 展示动画
plt.show()
