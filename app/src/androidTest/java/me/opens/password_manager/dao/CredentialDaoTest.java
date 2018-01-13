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
    private Credential credential;
    private Credential anotherCredential;

    @Before
    public void setUp() throws Exception {
        credential =
                createCredential("LOGIN", "user", "password", null);
        anotherCredential =
                createCredential("BANK", "user", "password", "user1");
        mCredentialDao.insertAll(asList(credential, anotherCredential));
    }

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
        List<Credential> loginCreds = mCredentialDao.getLoginCredentials();
        Credential actualCredential = loginCreds.get(0);
        assertNotNull(actualCredential);
        assertThat(loginCreds.size(), is(1));
        assertThat(actualCredential.getDomain(), is(credential.getDomain()));
        assertThat(actualCredential.getUsername(), is(credential.getUsername()));
        assertThat(actualCredential.getPassword(), is(credential.getPassword()));
    }

    @Test
    public void shouldInsertACredential() throws Exception {
        Credential credential =
                createCredential("EDUCATION", "usern", "pword", null);
        mCredentialDao.insert(credential);

        assertThat(mCredentialDao.getAll().size(), is(3));
    }

    @Test
    public void shouldGetCredentialsForASpecificUser() throws Exception {
        Credential credential =
                createCredential("NOTHING", "usern", "pasword", "user1");
        mCredentialDao.insert(credential);

        List<Credential> credentials = mCredentialDao.getAllFor("user1");
        assertThat(credentials.size(), is(2));
        assertThat(credentials.get(0).getDomain(), is("BANK"));
        assertThat(credentials.get(1).getDomain(), is("NOTHING"));
    }

    @Test
    public void shouldGetEmptyListOfCredentialsForASpecificUserWhenThereAreNothingForAUser() throws Exception {
        List<Credential> credentials = mCredentialDao.getAllFor("noone");
        assertThat(credentials.size(), is(0));
    }

    private Credential createCredential(String domain, String username, String password, String belongsTo) {
        Credential credential = new Credential();
        credential.setDomain(domain);
        credential.setUsername(username);
        credential.setPassword(password);
        credential.setBelongsTo(belongsTo);
        return credential;
    }
}
