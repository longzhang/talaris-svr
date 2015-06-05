## 基本定义
* 用户状态 ：status  1有效；0无效
* 配送员是否工作: online 1工作；0不工作
* 配送单状态：status 0:待取餐,1:待配送,2:配送中,3:已送达,4:已取消,-1:异常

## System

请求方式  | 接口                                    | 查询参数 | 请求参数               | 返回内容                                 | 描述
-------- | -------------                          |-------- | ------                | -------                                 | ------
GET      | /webapi/app/version/                   |         |                       |执行成功:[返回值](http://dwz.cn/NE4Sp "Title")  | 查看当前版本信息
GET      | /webapi/sys/time                       |         |                       |执行成功:[返回值](http://dwz.cn/NE9ji "Title")  | 从服务端获取当前时间

## Session

请求方式  | 接口                                    | 查询参数 | 请求参数               | 返回内容                                 | 描述
-------- | -------------                          |-------- | ------                | -------                                 | ------
POST     | /webapi/auth/send\_validate_code/      |         | mobile                |执行成功:[返回值](http://dwz.cn/NEaxC "Title")  | 用于发送验证码
POST     | /webapi/auth/login/                    |         | mobile, validate_code |执行成功:[返回值](http://dwz.cn/NEbVr "Title")  | 用户登录
GET      | /webapi/auth/loginout                  |         |                       |执行成功:[返回值](http://dwz.cn/NEaxC "Title")  | 登出
POST     | /webapi/auth/send_voice_validate_code/ |         | mobile                |执行成功:[返回值](http://dwz.cn/NEaxC "Title")  | 登陆界面，发送语音验证码
GET      | /webapi/auth/get\_userlicence          |         |                       |执行成功:[返回值](http://dwz.cn/NEiGH "Title")  | 获取用户协议接口
POST     | /webapi/auth/update\_user\_licence\_status    |  |                       |执行成功:[返回值](http://dwz.cn/NEaxC "Title")  | 更改用户协议状态
GET      | /webapi/my\_info/                      |         |                       |执行成功:[返回值](http://dwz.cn/NEoKz "Title")  | 查看个人信息
PUT      | /webapi/my\_info/                      |         | user_name, certificate_number |执行成功:[返回值](http://dwz.cn/NEoKz "Title")  | 修改个人信息

## 配送单

请求方式  | 接口                                    | 查询参数          | 请求参数                | 返回内容                                 | 描述
-------- | -----------------------------------    |--------          | ------                 | --------------                          | ------
GET      | /webapi/delivery_order/{:id}           | id\_list, ele\_order\_id, ele\_order\_id\_list, status, taker, passed\_by, payment\_type, from\_time, to\_time, detail\_level | |执行成功[返回值](http://dwz.cn/NEqov "Title")  | 其中id为路径参数. 查询参数:id\_list=[配送单ID列表], ele\_order\_id=[饿单ID], ele\_order\_id\_list=[饿单ID列表], status=[状态], taker=[当前配送员], passed\_by=[曾经经手过的用户ID], payment\_type=[支付类型], from\_time=[起始时间，默认为当日00:00:00], to\_time=[结束时间,默认为当前时间], detail\_level=[默认为0。（0.配送单信息，1.配送单+饿单信息]
POST     | /webapi/delivery\_order/create         |         | ele\_order\_id\_list, create\_type, customer\_phone|执行成功:[返回值](http://dwz.cn/NErda "Title")| create_type, 扫码为0， 勾选确认1， 录入非饿了么订单:3. customer\_phone为选填项，当create\_type为3时, 需要填写用户手机号.
POST     | /webapi/my/station/wait\_to\_delivery\_order   |         |         |执行成功:[返回值](http://dwz.cn/NErO5 "Title")| 获取待配送的配送订单
POST     | /webapi/delivery\_order/notify\_customer       |         | id_list |执行成功:[返回值](http://dwz.cn/NEsyx "Title")| 通知用户. 请求参数为配送ID列表
POST     | /webapi/delivery\_order/call\_receiver\_status |         | taskIdList |执行成功:[返回值](http://dwz.cn/NEsTV "Title")| 语音电话查询. 请求参数taskIdList为电话事件的ID
POST     | /webapi/delivery\_order/mark\_exception|         | id, remark |执行成功:[返回值](http://dwz.cn/NEtiA "Title")  | 标记为异常订单. 参数id为配送单ID, remark为异常备注
POST     | /webapi/delivery\_order/mark\_normal   |         | id_list    |执行成功:[返回值](http://dwz.cn/NEtRb "Title")  | 标记为正常订单. 
POST     | /webapi/delivery\_order/confirm\_delivered     |         | id_list |执行成功:[返回值](http://dwz.cn/NEup6 "Title")| 确认送达
GET      | /webapi/delivery\_order\_report/       | time, user, group\_by |   |执行成功:[返回值](http://dwz.cn/NEuN7 "Title")| 当日汇总. 查询参数中, group\_by=status/payment_type
POST     | /webapi/delivery_order/cancel          |                 | delivery_order_id       |执行成功:[返回值](http://dwz.cn/NEvtq "Title")| 当日汇总. 查询参数中, group\_by=status/payment_type

## 清结算

请求方式  | 接口                                           | 查询参数             | 请求参数                | 返回内容                                       | 描述
-------- | -----------------------------------           |--------             | ------                 | --------------                                | ------
GET      | /webapi/settlement/station/{:station\_id}/user| station_id, role_id |                        | 执行成功:[返回值](http://dwz.cn/NEwdg "Title")  | 结账站点下的配送员
GET      | /webapi/settlement/taker\_summary             | taker_id, rst_id    |                        | 执行成功:[返回值](http://dwz.cn/NEwxt "Title")  | 结算详情页面api
GET      | /webapi/settlement/history\_record            | taker_id, rst_id    |                        | 执行成功:[返回值](http://dwz.cn/NExCZ "Title")  | 结算详情历史记录api，查看7天历史记录
GET      | /webapi/settlement/delivery\_order            | taker_id, status, payment_type |             | 执行成功:[返回值](http://dwz.cn/NExTc "Title")  | 订单列表
POST     | /webapi/settlement/confirm\_settle            | taker_id, rst_id    |                        | 执行成功:[返回值](http://dwz.cn/NEyb6 "Title")  | 确定结算

## 公告通知

请求方式  | 接口                                           | 查询参数             | 请求参数                | 返回内容                                       | 描述
-------- | -----------------------------------           |--------             | ------                 | --------------                                | ------
GET      | /webapi/announcement                          |                     |                        | 执行成功:[返回值](http://dwz.cn/NEzEY "Title")  | 获取公告通知
GET      | /webapi/announcement/{:id}                    |                     |                        | 执行成功:[返回值](http://dwz.cn/NEA1V "Title")  | 获取公告详情
GET      | /webapi/announcement/unread                   |                     |                        | 执行成功:[返回值](http://dwz.cn/NEAlC "Title")  | 未读通知

## 和配送员相关的账户内容

请求方式  | 接口                                           | 查询参数             | 请求参数                | 返回内容                                       | 描述
-------- | -----------------------------------           |--------             | ------                 | --------------                                | ------
GET      | /webapi/wallet                                | page_num, page_size |                        | 执行成功:[返回值](http://dwz.cn/NECkL "Title")  | 获取配送员10日内的补贴金额明细，以及补贴总金额
GET      | /webapi/banks                                 |                     |                        | 执行成功:[返回值](http://dwz.cn/NECAZ "Title")  | 返回所有支持的银行列表
POST     | /webapi/bank_card/bind                        |                     | user_name, bank_id, bank_account | 执行成功:[返回值](http://dwz.cn/NEDGK "Title")  | 用户新增绑定银行卡. user_name: 用户姓名, bank_id: 银行id, bank_account: 银行卡号
PUT      | /webapi/bank_card                             |                     | user_name, bank_id, bank_account | 执行成功:[返回值](http://dwz.cn/NEDGK "Title")  | 用户修改绑定银行卡. user_name: 用户姓名, bank_id: 银行id, bank_account: 银行卡号
GET      | /webapi/bank_card                             |                     |                        | 执行成功:[返回值](http://dwz.cn/NEDGK "Title")  | 查询绑定的银行卡

## 商户管理

请求方式  | 接口                                           | 查询参数             | 请求参数                | 返回内容                                       | 描述
-------- | -----------------------------------           |--------             | ------                 | --------------                                | ------
GET      | /webapi/retailer/order\_count                 | status              |                        | 执行成功:[返回值](http://dwz.cn/NEFyf "Title")  | 查询配送员所关联的商户信息，其中包含了该商户下待配送的订单数



## upt，用户位置信息
请求方式  | 接口                                           | 查询参数             | 请求参数                | 返回内容                                       | 描述
-------- | -----------------------------------           |--------             | ------                 | --------------                                | ------
GET      | /webapi/upt\_single                           | acc, longitude, latitude, occurtime, status| | 执行成功:[返回值](http://dwz.cn/NEFZY "Title")  | acc: 精度, longitude: 经度, latitude: 纬度, occurtime: 位于该位置的时间, status: 状态 0:取得订单时定位 1:确认送达时定位 2:通知用户时的位置 3:平时报告位置
GET      | /webapi/upt\_batch                            | upt_list            |                        | 执行成功:[返回值](http://dwz.cn/NEGnV "Title")  |