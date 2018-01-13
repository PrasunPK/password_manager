package me.opens.password_manager.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.storage.CredentialDatabase;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class CredentialDaoTest {
    private CredentialDao mCredentialDao;

    private CredentialDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CredentialDatabase.class).build();
        mCredentialDao = mDb.credentialDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void shouldGetAllLoginCredentials() throws Exception {
        Credential credential = new Credential();
        credential.setDomain("LOGIN");
        credential.setUsername("user");
        credential.setPassword("password");
        Credential anotherCredential = new Credential();
        anotherCredential.setDomain("BANK");
        anotherCredential.setUsername("user");
        anotherCredential.setPassword("password");
        mCredentialDao.insertAll(asList(credential,anotherCredential));

        List<Credential> loginCreds = mCredentialDao.getLoginCredentials();
        Credential actualCredential = loginCreds.get(0);
        assertNotNull(actualCredential);
        assertThat(loginCreds.size(), is(1));
        assertThat(actualCredential.getDomain(), is(credential.getDomain()));
        assertThat(actualCredential.getUsername(), is(credential.getUsername()));
        assertThat(actualCredential.getPassword(), is(credential.getPassword()));
    }
}
