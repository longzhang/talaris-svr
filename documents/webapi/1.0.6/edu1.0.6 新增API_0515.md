## 获取公告
GET /webapi/announcement

#### 描述
### 道生

#### 参数：请求头里的token

#### 获取成功，则返回如下

```
{
  "err_code": "200",
  "msg": "",
  "data": {
   sys_time:1423049387459,
   list:[{
    "id": 111,
    "title": "发福利了",
    "summary": "第一次发福利",
    "status": 1,
    "created_at": 1423049387459,
     },
     ......
    ]
  }  
} 
``` 
####获取失败,则返回如下####
```
{
 "err_code": "ANNOUNCEMENT_ERROR_001”, 
 "msg": "获取公告异常,请重试"
 "data": "" 
}
```

## 获取公告详情

GET webapi/announcement/:id

#### 描述
### 道生

#### 参数: 请求头里token,以及

   名称           | 类型           | 必填 | 描述
   ------------- | ------------- |--------|------
  id  | String  |  是  | 公告ID

#### 返回参数
 

#### 若获取成功，则返回如下：

```
{
    "err_code":"200",
    "msg": "",
    "data": {
    "id": 111,
    "title": "发福利了",
    "content": "第一次发福利,raw html",
    "status":1,
    "created_at": 1423049387459,   
    }
}
```
* 若获取失败，则返回如下：

```
 {
  "err_code": "ANNOUNCEMENT_ERROR_002",
  "msg": "获取公告详情异常，请重试"，
  "data": ""
}
```

## 未读通知 
GET webapi/announcement/unread

#### 描述
### 道生

#### 参数: 请求头里token


#### 返回参数
 

#### 若成功，则返回如下：

 ```
{
    "err_code": "200",
    "msg": "",
    "data": {
    	count : 3,
    	popups : [{
    		"id": 111,
    		"title": "发福利了",
    		"popup_content": "第一次发福利 raw html ",
    		"created_at": 1423049387459,
    	}]
    }
}
 ```
 
* 若失败，则返回如下：

```
 {
  "err_code": "ANNOUNCEMENT_ERROR_003",
  "msg": "系统异常，请重试"，
  "data": ""
}
 ```


## 获取配送员补贴金额明细
GET /webapi/wallet

#### 描述
 * 返回配送员的补贴总金额以及10日内的补贴金额明细

#### 输入参数

### 周舒扬

请求头里token，以及

   名称           | 类型           | 必填| 描述
   ------------- | ------------- |--------|------
  page_num          | int      |  是   |查询起始日期
  page_size         | int      |  是   |查询数量
  
  
#### 输出
 * 成功返回

```
{
  "err_code": "200",
  "msg": "",
  "data":{
  	"user_id": 1,
    "total_allowance": 3400,    
    "paid_allowance": 3000,
    "unpaid_allowance": 400,
    "created_at": 1423028886004,
    "updated_at": 1423028886004,
  	[ 
  		{
    	"allowance": 300,    
  		"type": 0,
    	"is_paid": 1,
    	"predict_pay_date": 1423028886004,
    	"date": 1423028886004,
    	"created_at": 1423028886004,
    	"updated_at": 1423028886004
  		},
  		...
  	]
  	"daily_list_size":4
  }
}
```

 * 失败返回

```
{
  "err_code": "WALLET_ERROR_001",
  "msg": "获取失败，请重试！",
  "data": ""
}
```


## 查询我的餐厅（配送员）信息，一个查询接口
GET /webapi/retailer/order_count
#### 参数:请求头里的token

#### 请求参数：

| 名称       |  变量类型 | 参数类型     | 必填  | 说明    |
|---        |---       |---          |---   |---     |
|status     |integer   | Query Param | 是   | 配送员ID |

### 郑稳

#### 如果查询成功，返回我的餐厅信息列表。
```
  {
  "err_code": "200",
  "msg": "",
  "data": [{
    "rst_id": 0,
    "rst_name": "上海交通大学北区1号楼宿舍楼",
    "order_counts": 50//该餐厅有多少单需要待配送
  },{}.....]
}
}
```
#### 查询失败，则返回如下

