package fast.campus.fcss05.generator;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class MyGeneratorTest {

    @Test
    public void test() {
        // given
        BytesKeyGenerator generator = KeyGenerators.shared(16);

        // when
        byte[] key1 = generator.generateKey();
        byte[] key2 = generator.generateKey();

        // then
        assertEquals(key1, key2);
    }

    @Test
    public void test2() {
        String salt = KeyGenerators.string().generateKey();
        String password = "secret";
        String valueToEncrypt = "HELLO";

        BytesEncryptor bytesEncryptor = Encryptors.standard(password, salt);
        byte[] encrypted = bytesEncryptor.encrypt(valueToEncrypt.getBytes());
        byte[] decrypted = bytesEncryptor.decrypt(encrypted);

        assertArrayEquals(valueToEncrypt.getBytes(), decrypted);
    }

    @Test
    public void test3() {
        String valueToEncrypt = "HELLO";

        TextEncryptor textEncryptor = Encryptors.noOpText();
        String encrypted = textEncryptor.encrypt(valueToEncrypt);
        String decrypted = textEncryptor.decrypt(encrypted);

        assertEquals(valueToEncrypt, decrypted);
    }

    @Test
    public void test4() {
        String salt = KeyGenerators.string().generateKey();
        String password = "secret";
        String valueToEncrypt = "HELLO";

        TextEncryptor textEncryptor = Encryptors.delux(password, salt);
        String encrypted = textEncryptor.encrypt(valueToEncrypt);
        String decrypted = textEncryptor.decrypt(encrypted);

        assertEquals(valueToEncrypt, decrypted);
    }
}