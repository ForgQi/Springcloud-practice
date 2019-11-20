package com.forgqi.common;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 〈自定义无加密密码验证〉
 * 重写了PasswordEncoder  密码是不加密的
 * 加密的话 使用 BCryptPasswordEncoder
 *
 * @author ???
 * @since 1.0.0
 */
public class AESEncryptPasswordEncoder implements PasswordEncoder {
    private final TextEncryptor textEncryptor = Encryptors.text("lzu", "deadbeef");
    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        //密码对比 密码对 true 反之 false
        //CharSequence 数据库中的密码
        //s 前台传入的密码
        try {
            return textEncryptor.decrypt(charSequence.toString()).equals(textEncryptor.decrypt(s));
        }catch (Exception e){
            return false;
        }
    }
}
