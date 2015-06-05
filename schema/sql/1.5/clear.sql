
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
	subject_bill_id bigint(20) NOT NULL COMMENT '科目账单ID',
 	clear_date date NOT NULL COMMENT '清算日期',
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

