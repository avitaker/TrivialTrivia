package com.avinashdavid.trivialtrivia.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class QuizDBHelperTest {

    private QuizDBHelper quizDBHelper;

    @Mock
    Context mockContext = mock(Context.class);

    @Mock
    private SQLiteDatabase db = mock(SQLiteDatabase.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        quizDBHelper = QuizDBHelper.getInstance(mockContext);
    }

    @Test
    public void initialCategoryContentValuesFirstCategory() {
        ArrayList<ContentValues> content = quizDBHelper.initialCategoryContentValues();
        ContentValues generalCategory = content.get(0);
        assertEquals( "categoryTimeOverall=0 categoryTimeCorrect=0 categoryTotalQuestions=0 categoryCorrectlyAnswered=0 categoryName=general categoryTimeWrong=0", generalCategory.toString() );
    }




}