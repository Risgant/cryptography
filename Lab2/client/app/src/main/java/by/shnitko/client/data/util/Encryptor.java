package by.shnitko.client.data.util;

import android.os.Build;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

public class Encryptor {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptTextSerpent(String text, int[][] keys, byte[] initVector) {
        byte[] textBytes = Base64.getDecoder().decode(text);
        byte[] decryptedText = new byte[textBytes.length];
        byte[] previousBlock = initVector;
        for (int i = 0; i < textBytes.length; i += 16) {
            byte[] block = new byte[16];
            byte[] block2 = new byte[16];
            System.arraycopy(textBytes, i, block, 0, 16);
            System.arraycopy(textBytes, i, block2, 0, 16);
            byte[] decryptedBlock = Serpent_Standard.blockDecrypt(block, 0, keys);
            //xor block with previous block
            for (int j = 0; j < 16; j++) {
                decryptedBlock[j] = (byte) (decryptedBlock[j] ^ previousBlock[j]);
            }
            System.arraycopy(decryptedBlock, 0, decryptedText, i, 16);
            previousBlock = block2;
        }
        for(int i = decryptedText.length-1; i >= 0; --i) {
            if(decryptedText[i] == 0) {
                continue;
            } else if(decryptedText[i] == (byte)128) {
                decryptedText = Arrays.copyOfRange(decryptedText, 0, i);
                break;
            } else {
                break;
            }
        }

        return new String(decryptedText, StandardCharsets.UTF_8);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int[][] decryptKeys( List<String> encryptedKeys, PrivateKey privateKey) {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            int[][] keys = new int[encryptedKeys.size()][4];
            for(int i = 0; i < encryptedKeys.size(); ++i) {
                String encryptedKey = encryptedKeys.get(i);
                byte[] keyBytes = decryptCipher.doFinal(Base64.getDecoder().decode(encryptedKey));
                int[] key = byteArrayToIntArray(keyBytes);
                keys[i] = key;
            }
            return keys;
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] decryptInitVector(String encryptedVector, PrivateKey privateKey) {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return decryptCipher.doFinal(Base64.getDecoder().decode(encryptedVector));
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static byte[] intArrayToByteArray(int[] input) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(input.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(input);

        return byteBuffer.array();
    }

    //convert byte array to int array
    public static int[] byteArrayToIntArray(byte[] input) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(input.length);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        byteBuffer.put(input);
        int[] result = new int[intBuffer.remaining()];
        intBuffer.get(result);
        return result;
    }
}
