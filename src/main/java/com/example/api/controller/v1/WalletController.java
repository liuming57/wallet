package com.example.api.controller.v1;

import com.example.domain.entity.Wallet;
import com.example.domain.repository.WalletRepository;
import com.example.service.WalletService;
import com.example.utils.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Wallet)表控制层
 *
 * @author liuming
 * @since 2023-02-17 11:23:05
 */

@RestController("walletSiteController.v1")
@RequestMapping("/v1/wallet")
public class WalletController {

    @Resource
    private WalletRepository walletRepository;
    @Resource
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<List<Wallet>> list(Wallet wallet) {
        List<Wallet> list = walletService.selectList(wallet);
        return Results.success(list);
    }

    /**
     * 根据用户id查钱包
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> detail(@PathVariable Long userId) {
        Wallet wallet = walletRepository.selectByUserId(userId);
        return Results.success(wallet);
    }

    /**
     * 存取款操作
     * @param wallets
     * @return
     */
    @PostMapping
    public ResponseEntity<List<Wallet>> save(@RequestBody List<Wallet> wallets) {
        walletService.saveData(wallets);
        return Results.success(wallets);
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<Wallet> wallets) {
        return Results.success();
    }

}

