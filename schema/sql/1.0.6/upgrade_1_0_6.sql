
/**
** PURPOSE
**
**   机器人
**   角色字典表
**
 */
INSERT INTO role(id, role_name, role_description, status, created_at, updated_at)
     VALUES (3, '机器人', '机器人，用于一些自动化操作', 1, now(), now());

/**
** PURPOSE
**
**   机器人，定时更新订单状态
**   用户表数据
**   id: 指定为-10000
**
 */
INSERT INTO user(id, name, mobile, status, online, created_at, updated_at)
     VALUES (-10000, 'ROBOT_UPDATE_ORDER_STATUS', 18800000000, 1, 1, now(), now());

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
               user.id as user_id, 1 as device_id, 1 as device_type, '0993d700-f87f-11e4-90fe-4054e479b41b' as access_token, '0.0.0' as client_version, 1 as is_valid, now() as updated_at, '2020-1-1 0:0:0' as expire_at
           FROM
               user
           WHERE
               user.name = 'ROBOT_UPDATE_ORDER_STATUS')as tb;

/**
** PURPOSE
**
**   机器人
**   没有实际意义
**   在现在的逻辑中，token解析是查询user_station_role表。如果表中没有记录，禁止用户登录。这里逻辑应该修改。
**
 */
INSERT INTO user_station_role(
    role_id, station_id, user_id
)SELECT
     *
 FROM (
          SELECT
              3 as role_id, station.id as station_id, user.id as user_id
          FROM
              station, user
          WHERE
              user.name='ROBOT_UPDATE_ORDER_STATUS'
          LIMIT 1)as tb;


#公告表
DROP TABLE IF EXISTS nt_announcement;
CREATE TABLE nt_announcement (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) NOT NULL COMMENT '标题',
  `summary` longtext NOT NULL  COMMENT '摘要',
  `content` longtext NOT NULL  COMMENT '内容',
  `popup_content` longtext NOT NULL  COMMENT '弹出内容',
  `level`  tinyint(4) NOT NULL     COMMENT '等级：1.重要通知 2.普通通知',
  `city_id`  int(11) NOT NULL     COMMENT '城市',
  `role_type`  tinyint(4)  NOT NULL     COMMENT '角色:1.餐厅老板 2.配送员',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


