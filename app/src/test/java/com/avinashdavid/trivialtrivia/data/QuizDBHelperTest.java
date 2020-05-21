package com.avinashdavid.trivialtrivia.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class QuizDBHelperTest {

    private QuizDBHelper quizDBHelper;
    private QuizDBHelper secondQuizDBHelper;

    @Mock
    Context mockContext = mock(Context.class);

    @Mock
    private SQLiteDatabase db = mock(SQLiteDatabase.class);


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        quizDBHelper = QuizDBHelper.getInstance(mockContext);
        secondQuizDBHelper = new QuizDBHelper(RuntimeEnvironment.application);
    }

    @Test
    public void categoryContentValuesFirstCategory() {
        ArrayList<ContentValues> content = quizDBHelper.initialCategoryContentValues();
        ContentValues generalCategory = content.get(0);
        assertEquals( "categoryTimeOverall=0 categoryTimeCorrect=0 categoryTotalQuestions=0 categoryCorrectlyAnswered=0 categoryName=general categoryTimeWrong=0", generalCategory.toString() );
    }

    @Test
    public void initialCategoryContentValuesAfterCreatingDB() {
        ArrayList<ContentValues> content = quizDBHelper.initialCategoryContentValues();
        quizDBHelper.onCreate(db);
        ContentValues generalCategory = content.get(3);
        assertEquals( "history", generalCategory.get("categoryName"));
    }

    @Test
    public void initialCountofCategoryContent() {
        secondQuizDBHelper.onUpgrade(db, 0, 0);
        int total =  secondQuizDBHelper.getCount();
        assertEquals( 6, total );
    }




}