```
 {
  "err_code": "GET_RESTUARANT_ERROR_100",
  "msg": "获取餐厅列表失败"，
  "data": ""
 }
```

#### User Position Trace
##### 描述
* 目前存在redis中
##### WEB API
```
GET
/webapi/upt_single
```

#####输入参数 请求头中的token 以及:
|名称 | 类型 | 是否必填 | 传参位置 | 说明 |
|------|-----|---------|---------|-------|
|HTTP-ACCESS-TOKEN|String|Y|header|用户的access_token|
|acc|String|Y|body|精度|
|longitude|double|Y|body|经度|
|latitude|double|Y|body|维度|
|occurtime|timestamp|Y|body|位于该位置的时间|
|status|int|Y|body|状态 0:取得订单时定位 1:确认送达时定位 2:通知用户时的位置 3:平时报告位置|

#####返回参数
```
{
	"err_code":"200",
	"msg":"success",
}
```
##### WEB API
```
GET
/webapi/upt_batch
```

#####输入参数 请求头中的token 以及:
|名称 | 类型 | 是否必填 | 传参位置 | 说明 |
|------|-----|---------|---------|-------|
|HTTP-ACCESS-TOKEN|String|Y|header|用户的token|
|upt_list|list of single(不用token )|Y|body|一批地理位置|


#####返回参数
```
{
	"err_code":"200",
	"msg":"success",
}
```

## 当日汇总
GET webapi/delivery\_order\_report/?time=today&user=current\_user\_id&group_by=[status,payment_type]


#### 请求参数：

| 名称 |  类型 | 必填 | 说明 |
|---|---|---|---|----|
|taker_id|integer|是|配送员ID
|rst_id|integer|是|商户ID


#### 能汇总成功，则返回如下

```
{
  "err_code": "",
  "msg": "",
  "data": [
    {
      "status": "1",
      "payment_type": "1",
      "count": 30,
      "sum": 1000
    },
    {
      "status": "2",
      "payment_type": "1",
      "count": 30,
      "sum": 1000
    }
    .......
  ]
}

```
#### 汇总失败

```
 {
  "err_code": "DELIVERY_ORDER_ERROR_620",
  "msg": "系统异常"，
  "data": ""
 }
 
```

## 结算详情⻚⾯api ##
```
GET /webapi/settlement/taker_summary?taker_id=5&rst_id＝3
```
### 请求参数：

名称|类型|必填|说明
---|---|---|----
taker_id|integer|是|配送员ID
rst_id|integer|是|商户ID

####能正常查看，则返回如下####
```
{
 "err_code": “200”,
 "msg": "",
 "data": {
 "lastSettleTime":"2015-03-08 20:00:23",
 "deliveryOrderReport": [
      {
       "status": "1",
       "payment_type": "1",
       "count": 30,
       "sum": 1000
      },
      {
       "status": "2",
       "payment_type": "2",
       "count": 30,
       "sum": 1000
      }
    ]
  }
}
```

####查看失败,则返回如下####
```
{
"err_code": "SETTLEMENT_ERROR_100", 
"msg": "查看结算纪录系统异常"
"data": ""
}
```

## 结算详情历史记录api(最近7天历史纪录) ##
```
GET /webapi/settlement/history_record?taker_id=5&rst_id＝3

```

参数:

名称|类型|必填|说明
---|---|---|----
taker_id|integer|是|配送员ID
rst_id|integer|是|商户ID

####能正常查看,则返回如下####
```
{
  "err_code": “200”,
  "msg": "",
  "data": {
    "settlementRecord": [
      {
      "setttle_time": “2015-03-16 20:00:23",
      "normal_total_count": 100,
      "normal_total_sum": 3000
      },
      {
      "setttle_time": “2015-03-17 19:00:23",
      "normal_total_count": 50,
      "normal_total_sum": 1000
      }
     ]
   }
}
```

####查看失败,则返回如下####
```
{
 "err_code": "SETTLEMENT_ERROR_110”,
 "msg": "查看结算历史纪录系统异常"
 "data": ""
}

```


## 确定结算 ##

```
POST /webapi/settlement/confirm_settle?taker_id=2&rst_id=3

```