#用户公告状态表
DROP TABLE IF EXISTS nt_announcement_read;
CREATE TABLE nt_announcement_read (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `announcement_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '阅读状态.0:未读;1:已读',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `nt_announcement_read_id3567` (`announcement_id`),
  CONSTRAINT `announcement_id_refs_id_6d7ba788` FOREIGN KEY (`announcement_id`) REFERENCES `nt_announcement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

ALTER TABLE nt_announcement_read ADD UNIQUE (announcement_id,user_id);

DROP TABLE IF EXISTS `user_bank_info`;
DROP TABLE IF EXISTS `bank_serial_mapping`;
DROP TABLE IF EXISTS `user_bank_bind_abnormal`;

/** TABLE user_bank_info
**
** PURPOSE
**
**   人员银行账号信息
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   user_id : 用户id
**   user_name: 用户绑定银行卡时所使用的姓名
**   bank_id : 所持的银行卡对应的银行id
**   bank_account : 银行账号
**   created_at : 建立时间
**   updated_at : 修改时间
**   is_active : 该记录是否有效
**
** NOTES
*/

CREATE TABLE `user_bank_info`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` int(11) NOT NULL,
`user_name` varchar(32) NOT NULL,
`bank_id` int(11) NOT NULL,
`bank_account` varchar(32) NOT NULL,
`is_bind` tinyint(4) NOT NULL COMMENT '0：绑定中，1：绑定成功，2：绑定失败',
`is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** TABLE bank_serial_mapping
**
** PURPOSE
**
**   映射表，记录了银行id同其详情间的对应关系
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   bank_name : 银行名称
**   bank_serial : 银行序列号
**   is_active : 该记录是否有效
**   created_at : 建立时间
**   updated_at : 修改时间
**
** NOTES
*/

CREATE TABLE `bank_serial_mapping`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`bank_name` varchar(32) NOT NULL,
`bank_serial` varchar(32) NOT NULL,
`is_active` tinyint(4) NOT NULL COMMENT '0：无效，1：有效',
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** TABLE user_bank_bind_abnormal
**
** PURPOSE
**
**   映射表，记录了绑定出现异常的银行卡列表
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   trade_no : 交易流水号，全局唯一
**   partner_id : partner_id，对于该业务，全局相同
**   is_processed : 该记录是否已处理
**   created_at : 建立时间
**   updated_at : 修改时间
**
** NOTES
*/

CREATE TABLE `user_bank_bind_abnormal`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`trade_no` varchar(64) NOT NULL,
`partner_id` varchar(32) NOT NULL,
`is_processed` tinyint(4) NOT NULL COMMENT '0：未处理，1：已处理',
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into bank_serial_mapping (bank_name, bank_serial, is_active) values
('中国工商银行', '102100099996',1),('中国银行', '104100000004',1),('中国建设银行', '105100000017',1),
( '中国农业银行', '103100000026',1),( '交通银行', '301290000007',1),('中信银行', '302100011000',1),
('中国民生银行', '305100000013',1),
('广发银行', '306581000003', 1), ('招商银行', '308584000013', 1), ('中国光大银行', '303100000006', 1),
('北京银行', '313100000013',1), ('广州农商银行', '314581000011',1), ('上海农商银行', '322290000011',1),
('北京农商银行', '402100000018',1), ('平安银行', '307584007998',1), ('浦发银行', '310290000013',1 ),
('上海银行', '313290000017',1), ('华夏银行', '304100040000',1), ('邮政储蓄银行', '403100000004',1),
('兴业银行', '309391000011',1), ('宁波银行', '313332082914',1), ('威海市商业银行', '313465000010',1),
('浙江泰隆商业银行', '313345010019',1);


# 配送员账单表
DROP TABLE IF EXISTS user_bill;
create table user_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  user_id int(11) NOT NULL COMMENT '用户ID',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  clear_date date NOT NULL COMMENT '清算日期',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id),
  KEY `user_id` (`user_id`),
  KEY `clear_date` (`clear_date`),
  KEY `breakpoint_id` (`breakpoint_id`)
)ENGINE=INNODB charset=UTF8;

# 科目账单表
DROP TABLE IF EXISTS user_subject_bill;
create table user_subject_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  user_bill_id bigint(20) NOT NULL COMMENT '用户账单ID',
  clear_date date NOT NULL COMMENT '清算日期',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  activity_name varchar(100) NOT NULL COMMENT '活动名称',
  ele_finance_bill_detail_id bigint(20) NOT NULL COMMENT '财务账单明细ID',
    breakpoint_id bigint(20) NOT NULL COMMENT '断点记录',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id),
  KEY `user_id` (`user_id`),
  KEY `clear_date` (`clear_date`),
  KEY `breakpoint_id` (`breakpoint_id`),
  KEY `ele_finance_bill_detail_id` (`ele_finance_bill_detail_id`),
  KEY `user_bill_id` (`user_bill_id`),
  KEY `activity_id` (`activity_id`),
  KEY `subject_code` (`subject_code`)
)ENGINE=INNODB charset=UTF8;

# 配送员账单明细表
DROP TABLE IF EXISTS user_subject_bill_detail;
create table user_subject_bill_detail(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  user_subject_bill_id bigint(20) NOT NULL COMMENT '用户科目账单ID',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  delivery_info varchar(256) NOT NULL COMMENT '配送单信息',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点记录',
  remark varchar(256) DEFAULT NULL COMMENT '备注',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id),
  KEY `user_id` (`user_id`),
  KEY `breakpoint_id` (`breakpoint_id`),
  KEY `user_subject_bill_id` (`user_subject_bill_id`)
)ENGINE=INNODB charset=UTF8;

