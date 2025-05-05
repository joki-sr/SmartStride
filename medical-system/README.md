# 医疗系统后端

这是一个基于Spring Boot的医疗系统后端，用于管理医生、患者、医患关系以及患者报告数据。

## 技术栈

- Java 11
- Spring Boot 2.7.x
- Spring Data JPA
- Spring Security
- MySQL 8.0
- Maven

## 功能特性

- 用户认证与授权
- 医生管理
- 患者管理
- 医患关系管理
- 患者报告管理
- IMU数据上传与分析
- CSV数据管理（存储与清洗）

## 项目结构

```
medical-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── medicalsystem/
│   │   │               ├── config/           # 配置类
│   │   │               ├── controller/       # 控制器
│   │   │               ├── dto/              # 数据传输对象
│   │   │               ├── entity/           # 实体类
│   │   │               ├── exception/        # 异常处理
│   │   │               ├── repository/       # 数据访问层
│   │   │               ├── service/          # 服务层
│   │   │               │   └── impl/         # 服务实现
│   │   │               └── MedicalSystemApplication.java
│   │   └── resources/
│   │       └── application.properties        # 应用配置
│   └── test/                                 # 测试代码
└── pom.xml                                   # Maven配置
```

## 快速开始

### 前提条件

- JDK 11或更高版本
- Maven 3.6或更高版本
- MySQL 8.0

### 数据库设置

1. 创建MySQL数据库：

```sql
CREATE DATABASE medical_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 更新`application.properties`中的数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/medical_system?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 构建与运行

1. 克隆项目：

```bash
git clone https://github.com/yourusername/medical-system.git
cd medical-system
```

2. 使用Maven构建项目：

```bash
mvn clean package
```

3. 运行应用：

```bash
java -jar target/medical-system-0.0.1-SNAPSHOT.jar
```

或者使用Maven运行：

```bash
mvn spring-boot:run
```

应用将在 http://localhost:8080/medical-system 上运行。

## API文档

### 认证API

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 医生API

- `GET /api/doctors` - 获取所有医生
- `POST /api/doctors/register` - 注册医生

### 患者API

- `GET /api/patients` - 获取所有患者

### 医患关系API

- `GET /api/relations` - 获取所有医患关系
- `POST /api/relations` - 添加医患关系
- `PUT /api/relations/{relationId}` - 更新医患关系
- `DELETE /api/relations/{relationId}` - 删除医患关系

### 患者报告API

- `GET /api/reports/patient/{patientId}` - 获取患者的所有报告
- `GET /api/reports/patient/{patientId}/type/{type}` - 获取特定类型的患者报告
- `POST /api/reports/patient/{patientId}` - 创建患者报告
- `DELETE /api/reports/{reportId}` - 删除患者报告

### IMU数据API

- `POST /api/imu/patient/{patientId}/upload` - 上传IMU数据
- `GET /api/imu/patient/{patientId}` - 获取患者的IMU数据
- `DELETE /api/imu/patient/{patientId}` - 删除患者的IMU数据

### 数据管理API

- `POST /api/data/store-csv` - 将CSV文件数据存储到数据库中
- `POST /api/data/clean-csv` - 对CSV文件进行数据清洗和时间对齐，然后存储到数据库中

## 许可证

[MIT](LICENSE)
