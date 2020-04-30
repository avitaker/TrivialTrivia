package com.avinashdavid.trivialtrivia.AcknowledgementsTests;

import com.avinashdavid.trivialtrivia.acknowledgments.AcknowledgmentObject;
import com.avinashdavid.trivialtrivia.acknowledgments.AcknowledgmentStorage;
import android.net.Uri;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class AcknowledgmentStorageTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AcknowledgementStorage_getQuestionsAcknowledgmentObjectsTest(){
        AcknowledgmentStorage storage = new AcknowledgmentStorage();
        ArrayList<AcknowledgmentObject> list = storage.getQuestionsAcknowledgmentObjects();
        AcknowledgmentObject item = new AcknowledgmentObject("Mental Floss", "http://mentalfloss.com/");

        assertNotNull(list);
        assertEquals(item.getName(), list.get(1).getName());
    }

    @Test
    public void AcknowledgementStorage_getImagesAcknowledgmentObjectsTest(){
        
    }
}