package com.avinashdavid.trivialtrivia.QuestionTesting;

import android.test.AndroidTestCase;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;
import com.avinashdavid.trivialtrivia.questions.QuestionsHandling;
import com.avinashdavid.trivialtrivia.web.services.LocalQuestionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinashdavid on 11/2/16.
 */

public class TestDisplayQuestion extends AndroidTestCase {
    public void testDisplayQuestion() {
        LocalQuestionService questionsService = new LocalQuestionService(getContext());
        List<IndividualQuestion> allQuestions = questionsService.getFullQuestionSet();
        ArrayList<String> displayQuestionObject = QuestionsHandling.makeDisplayQuestionObject(allQuestions.get(0));
        assertEquals("Third choice of first question is 'longest railway station'","longest railway station",displayQuestionObject.get(QuestionsHandling.INDEX_CHOICE_3));
        displayQuestionObject = QuestionsHandling.makeDisplayQuestionObject(allQuestions.get(18));
        assertEquals("Category of 19th question is 'entertainment'","entertainment",displayQuestionObject.get(QuestionsHandling.INDEX_CATEGORY));
        assertEquals("Name of the IndividualCategory.CATEGORY_ENTERTAINMENTth element in the IndividualQuestion.categoryList is 'entertainment'","entertainment",IndividualQuestion.categoryList.get(IndividualQuestion.CATEGORY_ENTERTAINMENT));
    }
}