# 财务账单表
DROP TABLE IF EXISTS ele_finance_bill;
create table ele_finance_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  clear_date date NOT NULL COMMENT '清算日期',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id),
  KEY `subject_code` (`subject_code`),
  KEY `clear_date` (`clear_date`),
  KEY `breakpoint_id` (`breakpoint_id`) 
)ENGINE=INNODB charset=UTF8;

# 财务账单明细表
DROP TABLE IF EXISTS ele_finance_bill_detail;
create table ele_finance_bill_detail(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  user_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户类型，1：配送员；2：商家',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  finance_bill_id bigint(20) NOT NULL COMMENT '财务账单ID',
  clear_date date NOT NULL COMMENT '清算日期',
  breakpoint_id bigint(20) NOT NULL COMMENT '清算场次ID',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id),
  KEY `user_id` (`user_id`),
  KEY `subject_code` (`subject_code`),
  KEY `clear_date` (`clear_date`),
  KEY `finance_bill_id` (`finance_bill_id`),
  KEY `breakpoint_id` (`breakpoint_id`) 
)ENGINE=INNODB charset=UTF8;



# 配送员账单表
DROP TABLE IF EXISTS user_bill;
create table user_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  user_id int(11) NOT NULL COMMENT '用户ID',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  clear_date date NOT NULL COMMENT '清算日期',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

# 科目账单表
DROP TABLE IF EXISTS user_subject_bill;
create table user_subject_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  user_bill_id bigint(20) NOT NULL COMMENT '用户账单ID',
  clear_date date NOT NULL COMMENT '清算日期',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  activity_name varchar(100) NOT NULL COMMENT '活动名称',
  ele_finance_bill_detail_id bigint(20) NOT NULL COMMENT '财务账单明细ID',
    breakpoint_id bigint(20) NOT NULL COMMENT '断点记录',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

