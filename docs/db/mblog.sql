CREATE DATABASE `m_blog`;
USE `m_blog`;
DROP TABLE IF EXISTS `t_admin_log`;
CREATE TABLE `t_admin_log`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`       varchar(255) NULL     DEFAULT NULL COMMENT '操作用户',
    `request_url`    varchar(255) NULL     DEFAULT NULL COMMENT '请求接口',
    `type`           varchar(255) NULL     DEFAULT NULL COMMENT '请求方式',
    `operation_name` varchar(255) NULL     DEFAULT NULL COMMENT '操作名称',
    `ip`             varchar(255) NULL     DEFAULT NULL COMMENT 'ip',
    `source`         varchar(255) NULL     DEFAULT NULL COMMENT 'ip来源',
    `spend_time`     bigint(20)   NULL     DEFAULT NULL COMMENT '请求接口耗时',
    `params_json`    mediumtext   NULL COMMENT '请求参数',
    `class_path`     varchar(255) NULL     DEFAULT NULL COMMENT '类地址',
    `method_name`    varchar(255) NULL     DEFAULT NULL COMMENT '方法名',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`      bigint(20)   NULL     DEFAULT NULL COMMENT '用户ID',
    `category_id`  bigint(20)   NULL     DEFAULT NULL COMMENT '分类ID',
    `title`        varchar(255) NOT NULL DEFAULT '' COMMENT '文章标题',
    `avatar`       varchar(255) NULL     DEFAULT NULL COMMENT '文章封面地址',
    `summary`      varchar(255) NOT NULL DEFAULT '' COMMENT '文章简介',
    `content`      longtext     NULL COMMENT '文章内容',
    `content_md`   longtext     NULL COMMENT '文章内容md版',
    `is_secret`    tinyint(1)   NULL     DEFAULT 0 COMMENT '是否是私密文章(0否,1是)',
    `is_stick`     tinyint(1)   NULL     DEFAULT 0 COMMENT '是否置顶(0否,1是)',
    `is_publish`   tinyint(1)   NULL     DEFAULT 1 COMMENT '是否发布(0草稿,1发布)',
    `is_original`  tinyint(1)   NULL     DEFAULT 1 COMMENT '是否原创(0转载,1原创)',
    `original_url` varchar(255) NULL     DEFAULT NULL COMMENT '转载地址',
    `quantity`     int(16)      NULL     DEFAULT 0 COMMENT '文章阅读量',
    `remark`       varchar(255) NULL     DEFAULT '' COMMENT '说明',
    `keywords`     varchar(255) NULL     DEFAULT NULL COMMENT 'SEO关键词',
    `create_time`  datetime     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_GENERAL_CI COMMENT = '博客文章表'
  ROW_FORMAT = DYNAMIC;
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id` bigint(20) NOT NULL COMMENT '文章ID',
    `tag_id`     bigint(20) NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_article_tag` (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '博客-标签关联表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(64) NOT NULL DEFAULT '' COMMENT '分类名称',
    `click_volume` int(16)     NULL     DEFAULT 0 COMMENT '分类点击量',
    `sort`         int(16)     NULL     DEFAULT 0 COMMENT '排序',
    `create_time`  datetime    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime    NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `category_name` (`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '博客分类表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`     bigint(20) NULL     DEFAULT NULL COMMENT '父评论ID',
    `article_id`    bigint(20) NOT NULL COMMENT '文章ID',
    `user_id`       bigint(20) NOT NULL COMMENT '评论人ID',
    `reply_user_id` bigint(20) NULL     DEFAULT NULL COMMENT '回复人ID',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `create_time`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '评论表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(64)  NULL     DEFAULT NULL COMMENT '字典名称',
    `type`        varchar(100) NOT NULL COMMENT '字典类型',
    `is_publish`  tinyint(1)   NULL     DEFAULT 1 COMMENT '是否发布(0否,1是)',
    `sort`        int(16)      NULL     DEFAULT 0 COMMENT '排序',
    `remark`      varchar(255) NULL     DEFAULT NULL COMMENT '备注',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data`
(
    `id`         bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_id`    bigint(20)   NOT NULL COMMENT '字典类型ID',
    `label`      varchar(100) NOT NULL COMMENT '字典标签',
    `value`      varchar(100) NOT NULL COMMENT '字典键值',
    `style`      varchar(50)  NULL DEFAULT NULL COMMENT '回显样式',
    `is_default` tinyint(1)   NULL DEFAULT 1 COMMENT '是否默认(0否,1是)',
    `is_publish` tinyint(1)   NULL DEFAULT 1 COMMENT '是否发布(0否,1是)',
    `sort`       int(16)      NULL DEFAULT 0 COMMENT '排序',
    `remark`     varchar(255) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典数据表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_exception_log`;
CREATE TABLE `t_exception_log`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`          varchar(255) NULL     DEFAULT NULL COMMENT '用户名',
    `ip`                varchar(255) NULL     DEFAULT NULL COMMENT 'IP',
    `ip_source`         varchar(255) NULL     DEFAULT NULL COMMENT 'ip来源',
    `method`            varchar(255) NULL     DEFAULT NULL COMMENT '请求方法',
    `operation`         mediumtext   NULL COMMENT '描述',
    `params`            mediumtext   NULL COMMENT '参数',
    `exception_json`    mediumtext   NULL COMMENT '异常对象json格式',
    `exception_message` mediumtext   NULL COMMENT '异常简单信息,等同于e.getMessage',
    `create_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '异常日志表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_feed_back`;
CREATE TABLE `t_feed_back`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `email`       varchar(100) NOT NULL COMMENT '邮箱',
    `title`       varchar(100) NOT NULL COMMENT '标题',
    `content`     varchar(255) NULL     DEFAULT NULL COMMENT '详细内容',
    `img_url`     varchar(255) NULL     DEFAULT NULL COMMENT '图片地址',
    `type`        tinyint(1)   NOT NULL COMMENT '反馈类型(1需求,2缺陷)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户反馈表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_friend_link`;
CREATE TABLE `t_friend_link`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(64)  NOT NULL COMMENT '网站名称',
    `url`         varchar(100) NOT NULL COMMENT '网站地址',
    `avatar`      varchar(255) NULL     DEFAULT NULL COMMENT '网站头像地址',
    `info`        varchar(255) NOT NULL COMMENT '网站描述',
    `email`       varchar(100) NOT NULL COMMENT '邮箱',
    `sort`        int(16)      NULL     DEFAULT 0 COMMENT '排序',
    `status`      tinyint(1)   NOT NULL DEFAULT 0 COMMENT 'ENUM-状态(0待审核,1通过)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '友情链接表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job`
(
    `job_id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `job_name`        varchar(64)  NOT NULL DEFAULT '' COMMENT '任务名称',
    `job_group`       varchar(64)  NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
    `invoke_target`   varchar(500) NOT NULL COMMENT '调用目标字符串',
    `cron_expression` varchar(255) NULL     DEFAULT '' COMMENT 'cron执行表达式',
    `misfire_policy`  tinyint(1)   NULL     DEFAULT 3 COMMENT '计划执行错误策略(1立即执行,2执行一次,3放弃执行)',
    `concurrent`      tinyint(1)   NULL     DEFAULT 0 COMMENT '是否并发执行(0禁止,1允许)',
    `status`          tinyint(1)   NULL     DEFAULT 1 COMMENT '任务状态(0暂停,1正常)',
    `remark`          varchar(500) NULL     DEFAULT '' COMMENT '备注信息',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_job_log`;
CREATE TABLE `t_job_log`
(
    `id`             bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    `job_id`         bigint(20)    NOT NULL COMMENT '任务ID',
    `job_name`       varchar(64)   NOT NULL COMMENT '任务名称',
    `job_group`      varchar(64)   NOT NULL COMMENT '任务组名',
    `invoke_target`  varchar(500)  NOT NULL COMMENT '调用目标字符串',
    `job_message`    varchar(500)  NULL     DEFAULT NULL COMMENT '日志信息',
    `status`         tinyint(1)    NULL     DEFAULT 1 COMMENT '执行状态(0失败,1正常)',
    `exception_info` varchar(2000) NULL     DEFAULT '' COMMENT '异常信息',
    `start_time`     datetime      NULL     DEFAULT NULL COMMENT '开始时间',
    `stop_time`      datetime      NULL     DEFAULT NULL COMMENT '结束时间',
    `create_time`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`    bigint(20)   NULL     DEFAULT 0 COMMENT '上级资源ID',
    `title`        varchar(100) NULL     DEFAULT NULL COMMENT '资源名称',
    `url`          varchar(255) NULL     DEFAULT '' COMMENT '路由地址',
    `component`    varchar(255) NULL     DEFAULT NULL COMMENT '资源组件',
    `level`        int(8)       NULL     DEFAULT 0 COMMENT '资源级别',
    `sort_no`      int(8)       NULL     DEFAULT 0 COMMENT '显示顺序',
    `icon`         varchar(64)  NULL     DEFAULT '#' COMMENT '资源图标',
    `type`         char(1)      NULL     DEFAULT '' COMMENT '菜单类型(M菜单,F按钮)',
    `redirect`     varchar(255) NULL     DEFAULT NULL COMMENT '重定向地址',
    `name`         varchar(255) NULL     DEFAULT NULL COMMENT '跳转地址',
    `hidden`       tinyint(1)   NULL     DEFAULT 0 COMMENT '是否隐藏(0否,1是)',
    `remark`       varchar(255) NULL     DEFAULT '' COMMENT '备注',
    `created_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限资源表 '
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `content`     mediumtext   NOT NULL COMMENT '内容',
    `nickname`    varchar(255) NULL     DEFAULT NULL COMMENT '用户昵称',
    `avatar`      varchar(255) NULL     DEFAULT NULL COMMENT '用户头像',
    `ip_address`  varchar(255) NULL     DEFAULT NULL COMMENT 'ip地址',
    `ip_source`   varchar(255) NULL     DEFAULT NULL COMMENT 'ip来源',
    `time`        bigint(20)   NULL     DEFAULT NULL COMMENT '留言时间',
    `status`      tinyint(1)   NOT NULL DEFAULT 0 COMMENT '状态(0审核,1正常)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '留言板表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_page`;
CREATE TABLE `t_page`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `page_name`   varchar(20)  NULL     DEFAULT NULL COMMENT '页面名称',
    `page_label`  varchar(20)  NULL     DEFAULT NULL COMMENT '页面标签',
    `page_cover`  varchar(255) NULL     DEFAULT NULL COMMENT '页面图源',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '前端页面表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_photo`;
CREATE TABLE `t_photo`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `album_id`    bigint(20)   NOT NULL COMMENT '相册ID',
    `name`        varchar(20)  NOT NULL COMMENT '照片名',
    `info`        varchar(50)  NULL     DEFAULT NULL COMMENT '照片描述',
    `url`         varchar(255) NOT NULL COMMENT '照片地址',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '照片'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_photo_album`;
CREATE TABLE `t_photo_album`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(20)  NOT NULL COMMENT '相册名',
    `info`        varchar(64)  NULL     DEFAULT NULL COMMENT '相册描述',
    `cover`       varchar(255) NOT NULL COMMENT '相册封面',
    `status`      tinyint(1)   NOT NULL DEFAULT 0 COMMENT '状态值(0公开,1私密)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '相册'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code`         varchar(32)  NULL     DEFAULT NULL COMMENT '角色编码',
    `name`         varchar(32)  NULL     DEFAULT NULL COMMENT '角色名称',
    `remark`       varchar(255) NULL     DEFAULT NULL COMMENT '角色描述',
    `created_time` datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime(0)  NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`
(
    `id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role_menu` (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色-权限资源关联表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(64) NOT NULL DEFAULT '' COMMENT '标签名称',
    `click_volume` int(16)     NULL     DEFAULT 0 COMMENT '标签点击量',
    `sort`         int(16)     NULL     DEFAULT 0 COMMENT '排序',
    `create_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime    NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `tag_name` (`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '博客标签表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`        varchar(64)  NULL     DEFAULT NULL COMMENT '账号',
    `password`        varchar(64)  NULL     DEFAULT NULL COMMENT '登录密码',
    `status`          tinyint(1)   NOT NULL DEFAULT 1 COMMENT '用户状态(0禁用,1正常)',
    `login_type`      int(16)      NULL     DEFAULT NULL COMMENT '登录方式',
    `user_auth_id`    bigint(20)   NULL     DEFAULT NULL COMMENT '用户详情ID',
    `role_id`         bigint(20)   NULL     DEFAULT NULL COMMENT '角色ID',
    `ip_address`      varchar(255) NULL     DEFAULT NULL COMMENT 'ip地址',
    `ip_source`       varchar(255) NULL     DEFAULT NULL COMMENT 'ip来源',
    `os`              varchar(100) NULL     DEFAULT NULL COMMENT '登录系统',
    `last_login_time` datetime     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    `browser`         varchar(255) NULL     DEFAULT NULL COMMENT '浏览器',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`username`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户基础信息表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE `t_user_auth`
(
    `id`          bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `email`       varchar(50)   NULL     DEFAULT NULL COMMENT '邮箱号',
    `nickname`    varchar(50)   NOT NULL COMMENT '用户昵称',
    `avatar`      varchar(1024) NOT NULL DEFAULT '' COMMENT '用户头像',
    `intro`       varchar(255)  NULL     DEFAULT NULL COMMENT '用户简介',
    `web_site`    varchar(255)  NULL     DEFAULT NULL COMMENT '个人网站',
    `is_disable`  tinyint(1)    NOT NULL DEFAULT 0 COMMENT '是否禁用(0否,1是)',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户信息表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     bigint(20)   NULL     DEFAULT NULL COMMENT '操作用户ID',
    `ip`          varchar(64)  NULL     DEFAULT NULL COMMENT 'ip地址',
    `address`     varchar(255) NULL     DEFAULT NULL COMMENT '操作地址',
    `type`        varchar(100) NULL     DEFAULT NULL COMMENT '操作类型',
    `description` varchar(255) NULL     DEFAULT NULL COMMENT '操作日志',
    `model`       varchar(255) NULL     DEFAULT NULL COMMENT '操作模块',
    `result`      varchar(255) NULL     DEFAULT NULL COMMENT '操作结果',
    `access_os`   varchar(255) NULL     DEFAULT NULL COMMENT '操作系统',
    `browser`     varchar(255) NULL     DEFAULT NULL COMMENT '浏览器',
    `client_type` varchar(255) NULL     DEFAULT NULL COMMENT '客户端类型',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '日志表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_role` (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config`
(
    `id`                          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `qi_niu_access_key`           varchar(255) NULL     DEFAULT NULL COMMENT '七牛云公钥',
    `qi_niu_secret_key`           varchar(255) NULL     DEFAULT NULL COMMENT '七牛云私钥',
    `qi_niu_area`                 varchar(20)  NULL     DEFAULT NULL COMMENT '七牛云存储区域',
    `qi_niu_bucket`               varchar(255) NULL     DEFAULT NULL COMMENT '七牛云上传空间',
    `qi_niu_picture_base_url`     varchar(255) NULL     DEFAULT NULL COMMENT '七牛云域名前缀+http',
    `upload_qi_niu`               tinyint(1)   NULL     DEFAULT NULL COMMENT '文件上传七牛云(0否,1是)',
    `open_email_activate`         tinyint(1)   NULL     DEFAULT NULL COMMENT '是否开启注册用户邮件激活(0否,1是)',
    `start_email_notification`    tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知(0否,1是)',
    `open_dashboard_notification` tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否开启仪表盘通知(0否,1是)',
    `dashboard_notification_md`   longtext     NULL COMMENT '仪表盘通知【用于首次登录弹框】MD',
    `dashboard_notification`      longtext     NULL COMMENT '仪表盘通知【用于首次登录弹框】',
    `search_model`                tinyint(1)   NOT NULL DEFAULT 0 COMMENT '搜索模式(0SQL搜索,1全文检索)',
    `email_host`                  varchar(100) NULL     DEFAULT NULL COMMENT '邮箱地址',
    `email_username`              varchar(100) NULL     DEFAULT NULL COMMENT '邮箱发件人',
    `email_password`              varchar(255) NULL     DEFAULT NULL COMMENT '邮箱授权码',
    `email_port`                  int(8)       NULL     DEFAULT NULL COMMENT '邮箱发送端口',
    `open_email`                  tinyint(1)   NULL     DEFAULT NULL COMMENT '启用邮箱发送(0否,1是)',
    `local_file_url`              varchar(255) NULL     DEFAULT NULL COMMENT '本地文件地址',
    `file_upload_way`             tinyint(1)   NULL     DEFAULT NULL COMMENT '文件上传方式(1本地,2七牛云)',
    `ali_yun_access_key`          varchar(100)          DEFAULT NULL COMMENT '阿里云ak',
    `ali_yun_secret_key`          varchar(100)          DEFAULT NULL COMMENT '阿里云sk',
    `ali_yun_bucket`              varchar(100)          DEFAULT NULL COMMENT '阿里云存储桶名',
    `ali_yun_endpoint`            varchar(100)          DEFAULT NULL COMMENT '阿里云Endpoint',
    `create_time`                 datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                 datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统配置表'
  ROW_FORMAT = Dynamic;
DROP TABLE IF EXISTS `t_web_config`;
CREATE TABLE `t_web_config`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `logo`            varchar(255) NOT NULL COMMENT 'logo(文件UID)',
    `name`            varchar(255) NOT NULL COMMENT '网站名称',
    `summary`         varchar(255) NOT NULL COMMENT '介绍',
    `keyword`         varchar(255) NOT NULL COMMENT '关键字',
    `author`          varchar(255) NOT NULL COMMENT '作者',
    `record_num`      varchar(255) NOT NULL COMMENT '备案号',
    `web_url`         varchar(255) NULL     DEFAULT NULL COMMENT '网站地址',
    `ali_pay`         varchar(64)  NULL     DEFAULT NULL COMMENT '支付宝收款码FileId',
    `weixin_pay`      varchar(64)  NULL     DEFAULT NULL COMMENT '微信收款码FileId',
    `github`          varchar(255) NULL     DEFAULT NULL COMMENT 'github地址',
    `gitee`           varchar(255) NULL     DEFAULT NULL COMMENT 'gitee地址',
    `qq_number`       varchar(20)  NULL     DEFAULT NULL COMMENT 'QQ号',
    `email`           varchar(255) NULL     DEFAULT NULL COMMENT '邮箱',
    `show_list`       varchar(255) NULL     DEFAULT NULL COMMENT '显示的列表(用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端)',
    `login_type_list` varchar(255) NULL     DEFAULT NULL COMMENT '登录方式列表(用于控制前端登录方式，如账号密码,码云,Github,QQ,微信)',
    `open_comment`    tinyint(1)   NULL     DEFAULT 1 COMMENT '是否开启评论(0否,1是)',
    `open_admiration` tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否开启赞赏(0否,1是)',
    `tourist_avatar`  varchar(255) NULL     DEFAULT NULL COMMENT '游客头像',
    `bulletin`        varchar(255) NULL     DEFAULT NULL COMMENT '公告',
    `author_info`     varchar(100) NULL     DEFAULT NULL COMMENT '作者简介',
    `author_avatar`   varchar(255) NULL     DEFAULT NULL COMMENT '作者头像',
    `about_me`        varchar(500) NULL     DEFAULT NULL COMMENT '关于我',
    `is_music_player` tinyint(1)   NULL     DEFAULT 0 COMMENT '是否开启音乐播放器(0否,1是)',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '网站配置表'
  ROW_FORMAT = Dynamic;
