> 如果要做一个博客项目，其中，得有**用户**，用户通过分配不同的**角色**达到拥有不同的**权限**功能，此时RBAC(Role Base Access Control)模型就适用了。普通用户和管理员用户拥有的权限是不一样的，管理员有所有的权限，普通用户又可以分出几种，例如注册后的用户可以写**博客**，但是游客只有浏览，默认权限，毕竟博客操作得登录后才行。注册用户可以发表博客，发表**评论**，对于每篇博客，都可以选择一个**分类**和多个**标签**。关于后台得**菜单**栏管理，我倒是得考虑考虑。

- bigint
  - 20：一般为主键
- varchar
  - 2：一般为状态标志
  - 32：一般为标识字段
  - 128：一般为长标识字段
  - 256：一般为url
  - 1024：一般为简介
- date
- datetime
- longtext
- int
  - 16：一般为点赞数

#### 1、用户表`t_user`

| 字段        | 类型     | 描述                  |
| ----------- | -------- | --------------------- |
| id          | bigint   | 主键                  |
| user_name   | varchar  | 用户名                |
| nick_name   | varchar  | 昵称                  |
| password    | varchar  | 密码                  |
| mobile      | varchar  | 手机号                |
| email       | varchar  | 电子邮箱              |
| avatar      | varchar  | 用户头像              |
| intro       | varchar  | 个人简介              |
| birthday    | date     | 生日                  |
| status      | varchar  | 账号状态(1正常,0停用) |
| create_time | datetime | 注册时间              |
| update_time | datetime | 更新时间              |

```mysql
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name` VARCHAR(32) NOT NULL COMMENT '用户名',
    `nick_name` VARCHAR(32) DEFAULT NULL COMMENT '昵称',
    `password` VARCHAR(32) NOT NULL COMMENT '密码',
    `mobile` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '电子邮箱',
    `avatar` VARCHAR(256) DEFAULT NULL COMMENT '用户头像',
    `intro` VARCHAR(256) DEFAULT NULL COMMENT '个人简介',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `status` VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '账号状态(1正常,0停用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT=DYNAMIC;
```

#### 2、角色表`t_role`

| 字段        | 类型     | 描述                     |
| ----------- | -------- | ------------------------ |
| id          | bigint   | 主键                     |
| role_name   | varchar  | 角色名                   |
| description | varchar  | 角色描述                 |
| status      | varchar  | 角色状态（1正常，0停用） |
| create_time | datetime | 创建时间                 |
| update_time | datetime | 更新时间                 |

```mysql
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name` VARCHAR(32) NOT NULL COMMENT '角色名',
    `description` VARCHAR(128) NULL DEFAULT NULL COMMENT '角色描述',
    `status` VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '角色状态(1正常,0停用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

> 1、admin
>
> 2、user

#### 3、用户-角色表`t_user_role`

| 字段    | 类型   | 描述   |
| ------- | ------ | ------ |
| id      | bigint | 主键   |
| user_id | bigint | 用户id |
| role_id | bigint | 角色id |

```mysql
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB ROW_FORMAT = DYNAMIC;
```

#### 4、菜单表`t_menu`

| 字段        | 类型     | 描述                     |
| ----------- | -------- | ------------------------ |
| id          | bigint   | 主键                     |
| menu_name   | varchar  | 菜单名                   |
| pid         | bigint   | 父级菜单id               |
| url         | varchar  | 资源路径                 |
| component   | varchar  | 组件                     |
| icon        | varchar  | 菜单图标                 |
| status      | varchar  | 菜单状态（1正常，0停用） |
| create_time | datetime | 创建时间                 |
| update_time | datetime | 更新时间                 |

```mysql
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_name` VARCHAR(32) NOT NULL COMMENT '菜单名',
    `pid` BIGINT(20) NULL DEFAULT NULL COMMENT '父级菜单id',
    `url` VARCHAR(128) NULL DEFAULT NULL COMMENT '路由地址',
    `component` VARCHAR(128) NULL DEFAULT NULL COMMENT '组件',
    `icon` VARCHAR(128) NULL DEFAULT NULL COMMENT '菜单图标',
    `status` VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '菜单状态(1正常,0停用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

> xyy：x代表一级菜单，yy代表二级菜单
>
> 100、首页
>
> 200、文章管理
>
> ​	201、发表文章
>
> ​	202、文章列表
>
> ​	203、标签列表
>
> ​	204、分类列表
>
> 300、用户管理
>
> ​	301、用户列表
>
> ​	302、用户评论
>
> 400、权限管理
>
> ​	401、菜单列表
>
> ​	402、资源列表
>
> ​	403、角色列表
>
> 500、系统管理
>
> ​	501、定时任务
>
> ​	502、关于我

#### 5、角色-菜单表`t_role_menu`

| 字段    | 类型   | 描述   |
| ------- | ------ | ------ |
| id      | bigint | 主键   |
| role_id | bigint | 角色id |
| menu_id | bigint | 菜单id |

```mysql
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色id',
    `menu_id` BIGINT(20) NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB ROW_FORMAT = DYNAMIC;
