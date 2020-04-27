package com.avinashdavid.trivialtrivia.scoring;

import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuestionScorerTest {

    private QuestionScorer qsNoTimeCorrect;
    private QuestionScorer qsNoTimeIncorrect;
    private QuestionScorer qsWithTimeCorrect;

    @Before
    public void setUp() {
        qsNoTimeCorrect = new QuestionScorer(7, 3, 1, 1);
        qsNoTimeIncorrect = new QuestionScorer(1, 3, 1, 2);
        qsWithTimeCorrect = new QuestionScorer( 2, 4, 100, 3, 3);
    }

    @Test
    public void getCategory() {
        assertEquals(3, qsNoTimeCorrect.getCategory());
    }

    @Test
    public void getTimeTaken() {
        assertEquals(10, qsNoTimeCorrect.getTimeTaken());
        assertEquals(100, qsWithTimeCorrect.getTimeTaken());
    }

    @Test
    public void setTimeTaken() {
        qsNoTimeCorrect.setTimeTaken(88);
        assertEquals(88, qsNoTimeCorrect.getTimeTaken());
    }

    @Test
    public void getChosenAnswer() {
        assertEquals(1, qsNoTimeCorrect.getChosenAnswer());
    }

    @Test
    public void getQuestionNumber() {
        assertEquals(2, qsWithTimeCorrect.getQuestionNumber());
    }

    @Test
    public void getQuestionEvaluation() {
        assertTrue( qsNoTimeCorrect.getQuestionEvaluation()  );
        assertFalse( qsNoTimeIncorrect.getQuestionEvaluation() );
    }


    @Test
    public void writeToParcel() {
        QuestionScorer qs = new QuestionScorer(7, 3, 1, 1);
        Parcel parcel = Parcel.obtain();
        qs.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        QuestionScorer createdFromParcel = (QuestionScorer)  QuestionScorer.CREATOR.createFromParcel(parcel);

        assertEquals(3, createdFromParcel.getCategory());
        assertEquals(7, createdFromParcel.getQuestionNumber());
    }


//    @Test
//    public void describeContents() {
//
//    }


}