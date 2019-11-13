package com.aruba.wifirssi;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.aruba.wifirssi.data.AppDatabase;
import com.aruba.wifirssi.data.WifiAccessPointDAO;
import com.aruba.wifirssi.model.WifiAccessPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class WifiAccessPointReadWriteTest {

    private WifiAccessPointDAO accessPointDAO;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        accessPointDAO = db.accessPointDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
        accessPoint = null;
    }

    WifiAccessPoint accessPoint;
    @Test
    public void writeAccessPointAndReadInList() throws Exception {
        accessPoint = new WifiAccessPoint();
        accessPoint.setSsid("test_ap");
        accessPointDAO.insertAll(accessPoint);
        WifiAccessPoint bySsid = accessPointDAO.findBySsid("test_");
        assertThat(bySsid, equalTo(accessPoint));
    }

}
