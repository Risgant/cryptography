package by.shnitko.util;

import by.shnitko.controller.dto.NoteDto;
import by.shnitko.controller.dto.NotesDto;
import by.shnitko.controller.dto.SingleNoteDto;
import by.shnitko.error.BadCredentialsException;
import by.shnitko.repository.entity.NoteEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Encryptor {
    public static String encryptRSA(String text, String publicKey) {
        try {
            var decoder = Base64.getDecoder();
            var encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(publicKey));
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(publicKeySpec));
            var encrypted = encryptCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public static NotesDto encryptSerpent(List<NoteDto> notes, String publicKey) {

        try {
            byte[] keyMaterial = generateRandomBytes(16);
            byte[] initVector = generateRandomBytes(16);
            String encryptedInitVector = encryptRSA(initVector, publicKey);
            int[][] keys = Serpent_Standard.makeKey(keyMaterial);
            List<String> encryptedKeys = new ArrayList<>();
            for (int[] key : keys) {
                //encrypt key with RSA
                var encryptedKey = encryptRSA(intArrayToByteArray(key), publicKey);
                encryptedKeys.add(encryptedKey);
            }
            var encryptedNotes = new ArrayList<NoteDto>();
            for (var note : notes) {
                var encryptedNote = new NoteDto();
                encryptedNote.setId(note.getId());
                encryptedNote.setTitle(encryptTextSerpent(note.getTitle(), keys, initVector));
                encryptedNote.setText(encryptTextSerpent(note.getText(), keys, initVector));
                encryptedNotes.add(encryptedNote);
            }
            return new NotesDto(encryptedNotes, encryptedKeys , encryptedInitVector);
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
    public static SingleNoteDto encryptSerpent(String text, String publicKey) {
        try {
            byte[] keyMaterial = generateRandomBytes(16);
            byte[] initVector = generateRandomBytes(16);
            String encryptedInitVector = encryptRSA(initVector, publicKey);
            int[][] keys = Serpent_Standard.makeKey(keyMaterial);
            List<String> encryptedKeys = new ArrayList<>();
            for (int[] key : keys) {
                var encryptedKey = encryptRSA(intArrayToByteArray(key), publicKey);
                encryptedKeys.add(encryptedKey);
            }
            var encryptedNote = new SingleNoteDto();
            encryptedNote.setText(encryptTextSerpent(text, keys, initVector));
            encryptedNote.setKeys(encryptedKeys);
            encryptedNote.setInitVector(encryptedInitVector);
            return encryptedNote;
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }


    public static String encryptTextSerpent(String text, int[][] keys, byte[] initVector) {
        try {
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            if(textBytes.length % 16 != 0) {
                int padding = 16 - textBytes.length % 16;
                byte[] paddingBytes = new byte[padding];
                paddingBytes[0] = (byte)128;
                textBytes = ByteBuffer.allocate(textBytes.length + padding).put(textBytes).put(paddingBytes).array();
            }
            //encrypt textBytes with Serpent by 16 bytes blocks
            var encryptedText = new byte[textBytes.length];
            var previousBlock = initVector;
            for (int i = 0; i < textBytes.length; i += 16) {
                var block = new byte[16];
                System.arraycopy(textBytes, i, block, 0, 16);
                //xor block with previous block
                for (int j = 0; j < 16; j++) {
                    block[j] = (byte) (block[j] ^ previousBlock[j]);
                }
                var encryptedBlock = Serpent_Standard.blockEncrypt(block, 0, keys);
                System.arraycopy(encryptedBlock, 0, encryptedText, i, 16);
                previousBlock = encryptedBlock;
            }
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public static String decryptTextSerpent(String text, int[][] keys, byte[] initVector) {
        try {
            byte[] textBytes = Base64.getDecoder().decode(text);
            var decryptedText = new byte[textBytes.length];
            var previousBlock = initVector;
            for (int i = 0; i < textBytes.length; i += 16) {
                var block = new byte[16];
                var block2 = new byte[16];
                System.arraycopy(textBytes, i, block, 0, 16);
                System.arraycopy(textBytes, i, block2, 0, 16);
                var decryptedBlock = Serpent_Standard.blockDecrypt(block, 0, keys);
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
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    //encrypt byte array with RSA and return string
    public static String encryptRSA(byte[] data, String publicKey) {
        try {
            var decoder = Base64.getDecoder();
            var encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(publicKey));
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(publicKeySpec));
            var encrypted = encryptCipher.doFinal(data);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Throwable e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    //generate random byte array
    public static byte[] generateRandomBytes(int length) {
        var bytes = new byte[length];
        var random = new java.security.SecureRandom();
        random.nextBytes(bytes);
        return bytes;
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
