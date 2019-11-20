package com.forgqi.resourcebaseserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.encrypt.EncryptorFactory;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.test.context.junit4.SpringRunner;

import javax.security.auth.kerberos.EncryptionKey;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceBaseServerApplicationTests {

    @Test
    public void contextLoads() {
    }
}
