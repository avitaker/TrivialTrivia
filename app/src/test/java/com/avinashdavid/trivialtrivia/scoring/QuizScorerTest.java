package com.avinashdavid.trivialtrivia.scoring;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.avinashdavid.trivialtrivia.data.QuizDBContract;
import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

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

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class QuizScorerTest {

    /**
     *  Testing a Static Factory, so in order to prevent
     *  caching need to pass in a new quiz number between tests
     */
    private static final double DELTA = 1e-15;
    private static QuizScorer basic;
    private static int basicQuizSize = 10;
    private static int halfCorrectSize = 4;
    private QuestionScorer firstQuestion;
    private QuestionScorer secondQuestion;
    private QuestionScorer thirdQuestion;
    private QuestionScorer fourthQuestion;
    private ArrayList<QuestionScorer> halfCorrectQuestions;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context mockContext;

    @Mock
    Context mockContext2;

    @Before
    public void setUp() throws Exception {
        //Basic Quiz
        basicQuizSize = 10;

        // Four Question Quiz Half Right
        firstQuestion = new QuestionScorer(1, 3, 1, 1);
        secondQuestion = new QuestionScorer(2, 1, 1, 2);
        thirdQuestion = new QuestionScorer(3, 0, 3, 3);
        fourthQuestion = new QuestionScorer(4, 0, 2, 0);
        halfCorrectQuestions = new ArrayList<>();
        halfCorrectQuestions.add( firstQuestion );
        halfCorrectQuestions.add( secondQuestion );
        halfCorrectQuestions.add( thirdQuestion );
        halfCorrectQuestions.add( fourthQuestion );
    }

    @Test
    public void setSize() {
        //Should set the size of the quiz (the number of questions)
        basic = QuizScorer.getInstance(mockContext, basicQuizSize, 0);
        assertEquals(basicQuizSize, basic.getSize() );
        basic.setSize(100);

        //After Should not Be the Same as Before
        assertNotEquals(basicQuizSize, basic.getSize() );
        assertEquals(100, basic.getSize() );
    }

    @Test
    public void setSizeShouldNotBeNegative() {
        //Should not set a negative number of questions for a quiz
        basic = QuizScorer.getInstance(mockContext, basicQuizSize, 1);
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
        basic = QuizScorer.getInstance(mockContext, basicQuizSize, 2);
        try {
            ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );
            fail("Expected exception has not been thrown. Can not check the score of a quiz before complete.");
        }
        catch (Exception e) {
            assertThat(e.getMessage(), is("createQuizRecordContentValues() called before quiz completion (0, 10)"));
        }
    }

    @Test
    public void addQuestionsToEmptyQuiz() throws Exception {
        //Set up: Two question quiz with 1 wrong answer selected
        basic = QuizScorer.getInstance(mockContext, 2, 3);
        basic.addQuestionScorer(1, 2, 1, 2);
        basic.addQuestionScorer(1, 2, 3, 3);
        ArrayList<QuestionScorer> qs = basic.getQuestionScorers();
        ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );

        assertEquals("Quiz size:", 2, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE));
        assertEquals("Quiz score:", 1, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE));
    }


    @Test
    public void quizScoreShouldBeZeroWhenAllWrong() throws Exception {
        //Set up: One question quiz with wrong answer selected
        basic = QuizScorer.getInstance(mockContext, 1, 4);
        basic.addQuestionScorer(1, 0, 1, 2);
        ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );

        assertEquals("Quiz size:", 1, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE));
        assertEquals("Quiz score:", 0, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE));
    }

    @Test
    public void quizScoreMatchesContentValuesPassedToDB() throws Exception {
        //Set up: Add half correct questions to the QuizScorer
        QuizScorer halfCorrect = QuizScorer.getInstance(mockContext, halfCorrectSize, 5);
        halfCorrect.addQuestionScorer(firstQuestion);
        halfCorrect.addQuestionScorer(secondQuestion);
        halfCorrect.addQuestionScorer(thirdQuestion);
        halfCorrect.addQuestionScorer(fourthQuestion);
        ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, halfCorrect);
        int scoreForDB = (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE);
        int score = halfCorrect.scoreQuiz( halfCorrectQuestions );

        //Check that the content values for insertion into quizEntry table in database match the current score
        assertEquals( "The number of correct answers:  ", 2, score );
        assertEquals("Quiz score inserted into quizEntry table in database: ", score,  scoreForDB );
    }


    @Test
    public void averageTimeOnQuestions() throws Exception {
        //Set up: Two question quiz with wrong answer selected and time taken to complete
        basic = QuizScorer.getInstance(mockContext, 2, 6);
        basic.addQuestionScorer(1, 2,  301,1, 2);
        basic.addQuestionScorer(1, 2, 122, 3, 3);
        ArrayList<QuestionScorer> qs = basic.getQuestionScorers();
        double total = 0;
        double avg = 0;
        for( int i = 0; i < qs.size(); i++){
            total += qs.get(i).getTimeTaken();
        }
        avg = (double) total / qs.size();
        ContentValues cv = QuizScorer.createQuizRecordContentValues( mockContext, basic );

        // Average times should be compared using delta - the maximum delta between expected and actual for which both numbers are still considered equal.
        assertEquals( "Avg time on all questions: ", 211.5, (double)cv.getAsDouble(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL) , DELTA);
        assertEquals( "Avg time from get all question scorers: ", 211.5, avg, DELTA);
        assertEquals("Quiz size:", 2, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE));
        assertEquals("Quiz score:", 1, (int)cv.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE));
    }

    @Test
    public void quizScoreShouldNotTakeNullQuestionList()  {
        //Set up: Get instance of static quiz scorer
        basic = QuizScorer.getInstance(mockContext, basicQuizSize, 7);
        ArrayList<QuestionScorer> testNull = null;
        try {
            int score = basic.scoreQuiz( testNull );
            fail("Expected exception has not been thrown. Can not check null question list.");
        }
        catch (Exception e) {
            assertThat(e.getMessage(), is("scorequiz() called with null arraylist"));
        }
    }

    @Test
    public void getCategoryScoreReport() {
        //Setup:  Initially add 3 questions
        basic = QuizScorer.getInstance(mockContext, 3, 8);
        basic.addQuestionScorer(1, 2, 1, 1);
        basic.addQuestionScorer(2, 2, 3, 3);
        basic.addQuestionScorer(3, 0, 2, 0);

        int totalFromWorld = basic.getCategoryScoreReport().get(QuizScorer.SCORES_TOTAL_CATEGORY_QUESTIONS)[IndividualQuestion.CATEGORY_WORLD];
        int totalFromGeneral = basic.getCategoryScoreReport().get(QuizScorer.SCORES_TOTAL_CATEGORY_QUESTIONS)[IndividualQuestion.CATEGORY_GENERAL];
        int totalFromScience = basic.getCategoryScoreReport().get(QuizScorer.SCORES_TOTAL_CATEGORY_QUESTIONS)[IndividualQuestion.CATEGORY_SCIENCE];

        assertEquals("Two questions from 'world' category",2, totalFromWorld);
        assertEquals("One question from 'general' category",1, totalFromGeneral);
        assertEquals("Zero questions from 'science' category",0, totalFromScience);
    }


    @Test
    public void canNotGetCategoryScoreReportWhenNoQuestionsExist() {
        //Setup: No questions scored or added to quiz
        basic = QuizScorer.getInstance(mockContext, 3, 9);
        try {
            ArrayList<int[]> result = basic.getCategoryScoreReport();
            fail("Expected exception has not been thrown. Can not get score by categories with no questions exist.");
        }
        catch (NullPointerException e) {
            assertThat(e.getMessage(), is("getCategoryScoreReport() called with null mQuestionScorers"));
        }
    }

    @Test
    public void getCategoryTotalTimeReport() {
        //Setup:  Initially add 3 questions
        basic = QuizScorer.getInstance(mockContext, 5, 10);
        basic.addQuestionScorer(1, 2, 20, 1, 1);
        basic.addQuestionScorer(2, 2, 11, 3, 3);
        basic.addQuestionScorer(3, 0, 19, 2, 0);
        basic.addQuestionScorer(4, 3, 1, 2, 2);
        basic.addQuestionScorer(5, 3, 1, 2, 3);

        int timeWorld = basic.getCategoryTotalTimeReport().get(QuizScorer.TIMES_OVERALL_BY_CATEGORY)[IndividualQuestion.CATEGORY_WORLD];
        int timeGeneral = basic.getCategoryTotalTimeReport().get(QuizScorer.TIMES_OVERALL_BY_CATEGORY)[IndividualQuestion.CATEGORY_GENERAL];
        int timeHistory = basic.getCategoryTotalTimeReport().get(QuizScorer.TIMES_OVERALL_BY_CATEGORY)[IndividualQuestion.CATEGORY_HISTORY];
        int timeScience = basic.getCategoryTotalTimeReport().get(QuizScorer.TIMES_OVERALL_BY_CATEGORY)[IndividualQuestion.CATEGORY_SCIENCE];

        assertEquals("Seconds solving 'world' questions: ", 31, timeWorld);
        assertEquals("Seconds solving 'general' questions: ",19, timeGeneral);
        assertEquals("Seconds solving 'history' questions: ",2, timeHistory);
        assertEquals("Seconds solving 'science' questions: ",0, timeScience);
    }

    @Test
    public void getAverageTimeForCorrectAnswers() {
        //Setup:  Initially add 4 questions, 2 are correct
        basic = QuizScorer.getInstance(mockContext, 4, 11);
        basic.addQuestionScorer(1, 2, 20, 1, 1);
        basic.addQuestionScorer(2, 2, 10, 3, 3);
        basic.addQuestionScorer(3, 0, 3, 2, 0);
        basic.addQuestionScorer(4, 3, 2, 1, 2);
        QuizScorer.createAndInsertQuizRecord(mockContext, basic);

        double timeCorrect = basic.getTimeAverageCorrect();
        assertEquals("Avg sec for correct answers:",15.0, timeCorrect, DELTA);
    }

    @Test
    public void getAverageTimeForWrongAnswers() {
        //Setup:  Initially add 4 questions, 2 are correct
        basic = QuizScorer.getInstance(mockContext, 4, 12);
        basic.addQuestionScorer(1, 2, 20, 1, 1);
        basic.addQuestionScorer(2, 2, 10, 3, 3);
        basic.addQuestionScorer(3, 0, 3, 2, 0);
        basic.addQuestionScorer(4, 3, 2, 1, 2);
        QuizScorer.createAndInsertQuizRecord(mockContext, basic);

        double timeWrong = basic.getTimeAverageWrong();
        assertEquals("Avg sec for wrong answers:",2.5, timeWrong, DELTA);
    }

    @Test
    public void createAllCategoryRecordContentValues() throws Exception {
        //Set up: Add half correct questions to the QuizScorer
        QuizScorer halfCorrect = QuizScorer.getInstance(mockContext, halfCorrectSize, 13);
        halfCorrect.addQuestionScorer(firstQuestion);
        halfCorrect.addQuestionScorer(secondQuestion);
        halfCorrect.addQuestionScorer(thirdQuestion);
        halfCorrect.addQuestionScorer(fourthQuestion);

        //This method calls a cursor object so it should be 0 after updating
        QuizScorer.createAndInsertQuizRecord(mockContext2, halfCorrect);
        int categoriesUpdated = QuizScorer.createAndUpdateCategoryRecords(mockContext2, halfCorrect);
        assertEquals( 0, 0);
 }


}