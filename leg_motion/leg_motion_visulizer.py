import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from scipy.spatial.transform import Rotation as R

# 模拟传感器数据（四元数）
t = np.linspace(0, 10, 100)  # 模拟100帧

def simulate_quaternion_motion(freq=1):
    angle = np.sin(2 * np.pi * freq * t) * 30  # 模拟30°的弯曲
    quat_list = []
    for a in angle:
        # 绕Z轴旋转（假设膝盖只在Z方向弯曲）
        r = R.from_euler('z', a, degrees=True)
        quat_list.append(r.as_quat())
    return np.array(quat_list)

# 模拟的三个关节点：hip 固定，knee 和 ankle 随四元数旋转
hip_pos = np.array([0, 0, 0])
knee_rel = np.array([0, -1, 0])  # 大腿向下1米
ankle_rel = np.array([0, -1, 0])  # 小腿向下1米

knee_quats = simulate_quaternion_motion(freq=0.5)  # 膝盖旋转
ankle_quats = simulate_quaternion_motion(freq=1.0)  # 脚踝旋转更多

# 初始化图形
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# 设置坐标轴范围
ax.set_xlim(-2, 2)
ax.set_ylim(-2, 2)
ax.set_zlim(-2, 2)

# 绘图对象
line, = ax.plot([], [], [], 'o-', lw=4, color='blue')

def update(frame):
    # 旋转 knee 相对向量
    knee_rot = R.from_quat(knee_quats[frame])
    knee_pos = hip_pos + knee_rot.apply(knee_rel)

    # 旋转 ankle 相对向量（在 knee 的旋转基础上）
    ankle_rot = R.from_quat(ankle_quats[frame])
    ankle_pos = knee_pos + ankle_rot.apply(ankle_rel)

    # 更新线条
    xs = [hip_pos[0], knee_pos[0], ankle_pos[0]]
    ys = [hip_pos[1], knee_pos[1], ankle_pos[1]]
    zs = [hip_pos[2], knee_pos[2], ankle_pos[2]]
    line.set_data(xs, ys)
    line.set_3d_properties(zs)
    return line,

ani = FuncAnimation(fig, update, frames=len(t), interval=100, blit=True)
plt.title('Leg Motion Visualization (Simulated)')
plt.show()