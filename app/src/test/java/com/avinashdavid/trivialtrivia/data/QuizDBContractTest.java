package com.avinashdavid.trivialtrivia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class QuizDBContractTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testbuildUriQuizId(){
        //buildUriQuizId Should Return the URI Needed
        assertEquals("content://com.avinashdavid.trivialtrivia.data/quiz/3", QuizDBContract.QuizEntry.buildUriQuizId(3).toString() );
    }

    @Test
    public void testbuildUriCategoryId(){
        // buildUriCategoryId should return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/4", QuizDBContract.CategoryEntry.buildUriCategoryId(4).toString());
    }

    @Test
    public void testbuildUriCategoryName(){
        // buildUriCategoryName should return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/1", QuizDBContract.CategoryEntry.buildUriCategoryName("general").toString());
    }

}