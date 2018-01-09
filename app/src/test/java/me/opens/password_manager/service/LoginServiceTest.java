package me.opens.password_manager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Test
    public void shouldReturnTrueIfTheKeysMatch() throws Exception {
        boolean matched = loginService.validate("1234", "1234");
        assertTrue(matched);
    }

    @Test
    public void shouldReturnFalseIfTheKeysDonotMatch() throws Exception {
        boolean matched = loginService.validate("134", "abcd");
        assertFalse(matched);
    }
}