```

#### 6、权限表`t_resource`

| 字段           | 类型     | 描述     |
| -------------- | -------- | -------- |
| id             | bigint   | 主键     |
| resource_name  | varchar  | 资源名   |
| url            | varchar  | 权限路径 |
| request_method | varchar  | 请求方式 |
| pid            | bigint   | 父模块id |
| create_time    | datetime | 创建时间 |
| update_time    | datetime | 更新时间 |

```mysql
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `resource_name` VARCHAR(32) NOT NULL COMMENT '资源名',
    `url` VARCHAR(128) NULL DEFAULT NULL COMMENT '权限路径',
    `request_method` VARCHAR(128) NULL DEFAULT NULL COMMENT '请求方式',
    `pid` BIGINT(20) NULL DEFAULT NULL COMMENT '父模块id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

#### 7、角色-权限表`t_role_resource`

| 字段        | 类型   | 描述   |
| ----------- | ------ | ------ |
| id          | bigint | 主键   |
| role_id     | bigint | 角色id |
| resource_id | bigint | 权限id |

```mysql
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色id',
    `resource_id` BIGINT(20) NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB ROW_FORMAT = DYNAMIC;
```

#### 8、文章表`t_article`

| 字段          | 类型     | 描述                           |
| ------------- | -------- | ------------------------------ |
| id            | bigint   | 主键                           |
| user_id       | bigint   | 作者                           |
| title         | varchar  | 文章标题                       |
| content       | longtext | 文章内容                       |
| summary       | varchar  | 文章摘要                       |
| category_id   | bigint   | 所属分类                       |
| thumbnail     | varchar  | 缩略图                         |
| is_top        | varchar  | 是否置顶(1是,0否)              |
| status        | varchar  | 文章状态(1发布,0草稿,-1待删除) |
| view_count    | bigint   | 浏览量                         |
| is_comment    | varchar  | 是否允许评论(1是,0否)          |
| comment_count | bigint   | 评论数                         |
| star_count    | bigint   | 点赞数                         |
| create_time   | datetime | 创建时间                       |
| update_time   | datetime | 更新时间                       |

```mysql
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT(20) NOT NULL COMMENT '作者',
    `title` VARCHAR(256) NULL DEFAULT NULL COMMENT '文章标题',
    `content` LONGTEXT NULL COMMENT '文章内容',
    `summary` VARCHAR(1024) NULL DEFAULT NULL COMMENT '文章摘要',
    `category_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属分类',
    `thumbnail` VARCHAR(256) NULL DEFAULT NULL COMMENT '缩略图',
    `is_top` VARCHAR(2) DEFAULT '0' COMMENT '是否置顶(1是,0否)',
    `status` VARCHAR(2) DEFAULT '0' COMMENT '文章状态(1已发布,0草稿)',
    `view_count` BIGINT(20) NULL DEFAULT 0 COMMENT '浏览量',
    `is_comment` VARCHAR(2) DEFAULT '1' COMMENT '是否允许评论(1是,0否)',
    `comment_count` BIGINT(20) NULL DEFAULT 0 COMMENT '评论数',
    `star_count` BIGINT(20) NULL DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

