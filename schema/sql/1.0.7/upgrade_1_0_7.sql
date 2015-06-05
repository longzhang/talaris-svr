
/** TABLE fronted_app_version
**
** PURPOSE
**
**   蜂鸟客户端（h5版本）热更新版本记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   version : 更新版本号, 用于显示
**   version_code: 更新版本号, 用于版本比较
**   update_note : 更新说明
**   force: 强制更新
**   is_active : 0：无效，1：有效
**   created_at : 建立时间
**
** NOTES
*/
DROP TABLE IF EXISTS fronted_app_version;
CREATE TABLE `fronted_app_version`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `version` varchar(8) NOT NULL COMMENT '更新版本号, 用于显示',
    `version_code` int(11) NOT NULL COMMENT '更新版本号, 用于版本比较',
    `update_note` text NOT NULL COMMENT '更新说明',
    `force` tinyint(4) NOT NULL COMMENT '强制更新',
    `is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
    `created_at` timestamp NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** TABLE fronted_app_js_directory
**
** PURPOSE
**
**   蜂鸟客户端（h5版本）热更新javascript文件记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   version_code : 版本号
**   canonical_url: 连接
**   md5: md5值
**   is_active : 0：无效，1：有效
**   created_at : 创建时间
**
** NOTES
*/
DROP TABLE IF EXISTS fronted_app_js_directory;
CREATE TABLE `fronted_app_js_directory`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `version` varchar(8) NOT NULL COMMENT '更新版本号, 用于显示',
    `version_code` varchar(8) NOT NULL COMMENT '更新版本号, 用于比较',
    `canonical_url` varchar(128) NOT NULL COMMENT '连接',
    `md5` varchar(64) NOT NULL COMMENT 'md5值',
    `is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
    `created_at` timestamp NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** TABLE fronted_app_css_directory
**
** PURPOSE
**
**   蜂鸟客户端（h5版本）热更新css文件记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   version_code : 版本号
**   canonical_url: 文件路径
**   md5: md5值
**   is_active : 0：无效，1：有效
**   created_at : 创建时间
**
** NOTES
*/
DROP TABLE IF EXISTS fronted_app_css_directory;
CREATE TABLE `fronted_app_css_directory`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `version` varchar(8) NOT NULL COMMENT '更新版本号, 用于显示',
    `version_code` varchar(8) NOT NULL COMMENT '更新版本号, 用于比较',
    `canonical_url` varchar(128) NOT NULL COMMENT '连接',
    `md5` varchar(64) NOT NULL COMMENT 'md5值',
    `is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
    `created_at` timestamp NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** TABLE fronted_app_html_directory
**
** PURPOSE
**
**   蜂鸟客户端（h5版本）热更新html文件记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   version_code : 版本号
**   canonical_url: 文件路径
**   md5: md5值
**   is_active : 0：无效，1：有效
**   created_at : 创建时间
**
** NOTES
*/
DROP TABLE IF EXISTS fronted_app_html_directory;
CREATE TABLE `fronted_app_html_directory`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `version` varchar(8) NOT NULL COMMENT '更新版本号, 用于显示',
    `version_code` varchar(8) NOT NULL COMMENT '更新版本号, 用于比较',
    `canonical_url` varchar(128) NOT NULL COMMENT '连接',
    `md5` varchar(64) NOT NULL COMMENT 'md5值',
    `is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
    `created_at` timestamp NOT NULL,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;




/**
** PURPOSE
**
**   机器人，定时更新订单状态
**   用户表数据
**   id: 指定为-10000
**
 */
INSERT INTO user(id, name, mobile, status, online, created_at, updated_at)
     VALUES (-10001, 'ROBOT_NODE', 18800000001, 1, 1, now(), now());
/**
** PURPOSE
**
**   机器人，定时更新订单状态
**   用户验证数据
**   access_token:  0993d700-f87f-11e4-90fe-4054e479b41b 不可更改
**
 */
INSERT INTO user_device (
    user_id, device_id, device_type, access_token, client_version, is_valid, updated_at, expire_at
) SELECT
      *
  FROM (
           SELECT
               user.id as user_id, 1 as device_id, 1 as device_type, '370b92e0-05ae-11e5-b1bf-1d2873c68752' as access_token, '0.0.0' as client_version, 1 as is_valid, now() as updated_at, '2020-1-1 0:0:0' as expire_at
           FROM
               user
           WHERE
               user.name = 'ROBOT_NODE')as tb;
/**
** PURPOSE
**
**   机器人
**   没有实际意义
**   在现在的逻辑中，token解析是查询user_station_role表。如果表中没有记录，禁止用户登录。这里逻辑应该修改。
**
 */
INSERT INTO user_station_role (role_id, station_id, user_id) SELECT
	*
FROM
	(
		SELECT
			2 AS role_id,
			station.id AS station_id,
			user.id AS user_id
		FROM
			station,
			user
		WHERE
			user.NAME = 'ROBOT_NODE'
		AND station.id = 9
		LIMIT 1
	) AS tb;