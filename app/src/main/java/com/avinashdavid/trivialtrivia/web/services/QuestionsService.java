package com.avinashdavid.trivialtrivia.web.services;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

import java.util.List;

public interface QuestionsService {

    List<IndividualQuestion> getQuestions(int numberOfQuestions);

//    List<IndividualQuestion> getFullQuestionSet();

}
