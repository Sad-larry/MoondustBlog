# 月尘博客

<p align=center>月尘博客是使用 <span style="color:grey">Spring Boot</span> + <span style="color:grey">Vue</span> 开发的一款简洁前后端分离的博客系统</p>

#### 前言

今天是2022年9月19日，作为一名计算机专业学生，已经大四了，却感觉啥也不会，这3年里，一直在互联网里面摸爬滚打，即使了解新兴技术，也尝试去攻克他们，但是也感叹自身基础不足而难以继续学习下去。等到快要毕业的时候才醒悟，最缺的还是时间阿，毕竟浪费了那么多的时间了，想干出一番大事，却已经无能为力了。

最近在学 SpringBoot 和微信小程序，我想用这两个技术，写一个博客项目，虽然我跟着B站的 *三更草堂*
up主敲了一个博客项目，但是，毕竟不是自己写的，虽然在跟着老师一起敲项目中学到很多，但也因为不是自己亲自动手的，难免过段时间就忘掉了。所以，今天开始，不断完善创新我的个人项目，希望能在不久的未来中，上线我的服务器，然后供大家参考学习使用。

#### 项目介绍

月尘博客（**MoondustBlog**），一个基于 `SpringBoot` 的前后端分离博客系统，后端采用 `Restful API` 接口规范，后台管理系统采用 `Vue + ElementUI`
并借助 [Rouyi框架](https://gitee.com/y_project/RuoYi) 搭建，博客前台采用 `Vue + Vuetify` 搭建。

#### 在线地址

**项目链接：** [月尘博客](http://refrainblog.cn)

**后台链接：** [月尘博客后台管理系统](http://refrainblog.cn/admin)

账号：test 密码：123456

**Github地址：** [https://github.com/Sad-larry/MoondustBlog](https://github.com/Sad-larry/MoondustBlog)

您的star是我坚持的动力，感谢大家的支持，欢迎提交pr共同改进项目。

解释为什么博客网站的域名是 `refrainblog.cn`，因为 `moondustblog.cn` 被注册了，想了很久，就用了一首歌的名字（refrain）注册了域名，毕竟比较随便，所以就随便啦。

#### 技术栈

##### 后端技术

| 技术                  | 描述              |   版本    |
|:--------------------|:----------------|:-------:|
| Spring Boot         | MVC服务框架         |  2.7.3  |
| Spring Security     | 安全访问控制解决方案的安全框架 |  5.7.3  |
| hibernate-validator | 数据校验框架          |  6.24   |
| redis               | 数据缓存数据库         |  7.0.4  |
| quartz              | 作业调度框架          |  2.3.2  |
| lombok              | JavaBean代码简化插件  | 1.18.24 |
| swagger3            | 接口文档生成          |  3.0.0  |
| druid               | 数据库连接池          | 1.2.12  |
| MyBatis-Plus        | ORM框架，数据库连接桥梁   |  3.5.2  |
| Hutool              | Java工具类库        |  5.8.8  |
| 七牛云                 | 数据存储对象          | 7.11.0  |
| gson                | 谷歌json格式化依赖库    |  2.9.1  |
| jjwt                | Java web token  |  0.9.1  |
| fastjson            | Json库           | 2.0.12  |

##### 前端技术

| 技术         | 描述                     | 版本      |
|------------|------------------------|---------|
| vue        | 构建用户界面的渐进式JavaScript框架 | 2.6.11  |
| vue-router | 单页面应用的路径管理器            | 3.2.0   |
| vuex       | 状态管理模式                 | 3.4.0   |
| element-ui | 基于Vue封装的UI组件库          | 2.15.10 |
| vuetify    | 基于Vue为移动而生的UI组件框架      | 2.6.12  |
| axios      | 基于promise的网络请求库        | 1.1.3   |
| echarts    | 基于JavaScript的数据可视化图表库  | 5.4.0   |
