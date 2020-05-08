package com.avinashdavid.trivialtrivia.scoring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avinashdavid.trivialtrivia.Utility;
import com.avinashdavid.trivialtrivia.data.QuizDBContract;
import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

import java.util.ArrayList;

import static android.R.attr.id;
import static com.avinashdavid.trivialtrivia.questions.IndividualQuestion.categoryList;

/**
 * Created by avinashdavid on 11/2/16.
 */

public class QuizScorer {
    private static final String LOG_TAG = "QuizScorer";
    private static QuizScorer sQuizScorer;
    private Context mContext;
    private int size;
    public static int sQuizNumber = 0;
    private ArrayList<QuestionScorer> mQuestionScorers;
    private int currentQuestionCount;
    private ArrayList<int[]> mCategoryScoreReport;
    private ArrayList<int[]> mOverallTimeReport;
    private ArrayList<int[]> mCategoryTotalTimeReport;
    private ArrayList<double[]> mCategoryAverageTimeReport;

    public static final int SCORES_TOTAL_CATEGORY_QUESTIONS = 0;
    public static final int SCORES_CORRECT_CATEGORY_ANSWERS = 1;

    public static final int TIMES_CORRECT_OVERALL = 0;
    public static final int TIMES_WRONG_OVERALL = 1;
    public static final int TIMES_OVERALL_BY_CATEGORY = 2;
    public static final int TIMES_CORRECT_BY_CATEGORY = 0;
    public static final int TIMES_WRONG_BY_CATEGORY = 1;

    public static final String[] categoryQueryProjection = new String[]{
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG,
            QuizDBContract.CategoryEntry.COLUMN_NAME_NAME
    };

    public static final int COL_TOTAL_QUESTIONS_ANSWERED = 0;
    public static final int COL_CORRECTLY_ANSWERED = 1;
    public static final int COL_TOTAL_TIME_OVERALL = 2;
    public static final int COL_TOTAL_TIME_CORRECT = 3;
    public static final int COL_TOTAL_TIME_WRONG = 4;

    private QuizScorer(int size, Context context) {
        this.size = size;
        this.mQuestionScorers = new ArrayList<QuestionScorer>(size);
        mContext = context;
    }

    // static factory method to get current context's instance of QuizScorer or construct one and return it
    // (destroys previous static instance)
    public static QuizScorer getInstance(Context context, int size, int quizNumber){
        if (sQuizScorer==null || sQuizNumber != quizNumber){
            sQuizScorer = new QuizScorer(size, context);
            sQuizScorer.currentQuestionCount = 0;
            sQuizNumber = quizNumber;
        }
        if (!context.equals(sQuizScorer.mContext)){
            sQuizScorer.mContext = context;
        }
        return sQuizScorer;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.mQuestionScorers = new ArrayList<QuestionScorer>(size);
    }

    public ArrayList<QuestionScorer> getQuestionScorers() {
        return mQuestionScorers;
    }

