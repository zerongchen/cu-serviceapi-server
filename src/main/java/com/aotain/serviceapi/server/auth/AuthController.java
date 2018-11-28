package com.aotain.serviceapi.server.auth;

import com.aotain.cu.auth.AuthQuery;

import com.aotain.cu.auth.ResponseData;
import com.aotain.cu.auth.User;

import com.aotain.serviceapi.server.util.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/11/20
 */
@RestController
@RequestMapping(value="/oauth")
public class AuthController {

    @PostMapping("/token")
    public ResponseData auth(@RequestBody AuthQuery query) throws Exception {
        if (StringUtils.isBlank(query.getAccessKey()) || StringUtils.isBlank(query.getSecretKey())) {
            return ResponseData.failByParam("accessKey and secretKey not null");
        }

        if (!"admin123".equals(query.getAccessKey()) || !"1qaz@WSX".equals(query.getSecretKey())){
            return ResponseData.failByParam("认证失败");
        }

        User user = new User(1L);
//        if (user == null) {
//            return ResponseData.failByParam("认证失败");
//        }

        JWTUtils jwt = JWTUtils.getInstance();
        return ResponseData.ok(jwt.getToken(user.getId().toString()));
    }

    @GetMapping("/token")
    public ResponseData oauth(AuthQuery query) throws Exception {
        if (StringUtils.isBlank(query.getAccessKey()) || StringUtils.isBlank(query.getSecretKey())) {
            return ResponseData.failByParam("accessKey and secretKey not null");
        }

        if (!"admin123".equals(query.getAccessKey()) || !"1qaz@WSX".equals(query.getSecretKey())){
            return ResponseData.failByParam("认证失败");
        }

        User user = new User(2L);
//        if (user == null) {
//            return ResponseData.failByParam("认证失败");
//        }

        JWTUtils jwt = JWTUtils.getInstance();
        return ResponseData.ok(jwt.getToken(user.getId().toString()));
    }
}
