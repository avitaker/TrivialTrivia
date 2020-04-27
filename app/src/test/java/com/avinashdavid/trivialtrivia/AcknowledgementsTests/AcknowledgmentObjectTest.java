package com.avinashdavid.trivialtrivia.AcknowledgementsTests;

import android.net.Uri;

import com.avinashdavid.trivialtrivia.acknowledgments.AcknowledgmentObject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


public class AcknowledgmentObjectTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AcknowledgementObject_GetNameTest () {
        String name = "Bogus";
        String link = "http://www.bogus.com/";
        Uri uri = null;
        AcknowledgmentObject obj = new AcknowledgmentObject(name, uri);
        String actual = obj.getName();
        assertNotNull(obj);
        assertEquals("Bogus", actual);
    }

    @Test
    public void AcknowledgementObject_GetLinkTest () {

    }
}