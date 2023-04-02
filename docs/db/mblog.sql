CREATE DATABASE `m_blog`;
USE `m_blog`;
/*Table structure for table `t_admin_log` */

DROP TABLE IF EXISTS `t_admin_log`;

CREATE TABLE `t_admin_log`
(
    `id`             bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`       varchar(255)      DEFAULT NULL COMMENT '操作用户',
    `request_url`    varchar(255)      DEFAULT NULL COMMENT '请求接口',
    `type`           varchar(255)      DEFAULT NULL COMMENT '请求方式',
    `operation_name` varchar(255)      DEFAULT NULL COMMENT '操作名称',
    `ip`             varchar(255)      DEFAULT NULL COMMENT 'ip',
    `source`         varchar(255)      DEFAULT NULL COMMENT 'ip来源',
    `spend_time`     bigint            DEFAULT NULL COMMENT '请求接口耗时',
    `params_json`    mediumtext COMMENT '请求参数',
    `class_path`     varchar(255)      DEFAULT NULL COMMENT '类地址',
    `method_name`    varchar(255)      DEFAULT NULL COMMENT '方法名',
    `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='操作日志表';

/*Table structure for table `t_article` */

DROP TABLE IF EXISTS `t_article`;

CREATE TABLE `t_article`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`      bigint                DEFAULT NULL COMMENT '用户ID',
    `category_id`  bigint                DEFAULT NULL COMMENT '分类ID',
    `title`        varchar(255) NOT NULL DEFAULT '' COMMENT '文章标题',
    `avatar`       varchar(255)          DEFAULT NULL COMMENT '文章封面地址',
    `summary`      varchar(255) NOT NULL DEFAULT '' COMMENT '文章简介',
    `content`      longtext COMMENT '文章内容',
    `content_md`   longtext COMMENT '文章内容md版',
    `is_secret`    tinyint(1)            DEFAULT '0' COMMENT '是否是私密文章(0否,1是)',
    `is_stick`     tinyint(1)            DEFAULT '0' COMMENT '是否置顶(0否,1是)',
    `is_publish`   tinyint(1)            DEFAULT '1' COMMENT '是否发布(0草稿,1发布)',
    `is_original`  tinyint(1)            DEFAULT '1' COMMENT '是否原创(0转载,1原创)',
    `original_url` varchar(255)          DEFAULT NULL COMMENT '转载地址',
    `quantity`     int                   DEFAULT '0' COMMENT '文章阅读量',
    `remark`       varchar(255)          DEFAULT '' COMMENT '说明',
    `keywords`     varchar(255)          DEFAULT NULL COMMENT 'SEO关键词',
    `create_time`  datetime              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='博客文章表';

/*Table structure for table `t_article_tag` */

DROP TABLE IF EXISTS `t_article_tag`;

CREATE TABLE `t_article_tag`
(
    `id`         bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `tag_id`     bigint NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='博客-标签关联表';

/*Table structure for table `t_category` */

DROP TABLE IF EXISTS `t_category`;

CREATE TABLE `t_category`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(64) NOT NULL DEFAULT '' COMMENT '分类名称',
    `click_volume` int                  DEFAULT '0' COMMENT '分类点击量',
    `sort`         int                  DEFAULT '0' COMMENT '排序',
    `create_time`  datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `category_name` (`name`) USING BTREE COMMENT '博客分类名称'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='博客分类表';

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment`
(
    `id`            bigint     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`     bigint              DEFAULT NULL COMMENT '父评论ID',
    `article_id`    bigint     NOT NULL COMMENT '文章ID',
    `user_id`       bigint     NOT NULL COMMENT '评论人ID',
    `reply_user_id` bigint              DEFAULT NULL COMMENT '回复人ID',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `create_time`   datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='评论表';

/*Table structure for table `t_dict` */

DROP TABLE IF EXISTS `t_dict`;

CREATE TABLE `t_dict`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(64)           DEFAULT NULL COMMENT '字典名称',
    `type`        varchar(100) NOT NULL COMMENT '字典类型',
    `is_publish`  tinyint(1)            DEFAULT '1' COMMENT '是否发布(0否,1是)',
    `sort`        int                   DEFAULT '0' COMMENT '排序',
    `remark`      varchar(255)          DEFAULT NULL COMMENT '备注',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='字典表';

/*Data for the table `t_dict` */

insert into `t_dict`(`id`, `name`, `type`, `is_publish`, `sort`, `remark`, `create_time`, `update_time`)
values (1, '系统是否', 'sys_yes_no', 1, 0, '系统是否列表', '2022-11-11 09:27:18', '2022-11-11 15:47:24'),
       (2, '系统开关', 'sys_normal_disable', 1, 0, '系统开关列表', '2022-11-11 09:27:31', NULL),
       (3, '博客登录方式', 'sys_login_method', 1, 0, '博客登录方式', '2022-11-11 09:27:44', NULL),
       (4, '发布状态', 'sys_publish_status', 1, 0, '发布状态列表', '2022-11-11 14:40:24', NULL),
       (5, '定时任务分组', 'sys_job_group', 1, 0, '定时任务分组列表', '2022-11-19 10:17:01', NULL),
       (6, '定时任务状态', 'sys_job_status', 1, 0, '任务状态列表', '2022-11-19 10:19:04', NULL),
       (7, '任务执行策略', 'sys_job_misfire', 1, 0, '任务执行策略列表', '2022-11-19 13:52:02', NULL);

/*Table structure for table `t_dict_data` */

DROP TABLE IF EXISTS `t_dict_data`;

CREATE TABLE `t_dict_data`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_id`    bigint       NOT NULL COMMENT '字典类型ID',
    `label`      varchar(100) NOT NULL COMMENT '字典标签',
    `value`      varchar(100) NOT NULL COMMENT '字典键值',
    `style`      varchar(50)  DEFAULT NULL COMMENT '回显样式',
    `is_default` tinyint(1)   DEFAULT '1' COMMENT '是否默认(0否,1是)',
    `is_publish` tinyint(1)   DEFAULT '1' COMMENT '是否发布(0否,1是)',
    `sort`       int          DEFAULT '0' COMMENT '排序',
    `remark`     varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='字典数据表';

/*Data for the table `t_dict_data` */

insert into `t_dict_data`(`id`, `dict_id`, `label`, `value`, `style`, `is_default`, `is_publish`, `sort`, `remark`)
values (1, 1, '是', '1', 'success', 1, 1, 0, '系统是否 是'),
       (2, 1, '否', '0', 'warning', 0, 1, 0, '系统是否 否'),
       (3, 2, '开启', '1', 'success', 1, 1, 0, '系统开关 开启'),
       (4, 2, '关闭', '0', 'warning', 0, 1, 0, '系统开关 关闭'),
       (5, 3, '账号', '1', 'primary', 1, 1, 0, '博客账号密码登录'),
       (6, 3, '邮箱', '2', 'warning', 0, 1, 0, '邮箱账号密码登录'),
       (7, 3, 'QQ', '3', 'success', 0, 1, 0, 'QQ登录'),
       (8, 4, '发布', '1', 'success', 1, 1, 0, '发布状态 发布'),
       (9, 4, '下架', '0', 'danger', 0, 1, 0, '发布状态 下架'),
       (10, 5, '默认', 'DEFAULT', 'primary', 1, 1, 1, '默认分组'),
       (11, 5, '系统', 'SYSTEM', 'warning', 0, 1, 2, '系统分组'),
       (12, 6, '正常', '1', 'primary', 1, 1, 1, '正常状态'),
       (13, 6, '暂停', '0', 'danger', 0, 1, 2, '暂停状态'),
       (14, 7, '默认策略', '0', 'default', 1, 1, 1, '默认策略'),
       (15, 7, '立即执行', '1', 'info', 0, 1, 2, '立即执行'),
       (16, 7, '执行一次', '2', 'warning', 0, 1, 3, '执行一次'),
       (17, 7, '放弃执行', '3', 'danger', 0, 1, 4, '放弃执行');

/*Table structure for table `t_exception_log` */

DROP TABLE IF EXISTS `t_exception_log`;

CREATE TABLE `t_exception_log`
(
    `id`                bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`          varchar(255)      DEFAULT NULL COMMENT '用户名',
    `ip`                varchar(255)      DEFAULT NULL COMMENT 'IP',
    `ip_source`         varchar(255)      DEFAULT NULL COMMENT 'ip来源',
    `method`            varchar(255)      DEFAULT NULL COMMENT '请求方法',
    `operation`         mediumtext COMMENT '描述',
    `params`            mediumtext COMMENT '参数',
    `exception_json`    mediumtext COMMENT '异常对象json格式',
    `exception_message` mediumtext COMMENT '异常简单信息,等同于e.getMessage',
    `create_time`       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='异常日志表';

/*Table structure for table `t_feed_back` */

DROP TABLE IF EXISTS `t_feed_back`;

CREATE TABLE `t_feed_back`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `email`         varchar(100) NOT NULL COMMENT '邮箱',
    `title`         varchar(100) NOT NULL COMMENT '标题',
    `content`       varchar(255)          DEFAULT NULL COMMENT '详细内容',
    `reply_content` varchar(255)          DEFAULT NULL COMMENT '答复内容',
    `img_url`       varchar(255)          DEFAULT NULL COMMENT '图片地址',
    `type`          tinyint(1)   NOT NULL COMMENT '反馈类型(1需求,2缺陷)',
    `status`        tinyint(1)   NOT NULL DEFAULT '0' COMMENT '状态(0未答复,1已答复)',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户反馈表';

/*Table structure for table `t_friend_link` */

DROP TABLE IF EXISTS `t_friend_link`;

CREATE TABLE `t_friend_link`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(64)  NOT NULL COMMENT '网站名称',
    `url`         varchar(100) NOT NULL COMMENT '网站地址',
    `avatar`      varchar(255)          DEFAULT NULL COMMENT '网站头像地址',
    `info`        varchar(255) NOT NULL COMMENT '网站描述',
    `email`       varchar(100) NOT NULL COMMENT '邮箱',
    `sort`        int                   DEFAULT '0' COMMENT '排序',
    `status`      tinyint(1)   NOT NULL DEFAULT '0' COMMENT 'ENUM-状态(0待审核,1通过)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='友情链接表';

/*Table structure for table `t_job` */

DROP TABLE IF EXISTS `t_job`;

CREATE TABLE `t_job`
(
    `job_id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `job_name`        varchar(64)  NOT NULL DEFAULT '' COMMENT '任务名称',
    `job_group`       varchar(64)  NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
    `invoke_target`   varchar(500) NOT NULL COMMENT '调用目标字符串',
    `cron_expression` varchar(255)          DEFAULT '' COMMENT 'cron执行表达式',
    `misfire_policy`  tinyint(1)            DEFAULT '3' COMMENT '计划执行错误策略(0默认策略,1立即执行,2执行一次,3放弃执行)',
    `concurrent`      tinyint(1)            DEFAULT '0' COMMENT '是否并发执行(0禁止,1允许)',
    `status`          tinyint(1)            DEFAULT '1' COMMENT '任务状态(0暂停,1正常)',
    `remark`          varchar(500)          DEFAULT '' COMMENT '备注信息',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='定时任务调度表';

/*Data for the table `t_job` */

insert into `t_job`(`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`,
                    `concurrent`, `status`, `remark`, `create_time`, `update_time`)
values (1, '自动更新文章阅读数', 'DEFAULT', 'blogQuartz.updateReadQuantity()', '0 0 0/1 * * ?', 2, 1, 1, '每一小时执行一次',
        '2022-11-18 20:25:36', '2023-03-17 21:01:24'),
       (3, '定时清理离线用户', 'DEFAULT', 'blogQuartz.clearOfflineUser(30)', '0 0,30 * * * ?', 2, 0, 1, '每30分钟执行一次',
        '2023-02-02 13:54:30', '2023-03-17 21:00:06'),
       (4, '定时清理过期的登录用户', 'DEFAULT', 'blogQuartz.clearExpireToken()', '0 0 0 1/1 * ?', 0, 0, 1, '每天执行一次',
        '2023-02-03 11:29:35', '2023-03-17 21:00:18');

/*Table structure for table `t_job_log` */

DROP TABLE IF EXISTS `t_job_log`;

CREATE TABLE `t_job_log`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    `job_id`         bigint       NOT NULL COMMENT '任务ID',
    `job_name`       varchar(64)  NOT NULL COMMENT '任务名称',
    `job_group`      varchar(64)  NOT NULL COMMENT '任务组名',
    `invoke_target`  varchar(500) NOT NULL COMMENT '调用目标字符串',
    `job_message`    varchar(500)          DEFAULT NULL COMMENT '日志信息',
    `status`         tinyint(1)            DEFAULT '1' COMMENT '执行状态(0失败,1正常)',
    `exception_info` varchar(2000)         DEFAULT '' COMMENT '异常信息',
    `start_time`     datetime              DEFAULT NULL COMMENT '开始时间',
    `stop_time`      datetime              DEFAULT NULL COMMENT '结束时间',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='定时任务调度日志表';

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`   bigint            DEFAULT '0' COMMENT '上级资源ID',
    `title`       varchar(100)      DEFAULT NULL COMMENT '资源名称',
    `url`         varchar(255)      DEFAULT '' COMMENT '路由地址',
    `component`   varchar(255)      DEFAULT NULL COMMENT '资源组件',
    `level`       int               DEFAULT '0' COMMENT '资源级别',
    `sort_no`     int               DEFAULT '0' COMMENT '显示顺序',
    `icon`        varchar(64)       DEFAULT NULL COMMENT '资源图标',
    `type`        char(1)           DEFAULT 'M' COMMENT '菜单类型(M菜单,F按钮)',
    `perms`       varchar(100)      DEFAULT NULL COMMENT '权限标识',
    `redirect`    varchar(255)      DEFAULT NULL COMMENT '重定向地址',
    `name`        varchar(255)      DEFAULT NULL COMMENT '跳转地址',
    `hidden`      tinyint(1)        DEFAULT '0' COMMENT '是否隐藏(0否,1是)',
    `is_cache`    tinyint(1)        DEFAULT '1' COMMENT '是否缓存（0不缓存,1缓存）',
    `remark`      varchar(255)      DEFAULT '' COMMENT '备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime          DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='权限资源表 ';

/*Data for the table `t_menu` */

insert into `t_menu`(`id`, `parent_id`, `title`, `url`, `component`, `level`, `sort_no`, `icon`, `type`, `perms`,
                     `redirect`, `name`, `hidden`, `is_cache`, `remark`, `create_time`, `update_time`)
values (1, 0, '其他', '/other', 'Layout', 0, 1, 'el-icon-more-outline', 'M', NULL, NULL, 'other', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (2, 0, '文章管理', '/articles', 'Layout', 0, 2, 'el-icon-document-copy', 'M', NULL, NULL, 'articles', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (3, 0, '网站管理', '/site', 'Layout', 0, 3, 'el-icon-guide', 'M', NULL, NULL, 'site', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (4, 0, '消息管理', '/news', 'Layout', 0, 4, 'el-icon-bell', 'M', NULL, NULL, 'news', 0, 1, '', '2022-10-31 10:25:41',
        NULL),
       (5, 0, '相册管理', '/album', 'Layout', 0, 5, 'el-icon-picture-outline', 'M', NULL, NULL, 'album', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (6, 0, '日志管理', '/logs', 'Layout', 0, 6, 'el-icon-document', 'M', NULL, NULL, 'logs', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (7, 0, '系统管理', '/system', 'Layout', 0, 7, 'el-icon-setting', 'M', NULL, NULL, 'system', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (8, 0, '监控中心', '/listener', 'Layout', 0, 8, 'el-icon-monitor', 'M', NULL, NULL, 'listener', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (101, 1, '首页', '/home', 'Layout', 1, 1, 'el-icon-picture-outline', 'M', NULL, NULL, 'home', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (102, 1, '图片管理', '/image', '/image', 1, 2, 'el-icon-s-home', 'M', NULL, NULL, 'image', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (103, 1, '个人中心', '/user/profile', '/system/user/profile', 1, 3, 'el-icon-user-solid', 'M', NULL, NULL,
        'userProfile', 1, 1, '', '2023-03-23 21:46:57', '2023-03-23 21:46:57'),
       (201, 2, '发布文章', '/publish', '/articles/article/publish', 1, 1, 'el-icon-edit', 'M', NULL, NULL, 'publish', 0, 1,
        '', '2022-11-04 10:25:02', '2022-11-04 10:25:02'),
       (202, 2, '文章管理', '/article', '/articles/article', 1, 2, 'el-icon-notebook-2', 'M', NULL, NULL, 'article', 0, 1,
        '', '2022-11-04 10:20:58', '2022-11-04 10:20:58'),
       (203, 2, '标签管理', '/tags', '/articles/tags', 1, 3, 'el-icon-collection-tag', 'M', NULL, NULL, 'tags', 0, 1, '',
        '2022-11-04 10:20:43', '2022-11-04 10:20:43'),
       (204, 2, '分类管理', '/category', '/articles/category', 1, 4, 'el-icon-files', 'M', NULL, NULL, 'category', 0, 1, '',
        '2022-11-04 10:20:35', '2022-11-04 10:20:35'),
       (301, 3, '字典管理', '/dict', '/site/dict/index', 1, 1, 'el-icon-heavy-rain', 'M', NULL, NULL, 'dict', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (302, 3, '字典数据', '/dictData', '/site/dict/data', 1, 2, 'el-icon-sunset', 'M', NULL, NULL, 'dictData', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (303, 3, '友情链接', '/friendLink', '/site/friendLink', 1, 3, 'el-icon-link', 'M', NULL, NULL, 'friendLink', 0, 1,
        '', '2022-10-31 10:25:41', NULL),
       (304, 3, '网站配置', '/webConfig', '/site/webConfig', 1, 4, 'el-icon-setting', 'M', NULL, NULL, 'webConfig', 0, 1,
        '', '2022-10-31 10:25:41', NULL),
       (305, 3, '页面管理', '/webPage', '/site/webPage', 1, 5, 'el-icon-document-remove', 'M', NULL, NULL, 'webPage', 0, 1,
        '', '2022-11-27 13:53:54', '2022-11-27 13:53:54'),
       (401, 4, '评论管理', '/comment', '/news/comment', 1, 1, 'el-icon-chat-dot-round', 'M', NULL, NULL, 'comment', 0, 1,
        '', '2022-10-31 10:25:41', NULL),
       (402, 4, '留言管理', '/messages', '/news/message', 1, 2, 'el-icon-message', 'M', NULL, NULL, 'messages', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (403, 4, '反馈管理', '/feedbacks', '/news/feedback', 1, 3, 'el-icon-warning-outline', 'M', NULL, NULL, 'feedbacks',
        0, 1, '', '2022-10-31 10:25:41', NULL),
       (501, 5, '相册列表', '/albums', '/site/album/album', 1, 1, 'el-icon-camera', 'M', NULL, NULL, 'albums', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (502, 5, '照片管理', '/photos', '/site/album/photo', 1, 2, 'el-icon-camera', 'M', NULL, NULL, 'photos', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (601, 6, '用户日志', '/userLog', '/logs/userLog', 1, 1, 'el-icon-coordinate', 'M', NULL, NULL, 'userLog', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (602, 6, '操作日志', '/adminLog', '/logs/adminLog', 1, 2, 'el-icon-magic-stick', 'M', NULL, NULL, 'adminLog', 0, 1,
        '', '2022-10-31 10:25:41', NULL),
       (603, 6, '异常日志', '/exceptionLog', '/logs/exceptionLog', 1, 3, 'el-icon-cpu', 'M', NULL, NULL, 'exceptionLog', 0,
        1, '', '2022-10-31 10:25:41', NULL),
       (701, 7, '用户管理', '/user', '/system/user', 1, 1, 'el-icon-user', 'M', NULL, NULL, 'user', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (702, 7, '角色管理', '/role', '/system/role', 1, 2, 'el-icon-user-solid', 'M', NULL, NULL, 'role', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (703, 7, '系统配置', '/systemconfig', '/system/config', 1, 3, 'el-icon-setting', 'M', NULL, NULL, 'systemconfig', 0,
        1, '', '2022-10-31 10:25:41', NULL),
       (704, 7, '接口管理', '/api', '/system/api', 1, 4, 'el-icon-edit', 'M', NULL, NULL, 'api', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (705, 7, '菜单管理', '/menu', '/system/menu', 1, 5, 'el-icon-menu', 'M', NULL, NULL, 'menu', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (801, 8, '定时任务', '/job', '/listener/job', 1, 1, 'el-icon-coordinate', 'M', NULL, NULL, 'job', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (802, 8, '任务日志', '/jobLog', '/listener/job/log', 1, 2, 'el-icon-help', 'M', NULL, NULL, 'jobLog', 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (803, 8, '在线用户', '/onlineUser', '/listener/user', 1, 3, 'el-icon-user', 'M', NULL, NULL, 'onlineUser', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (804, 8, 'Druid监控', '/druids', '/listener/druid', 1, 4, 'el-icon-help', 'M', NULL, NULL, 'druids', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (805, 8, '缓存监控', '/cache', '/listener/cache', 1, 5, 'el-icon-hot-water', 'M', NULL, NULL, 'cache', 0, 1, '',
        '2022-10-31 10:25:41', NULL),
       (806, 8, '七牛监控', '/qiniu', '/listener/qiniu', 1, 6, 'el-icon-tickets', 'M', NULL, NULL, 'qiniu', 0, 1, '',
        '2022-11-23 10:23:02', '2022-11-23 10:23:02'),
       (10101, 101, '首页图表统计信息', '/system/home/init', '', 2, NULL, NULL, 'F', 'system:home:init', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (10201, 102, '上传图片', '/system/upload/image', '', 2, NULL, NULL, 'F', 'system:upload:image', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (10301, 103, '登录用户信息', '/system/user/getInfo', '', 2, NULL, NULL, 'F', 'system:user:getInfo', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (20101, 201, '发表文章', '/system/article/publish', '', 2, NULL, NULL, 'F', 'system:article:publish', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (20102, 201, '修改文章', '/system/article/update', '', 2, NULL, NULL, 'F', 'system:article:update', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (20103, 201, '更新文章分类', '/system/article/updateCategory', '', 2, NULL, NULL, 'F', 'system:article:updateCategory',
        NULL, NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (20104, 201, '更新文章标签', '/system/article/updateTags', '', 2, NULL, NULL, 'F', 'system:article:updateTags', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (20105, 201, '上传本地文章', '/system/article/upload', '', 2, NULL, NULL, 'F', 'system:article:upload', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (20106, 201, '文章详情', '/system/article/info', '', 2, NULL, NULL, 'F', 'system:article:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20201, 202, '文章列表', '/system/article/list', '', 2, NULL, NULL, 'F', 'system:article:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20202, 202, '删除文章', '/system/article/delete', '', 2, NULL, NULL, 'F', 'system:article:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (20203, 202, '文章置顶', '/system/article/top', '', 2, NULL, NULL, 'F', 'system:article:top', NULL, NULL, 1, 1, '',
        '2022-11-08 17:08:07', NULL),
       (20204, 202, '发布或下架文章', '/system/article/pubOrShelf', '', 2, NULL, NULL, 'F', 'system:article:pubOrShelf', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (20301, 203, '标签列表', '/system/tags/list', '', 2, NULL, NULL, 'F', 'system:tags:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20302, 203, '标签详情', '/system/tags/info', '', 2, NULL, NULL, 'F', 'system:tags:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20303, 203, '添加标签', '/system/tags/add', '', 2, NULL, NULL, 'F', 'system:tags:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20304, 203, '修改标签', '/system/tags/update', '', 2, NULL, NULL, 'F', 'system:tags:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20305, 203, '删除标签', '/system/tags/delete', '', 2, NULL, NULL, 'F', 'system:tags:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20401, 204, '分类列表', '/system/category/list', '', 2, NULL, NULL, 'F', 'system:category:list', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (20402, 204, '分类详情', '/system/category/info', '', 2, NULL, NULL, 'F', 'system:category:info', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (20403, 204, '添加分类', '/system/category/add', '', 2, NULL, NULL, 'F', 'system:category:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (20404, 204, '修改分类', '/system/category/update', '', 2, NULL, NULL, 'F', 'system:category:update', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (20405, 204, '删除分类', '/system/category/delete', '', 2, NULL, NULL, 'F', 'system:category:delete', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (30101, 301, '字典列表', '/system/dict/list', '', 2, NULL, NULL, 'F', 'system:dict:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30102, 301, '添加字典', '/system/dict/add', '', 2, NULL, NULL, 'F', 'system:dict:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30103, 301, '修改字典', '/system/dict/update', '', 2, NULL, NULL, 'F', 'system:dict:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30104, 301, '删除字典', '/system/dict/delete', '', 2, NULL, NULL, 'F', 'system:dict:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30201, 302, '字典数据列表', '/system/dictData/list', '', 2, NULL, NULL, 'F', 'system:dictData:list', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (30202, 302, '添加字典数据', '/system/dictData/add', '', 2, NULL, NULL, 'F', 'system:dictData:add', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (30203, 302, '修改字典数据', '/system/dictData/update', '', 2, NULL, NULL, 'F', 'system:dictData:update', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30204, 302, '删除字典数据', '/system/dictData/delete', '', 2, NULL, NULL, 'F', 'system:dictData:delete', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30205, 302, '类型集合字典数据', '/system/dictData/getByType', '', 2, NULL, NULL, 'F', 'system:dictData:getByType', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (30301, 303, '友链列表', '/system/friendlink/list', '', 2, NULL, NULL, 'F', 'system:friendlink:list', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (30302, 303, '添加友链', '/system/friendlink/add', '', 2, NULL, NULL, 'F', 'system:friendlink:add', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (30303, 303, '修改友链', '/system/friendlink/update', '', 2, NULL, NULL, 'F', 'system:friendlink:update', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30304, 303, '删除友链', '/system/friendlink/delete', '', 2, NULL, NULL, 'F', 'system:friendlink:delete', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30305, 303, '置顶友链', '/system/friendlink/top', '', 2, NULL, NULL, 'F', 'system:friendlink:top', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (30306, 303, '审核通过友链', '/system/friendlink/pass', '', 2, NULL, NULL, 'F', 'system:friendlink:pass', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30401, 304, '查询网站配置', '/system/webConfig/info', '', 2, NULL, NULL, 'F', 'system:webConfig:info', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (30402, 304, '修改网站配置', '/system/webConfig/update', '', 2, NULL, NULL, 'F', 'system:webConfig:update', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (30501, 305, '页面列表', '/system/webPage/list', '', 2, NULL, NULL, 'F', 'system:webPage:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30502, 305, '添加页面', '/system/webPage/add', '', 2, NULL, NULL, 'F', 'system:webPage:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (30503, 305, '修改页面', '/system/webPage/update', '', 2, NULL, NULL, 'F', 'system:webPage:update', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (30504, 305, '删除页面', '/system/webPage/delete', '', 2, NULL, NULL, 'F', 'system:webPage:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (40101, 401, '评论列表', '/system/comment/list', '', 2, NULL, NULL, 'F', 'system:comment:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (40102, 401, '删除评论', '/system/comment/delete', '', 2, NULL, NULL, 'F', 'system:comment:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (40201, 402, '留言列表', '/system/message/list', '', 2, NULL, NULL, 'F', 'system:message:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (40202, 402, '删除留言', '/system/message/delete', '', 2, NULL, NULL, 'F', 'system:message:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (40203, 402, '审核通过留言', '/system/message/pass', '', 2, NULL, NULL, 'F', 'system:message:pass', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (40301, 403, '反馈列表', '/system/feedback/list', '', 2, NULL, NULL, 'F', 'system:feedback:list', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (40302, 403, '删除反馈', '/system/feedback/delete', '', 2, NULL, NULL, 'F', 'system:feedback:delete', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (40303, 403, '回复反馈', '/system/feedback/reply', '', 2, NULL, NULL, 'F', 'system:feedback:reply', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (50101, 501, '相册列表', '/system/album/list', '', 2, NULL, NULL, 'F', 'system:album:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50102, 501, '相册详情', '/system/album/info', '', 2, NULL, NULL, 'F', 'system:album:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50103, 501, '添加相册', '/system/album/add', '', 2, NULL, NULL, 'F', 'system:album:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50104, 501, '修改相册', '/system/album/update', '', 2, NULL, NULL, 'F', 'system:album:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50105, 501, '删除相册', '/system/album/delete', '', 2, NULL, NULL, 'F', 'system:album:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50201, 502, '照片列表', '/system/photo/list', '', 2, NULL, NULL, 'F', 'system:photo:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50202, 502, '照片详情', '/system/photo/info', '', 2, NULL, NULL, 'F', 'system:photo:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50203, 502, '添加照片', '/system/photo/add', '', 2, NULL, NULL, 'F', 'system:photo:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50204, 502, '修改照片', '/system/photo/update', '', 2, NULL, NULL, 'F', 'system:photo:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50205, 502, '删除照片', '/system/photo/delete', '', 2, NULL, NULL, 'F', 'system:photo:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (50206, 502, '移动照片', '/system/photo/movePhoto', '', 2, NULL, NULL, 'F', 'system:photo:movePhoto', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (60101, 601, '用户日志列表', '/system/userLog/list', '', 2, NULL, NULL, 'F', 'system:userLog:list', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (60102, 601, '删除用户日志', '/system/userLog/delete', '', 2, NULL, NULL, 'F', 'system:userLog:delete', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (60201, 602, '操作日志列表', '/system/adminLog/list', '', 2, NULL, NULL, 'F', 'system:adminLog:list', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (60202, 602, '删除操作日志', '/system/adminLog/delete', '', 2, NULL, NULL, 'F', 'system:adminLog:delete', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (60301, 603, '异常日志列表', '/system/exceptionLog/list', '', 2, NULL, NULL, 'F', 'system:exceptionLog:list', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (60302, 603, '删除异常日志', '/system/exceptionLog/delete', '', 2, NULL, NULL, 'F', 'system:exceptionLog:delete', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (70101, 701, '用户列表', '/system/user/list', '', 2, NULL, NULL, 'F', 'system:user:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70102, 701, '用户详情', '/system/user/info', '', 2, NULL, NULL, 'F', 'system:user:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70103, 701, '删除用户', '/system/user/delete', '', 2, NULL, NULL, 'F', 'system:user:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70104, 701, '修改密码', '/system/user/updatePassword', '', 2, NULL, NULL, 'F', 'system:user:updatePassword', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (70105, 701, '修改状态', '/system/user/updateStatus', '', 2, NULL, NULL, 'F', 'system:user:updateStatus', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (70108, 701, '用户详细信息', '/system/user/profile', '', 2, NULL, NULL, 'F', 'system:userProfile:info', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (70109, 701, '修改用户信息', '/system/user/profile', '', 2, NULL, NULL, 'F', 'system:userProfile:update', NULL, NULL,
        1, 1, '', '2022-10-31 10:25:41', NULL),
       (70110, 701, '修改用户头像', '/system/user/profile/avatar', '', 2, NULL, NULL, 'F', 'system:userProfile:updateAvatar',
        NULL, NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (70201, 702, '角色列表', '/system/role/list', '', 2, NULL, NULL, 'F', 'system:role:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70202, 702, '角色详情', '/system/role/info', '', 2, NULL, NULL, 'F', 'system:role:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70203, 702, '添加角色', '/system/role/add', '', 2, NULL, NULL, 'F', 'system:role:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70204, 702, '修改角色', '/system/role/update', '', 2, NULL, NULL, 'F', 'system:role:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70205, 702, '删除角色', '/system/role/delete', '', 2, NULL, NULL, 'F', 'system:role:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70206, 702, '获取角色权限', '/system/role/rolePerms', '', 2, NULL, NULL, 'F', 'system:role:rolePerms', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (70207, 702, '获取所有权限', '/system/role/allPerms', '', 2, NULL, NULL, 'F', 'system:role:allPerms', NULL, NULL, 1, 1,
        '', '2023-03-25 14:54:27', NULL),
       (70208, 702, '更新角色权限', '/system/role/updatePerms', '', 2, NULL, NULL, 'F', 'system:role:updatePerms', NULL, NULL,
        1, 1, '', '2023-03-25 14:54:47', NULL),
       (70301, 703, '系统配置列表', '/system/config/list', '', 2, NULL, NULL, 'F', 'system:config:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70302, 703, '查询系统配置', '/system/config/info', '', 2, NULL, NULL, 'F', 'system:config:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70303, 703, '添加系统配置', '/system/config/add', '', 2, NULL, NULL, 'F', 'system:config:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70304, 703, '修改系统配置', '/system/config/update', '', 2, NULL, NULL, 'F', 'system:config:update', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (70305, 703, '删除系统配置', '/system/config/delete', '', 2, NULL, NULL, 'F', 'system:config:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (70306, 703, '查询指定配置键名', '/system/config/configKey', '', 2, NULL, NULL, 'F', 'system:config:configKey', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (70307, 703, '刷新配置缓存', '/system/config/refreshCache', '', 2, NULL, NULL, 'F', 'system:config:refreshCache', NULL,
        NULL, 1, 1, '', '2022-10-31 10:25:41', NULL),
       (70401, 704, '接口列表', '/system/api/list', '', 2, NULL, NULL, 'F', 'system:api:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70501, 705, '菜单列表', '/system/menu/list', '', 2, NULL, NULL, 'F', 'system:menu:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70502, 705, '添加菜单', '/system/menu/add', '', 2, NULL, NULL, 'F', 'system:menu:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70503, 705, '修改菜单', '/system/menu/update', '', 2, NULL, NULL, 'F', 'system:menu:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (70504, 705, '删除菜单', '/system/menu/delete', '', 2, NULL, NULL, 'F', 'system:menu:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80101, 801, '任务列表', '/system/job/list', '', 2, NULL, NULL, 'F', 'system:job:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80102, 801, '任务详情', '/system/job/info', '', 2, NULL, NULL, 'F', 'system:job:info', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80103, 801, '添加任务', '/system/job/add', '', 2, NULL, NULL, 'F', 'system:job:add', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80104, 801, '修改任务', '/system/job/update', '', 2, NULL, NULL, 'F', 'system:job:update', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80105, 801, '删除任务', '/system/job/delete', '', 2, NULL, NULL, 'F', 'system:job:delete', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80106, 801, '立即执行任务', '/system/job/run', '', 2, NULL, NULL, 'F', 'system:job:run', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80107, 801, '立即暂停任务', '/system/job/pause', '', 2, NULL, NULL, 'F', 'system:job:pause', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80108, 801, '修改任务状态', '/system/job/change', '', 2, NULL, NULL, 'F', 'system:job:change', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80201, 802, '任务日志列表', '/system/jobLog/list', '', 2, NULL, NULL, 'F', 'system:jobLog:list', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80202, 802, '删除任务日志', '/system/jobLog/delete', '', 2, NULL, NULL, 'F', 'system:jobLog:delete', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (80203, 802, '清空任务日志', '/system/jobLog/clean', '', 2, NULL, NULL, 'F', 'system:jobLog:clean', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (80301, 803, '在线用户列表', '/system/user/online', '', 2, NULL, NULL, 'F', 'system:user:online', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80302, 803, '用户强制下线', '/system/user/kick', '', 2, NULL, NULL, 'F', 'system:user:kick', NULL, NULL, 1, 1, '',
        '2022-10-31 10:25:41', NULL),
       (80401, 804, 'Druid监控', '/system/monitor/druid', '', 2, NULL, NULL, 'F', 'system:monitor:druid', NULL, NULL, 1,
        1, '', '2022-10-31 10:25:41', NULL),
       (80501, 805, '获取缓存监控', '/system/monitor/cache', '', 2, NULL, NULL, 'F', 'system:monitor:cache', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL),
       (80601, 806, '七牛监控', '/system/monitor/qiniu', '', 2, NULL, NULL, 'F', 'system:monitor:qiniu', NULL, NULL, 1, 1,
        '', '2022-10-31 10:25:41', NULL);

/*Table structure for table `t_message` */

DROP TABLE IF EXISTS `t_message`;

CREATE TABLE `t_message`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `content`     mediumtext NOT NULL COMMENT '内容',
    `nickname`    varchar(255)        DEFAULT NULL COMMENT '用户昵称',
    `avatar`      varchar(255)        DEFAULT NULL COMMENT '用户头像',
    `ip_address`  varchar(255)        DEFAULT NULL COMMENT 'ip地址',
    `ip_source`   varchar(255)        DEFAULT NULL COMMENT 'ip来源',
    `time`        bigint              DEFAULT NULL COMMENT '留言展示时间',
    `status`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0审核,1正常)',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='留言板表';

/*Table structure for table `t_page` */

DROP TABLE IF EXISTS `t_page`;

CREATE TABLE `t_page`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `page_name`   varchar(20)       DEFAULT NULL COMMENT '页面名称',
    `page_label`  varchar(20)       DEFAULT NULL COMMENT '页面标签',
    `page_cover`  varchar(255)      DEFAULT NULL COMMENT '页面图源',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime          DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='前端页面表';

/*Table structure for table `t_photo` */

DROP TABLE IF EXISTS `t_photo`;

CREATE TABLE `t_photo`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `album_id`    bigint       NOT NULL COMMENT '相册ID',
    `name`        varchar(20)  NOT NULL COMMENT '照片名',
    `info`        varchar(50)           DEFAULT NULL COMMENT '照片描述',
    `url`         varchar(255) NOT NULL COMMENT '照片地址',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='照片';

/*Table structure for table `t_photo_album` */

DROP TABLE IF EXISTS `t_photo_album`;

CREATE TABLE `t_photo_album`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(20)  NOT NULL COMMENT '相册名',
    `info`        varchar(64)           DEFAULT NULL COMMENT '相册描述',
    `cover`       varchar(255) NOT NULL COMMENT '相册封面',
    `status`      tinyint(1)   NOT NULL DEFAULT '1' COMMENT '相册状态(0私密,1公开)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='相册';

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code`        varchar(32)       DEFAULT NULL COMMENT '角色编码',
    `name`        varchar(32)       DEFAULT NULL COMMENT '角色名称',
    `remark`      varchar(255)      DEFAULT NULL COMMENT '角色描述',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime          DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='角色表';

/*Data for the table `t_role` */

insert into `t_role`(`id`, `code`, `name`, `remark`, `create_time`, `update_time`)
values (1, 'ROLE_admin', '管理员', '系统管理员', '2022-10-30 10:12:01', NULL),
       (2, 'ROLE_user', '用户', '普通用户', '2022-10-30 10:12:38', '2023-03-25 20:29:19');

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu`
(
    `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='角色-权限资源关联表';

/*Table structure for table `t_system_config` */

DROP TABLE IF EXISTS `t_system_config`;

CREATE TABLE `t_system_config`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_name`  varchar(100)      DEFAULT '' COMMENT '配置名称',
    `config_key`   varchar(100)      DEFAULT '' COMMENT '配置键名',
    `config_value` varchar(500)      DEFAULT '' COMMENT '配置键值',
    `config_type`  tinyint(1)        DEFAULT '0' COMMENT '系统内置(0否,1是)',
    `remark`       varchar(500)      DEFAULT NULL COMMENT '备注',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime          DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统配置表';

/*Data for the table `t_system_config` */

insert into `t_system_config`(`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`,
                              `update_time`)
values (1, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 1, '是否开启验证码功能(true开启,false关闭)', '2022-12-04 19:04:05',
        NULL),
       (4, '注册账号-默认头像', 'sys.account.defaultAvatar', '', 1, '注册时使用的默认头像', '2023-03-04 09:46:49', NULL);

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(64) NOT NULL DEFAULT '' COMMENT '标签名称',
    `click_volume` int                  DEFAULT '0' COMMENT '标签点击量',
    `sort`         int                  DEFAULT '0' COMMENT '排序',
    `create_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `tag_name` (`name`) USING BTREE COMMENT '博客标签名称'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='博客标签表';

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user`
(
    `id`              bigint     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`        varchar(64)         DEFAULT NULL COMMENT '账号',
    `password`        varchar(64)         DEFAULT NULL COMMENT '登录密码',
    `status`          tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户状态(0禁用,1正常)',
    `login_type`      int                 DEFAULT NULL COMMENT '登录方式',
    `user_auth_id`    bigint              DEFAULT NULL COMMENT '用户详情ID',
    `role_id`         bigint              DEFAULT NULL COMMENT '角色ID',
    `ip_address`      varchar(255)        DEFAULT NULL COMMENT 'ip地址',
    `ip_source`       varchar(255)        DEFAULT NULL COMMENT 'ip来源',
    `os`              varchar(100)        DEFAULT NULL COMMENT '登录系统',
    `last_login_time` datetime            DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    `browser`         varchar(255)        DEFAULT NULL COMMENT '浏览器',
    `create_time`     datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime            DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户基础信息表';

/*Table structure for table `t_user_auth` */

DROP TABLE IF EXISTS `t_user_auth`;

CREATE TABLE `t_user_auth`
(
    `id`          bigint        NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `email`       varchar(50)            DEFAULT NULL COMMENT '邮箱号',
    `nickname`    varchar(50)   NOT NULL COMMENT '用户昵称',
    `avatar`      varchar(1024) NOT NULL DEFAULT '' COMMENT '用户头像',
    `intro`       varchar(255)           DEFAULT NULL COMMENT '用户简介',
    `web_site`    varchar(255)           DEFAULT NULL COMMENT '个人网站',
    `is_disable`  tinyint(1)    NOT NULL DEFAULT '0' COMMENT '是否禁用(0否,1是)',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime               DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户信息表';

/*Table structure for table `t_user_log` */

DROP TABLE IF EXISTS `t_user_log`;

CREATE TABLE `t_user_log`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     bigint            DEFAULT NULL COMMENT '操作用户ID',
    `ip`          varchar(64)       DEFAULT NULL COMMENT 'ip地址',
    `address`     varchar(255)      DEFAULT NULL COMMENT '操作地址',
    `type`        varchar(100)      DEFAULT NULL COMMENT '操作类型',
    `description` varchar(255)      DEFAULT NULL COMMENT '操作日志',
    `model`       varchar(255)      DEFAULT NULL COMMENT '操作模块',
    `result`      varchar(255)      DEFAULT NULL COMMENT '操作结果',
    `access_os`   varchar(255)      DEFAULT NULL COMMENT '操作系统',
    `browser`     varchar(255)      DEFAULT NULL COMMENT '浏览器',
    `client_type` varchar(255)      DEFAULT NULL COMMENT '客户端类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='日志表';

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role`
(
    `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户角色关联表';

/*Table structure for table `t_web_config` */

DROP TABLE IF EXISTS `t_web_config`;

CREATE TABLE `t_web_config`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `logo`            varchar(255) NOT NULL COMMENT 'logo(文件UID)',
    `name`            varchar(255) NOT NULL COMMENT '网站名称',
    `summary`         varchar(255) NOT NULL COMMENT '介绍',
    `keyword`         varchar(255) NOT NULL COMMENT '关键字',
    `author`          varchar(255) NOT NULL COMMENT '作者',
    `record_num`      varchar(255) NOT NULL COMMENT '备案号',
    `web_url`         varchar(255)          DEFAULT NULL COMMENT '网站地址',
    `ali_pay`         varchar(127)          DEFAULT NULL COMMENT '支付宝收款码FileId',
    `weixin_pay`      varchar(127)          DEFAULT NULL COMMENT '微信收款码FileId',
    `github`          varchar(255)          DEFAULT NULL COMMENT 'github地址',
    `gitee`           varchar(255)          DEFAULT NULL COMMENT 'gitee地址',
    `qq_number`       varchar(20)           DEFAULT NULL COMMENT 'QQ号',
    `email`           varchar(255)          DEFAULT NULL COMMENT '邮箱',
    `show_list`       varchar(255)          DEFAULT NULL COMMENT '显示的列表(用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端)',
    `login_type_list` varchar(255)          DEFAULT NULL COMMENT '登录方式列表(用于控制前端登录方式，如账号密码,码云,Github,QQ,微信)',
    `open_comment`    tinyint(1)            DEFAULT '1' COMMENT '是否开启评论(0否,1是)',
    `open_admiration` tinyint(1)            DEFAULT '0' COMMENT '是否开启赞赏(0否,1是)',
    `tourist_avatar`  varchar(255)          DEFAULT NULL COMMENT '游客头像',
    `bulletin`        varchar(255)          DEFAULT NULL COMMENT '公告',
    `author_info`     varchar(100)          DEFAULT NULL COMMENT '作者简介',
    `author_avatar`   varchar(255)          DEFAULT NULL COMMENT '作者头像',
    `about_me`        varchar(500)          DEFAULT NULL COMMENT '关于我',
    `is_music_player` tinyint(1)            DEFAULT '0' COMMENT '是否开启音乐播放器(0否,1是)',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime              DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='网站配置表';

/*Table structure for table `t_web_page` */

DROP TABLE IF EXISTS `t_web_page`;

CREATE TABLE `t_web_page`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `page_name`   varchar(20)       DEFAULT NULL COMMENT '页面名称',
    `page_label`  varchar(20)       DEFAULT NULL COMMENT '页面标签',
    `page_cover`  varchar(255)      DEFAULT NULL COMMENT '页面图源',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime          DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='前端页面表';