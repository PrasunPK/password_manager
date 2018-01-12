package me.opens.password_manager.service;

import org.junit.Before;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private CredentialDataSource credentialDataSource;
    private Credential credential;

    @Before
    public void setUp() throws Exception {
        credential = new Credential();
        credential.setDomain("LOGIN");
        credential.setUsername("user");
        credential.setPassword("password");
    }

    @Test
    public void shouldReturnTrueIfTheKeysMatch() throws Exception {
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
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
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
        assertTrue(authorizationService.isValidUser("user", "password"));
    }

    @Test
    public void shouldReturnFalseIfTheCredentialsDoNotMatch() throws Exception {
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
        assertFalse(authorizationService.isValidUser("user", "pwd"));
    }

    @Test
    public void shouldRegisterIfTheAccountDoesNotExists() throws Exception {
        when(credentialDataSource.registerAccount("username", "password")).thenReturn(true);
        boolean registered = authorizationService.register("username", "password");
        assertTrue(registered);
    }

    @Test
    public void shouldNotRegisterWhenTheUserExists() throws Exception {
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
        boolean canBeRegistered = authorizationService.register("user", "password");
        verify(credentialDataSource, times(0)).registerAccount("user", "password");
        assertFalse(canBeRegistered);
    }
}