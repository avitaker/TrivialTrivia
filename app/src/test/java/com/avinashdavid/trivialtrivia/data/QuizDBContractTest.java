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
    public void buildUriQuizId(){
        //buildUriQuizId Should Return the URI Needed
        assertEquals("content://com.avinashdavid.trivialtrivia.data/quiz/3", QuizDBContract.QuizEntry.buildUriQuizId(3).toString() );
    }

    @Test
    public void buildUriCategoryId(){
        // buildUriCategoryId should return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/4", QuizDBContract.CategoryEntry.buildUriCategoryId(4).toString());
    }

    @Test
    public void buildUriCategoryName(){
        // buildUriCategoryName should return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/1", QuizDBContract.CategoryEntry.buildUriCategoryName("general").toString());
    }

    @Test
    public void buildUriCategoryTotalQuestionsAnsweredForId(){
        // total questions answered should return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/categoryTotalQuestions/_id/3", QuizDBContract.CategoryEntry.buildUriCategoryTotalQuestionsAnsweredForId(3).toString());
    }

    @Test
    public void buildUriCategoryIndex(){
        // category index return the expected URI
        assertEquals("content://com.avinashdavid.trivialtrivia.data/category/4", QuizDBContract.CategoryEntry.buildUriCategoryIndex(3).toString());
    }
}