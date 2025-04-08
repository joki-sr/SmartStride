# 数据库技术路线图

## 1. 核心数据库引擎

*   **技术:** **MySQL Community Server** 
*   **版本:** 使用最新的稳定 LTS（长期支持）版本（例如 `MySQL 8.0.x` 或更高版本），以利用其性能改进、安全特性（如角色）和增强的 JSON 支持。
*   **理由:**
    *   满足明确的需求。
    *   成熟、可靠、文档完善、拥有庞大的社区支持。
    *   能很好地处理关系型数据，适用于患者信息、会话数据和结构化的分析结果。
    *   对于“不是很大”的数据库来说性能足够，尤其是在有适当索引的情况下。
    *   支持必要的数据类型（`TIMESTAMP`、`FLOAT`、`DECIMAL` 等，可能还会用到 `JSON`）。

## 2. 数据库交互层（应用程序端）

*   **技术:** **ORM (Object-Relational Mapper, 对象关系映射器)**
*   **具体库:**
    *   **Python:** **SQLAlchemy** (强烈推荐)。它功能强大、灵活，能很好地与 Flask/FastAPI/Django 等框架配合使用，并通过 Alembic 提供出色的迁移支持。备选：Django ORM（如果使用 Django 框架）、Peewee（用于更简单的场景）。
    *   **Java:** **JPA (Jakarta Persistence API)** 配合 **Hibernate** 实现。这是行业标准，健壮，并且与 Spring Boot 集成良好。
    *   **Node.js:** **Sequelize** 或 **TypeORM**。两者都是流行的选择，为 TypeScript/JavaScript 应用提供 ORM 功能。
*   **理由:**
    *   **生产力:** 抽象了原生 SQL，将数据库表映射到应用程序对象，加快了 CRUD 操作（管理员需求）的开发速度。
    *   **可维护性:** 更容易管理数据库交互和模式（Schema）变更。
    *   **数据库无关性 (部分):** 虽然目标是 MySQL，但使用 ORM 使得未来潜在的数据库迁移稍微容易一些。
    *   **连接池:** 大多数 ORM 会集成或配合连接池库一起工作，这对于性能和管理来自设备或用户的并发连接至关重要。

## 3. 模式管理 / 迁移

*   **技术:** 数据库迁移工具
*   **具体库:**
    *   **Python/SQLAlchemy:** **Alembic**。它被设计用来与 SQLAlchemy 模型无缝协作。
    *   **Java/JPA:** **Flyway** 或 **Liquibase**。两者都是优秀的、与框架无关的工具，用于管理模式演变。
    *   **Node.js:** Sequelize/TypeORM 内置的迁移工具，或者像 Flyway/Liquibase 这样的独立工具。
*   **理由:**
    *   **模式的版本控制:** 与应用程序代码一起跟踪数据库模式的更改。
    *   **可重复部署:** 确保在不同环境（开发、测试、生产）中数据库模式的一致性。
    *   **协作:** 使多个开发人员更容易在数据库更改上协作，而不会产生冲突。

## 4. 数据处理与清洗集成

*   **方法:** 主要在**应用程序层**执行数据清洗（异常值剔除、插值补偿），要么在最终存储*之前*，要么在分析阶段*期间*进行，而不是在 MySQL 内部执行复杂的存储过程。
*   **库 (应用程序端):**
    *   **Python:** **Pandas** 和 **NumPy**。非常适合数值数据操作、过滤（例如 `df[(df['knee_angle'] >= 0) & (df['knee_angle'] <= 140)]`）和插值（`.interpolate(method='linear')`）。
*   **数据库角色:** MySQL 存储原始数据和清洗/处理后的数据（可能在不同的表中，或使用标志/列来指示有效性/处理状态）。它高效地检索数据以供处理，并存储结果。
*   **理由:**
    *   让数据库逻辑专注于存储和检索。
    *   利用应用程序语言中强大的数据处理库，这些库通常比单独使用 SQL 更适合复杂的算法。
    *   便于更容易地测试清洗逻辑。

## 5. 处理传感器数据

*   **模式设计:**
    *   使用适当的数据类型：`DATETIME` 或 `TIMESTAMP` 用于时间，`FLOAT` 或 `DECIMAL` 用于传感器读数（角度等）。
    *   考虑为时间序列传感器读数设置一个专用表（例如 `SensorReadings`），通过外键链接到 `Session` 表。
    *   `Session` 表链接到 `Patient` 和 `Device` 表。
*   **索引:** 对性能至关重要，尤其是在按时间范围或会话查询传感器数据时。需要对 `session_id`、`timestamp` 等列建立索引。
*   **潜在优化 (如果数据量增长):** 虽然声明为“不是很大”，但如果传感器数据随着时间的推移变得非常庞大，可以考虑使用 MySQL 分区 (Partitioning)（例如，在 `timestamp` 列上按日期范围分区），以提高查询性能和数据管理（归档/删除旧数据）的效率。

## 6. 备份与恢复

*   **技术:** 标准 MySQL 工具（`mysqldump` 用于逻辑备份，可能使用二进制日志进行时间点恢复）或云提供商解决方案（例如 AWS RDS 快照、Azure SQL Database 备份）。
*   **策略:** 实施定期的自动化备份。定义 RPO（恢复点目标）和 RTO（恢复时间目标）。
*   **理由:** 对于数据安全和灾难恢复至关重要。

## 7. 安全性

*   **方法:**
    *   为数据库用户使用强密码。
    *   创建具有有限权限的特定数据库用户（例如，用于 UI 的只读用户，用于应用程序后端的具有必要 CRUD 权限的特定用户）。避免应用程序使用 root 用户。
    *   如果使用 `v8.0+`，则利用 MySQL 角色。
    *   在应用程序内部安全地管理连接凭据（使用环境变量或密钥管理系统）。
    *   考虑在应用程序和数据库服务器之间使用 SSL/TLS 连接，特别是当它们位于不同的机器/网络上时。

## 总结图示 (概念层)

```mermaid
graph LR
    A["应用程序层<br/>(Python/Java/Node.js)<br/>- 业务逻辑<br/>- 数据处理 (Pandas/NumPy)<br/>- API 接口"] --> B("数据库交互层<br/>(ORM: SQLAlchemy/JPA/Sequelize, 连接池)");
    B --> C["MySQL 数据库<br/>(引擎, 存储)<br/>- 表 (患者, 会话, 传感器读数)<br/>- 索引<br/>- 备份 (mysqldump)"];
    D["模式管理<br/>(Alembic / Flyway / Liquibase)"] --> B;

    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#ccf,stroke:#333,stroke-width:2px
    style C fill:#cdf,stroke:#333,stroke-width:2px
    style D fill:#fcc,stroke:#333,stroke-width:2px