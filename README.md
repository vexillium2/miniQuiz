# 智测天地——AI 智能答题测试平台

> 作者：[vexillium](https://github.com/vexillium2)

基于 SpringBoot 开发， 使用 ChatGLM AI 协助用户轻松出题，实现 AI 智能评测。

[toc]

## 技术栈

### 框架

- Spring Boot 2.7
- Spring MVC
- MyBatis + MyBatis Plus 数据访问（开启分页）
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程
- Spring Scheduler 定时任务
- Spring 事务注解

### 数据存储

- MySQL 数据库
- Redis 内存数据库

### 库表设计：

- user表：存储用户账号密码、个人信息
- app表：存储应用信息
- Question表：存储题目信息
- Answer表：存储答案信息
- userAnswer表：存储用户答题记录

### 工具类

- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- 业务代码生成器（支持自动生成 Service、Controller、数据模型代码）
- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置


## 业务功能


### 架构设计

- 合理分层
