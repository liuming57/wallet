package com.example.infra.repository.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.domain.entity.OptionVersion;
import com.example.domain.entity.Wallet;
import com.example.domain.repository.WalletRepository;
import com.example.infra.mapper.OptionVersionMapper;
import com.example.infra.mapper.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (Wallet)资源库
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
@Component
@Slf4j
public class WalletRepositoryImpl implements WalletRepository {
    @Resource
    private WalletMapper walletMapper;
    @Resource
    private OptionVersionMapper optionVersionMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public List<Wallet> selectList(Wallet wallet) {
        return walletMapper.selectList(wallet);
    }

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

    /**
     * 批量更新
     *
     * @param updateList 更新
     * @return 返回值
     */
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

    /**
     * 批量插入
     *
     * @param insertList 更新
     * @return 返回值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Wallet> batchInsert(List<Wallet> insertList) {
        RLock lock = redissonClient.getLock("user01");
        log.info("加锁" + Thread.currentThread());
        try {
            lock.lock();
            // 实现业务逻辑
            insertList.forEach(w -> {
                walletMapper.insert(w);
                // 添加明细记录
                optionVersionMapper.insert(this.getEntity(w));
                stringRedisTemplate.opsForValue().set("wallet_" + w.getId(), JSON.toJSONString(w), 1, TimeUnit.DAYS);
            });
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info("解锁" + Thread.currentThread());
                lock.unlock();
            }
        }
        return insertList;
    }

    private OptionVersion getEntity(Wallet wallet) {
        OptionVersion optionVersion = new OptionVersion();
        optionVersion.setUserId(wallet.getUserId())
                .setBankId(-1L)
                .setOptionId(wallet.getOptionId())
                .setOptionMoney(wallet.getOptionMoney());
        return optionVersion;
    }
}

