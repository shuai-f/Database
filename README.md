﻿﻿﻿﻿﻿# Database

数据库系统设计

- 数据库 MySQL 8.0
- Maven 项目，Mybatis连接数据库

项目介绍：

- /src - 项目所有文件
  - /main - 项目源文件
     - /java
         - /complex - 重构复合类
             - /Conn.java - Class和Department的复合类，用于连接查询的返回结果
             - /Group.java - 无用
         - /dao/IUserDao.java - 接口，定义方法，连接数据库
         - /entity - 实体类--对应表项
         - /relation - 联系类--对应表项
         - /ui - 图形化界面
             - UI.java - 用户主界面（登录界面）
             - AdminUI.java - 管理员操作界面
      - /resourses - 项目配置文件
         - /dao/IUserDao.xml - 配置Sql语句，Mapper
         - log4j.properties - 配置log4j
         - MybatisConfig.xml - Mybatis主配置文件
         - mysql.properties - MySQL数据库信息配置文件
   - /tset - 项目测试文件