#### 9、评论表`t_comment`

| 字段        | 类型     | 描述                  |
| ----------- | -------- | --------------------- |
| id          | bigint   | 主键                  |
| article_id  | bigint   | 文章id                |
| user_id     | bigint   | 评论人id              |
| pid         | bigint   | 父级评论id            |
| content     | varchar  | 评论内容              |
| avatar      | varchar  | 评论人头像            |
| nickname    | varchar  | 评论人昵称            |
| url         | varchar  | 评论人网站地址        |
| support     | int      | 支持(赞)              |
| oppose      | int      | 反对(踩)              |
| status      | varchar  | 评论状态(1正常,0删除) |
| create_time | datetime | 创建时间              |
| update_time | datetime | 更新时间              |

```mysql
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id` BIGINT(20) NOT NULL COMMENT '文章id',
    `user_id` BIGINT(20) NOT NULL COMMENT '评论人id',
    `pid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '父级评论id',
    `content` VARCHAR(1024) NULL DEFAULT NULL COMMENT '评论内容',
    `avatar` VARCHAR(256) NULL DEFAULT NULL COMMENT '评论人头像',
    `nickname` VARCHAR(256) NULL DEFAULT NULL COMMENT '评论人昵称',
    `url` VARCHAR(256) NULL DEFAULT NULL COMMENT '评论人网站地址',
    `support` INT(16) NULL DEFAULT 0 COMMENT '支持(赞)',
    `oppose` INT(16) NULL DEFAULT 0 COMMENT '反对(踩)',
    `status` VARCHAr(2) NOT NULL DEFAULT '1' COMMENT '评论状态(1正常,0删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

#### 10、分类表`t_category`

| 字段          | 类型     | 描述                  |
| ------------- | -------- | --------------------- |
| id            | bigint   | 主键                  |
| category_name | varchar  | 分类名                |
| description   | varchar  | 分类描述              |
| status        | varchar  | 分类状态(1正常,0停用) |
| create_time   | datetime | 创建时间              |
| update_time   | datetime | 更新时间              |

```mysql
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_name` VARCHAR(32) NOT NULL COMMENT '分类名',
    `description` VARCHAR(128) NULL DEFAULT NULL COMMENT '分类描述',
    `status` VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '分类状态(1正常,0停用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

#### 11、标签表`t_tag`

| 字段        | 类型     | 描述                  |
| ----------- | -------- | --------------------- |
| id          | bigint   | 主键                  |
| tag_name    | varchar  | 标签名                |
| description | varchar  | 标签描述              |
| status      | varchar  | 标签状态(1正常,0停用) |
| create_time | datetime | 创建时间              |
| update_time | datetime | 更新时间              |

```mysql
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tag_name` VARCHAR(32) NOT NULL COMMENT '标签名',
    `description` VARCHAR(128) NULL DEFAULT NULL COMMENT '标签描述',
    `status` VARCHAR(2) NOT NULL DEFAULT '1' COMMENT '标签状态(1正常,0停用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY(`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARACTER SET=utf8mb4 ROW_FORMAT = DYNAMIC;
```

#### 12、文章-标签表`t_article_tag`

| 字段       | 类型   | 描述   |
| ---------- | ------ | ------ |
| id         | bigint | 主键   |
| article_id | bigint | 文章id |
| tag_id     | bigint | 标签id |

```mysql
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id` BIGINT(20) NOT NULL COMMENT '文章id',
    `tag_id` BIGINT(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB ROW_FORMAT = DYNAMIC;
```

