package me.opens.password_manager.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CryptServiceTest {

    @Test
    public void shouldReturnAdifferentStringAfterEncryption() throws Exception {
        CryptService cryptService = new CryptService("1234");
        String encryptedText = cryptService.encrypt("sometext");
        assertNotEquals("sometext", encryptedText);
    }

    @Test
    public void shouldReturnTheSameTextAfterDecryption() throws Exception {
        CryptService cryptService = new CryptService("1234");
        String encryptedText = cryptService.encrypt("sometext");
        String decryptedText = cryptService.decrypt(encryptedText);
        assertEquals("sometext", decryptedText);
    }
}