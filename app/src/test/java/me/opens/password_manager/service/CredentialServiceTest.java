package me.opens.password_manager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import me.opens.password_manager.config.SharedPreferenceUtils;

import static me.opens.password_manager.util.Constants.USER_KEY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CredentialServiceTest {

    @InjectMocks
    CredentialService credentialService;

    @Mock
    SharedPreferenceUtils sharedPreferenceUtils;

    @Test
    public void shouldReturnTrueIfTheyMatches() throws Exception {
        when(sharedPreferenceUtils.getUserKey(USER_KEY)).thenReturn("key");
        assertTrue(credentialService.isKeyMatched("key"));
    }

    @Test
    public void shouldReturnFalseIfTheyMatches() throws Exception {
        when(sharedPreferenceUtils.getUserKey(USER_KEY)).thenReturn("key");
        assertFalse(credentialService.isKeyMatched("randomKey"));
    }
}