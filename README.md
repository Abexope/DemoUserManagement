# DemoUserManagement

这个项目是一个简单的 Web 客户管理系统。系统的搭建过程参考[黑马 Java Web 系列课程](https://www.bilibili.com/video/BV1uJ411k7wy?p=782)中的P782~P808。本人基于任课老师在课上讲授内容的基础上进行了进一步的功能拓展和代码优化。

项目中并没有使用当前比较热门的一些框架和中间件，比较适合刚刚接触 Java Web 的初学者学习和参考。开发过程中的大部分修改痕迹以注释保留，旨在希望其它开发者在阅读和学习本代码的过程中能够了解视频任课老师的讲解思路，以及笔者自己对部分业务逻辑的思考和理解。

项目的前端展示存在一点小 BUG，由于我主要学习的是后端，且这个案例的前端代码采用JSP实现，所以有些前端的代码实在不会 DEBUG 就过了。但是整体上不会影响整个项目的运行和对后端执行逻辑的理解。欢迎各位大神前来拔草。

## 技术选型

- JDK版本：1.8
- Web服务器：
  - Tomcat 8.5
  - Servlet

- 前端：
  - JSP
  - BootStrap
- 数据库：
  - MySQL 5.1 + SQLyog
  - Druid 1.1
  - JDBCTemplate

## 数据库设计

```mysql
-- 创建和使用数据库
CREATE DATABASE day17;		-- 创建数据库
USE day17;					-- 使用数据库

-- 客户表
CREATE TABLE USER (
	id INT PRIMARY KEY AUTO_INCREMENT,	-- 编号（主键、自增）
	NAME VARCHAR(20) NOT NULL,			-- 姓名
	gender VARCHAR(5),					-- 性别
	age INT,							-- 年龄
	address VARCHAR(32),				-- 地址
	qq VARCHAR(20),						-- QQ
	email VARCHAR(50)					-- 邮箱
);

-- 管理员表
CREATE TABLE administrator (
	id INT PRIMARY KEY AUTO_INCREMENT,	-- 编号（主键、自增）
	username VARCHAR(32) NOT NULL,		-- 管理员用户名
	PASSWORD VARCHAR(32) NOT NULL		-- 管理员密码
);
```

## 项目架构草图

采用经典的 “三层结构” + MVC开发模式

![三层架构](D:\iJava\DemoUserManagement\img\三层架构.bmp)

