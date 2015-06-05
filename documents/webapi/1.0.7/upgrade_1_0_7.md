## 扫描创建配送单/勾选确认改变配送单状态， 手动录入创建配送单
POST /webapi/delivery_order/create


#### 参数：请求头里的token,以及以下参数


  名称            |  类型                    |必填 | 描述
  ---------------|--------------------------|----|----
  ele\_order\_id\_list | string[]           | 否 |饿单ID
  create\_type   | string 扫码为0，勾选确认为1, 手工录入为3 | 是 |饿单类型
  customer_phone | String|  否 | 用户联系电话
  rst_id | int | 否 |当前所在餐厅

* 创建成功，则返回如下

```
{
  "err_code": "200",
  "msg": "",
  "data": [{
    "id": 111,
    "rst_id": 11111,
    "source": 1,
    "ele_order_id": 11111,
    "ele_order_sn": 5,
    "taker_id": 1111,
    "taker_mobile": 1867777777,
    "station_id": 2000,
    "receiver_name": "张三",
    "receiver_mobile": 1895678002,
    "receiver_address": "zhengwen505@163.com",
    "total_amount": 2000,
    "paied_amount": 2000,
    "payment_type": 1,
    "status": 1,
    "created_type": 1,
    "created_at": 1423049387459,
    "updated_at": 1423049387459
  },{}.....]
}

```

