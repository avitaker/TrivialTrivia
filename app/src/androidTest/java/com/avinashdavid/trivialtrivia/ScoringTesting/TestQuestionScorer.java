package com.avinashdavid.trivialtrivia.ScoringTesting;

import android.test.AndroidTestCase;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;
import com.avinashdavid.trivialtrivia.scoring.QuestionScorer;
import com.avinashdavid.trivialtrivia.web.services.LocalQuestionService;

import java.util.List;

/**
 * Created by avinashdavid on 11/2/16.
 */

public class TestQuestionScorer extends AndroidTestCase {
    public void testQuestionScorer(){
        LocalQuestionService questionsService = new LocalQuestionService(getContext());
        List<IndividualQuestion> allQuestions = questionsService.getFullQuestionSet();
        IndividualQuestion fifthQuestion = allQuestions.get(4);
        QuestionScorer questionScorer = new QuestionScorer(fifthQuestion, -1, 2);
        assertEquals(false,questionScorer.getQuestionEvaluation());
    }
}
