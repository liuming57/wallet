# wallet
#### 建表SQL

```sql
`CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `created_by` bigint NOT NULL COMMENT '创建人',
  `creation_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_by` bigint NOT NULL COMMENT '最后修改人',
  `last_update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `object_version` bigint NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_u1` (`user_id`) USING BTREE COMMENT '用户唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `option_version` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `bank_id` bigint NOT NULL COMMENT '银行卡id',
  `option_id` int NOT NULL COMMENT '操作类型id，1:存钱，2:取钱，3:转钱，4:提现',
  `option_money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '操作金额',
  `created_by` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `creation_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_by` bigint NOT NULL DEFAULT '-1' COMMENT '最后修改人',
  `last_update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `object_version` bigint NOT NULL DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `user_update_n1` (`user_id`,`last_update_time`) USING BTREE COMMENT '方便查询，添加联合索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

#### 分析

查询接口实现了redis缓存，代码如下：

```java
@Override
    public Wallet selectByUserId(Long userId) {
        Wallet o = JSON.parseObject(stringRedisTemplate.opsForValue().get("wallet_" + userId), Wallet.class);
        if (o != null) {
            // 查询到数据
            return o;
        } else {
            // 缓存中没有，从数据库中查询
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            List<Wallet> wallets = walletMapper.selectList(wallet);
            if (wallets.size() == 0) {
                return null;
            } else {
                // 将查询结果存入到缓存中，同时还可以设置有效期
                stringRedisTemplate.opsForValue().set("wallet_" + userId, JSON.toJSONString(wallets.get(0)), 1, TimeUnit.DAYS);
                return wallets.get(0);
            }
        }
    }
```

消费，退款接口我使用option_id来区分操作类型，后续可用枚举实现

数据修改使用redission分布式锁来保证高并发的安全，**这里默认前端会传操作金额optionMoney和操作后余额money**
调用示例，id为null新增，id不为null更新
POST http://localhost:8090/v1/wallet
Content-Type: application/json

[
{
"id": 11,
"userId": 1,
"money": 120,
"optionMoney": 20,
"optionId": 3,
"objectVersion": 1,
"lastUpdateBy": -1
}
]

```java

@Override
    @Transactional(rollbackFor = Exception.class)
    public List<Wallet> batchUpdate(List<Wallet> updateList) {
        if (updateList == null) {
            return null;
        }
        // redission分布式锁
        RLock lock = redissonClient.getLock("user01");
        log.info("加锁" + Thread.currentThread());
        try {
            lock.lock();
            // 实现业务逻辑
            updateList.forEach(wallet -> {
                walletMapper.updateById(wallet);
                // 添加明细记录
                optionVersionMapper.insert(this.getEntity(wallet));
            });
            updateList.forEach(w -> stringRedisTemplate.opsForValue().set("wallet_" + w.getId(), JSON.toJSONString(w), 1, TimeUnit.DAYS));
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info("解锁" + Thread.currentThread());
                lock.unlock();
            }
        }
        return updateList;
    }
```

查询用户钱包金额变动明细的接口，我在表设计加了userId和lastUpdateTime的联合索引，以至于数据量大的时候，加快查询排序速度。

#### 扩展优化

变动明细表可以加入余额字段，数据量大的话可以考虑分库分表，毕竟压力都在数据库端。

其次，明细表写入数据可以用消息中间件，异步解耦，使得支付的api响应更快。