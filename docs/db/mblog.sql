/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 8.0.12 : Database - m_blog
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = ''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS */`m_blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `m_blog`;

/*Table structure for table `t_article` */

DROP TABLE IF EXISTS `t_article`;

CREATE TABLE `t_article`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       bigint(20) NOT NULL COMMENT '作者',
    `title`         varchar(256)                                                DEFAULT NULL COMMENT '文章标题',
    `content`       longtext COMMENT '文章内容',
    `summary`       varchar(1024)                                               DEFAULT NULL COMMENT '文章摘要',
    `category_id`   bigint(20)                                                  DEFAULT NULL COMMENT '所属分类',
    `thumbnail`     varchar(256)                                                DEFAULT NULL COMMENT '缩略图',
    `is_top`        varchar(2)                                                  DEFAULT '0' COMMENT '是否置顶(1是,0否)',
    `status`        varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '文章状态(1发布,0草稿,-1待删除)',
    `view_count`    bigint(20)                                                  DEFAULT '0' COMMENT '浏览量',
    `is_comment`    varchar(2)                                                  DEFAULT '1' COMMENT '是否允许评论(1是,0否)',
    `comment_count` bigint(20)                                                  DEFAULT '0' COMMENT '评论数',
    `star_count`    bigint(20)                                                  DEFAULT '0' COMMENT '点赞数',
    `create_time`   datetime   NOT NULL                                         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime                                                    DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_article` */

insert into `t_article`(`id`, `user_id`, `title`, `content`, `summary`, `category_id`, `thumbnail`, `is_top`, `status`,
                        `view_count`, `is_comment`, `comment_count`, `star_count`, `create_time`, `update_time`)
values (1, 1, '第一篇测试博客', NULL, NULL, NULL, NULL, '0', '0', 0, '1', 0, 0, '2022-09-22 11:20:06', NULL);

/*Table structure for table `t_article_tag` */

DROP TABLE IF EXISTS `t_article_tag`;

CREATE TABLE `t_article_tag`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id` bigint(20) NOT NULL COMMENT '文章id',
    `tag_id`     bigint(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_article_tag` */

insert into `t_article_tag`(`article_id`, `tag_id`)
values (1, 1),
       (1, 2),
       (1, 3);

/*Table structure for table `t_category` */

DROP TABLE IF EXISTS `t_category`;

CREATE TABLE `t_category`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_name` varchar(32) NOT NULL COMMENT '分类名',
    `description`   varchar(128)         DEFAULT NULL COMMENT '分类描述',
    `status`        varchar(2)  NOT NULL DEFAULT '1' COMMENT '分类状态(1正常,0停用)',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_category` */

insert into `t_category`(`id`, `category_name`, `description`, `status`, `create_time`, `update_time`)
values (1, 'Java', 'java语言', '1', '2022-09-22 11:16:29', NULL),
       (2, 'Vue', 'vue框架', '1', '2022-09-22 11:16:43', NULL),
       (3, 'Mini-Program', '微信小程序', '1', '2022-09-22 11:16:56', NULL);

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id`  bigint(20) NOT NULL COMMENT '文章id',
    `user_id`     bigint(20) NOT NULL COMMENT '评论人id',
    `pid`         bigint(20) NOT NULL DEFAULT '0' COMMENT '父级评论id',
    `content`     varchar(1024)       DEFAULT NULL COMMENT '评论内容',
    `avatar`      varchar(256)        DEFAULT NULL COMMENT '评论人头像',
    `nickname`    varchar(256)        DEFAULT NULL COMMENT '评论人昵称',
    `url`         varchar(256)        DEFAULT NULL COMMENT '评论人网站地址',
    `support`     int(16)             DEFAULT '0' COMMENT '支持(赞)',
    `oppose`      int(16)             DEFAULT '0' COMMENT '反对(踩)',
    `status`      varchar(2) NOT NULL DEFAULT '1' COMMENT '评论状态(1正常,0删除)',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_comment` */

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_name`   varchar(32) NOT NULL COMMENT '菜单名',
    `pid`         bigint(20)           DEFAULT NULL COMMENT '父级菜单id',
    `url`         varchar(128)         DEFAULT NULL COMMENT '路由地址',
    `component`   varchar(128)         DEFAULT NULL COMMENT '组件',
    `icon`        varchar(128)         DEFAULT NULL COMMENT '菜单图标',
    `status`      varchar(2)  NOT NULL DEFAULT '1' COMMENT '菜单状态(1正常,0停用)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 503
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_menu` */

