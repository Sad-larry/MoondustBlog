DROP DATABASE IF EXISTS `m_blog`;
CREATE DATABASE `m_blog`;
USE `m_blog`;
-- ---------------------
-- MBlog t_user 用户表 --
-- ---------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`   VARCHAR(32) NOT NULL COMMENT '用户名',
    `nick_name`   VARCHAR(32)          DEFAULT NULL COMMENT '昵称',
    `password`    VARCHAR(32) NOT NULL COMMENT '密码',
    `mobile`      VARCHAR(32)          DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(128)         DEFAULT NULL COMMENT '电子邮箱',
    `avatar`      VARCHAR(256)         DEFAULT NULL COMMENT '用户头像',
    `intro`       VARCHAR(256)         DEFAULT NULL COMMENT '个人简介',
    `birthday`    DATE                 DEFAULT NULL COMMENT '生日',
    `status`      VARCHAR(2)  NOT NULL DEFAULT 0 COMMENT '账号状态(1正常,0停用)',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` DATETIME    NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- ---------------------
-- MBlog t_role 角色表 --
-- ---------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`   VARCHAR(32)  NOT NULL COMMENT '角色名',
    `description` VARCHAR(128) NULL     DEFAULT NULL COMMENT '角色描述',
    `status`      VARCHAR(2)   NOT NULL DEFAULT 0 COMMENT '角色状态(1正常,0停用)',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- -----------------------------
-- MBlog t_user_role 用户角色表 --
-- -----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色id',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = INNODB
  ROW_FORMAT = DYNAMIC;

-- ---------------------
-- MBlog t_menu 菜单表 --
-- ---------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_name`   VARCHAR(32)  NOT NULL COMMENT '菜单名',
    `pid`         BIGINT(20)   NULL     DEFAULT NULL COMMENT '父级菜单id',
    `url`         VARCHAR(128) NULL     DEFAULT NULL COMMENT '路由地址',
    `component`   VARCHAR(128) NULL     DEFAULT NULL COMMENT '组件',
    `icon`        VARCHAR(128) NULL     DEFAULT NULL COMMENT '菜单图标',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- -----------------------------
-- MBlog t_role_menu 角色菜单表 --
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`
(
    `role_id` BIGINT(20) NOT NULL COMMENT '角色id',
    `menu_id` BIGINT(20) NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = INNODB
  ROW_FORMAT = DYNAMIC;

-- -------------------------
-- MBlog t_resource 资源表 --
-- -------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`
(
    `id`             BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `resource_name`  VARCHAR(32)  NOT NULL COMMENT '资源名',
    `url`            VARCHAR(128) NULL     DEFAULT NULL COMMENT '权限路径',
    `request_method` VARCHAR(128) NULL     DEFAULT NULL COMMENT '请求方式',
    `pid`            BIGINT(20)   NULL     DEFAULT NULL COMMENT '父模块id',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- ---------------------------------
-- MBlog t_role_resource 角色资源表 --
-- ---------------------------------
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource`
(
    `role_id`     BIGINT(20) NOT NULL COMMENT '角色id',
    `resource_id` BIGINT(20) NOT NULL COMMENT '权限id',
    PRIMARY KEY (`role_id`, `resource_id`) USING BTREE
) ENGINE = INNODB
  ROW_FORMAT = DYNAMIC;

-- ------------------------
-- MBlog t_article 文章表 --
-- ------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`
(
    `id`            BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       BIGINT(20)    NOT NULL COMMENT '作者',
    `title`         VARCHAR(256)  NULL     DEFAULT NULL COMMENT '文章标题',
    `content`       LONGTEXT      NULL COMMENT '文章内容',
    `summary`       VARCHAR(1024) NULL     DEFAULT NULL COMMENT '文章摘要',
    `category_id`   BIGINT(20)    NULL     DEFAULT NULL COMMENT '所属分类',
    `thumbnail`     VARCHAR(256)  NULL     DEFAULT NULL COMMENT '缩略图',
    `is_top`        VARCHAR(2)             DEFAULT '0' COMMENT '是否置顶(1是,0否)',
    `status`        VARCHAR(2)             DEFAULT '0' COMMENT '文章状态(1已发布,0草稿)',
    `view_count`    BIGINT(20)    NULL     DEFAULT 0 COMMENT '浏览量',
    `is_comment`    VARCHAR(2)             DEFAULT '1' COMMENT '是否允许评论(1是,0否)',
    `comment_count` BIGINT(20)    NULL     DEFAULT 0 COMMENT '评论数',
    `star_count`    BIGINT(20)    NULL     DEFAULT 0 COMMENT '点赞数',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- ------------------------
-- MBlog t_comment 评论表 --
-- ------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`
(
    `id`          BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id`  BIGINT(20)    NOT NULL COMMENT '文章id',
    `user_id`     BIGINT(20)    NOT NULL COMMENT '评论人id',
    `pid`         BIGINT(20)    NOT NULL DEFAULT 0 COMMENT '父级评论id',
    `content`     VARCHAR(1024) NULL     DEFAULT NULL COMMENT '评论内容',
    `avatar`      VARCHAR(256)  NULL     DEFAULT NULL COMMENT '评论人头像',
    `nickname`    VARCHAR(256)  NULL     DEFAULT NULL COMMENT '评论人昵称',
    `url`         VARCHAR(256)  NULL     DEFAULT NULL COMMENT '评论人网站地址',
    `support`     INT(16)       NULL     DEFAULT 0 COMMENT '支持(赞)',
    `oppose`      INT(16)       NULL     DEFAULT 0 COMMENT '反对(踩)',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- -------------------------
-- MBlog t_category 分类表 --
-- -------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`
(
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_name` VARCHAR(32)  NOT NULL COMMENT '分类名',
    `description`   VARCHAR(128) NULL     DEFAULT NULL COMMENT '分类描述',
    `status`        VARCHAR(2)   NOT NULL DEFAULT 0 COMMENT '分类状态(1正常,0停用)',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- ---------------------
-- MBlog t_tag 标签表 ---
-- ---------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tag_name`    VARCHAR(32)  NOT NULL COMMENT '标签名',
    `description` VARCHAR(128) NULL     DEFAULT NULL COMMENT '标签描述',
    `status`      VARCHAR(2)   NOT NULL DEFAULT 0 COMMENT '标签状态(1正常,0停用)',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  DEFAULT CHARACTER SET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- -------------------------------
-- MBlog t_article_tag 文章标签表 --
-- -------------------------------
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`
(
    `article_id` BIGINT(20) NOT NULL COMMENT '文章id',
    `tag_id`     BIGINT(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`article_id`, `tag_id`) USING BTREE
) ENGINE = INNODB
  ROW_FORMAT = DYNAMIC;