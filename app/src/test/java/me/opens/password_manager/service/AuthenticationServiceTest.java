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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

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
    public void shouldReturnTrueIfTheCredentialsMatch() throws Exception {
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
        assertTrue(authenticationService.isValidUser("user", "password"));
    }

    @Test
    public void shouldReturnFalseIfTheCredentialsDoNotMatch() throws Exception {
        when(credentialDataSource.getLoginCredentials()).thenReturn(asList(credential));
        assertFalse(authenticationService.isValidUser("user", "pwd"));
    }
}