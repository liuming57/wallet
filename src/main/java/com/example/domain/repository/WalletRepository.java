package com.example.domain.repository;

import com.example.domain.entity.Wallet;

import java.util.List;

/**
 * (Wallet)资源库
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */
public interface WalletRepository {
    /**
     * 查询
     *
     * @param wallet 查询条件
     * @return 返回值
     */
    List<Wallet> selectList(Wallet wallet);

    /**
     * 根据用户id查询（可关联表）
     *
     * @param userId 用户id
     * @return 返回值
     */
    Wallet selectByUserId(Long userId);

    /**
     * 批量更新
     *
     * @param updateList 更新
     * @return 返回值
     */
    List<Wallet> batchUpdate(List<Wallet> updateList);

    /**
     * 批量插入
     *
     * @param insertList 更新
     * @return 返回值
     */
    List<Wallet> batchInsert(List<Wallet> insertList);
}
