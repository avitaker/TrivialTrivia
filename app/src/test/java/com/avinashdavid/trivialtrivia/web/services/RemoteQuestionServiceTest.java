package com.avinashdavid.trivialtrivia.web.services;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RemoteQuestionServiceTest {

    @Test
    public void test() {
        RemoteQuestionService remoteQuestionService = new RemoteQuestionService();
        List<IndividualQuestion> questions = remoteQuestionService.getQuestions(10);
        System.out.println(questions);
    }
}