// src/main/java/com/querybuilder/controller/CryptoController.java
package com.querybuilder.controller;

import com.querybuilder.util.CryptoUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crypto")
@Profile("dev")
public class CryptoController {

    private final CryptoUtil cryptoUtil;

    public CryptoController(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String value) {
        return cryptoUtil.encrypt(value);
    }
}