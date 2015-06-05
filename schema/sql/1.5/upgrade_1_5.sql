/** TABLE retailer_restaurant_mapping
**
** PURPOSE
**
**   商家表，存放商家数据
**   对于商家的理解，在众包模式下属于提供服务方，是所有订单产生源头。
**   目前，主要指餐厅
**  
**
** COLUMNS
**
**   id :  表主键，自增长
**   retailer_id : 和商家表[retailer]关联
**   ele_restaurant_id : 和饿了么主站餐厅关联. 因为当前业务只包括餐厅订单，所以只和主站餐厅数据关联。
**   created_at : 创建时间
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**
** NOTES
*/
CREATE TABLE `retailer_restaurant_mapping` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `retailer_id` int(11) NOT NULL COMMENT '和商家表关联',
    `ele_restaurant_id` int(11) NOT NULL COMMENT '和饿了么主站餐厅关联',
    `created_at` datetime NOT NULL,
    `is_active` tinyint(4) NOT NULL DEFAULT 1 COMMENT '软删除 0: 无效; 1: 有效',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** VIEW v_station
**
** PURPOSE
**
**   创建临时视图，逐步代替之前的station表
**  
**
** COLUMNS
**
**   字段同station表
**
** NOTES
*/
CREATE VIEW `v_station` (
    `id`,
    `name`,
    `phone`,
    `address`,
    `longitude`,
    `latitude`,
    `manager_id`,
    `manager_mobile`,
    `city_id`,
    `status`,
    `created_at`,
    `updated_at`) 
AS SELECT 
    `retailer`.`id`,
    `retailer`.`name`,
    `retailer`.`phone`,
    `retailer`.`address`,
    `retailer`.`longitude`,
    `retailer`.`latitude`,
    `retailer`.`owner_id`,
    `retailer`.`owner_mobile`,
    `retailer`.`city_id`,
    `retailer`.`is_active`,
    `retailer`.`created_at`,
    `retailer`.`updated_at` 
FROM `retailer`;

