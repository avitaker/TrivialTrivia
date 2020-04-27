package com.avinashdavid.trivialtrivia.AcknowledgementsTests;

import android.net.Uri;

import com.avinashdavid.trivialtrivia.acknowledgments.AcknowledgmentObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AcknowledgmentObjectTest {

    @Before
    public void setUp() throws Exception {
        String name = "Bogus";
        String link = "http://www.bogus.com/";
        Uri uri = Uri.parse(link);
        AcknowledgmentObject obj = new AcknowledgmentObject(name, uri);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AcknowledgementObject_GetNameTest () {

    }

    @Test
    public void AcknowledgementObject_GetLinkTest () {

    }
}