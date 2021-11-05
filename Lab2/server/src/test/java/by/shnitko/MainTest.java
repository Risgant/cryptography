package by.shnitko;


import by.shnitko.util.Encryptor;
import by.shnitko.util.Serpent_Standard;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

@Slf4j
public class MainTest {
    @Test
    public void test() {
        String pass = "123456";
        String encoded = new BCryptPasswordEncoder().encode(pass);
        System.out.println(encoded);
    }

    @Test @SneakyThrows
    public void cipherTest() {
        byte[] keyMaterial = Encryptor.generateRandomBytes(16);
        byte[] initVector = Encryptor.generateRandomBytes(16);
        int[][] keys = Serpent_Standard.makeKey(keyMaterial);
        String text = "Hello world! af asf wqer ASD sadafsfg sfhdg asf e 345234 5w";
        String encryptedText = Encryptor.encryptTextSerpent(text, keys, initVector);
        String decryptedText = Encryptor.decryptTextSerpent(encryptedText, keys, initVector);

        log.info("encryptedText: {}", encryptedText);
        log.info("decryptedText: {}", decryptedText);
    }
}
