package me.opens.password_manager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.repository.CredentialDataSource;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private CredentialDataSource credentialDataSource;

    @Test
    public void shouldReturnTrueIfTheKeysMatch() throws Exception {
        boolean matched = authorizationService.validate("1234", "1234");
        assertTrue(matched);
    }

    @Test
    public void shouldReturnFalseIfTheKeysDonotMatch() throws Exception {
        boolean matched = authorizationService.validate("134", "abcd");
        assertFalse(matched);
    }

    @Test
    public void shouldReturnTrueIfTheCredentialsMatch() throws Exception {
        Credential credential = new Credential();
        credential.setDomain("LOGIN");
        credential.setUsername("user");
        credential.setPassword("password");
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));

        assertTrue(authorizationService.validateKey("user", "password"));
    }

    @Test
    public void shouldReturnFalseIfTheCredentialsDoNotMatch() throws Exception {
        Credential credential = new Credential();
        credential.setDomain("LOGIN");
        credential.setUsername("user");
        credential.setPassword("password");
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));

        assertTrue(authorizationService.validateKey("user", "pwd"));
    }
}