/** TABLE deliverer_authorization
**
** PURPOSE
**
**   授权表，记录了当前所有授权类型
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   name : 认证的名字
**   description : 对认证的描述
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**
** NOTES
*/
CREATE TABLE `deliverer_authorization` (
    `id` int(11) NOT NULL,
    `name` varchar(32) NOT NULL COMMENT '认证名称',
    `description` varchar(128) NOT NULL DEFAULT '' COMMENT '对认证的描述',
    `is_active` tinyint(4) NOT NULL DEFAULT 1 COMMENT '软删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE deliverer_authorization_apply
**
** PURPOSE
**
**   配送员申请授权记录表
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   deliverer_id : 配送员ID
**   deliverer_name : 配送员名字
**   deliverer_mobile : 配送员手机号
**   deliverer_auth_id : 申请认证的ID
**   status : 申请流程当前所处的状态
**   created_at : 创建时间
**   updated_at : 更新时间
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**   reject_reason : 驳回申请的原因
**   operator_user_id : 审核申请人的ID
**   operator_user_name : 审核申请人的名字
**   operator_user_mobile : 审核申请人的手机号
**
** NOTES
*/

CREATE TABLE `deliverer_authorization_apply` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `deliverer_id` int(11) NOT NULL COMMENT '配送员ID',
    `deliverer_name` varchar(32) NOT NULL DEFAULT '' COMMENT '配送员名字',
    `deliverer_mobile` bigint(11) NOT NULL COMMENT '配送员手机号',
    `deliverer_auth_id` int(11) NOT NULL COMMENT '申请认证的ID',
    `status` tinyint(4) NOT NULL COMMENT '申请流程当前所处的状态 ',
    `created_at` datetime NOT NULL COMMENT '创建时间',
    `updated_at` datetime NOT NULL COMMENT '更新时间',
    `is_active` tinyint(4) NOT NULL DEFAULT 1 COMMENT '软删除',
    `reject_reason` varchar(128) NOT NULL DEFAULT '' COMMENT '驳回申请原因',
    `operator_user_id` int(11) NOT NULL DEFAULT 0 COMMENT '审核申请人的ID',
    `operator_user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '审核申请人的名字',
    `operator_user_mobile` bigint(11) NOT NULL DEFAULT 0 COMMENT '审核申请人的手机号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE certificate_snapshot
**
** PURPOSE
**
**   用户提交证件照片在文件服务器上存储位置记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   applicant_id : 申请人ID
**   applicant_name : 申请人名字
**   applicant_mobile : 申请人手机号
**   url_head : 证件照片(正面)文件储存路径
**   url_tail : 证件照片(反面)文件储存路径
**   created_at : 创建时间
**   updated_at : 更新时间
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**   file_system_type : 文件服务器类型
**                      0 : localhost 本地
**
** NOTES
*/
CREATE TABLE `certificate_snapshot` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `applicant_id` int(11) NOT NULL COMMENT '申请人ID',
    `applicant_name` varchar(32) NOT NULL COMMENT '申请人名字',
    `applicant_mobile` bigint(11) NOT NULL COMMENT '申请人手机号',
    `url_head` varchar(128) NOT NULL COMMENT '证件照片(正面)文件储存路径',
    `url_tail` varchar(128) NOT NULL COMMENT '证件照片(反面)文件储存路径',
    `file_system_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '文件服务器类型',
    `is_active` tinyint(4) NOT NULL DEFAULT 1 COMMENT '软删除',
    `created_at` datetime NOT NULL COMMENT '创建时间',
    `updated_at` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE reward
**
** PURPOSE
**
**   字典表，记录所有奖励类型
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   name : 奖励名字
**   description : 奖励的描述
**   level : 奖励分级
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**
** NOTES
*/
CREATE TABLE `reward` (
    `id` int(11) NOT NULL,
    `name` int(11) NOT NULL COMMENT '奖励名',
    `description` varchar(32) NOT NULL COMMENT '奖励的描述',
    `level` bigint(11) NOT NULL COMMENT '奖励分级',
    `is_active` varchar(128) NOT NULL COMMENT '软删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE reward_record
**
** PURPOSE
**
**   对配送员奖励记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   deliverer_id : 待奖励的配送员ID
**   deliverer_name : 待奖励的配送员名字
**   deliverer_mobile : 待奖励的配送员手机号
**   reward_reason : 奖励原因
**   reward_id : 奖励ID
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**   created_at : 创建时间
**   applicant_user_id : 提交奖励申请人的ID
**   applicant_user_name : 提交奖励申请人的名字
**   applicant_user_mobile : 提交奖励申请人的手机号
**   operator_user_id : 审核人ID
**   operator_user_name : 审核人名字
**   operator_user_mobile : 审核人手机号
**
** NOTES
*/
CREATE TABLE `reward_record` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `deliverer_id` int(11) NOT NULL COMMENT '待奖励的配送员ID',
    `deliverer_name` varchar(32) NOT NULL COMMENT '待奖励的配送员名字',
    `deliverer_mobile` bigint(11) NOT NULL COMMENT '待奖励的配送员手机号',
    `reward_reason` varchar(128) NOT NULL COMMENT '奖励原因',
    `reward_id` varchar(128) NOT NULL COMMENT '奖励ID',
    `is_active` varchar(128) NOT NULL COMMENT '软删除',
    `created_at` varchar(128) NOT NULL COMMENT '创建时间',
    `applicant_user_id` varchar(128) NOT NULL COMMENT '提交奖励申请人的ID',
    `applicant_user_name` varchar(128) NOT NULL COMMENT '提交奖励申请人的名字',
    `applicant_user_mobile` varchar(128) NOT NULL COMMENT '提交奖励申请人的手机号',
    `operator_user_id` varchar(128) NOT NULL COMMENT '审核人ID',
    `operator_user_name` varchar(128) NOT NULL COMMENT '审核人名字',
    `operator_user_mobile` varchar(128) NOT NULL COMMENT '审核人手机号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE punishment
**
** PURPOSE
**
**   字典表，记录所有惩罚类型
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   name : 惩罚名字
**   description : 惩罚的描述
**   level : 惩罚分级
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**
** NOTES
*/
CREATE TABLE `punishment` (
    `id` int(11) NOT NULL,
    `name` int(11) NOT NULL COMMENT '惩罚名字',
    `description` varchar(32) NOT NULL COMMENT '惩罚描述',
    `level` bigint(11) NOT NULL COMMENT '惩罚分级',
    `is_active` varchar(128) NOT NULL COMMENT '软删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE punishment_record
**
** PURPOSE
**
**   对配送员惩罚记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   deliverer_id : 待惩罚的配送员ID
**   deliverer_name : 待惩罚的配送员名字
**   deliverer_mobile : 待惩罚的配送员手机号
**   punishment_reason : 奖励原因
**   punishment_id : 惩罚ID
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**   created_at : 创建时间
**   applicant_user_id : 提交惩罚申请人的ID
**   applicant_user_name : 提交惩罚申请人的名字
**   applicant_user_mobile : 提交惩罚申请人的手机号
**   operator_user_id : 审核人ID
**   operator_user_name : 审核人名字
**   operator_user_mobile : 审核人手机号
**
** NOTES
*/
CREATE TABLE `punishment_record` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `deliverer_id` int(11) NOT NULL COMMENT '待惩罚的配送员ID',
    `deliverer_name` varchar(32) NOT NULL COMMENT '待惩罚的配送员名字',
    `deliverer_mobile` bigint(11) NOT NULL COMMENT '待惩罚的配送员手机号',
    `punishment_reason` varchar(128) NOT NULL COMMENT '惩罚原因',
    `punishment_id` varchar(128) NOT NULL COMMENT '惩罚ID',
    `is_active` varchar(128) NOT NULL COMMENT '软删除',
    `created_at` varchar(128) NOT NULL COMMENT '创建时间',
    `applicant_user_id` varchar(128) NOT NULL COMMENT '提交惩罚申请人的ID',
    `applicant_user_name` varchar(128) NOT NULL COMMENT '提交惩罚申请人的名字',
    `applicant_user_mobile` varchar(128) NOT NULL COMMENT '提交惩罚申请人的手机号',
    `operator_user_id` varchar(128) NOT NULL COMMENT '审核人ID',
    `operator_user_name` varchar(128) NOT NULL COMMENT '审核人名字',
    `operator_user_mobile` varchar(128) NOT NULL COMMENT '审核人手机号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
## TABLE deliverer_complaint_record
**
** PURPOSE
**
**   对配送员投诉记录
**
**
** COLUMNS
**
**   id :  表主键，自增长
**   deliverer_id : 待惩罚的配送员ID
**   deliverer_name : 待惩罚的配送员名字
**   deliverer_mobile : 待惩罚的配送员手机号
**   complaint_reason : 奖励原因
**   proof_url_1 : 上传凭证url-1
**   proof_url_2 : 上传凭证url-2
**   proof_url_3 : 上传凭证url-3
**   proof_url_4 : 上传凭证url-4
**   file_system_type : 使用文件服务器类型
**   created_at : 创建时间
**   status : 投诉申请当前进度
**   applicant_user_id :
**   applicant_user_name :
**   applicant_user_mobile :
**   operator_user_id :
**   operator_user_name :
**   operator_user_mobile :
**   operator_msg :
**   is_active : 软删除
**               1 : 有效
**               0 : 无效
**
** NOTES
*/
CREATE TABLE `deliverer_complaint_record` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `deliverer_id` int(11) NOT NULL COMMENT '被投诉的配送员ID',
    `deliverer_name` varchar(32) NOT NULL COMMENT '被投诉的配送员名字',
    `deliverer_mobile` bigint(11) NOT NULL COMMENT '被投诉的配送员手机号',
    `complaint_reason` varchar(128) NOT NULL COMMENT '投诉原因',
    `proof_url_1` varchar(128) NOT NULL COMMENT '上传凭证url-1',
    `proof_url_2` varchar(128) NOT NULL COMMENT '上传凭证url-2',
    `proof_url_3` varchar(128) NOT NULL COMMENT '上传凭证url-3',
    `proof_url_4` varchar(128) NOT NULL COMMENT '上传凭证url-4',
    `file_system_type` varchar(128) NOT NULL COMMENT '使用文件服务器类型',
    `created_at` varchar(128) NOT NULL COMMENT '创建时间',
    `status` varchar(128) NOT NULL COMMENT '投诉申请当前进度',
    `applicant_user_id` varchar(128) NOT NULL COMMENT '提交投诉申请人的ID',
    `applicant_user_name` varchar(128) NOT NULL COMMENT '提交投诉申请人的名字',
    `applicant_user_mobile` varchar(128) NOT NULL COMMENT '提交投诉申请人的手机号',
    `operator_user_id` varchar(128) NOT NULL COMMENT '审核人ID',
    `operator_user_name` varchar(128) NOT NULL COMMENT '审核人名字',
    `operator_user_mobile` varchar(128) NOT NULL COMMENT '审核人手机号',
    `operator_msg` varchar(128) NOT NULL COMMENT '审核人备注',
    `is_active` varchar(128) NOT NULL COMMENT '软删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
