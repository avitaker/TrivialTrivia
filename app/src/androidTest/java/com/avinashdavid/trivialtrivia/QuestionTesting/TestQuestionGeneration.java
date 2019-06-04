package com.avinashdavid.trivialtrivia.QuestionTesting;

import android.test.AndroidTestCase;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;
import com.avinashdavid.trivialtrivia.questions.QuestionsHandling;
import com.avinashdavid.trivialtrivia.web.services.LocalQuestionService;
import com.avinashdavid.trivialtrivia.web.services.QuestionsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinashdavid on 11/1/16.
 */

public class TestQuestionGeneration extends AndroidTestCase {
    static final int SET_SIZE = 10;

    public void testGeneration(){
        LocalQuestionService questionsService = new LocalQuestionService(getContext());
        List<IndividualQuestion> allQuestions = questionsService.getFullQuestionSet();
        IndividualQuestion firstQuestion = allQuestions.get(0);
        IndividualQuestion secondQuestion = allQuestions.get(1);
        assertEquals("Answer index of first question is 0", 0, firstQuestion.correctAnswer);
        assertEquals("Question of second question is correct","Entomology is the science that studies",secondQuestion.question);
        assertEquals("Size of the question set is 96",96,allQuestions.size());
        assertEquals("Last choice of first question is 'None of the above'","None of the above",firstQuestion.choicesList[3]);
    }
}