    //create content values for insertion into quizEntry table in database
    public static ContentValues createQuizRecordContentValues(Context context, QuizScorer thisScorer) throws Exception{
        if (thisScorer.size - thisScorer.currentQuestionCount > 1){
            throw new Exception("createQuizRecordContentValues() called before quiz completion (" + thisScorer.currentQuestionCount + ", " + thisScorer.size + ")");
        }
        ContentValues quizValues = null;
        try {
            int quizSize = thisScorer.size;
            int score = thisScorer.scoreQuiz(thisScorer.mQuestionScorers);
            double timeAverageOverall = thisScorer.getTimeAverageAllQuestions();
            double timeAverageCorrect = thisScorer.getTimeAverageCorrect();
            double timeAverageWrong = thisScorer.getTimeAverageWrong();
            quizValues = new ContentValues();
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE, quizSize);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE, score);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL, timeAverageOverall);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT, timeAverageCorrect);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG, timeAverageWrong);
        } catch (Exception e){
            Log.d(LOG_TAG, "Error at addquizrecord(): " + e.getMessage());
        }
        return quizValues;
    }

    //create an arraylist of contentvalues that correspond to the question categories, for updating category records
    public static ArrayList<ContentValues> createAllCategoryRecordContentValues(Context context, QuizScorer thisScorer) throws Exception{
        if (thisScorer.size - thisScorer.currentQuestionCount > 1){
            throw new Exception("createCategoryRecordContentValues() called before quiz completion");
        }
        ArrayList<ContentValues> returnList = new ArrayList<>(categoryList.size());

        if (thisScorer.mCategoryTotalTimeReport==null){
            thisScorer.mCategoryTotalTimeReport = thisScorer.getCategoryTotalTimeReport();
        }
        if (thisScorer.mCategoryScoreReport == null){
            thisScorer.mCategoryScoreReport = thisScorer.getCategoryScoreReport();
        }
        Cursor c;
        c = context.getContentResolver().query(QuizDBContract.CategoryEntry.CONTENT_URI, categoryQueryProjection, null, null, QuizDBContract.CategoryEntry._ID +" ASC");
        try {
            if (c.moveToFirst()) {
                do {
                    int currentPosition = c.getPosition();
                    int totalAnswered = c.getInt(COL_TOTAL_QUESTIONS_ANSWERED);
                    int correctlyAnswered = c.getInt(COL_CORRECTLY_ANSWERED);
                    int totalTimeOverall = c.getInt(COL_TOTAL_TIME_OVERALL);
                    int totalTimeCorrect = c.getInt(COL_TOTAL_TIME_CORRECT);
                    int totalTimeWrong = c.getInt(COL_TOTAL_TIME_WRONG);
                    int newCorrectAnswer = correctlyAnswered + thisScorer.mCategoryScoreReport.get(SCORES_CORRECT_CATEGORY_ANSWERS)[currentPosition];
                    int newTotalAnswer = totalAnswered + thisScorer.mCategoryScoreReport.get(SCORES_TOTAL_CATEGORY_QUESTIONS)[currentPosition];
                    int newTotalTime = totalTimeOverall + thisScorer.mCategoryTotalTimeReport.get(TIMES_OVERALL_BY_CATEGORY)[currentPosition];
                    int newCorrectTime = totalTimeCorrect + thisScorer.mCategoryTotalTimeReport.get(TIMES_CORRECT_BY_CATEGORY)[currentPosition];
                    int newWrongTime = totalTimeWrong + thisScorer.mCategoryTotalTimeReport.get(TIMES_WRONG_BY_CATEGORY)[currentPosition];
                    ContentValues cv = new ContentValues();
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED, newTotalAnswer);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED, newCorrectAnswer);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL, newTotalTime);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT, newCorrectTime);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG, newWrongTime);
                    returnList.add(currentPosition, cv);
                    c.moveToNext();
                }
                while (c.getPosition()< categoryList.size());
            }
        } finally {
            c.close();
        }
        return returnList;
    }

    @Nullable
    public static Uri createAndInsertQuizRecord(Context context, QuizScorer thisScorer){
        ContentValues contentValues = null;
        Uri retUri = null;
        try {
            contentValues = createQuizRecordContentValues(context, thisScorer);
            retUri  = context.getContentResolver().insert(QuizDBContract.QuizEntry.CONTENT_URI, contentValues);
        } catch (Exception e) {
            Log.d(LOG_TAG, "error while inserting quiz record: " + e);
        }
        return retUri;
    }

    public static int createAndUpdateCategoryRecords(Context context, QuizScorer thisScorer){
        int retInt = 0;
        try {
            ArrayList<ContentValues> contentValuesArrayList = createAllCategoryRecordContentValues(context, thisScorer);
            int i= 0;
            while (i < contentValuesArrayList.size()){
                try {
                    retInt += context.getContentResolver().update(QuizDBContract.CategoryEntry.buildUriCategoryIndex(i), contentValuesArrayList.get(i), null, null);
                } finally {
                    i+=1;
                }
            }
        } catch (Exception e){
            Log.d(LOG_TAG, "error while inserting category records: " + e);
        }
        return retInt;
    }

    //add a QuestionScorer to the member list of QuestionScorers with internal call of QuestionScorer constructor (no time taken)
    public void addQuestionScorer(int questionNumber, int category, int correctAnswer, int chosenAnswer){
        QuestionScorer questionScorer = new QuestionScorer(questionNumber, category, correctAnswer,chosenAnswer);
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    //add a QuestionScorer to the member list of QuestionScorers with internal call of QuestionScorer constructor (with time taken)
    public void addQuestionScorer(int questionNumber, int category, int timeTaken, int correctAnswer, int chosenAnswer){
        QuestionScorer questionScorer = new QuestionScorer(questionNumber, category, timeTaken, correctAnswer,chosenAnswer);
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    //add a pre-constructed QuestionScorer to the member list of QuestionScorers
    public void addQuestionScorer(QuestionScorer questionScorer){
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    //calculate and return the overall int score of the current quiz
    public int scoreQuiz(ArrayList<QuestionScorer> quizQuestions) throws Exception{
        int finalScore = 0;
        if (quizQuestions == null){
            throw new Exception("scorequiz() called with null arraylist");
        }
        for (int i = 0; i < quizQuestions.size(); i++){
            if (quizQuestions.get(i).getQuestionEvaluation()){
                finalScore ++;
            }
        }
        return finalScore;
    }

    // returns a category-wise breakdown of the current quiz,
    // with the first int[] providing the total number of questions from each category in the quiz,
    //  and the second int[] providing the correctly answered question from each category in the quiz.
    //  Refer to the respective indices using IndividualQuestions fields starting with 'CATEGORY_'
    public ArrayList<int[]> getCategoryScoreReport() throws NullPointerException{
        mCategoryScoreReport = new ArrayList<>(2);
        ArrayList<String> categoryList = IndividualQuestion.categoryList;
        int numberOfCategories = categoryList.size();
        int[] totalCategoryQuestions = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] correctCategoryAnswers = Utility.returnArrayOfZeroInts(numberOfCategories);
        Log.d("getCategoryScoreReport", "checkpoint 3");
        for (int i = 0; i < mQuestionScorers.size(); i++){
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);
            for (int c = 0; c < numberOfCategories; c++){
                if (thisQuestionScorer.getCategory()==c){
                    totalCategoryQuestions[c] = totalCategoryQuestions[c]+1;
                    Log.d("getCategoryScoreReport", Integer.toString(totalCategoryQuestions[c]) + " questions in category " + Integer.toString(c));
                    if (thisQuestionScorer.getQuestionEvaluation()){
                        correctCategoryAnswers[c] = correctCategoryAnswers[c]+1;
                    }
                }
            }
        }
        mCategoryScoreReport.add(SCORES_TOTAL_CATEGORY_QUESTIONS, totalCategoryQuestions);
        mCategoryScoreReport.add(SCORES_CORRECT_CATEGORY_ANSWERS, correctCategoryAnswers);
        Log.d("getCategoryScoreReport", "correctCategoryAnswers length is " + correctCategoryAnswers.length);
        return mCategoryScoreReport;
    }


    public ArrayList<int[]> getOverallTimeReport(){
        mOverallTimeReport = new ArrayList<>(2);
        int[] correctOverallTimes = new int[mQuestionScorers.size()];
        int[] wrongOverallTimes = new int[mQuestionScorers.size()];
        for (int i = 0; i < mQuestionScorers.size(); i++){
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);
            if (thisQuestionScorer.getQuestionEvaluation()){
                correctOverallTimes[i] = thisQuestionScorer.getTimeTaken();
                wrongOverallTimes[i] = -1;
            } else {
                wrongOverallTimes[i] = thisQuestionScorer.getTimeTaken();
                correctOverallTimes[i]=-1;
            }
        }
        mOverallTimeReport.add(TIMES_CORRECT_OVERALL,correctOverallTimes);
        mOverallTimeReport.add(TIMES_WRONG_OVERALL, wrongOverallTimes);
        return mOverallTimeReport;
    }

    public double getTimeAverageCorrect(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] correctAnswerTimes = mOverallTimeReport.get(TIMES_CORRECT_OVERALL);
        return Utility.getAverageFromIntListWITHNegativeValueElimination(correctAnswerTimes);
    }

    public double getTimeAverageWrong(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] wrongAnswerTimes = mOverallTimeReport.get(TIMES_WRONG_OVERALL);
        return Utility.getAverageFromIntListWITHNegativeValueElimination(wrongAnswerTimes);
    }

    public double getTimeAverageAllQuestions(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] wrongAnswerTimes = mOverallTimeReport.get(TIMES_WRONG_OVERALL);
        int[] correctAnswerTimes = getOverallTimeReport().get(TIMES_CORRECT_OVERALL);
        return Utility.getAverageFromCOMPLEMENTARYIntListsWITHNegativeValueElimination(wrongAnswerTimes,correctAnswerTimes);
    }

    public ArrayList<int[]> getCategoryTotalTimeReport(){
        ArrayList<int[]> returnList = new ArrayList<>(2);
        int numberOfCategories = categoryList.size();
        int[] overallCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] correctCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] wrongCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        for (int i = 0; i < mQuestionScorers.size(); i++){
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);
            for (int c = 0; c < numberOfCategories; c++){
                if (thisQuestionScorer.getCategory()==c){
                    overallCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
                    if (thisQuestionScorer.getQuestionEvaluation()){
                        correctCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
                    } else {
                        wrongCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
                    }
                }
            }
        }
        returnList.add(TIMES_CORRECT_BY_CATEGORY,correctCategoryTimes);
        returnList.add(TIMES_WRONG_BY_CATEGORY, wrongCategoryTimes);
        returnList.add(TIMES_OVERALL_BY_CATEGORY,overallCategoryTimes);
        mCategoryTotalTimeReport = returnList;
        return returnList;
    }
}