参数:

|名称|类型|必填|说明
|---|---|---|----|
|taker_id|integer|是|配送员ID|
|rst_id|integer|是|商户ID|

####能正常查看,则返回如下####
```
{
  "err_code": “200”,
  "msg": "",
  "data": {
      "taker_id":11,
      "settle_result”:1/0 //1成功,0失败
  }
}
```
####查看失败,则返回如下####
```
{
"err_code": "SETTLEMENT_ERROR_130”, 
"msg": “结算异常”
"data": ""
}
```

## 返回所有支持的银行列表
GET /webapi/banks

#### 描述
 * 返回系统支持的银行列表

#### 输入参数

 * 无  
  
#### 输出
 * 成功返回

```
{
  "err_code": "200",
  "msg": "",
  "data":[
  {
  "bank_id": 1,
  "bank_name": "中国工商银行"
  },
  ...
  ]
}
```

 * 失败返回

```
{
  "err_code": "BANK_ERROR_002",
  "msg": "获取失败，请重试！",
  "data": ""
}
```

## 用户新增绑定银行卡
POST /webapi/bank/bind

#### 描述
 * 用户绑定银行卡

#### 输入参数
请求头中的token以及

   名称           | 类型           | 必填| 描述
   ------------- | ------------- |--------|------
  user_name          | String      |  是   |用户姓名
  bank_id          | int      |  是   |银行id
  bank_account     | String      |  是   |银行卡号
  
    
#### 输出
 * 成功返回

```
{
  "err_code": "200",
  "msg": "",
  "data":
  {
  "id": 1,
  "user_id": 3,
  "bank_id": 4,
  "user_name": “王二小”
  "bank_name": "中国工商银行",
  "bank_account": "1111111111111111111",
  "is_bind": 0,
  "created_at": 1423028886004,
  "updated_at": 1423028886004
  }
}
```

 * 失败返回

```
{
  "err_code": "BANK_ERROR_003",
  "msg": "绑定失败，请重试！",
  "data": ""
}
```

## 用户修改绑定的银行卡
PUT /webapi/banks

#### 描述
 * 修改银行卡绑定信息

#### 输入参数
请求头中的token以及

   名称           | 类型           | 必填| 描述
   ------------- | ------------- |--------|------
  user_name          | String      |  是   |用户姓名
  bank_id          | int      |  是   |银行id
  bank_account     | String      |  是   |银行卡号
  
    
#### 输出
 * 成功返回

```
{
  "err_code": "200",
  "msg": "",
  "data":
  {
  "id": 1,
  "user_id": 3,
  "bank_id": 4,
  "user_name": “王二小”
  "bank_name": "中国工商银行",
  "bank_account": "1111111111111111111",
  "is_bind": 0,  
  "created_at": 1423028886004,
  "updated_at": 1423028886004
  }
}
```

 * 失败返回

```
{
  "err_code": "BANK_ERROR_004",
  "msg": "修改绑定失败，请重试！",
  "data": ""
}
```

## 用户查看绑定的银行卡
GET /webapi/bank_card

#### 描述
 * 查看银行卡绑定信息

#### 输入参数

请求头中的token
    
#### 输出
 * 成功返回

```
{
  "err_code": "200",
  "msg": "",
  "data":
  {
  "id": 1,
  "user_id": 3,
  "user_name": “王二小”
  "bank_name": "中国工商银行",
  "bank_account": "1111111111111111111",
  "is_bind": 0,
  "created_at": 1423028886004,
  "updated_at": 1423028886004
  }
}
```

 * 失败返回

```
{
  "err_code": "BANK_ERROR_005",
  "msg": "绑定信息获取失败，请重试！",
  "data": ""
}
```



#### 初次结算，不再弹框，后台默认初始化   负责人：邵荣飞

#### 银行卡绑定 负责人：周舒扬 道生

#### 第三方人员导入：没有接口： 负责人：道生，产品介入

#### 第三方清算： 负责人：邵荣飞

#### 郑稳：拆Napos

##### 一对多：每个人都有，郑稳、邓尊比较多的业务

#### 清算：王志平、张凯帆