insert into `t_menu`(`id`, `menu_name`, `pid`, `url`, `component`, `icon`, `status`, `create_time`, `update_time`)
values (100, '首页', 0, '/', '/home/Home.vue', NULL, '1', '2022-09-22 10:49:37', NULL),
       (200, '文章管理', 0, '/article-submenu', NULL, NULL, '1', '2022-09-22 10:52:42', NULL),
       (201, '发表文章', 200, '/article-push', '/article/Article.vue', NULL, '1', '2022-09-22 10:54:36', NULL),
       (202, '文章列表', 200, '/article-list', '/article/ArticleList.vue', NULL, '1', '2022-09-22 10:55:34', NULL),
       (203, '标签列表', 200, '/tag-list', '/tag/TagList.vue', NULL, '1', '2022-09-22 10:56:41', NULL),
       (204, '分类列表', 200, '/category-list', '/category/CategoryList.vue', NULL, '1', '2022-09-22 10:57:24', NULL),
       (300, '用户管理', 0, '/users-submenu', NULL, NULL, '1', '2022-09-22 10:58:39', NULL),
       (301, '用户列表', 300, '/user-list', '/user/UserList.vue', NULL, '1', '2022-09-22 10:59:34', NULL),
       (302, '用户评论', 300, '/comment-list', '/comment/CommentList.vue', NULL, '1', '2022-09-22 11:01:36', NULL),
       (400, '权限管理', 0, '/permission-submenu', NULL, NULL, '1', '2022-09-22 11:02:33', NULL),
       (401, '菜单列表', 400, '/menu-list', '/menu/MenuList.vue', NULL, '1', '2022-09-22 11:03:35', NULL),
       (402, '资源列表', 400, '/resource-list', '/resource/ResourceList.vue', NULL, '1', '2022-09-22 11:04:34', NULL),
       (403, '角色列表', 400, '/role-list', '/role/RoleList.vue', NULL, '1', '2022-09-22 11:05:09', NULL),
       (500, '系统管理', 0, '/system-submenu', NULL, NULL, '1', '2022-09-22 11:05:36', NULL),
       (501, '定时任务', 500, '/quartz', '/system/QuartzList.vue', NULL, '1', '2022-09-22 11:06:22', NULL),
       (502, '关于我', 500, '/about', '/system/AboutMe.vue', NULL, '1', '2022-09-22 11:06:44', NULL);

/*Table structure for table `t_resource` */

DROP TABLE IF EXISTS `t_resource`;

CREATE TABLE `t_resource`
(
    `id`             bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `resource_name`  varchar(32) NOT NULL COMMENT '资源名',
    `url`            varchar(128)         DEFAULT NULL COMMENT '权限路径',
    `request_method` varchar(128)         DEFAULT NULL COMMENT '请求方式',
    `pid`            bigint(20)           DEFAULT NULL COMMENT '父模块id',
    `create_time`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_resource` */

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`   varchar(32) NOT NULL COMMENT '角色名',
    `description` varchar(128)         DEFAULT NULL COMMENT '角色描述',
    `status`      varchar(2)  NOT NULL DEFAULT '1' COMMENT '角色状态(1正常,0停用)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_role` */

insert into `t_role`(`id`, `role_name`, `description`, `status`, `create_time`, `update_time`)
values (1, 'admin', '管理员', '1', '2022-09-22 09:51:36', NULL),
       (2, 'user', '普通用户', '1', '2022-09-22 11:09:14', NULL),
       (3, 'admin-test', '测试的管理员', '1', '2022-09-22 11:12:59', NULL);

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` bigint(20) NOT NULL COMMENT '角色id',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_role_menu` */

insert into `t_role_menu`(`role_id`, `menu_id`)
values (1, 100),
       (1, 200),
       (1, 201),
       (1, 202),
       (1, 203),
       (1, 204),
       (1, 300),
       (1, 301),
       (1, 302),
       (1, 400),
       (1, 401),
       (1, 402),
       (1, 403),
       (1, 500),
       (1, 501),
       (1, 502);

/*Table structure for table `t_role_resource` */

DROP TABLE IF EXISTS `t_role_resource`;

CREATE TABLE `t_role_resource`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`     bigint(20) NOT NULL COMMENT '角色id',
    `resource_id` bigint(20) NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_role_resource` */

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tag_name`    varchar(32) NOT NULL COMMENT '标签名',
    `description` varchar(128)         DEFAULT NULL COMMENT '标签描述',
    `status`      varchar(2)  NOT NULL DEFAULT '1' COMMENT '标签状态(1正常,0停用)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_tag` */

insert into `t_tag`(`id`, `tag_name`, `description`, `status`, `create_time`, `update_time`)
values (1, 'Java', 'java', '1', '2022-09-22 11:18:36', NULL),
       (2, 'Vue', 'vue', '1', '2022-09-22 11:18:44', NULL),
       (3, 'mini-program', 'mini-program', '1', '2022-09-22 11:18:54', NULL);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`   varchar(32) NOT NULL COMMENT '用户名',
    `nick_name`   varchar(32)          DEFAULT NULL COMMENT '昵称',
    `password`    varchar(32) NOT NULL COMMENT '密码',
    `mobile`      varchar(32)          DEFAULT NULL COMMENT '手机号',
    `email`       varchar(128)         DEFAULT NULL COMMENT '电子邮箱',
    `avatar`      varchar(256)         DEFAULT NULL COMMENT '用户头像',
    `intro`       varchar(256)         DEFAULT NULL COMMENT '个人简介',
    `birthday`    date                 DEFAULT NULL COMMENT '生日',
    `status`      varchar(2)  NOT NULL DEFAULT '1' COMMENT '账号状态(1正常,0停用)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_user` */

insert into `t_user`(`id`, `user_name`, `nick_name`, `password`, `mobile`, `email`, `avatar`, `intro`, `birthday`,
                     `status`, `create_time`, `update_time`)
values (1, 'md', 'moondust', '1', '15179853855', 'md@md.md', NULL, '管理员', '2022-09-21', '1', '2022-09-22 09:44:38',
        NULL),
       (2, 'test', 'test-name', '1', '111', '1@1.1', NULL, '测试的普通用户', '2022-09-22', '1', '2022-09-22 11:10:02', NULL);

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `role_id` bigint(20) NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

/*Data for the table `t_user_role` */

insert into `t_user_role`(`user_id`, `role_id`)
values (1, 1),
       (2, 2),
       (2, 3);

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