# 配送员账单明细表
DROP TABLE IF EXISTS user_subject_bill_detail;
create table user_subject_bill_detail(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  user_subject_bill_id bigint(20) NOT NULL COMMENT '用户科目账单ID',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  delivery_info varchar(256) NOT NULL COMMENT '配送单信息',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点记录',
  remark varchar(256) DEFAULT NULL COMMENT '备注',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 财务账单表
DROP TABLE IF EXISTS ele_finance_bill;
create table ele_finance_bill(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  clear_date date NOT NULL COMMENT '清算日期',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 财务账单明细表
DROP TABLE IF EXISTS ele_finance_bill_detail;
create table ele_finance_bill_detail(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  user_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户类型，1：配送员；2：商家',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  amount_type tinyint(4) NOT NULL DEFAULT 1 COMMENT '金额变更类型：0：支出；1：收入',
  finance_bill_id bigint(20) NOT NULL COMMENT '财务账单ID',
  clear_date date NOT NULL COMMENT '清算日期',
  breakpoint_id bigint(20) NOT NULL COMMENT '清算场次ID',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 科目表 （字典表）
DROP TABLE IF EXISTS ele_subject;
create table ele_subject(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_type varchar(20) NOT NULL COMMENT '科目类型',
  description varchar(256) COMMENT '描述',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 配送员数据统计表
DROP TABLE IF EXISTS user_statistics_result;
create table user_statistics_result(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int(11) NOT NULL COMMENT '用户ID',
  total_nums int(11) NOT NULL DEFAULT 0 COMMENT '总配送单数',
  total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
  finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '完成的总配送单数',
  finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '完成的配送单总金额',
  valid_finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '有效完成的配送单总数',
  valid_finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '有效完成的配送单总金额',
  online_finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '在线支付完成的总配送单数',
  online_finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '在线支付完成的配送单总金额',
  online_valid_finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '在线支付有效完成的配送单总数',
  online_valid_finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '在线支付有效完成的配送单总金额',
  offline_finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '货到付款完成的配送单总数',
  offline_finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '货到付款完成的配送单总金额',
  offline_valid_finished_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '货到付款有效完成的配送单总数',
  offline_valid_finished_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '货到付款有效完成的配送单总金额',
  abnormal_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '异常单总数',
  abnormal_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '异常配送单总金额',
  abnormal_online_total_nums  int(11) NOT NULL DEFAULT 0 COMMENT '在线支付异常配送单总数',
  abnormal_online_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '在线支付异常配送单总金额',
  abnormal_offline_total_nums int(11) NOT NULL DEFAULT 0 COMMENT '货到付款异常配送单总数',
  abnormal_offline_total_amount decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '货到付款异常配送单总金额',
  statistics_date date NOT NULL COMMENT '统计数据日期',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效', 
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 断点表
DROP TABLE IF EXISTS breakpoint;
create table breakpoint(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  date date NOT NULL COMMENT '数据的日期',
  created_at datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;
# 断点明细表
DROP TABLE IF EXISTS breakpoint_detail;
create table breakpoint_detail(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  start_index bigint(20) NOT NULL COMMENT '开始位置',
  end_index bigint(20) NOT NULL COMMENT '结束位置',
  table_name varchar(40) NOT NULL COMMENT '待统计的数据表名',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID（场次ID）',
  date date NOT NULL COMMENT '数据的日期',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效:0,无效；1,有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

# 清算执行指令表
DROP TABLE IF EXISTS clear_command_execute_log;
create table clear_command_execute_log(
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', 
  shell_name varchar(20) NOT NULL COMMENT 'SHELL指令名称',
  is_manual tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否手动执行；0：自动；1，手动',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID',
  reason varchar(255) COMMENT '执行失败原因',
  remark varchar(255) COMMENT '执行原因：在执行时，可以加入执行的原因',
  created_at datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

# 清算结果临时仓库
DROP TABLE IF EXISTS result_user_bill;
DROP TABLE IF EXISTS result_user_subject_bill;
DROP TABLE IF EXISTS result_user_subject_bill_detail;
DROP TABLE IF EXISTS result_ele_finance_bill;
DROP TABLE IF EXISTS result_ele_finance_bill_detail;

create table result_user_bill like user_bill;
create table result_user_subject_bill like user_subject_bill;
create table result_user_subject_bill_detail like user_subject_bill_detail;
create table result_ele_finance_bill like ele_finance_bill;
create table result_ele_finance_bill_detail like ele_finance_bill_detail;

# stash结果临时仓库
DROP TABLE IF EXISTS stash_user_bill;
DROP TABLE IF EXISTS stash_user_subject_bill;
DROP TABLE IF EXISTS stash_user_subject_bill_detail;
DROP TABLE IF EXISTS stash_ele_finance_bill;
DROP TABLE IF EXISTS stash_ele_finance_bill_detail;
create table stash_user_bill like user_bill;
create table stash_user_subject_bill like user_subject_bill;
create table stash_user_subject_bill_detail like user_subject_bill_detail;
create table stash_ele_finance_bill like ele_finance_bill;
create table stash_ele_finance_bill_detail like ele_finance_bill_detail;


####################活动规则########################
DROP TABLE IF EXISTS activity_template;
create table activity_template(
  id int(11) NOT NULL AUTO_INCREMENT,
  function varchar(50) NOT NULL COMMENT '活动模板的PYTHON函数',
  args text NOT NULL COMMENT 'function的参数定义',
  created_at datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;


DROP TABLE IF EXISTS activity;
create table activity(
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(50) NOT NULL COMMENT '活动名称',
  start_time datetime NOT NULL COMMENT '活动开始时间',
  end_time datetime NOT NULL COMMENT '活动的结束时间',
  args_value text NOT NULL COMMENT '传递给活动模板的具体规则数值',
  activity_template_id int(11) NOT NULL COMMENT '活动模板ID',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS activity_city;
create table activity_city(
  id int(11) NOT NULL AUTO_INCREMENT,
  activity_id  int(11) NOT NULL COMMENT '活动ID',
  city_id  int(11) NOT NULL COMMENT '城市ID',
  city_name varchar(50) NOT NULL COMMENT '城市名称',
  created_at datetime NOT NULL COMMENT '创建时间',
  is_active tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS delivery_order_antifraud_detail;
create table delivery_order_antifraud_detail(
  id int(11) NOT NULL AUTO_INCREMENT,
  delivery_order_id bigint(20) NOT NULL COMMENT '配送单ID',
  remark text COMMENT '异常备注',
  created_at datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;


DROP TABLE IF EXISTS delivery_order_rule_args;
create table delivery_order_rule_args(
  id bigint(11) NOT NULL AUTO_INCREMENT,
  delivery_order_id bigint(20) NOT NULL COMMENT '配送单ID',
  city_id int(11) NOT NULL COMMENT '城市ID',
  delivery_order_no int(11) NOT NULL COMMENT '配送员的第几单',
  user_id int(11) NOT NULL COMMENT '用户ID',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  type int(11) NOT NULL COMMENT '配送单信息类型；从右到左：第一位：是否异常，第二位：是否完成；第三位：是否有效配送；第四位：是否在线支付',
    paied_amount decimal(10,2) COMMENT '支付的金额',
    total_amount decimal(10,2) COMMENT '总金额',
  created_at datetime NOT NULL,
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS clear_activity_settings;
create table clear_activity_settings(
  id int(11) NOT NULL AUTO_INCREMENT,
  activity_id int(11) NOT NULL COMMENT '活动ID',
  function varchar(50) NOT NULL COMMENT '计算函数',
  args varchar(255) NOT NULL COMMENT '参数',
  created_at datetime NOT NULL,
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS user_activity_info;
create table user_activity_info(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL COMMENT '用户ID',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  total_days int(11) NOT NULL COMMENT '参与活动累积天数',
  total_delivery_order_nums int(11) NOT NULL COMMENT '累积参与活动有效配送单数',
  total_exception_delivery_order_nums int(11) NOT NULL COMMENT '累积参与活动的异常单数', 
  created_at datetime NOT NULL ,
  updated_at datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS user_activity_info_detail;
create table user_activity_info_detail(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL COMMENT '用户ID',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  user_activity_info_id int(11) NOT NULL COMMENT '用户活动汇总ID',
  total_delivery_order_nums int(11) NOT NULL COMMENT '累积参与活动有效配送单数',
  total_exception_delivery_order_nums int(11) NOT NULL COMMENT '累积参与活动的异常单数',
  breakpoint_id bigint(20) NOT NULL COMMENT '断点ID',
  created_at datetime NOT NULL,
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;

DROP TABLE IF EXISTS ele_subject_activity;
create table ele_subject_activity(
  id int(11) NOT NULL AUTO_INCREMENT,
  subject_code varchar(12) NOT NULL COMMENT '科目代码',
  subject_name varchar(20) NOT NULL COMMENT '科目名称',
  activity_id int(11) NOT NULL COMMENT '活动ID',
  activity_name varchar(100) COMMENT '活动名称',
  created_at datetime NOT NULL,
  is_active tinyint(4) NOT NULL,
  PRIMARY KEY(id)
)ENGINE=INNODB charset=UTF8;


alter table delivery_order add column ele_created_time datetime;
alter table delivery_order add column booked_time datetime;
alter table delivery_order add column uuID varchar(64);

-- ----------------------------
--  Table structure for `channel_restaurant_module`
-- ----------------------------
DROP TABLE IF EXISTS `channel_restaurant_module`;
CREATE TABLE `channel_restaurant_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `current_module_id` int(11) NOT NULL,
  `rst_count_once` int(11) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into channel_restaurant_module(current_module_id,rst_count_once,create_at) values (1,30,'2015-05-24 16:37:08');
