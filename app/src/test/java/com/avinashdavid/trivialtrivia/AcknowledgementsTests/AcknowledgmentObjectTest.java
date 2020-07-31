package com.avinashdavid.trivialtrivia.AcknowledgementsTests;

import android.net.Uri;

import com.avinashdavid.trivialtrivia.acknowledgments.AcknowledgmentObject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.mockito.MockitoAnnotations;


@RunWith(RobolectricTestRunner.class)
public class AcknowledgmentObjectTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AcknowledgementObject_GetNameTest () {
        String name = "Bogus";
        String link = "http://www.bogus.com/";
        AcknowledgmentObject obj = new AcknowledgmentObject(name, link);
        String actual = obj.getName();
        assertNotNull(obj);
        assertEquals("Bogus", actual);
    }

    @Test
    public void AcknowledgementObject_GetLinkTest () {
        String name = "Bogus";
        String link = "http://www.bogus.com/";
        Uri uri = Uri.parse(link);
        AcknowledgmentObject obj = new AcknowledgmentObject(name, uri);
        Uri actual = obj.getLink();
        assertNotNull(obj);
        assertEquals(uri, actual);
    }
}