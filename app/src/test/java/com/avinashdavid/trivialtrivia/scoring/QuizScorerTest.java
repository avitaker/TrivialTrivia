package com.avinashdavid.trivialtrivia.scoring;

import android.content.ContentValues;
import android.content.Context;
import com.avinashdavid.trivialtrivia.data.QuizDBContract;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class QuizScorerTest {

    /**
     *  Testing a Static Factory, so in order to prevent
     *  caching need to pass in a new quiz number between tests
     */
    private static QuizScorer basic;
    private static int quizSize;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws Exception {
        quizSize = 10;
    }

    @Test
    public void setSize() {
        //Should set the size of the quiz, which is the number of questions
        basic = QuizScorer.getInstance(mockContext, quizSize, 0);
        assertEquals(quizSize, basic.getSize() );
        basic.setSize(100);

        //After Should not Be the Same as Before
        assertNotEquals(quizSize, basic.getSize() );
        assertEquals(100, basic.getSize() );
    }

    @Test
    public void setSizeShouldNotBeNegative() {
        //Should not set a negative number of questions for a quiz
        basic = QuizScorer.getInstance(mockContext, quizSize, 1);
        try {
            basic.setSize(-1);
            fail("Expected exception has not been thrown");
        }
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Illegal Capacity: -1"));
        }
    }

    @Test
    public void canNotScoreQuizBeforeItIsComplete() throws Exception {
        //Can not check the score of a quiz before quiz completion
        basic = QuizScorer.getInstance(mockContext, quizSize, 2);
        try {
            ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );
            fail("Expected exception has not been thrown. Can not check the score of a quiz before complete.");
        }
        catch (Exception e) {
            assertThat(e.getMessage(), is("createQuizRecordContentValues() called before quiz completion"));
        }
    }

    @Test
    public void quizScoreShouldBe0WhenAllWrongAnswers() throws Exception {
        //Set up: One question quiz with wrong answer selected
        basic = QuizScorer.getInstance(mockContext, 1, 3);
        basic.addQuestionScorer(1, 0, 1, 2);
        ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );

        assertEquals("Quiz size:", 1, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE));
        assertEquals("Quiz score:", 0, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE));
    }

}