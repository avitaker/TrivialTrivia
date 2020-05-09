package com.avinashdavid.trivialtrivia.scoring;

import android.os.Parcel;
import com.avinashdavid.trivialtrivia.MockParcel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
    public void writeToParcelThenCreateFromParcel() {
        //Create a mock parcel and write the contents of qsWithTimeCorrect to Parcel
        Parcel parcel = MockParcel.obtain();
        qsWithTimeCorrect.writeToParcel(parcel, qsWithTimeCorrect.describeContents());
        parcel.setDataPosition(0);

        //Create a new QuestionScorer from the parcel contents
        QuestionScorer createdFromParcel = (QuestionScorer)  QuestionScorer.CREATOR.createFromParcel(parcel);

        //Compare what wat created from the Parcel
        assertEquals(2, createdFromParcel.getQuestionNumber());
        assertEquals(4, createdFromParcel.getCategory());
        assertEquals( 100, createdFromParcel.getTimeTaken());
        assertEquals(3, createdFromParcel.getChosenAnswer());
        assertEquals( true, createdFromParcel.getQuestionEvaluation() );
    }

    @Test
    public void newArrayOfTheParcelableClass() {
        QuestionScorer[] origQS = { qsWithTimeCorrect, qsNoTimeIncorrect };
        QuestionScorer[] newArrayFromQS = (QuestionScorer[]) QuestionScorer.CREATOR.newArray(2);
        System.arraycopy(origQS, 0, newArrayFromQS,0,2);

        for ( int i = 0; i < 2; i++ ) {
            assertEquals(origQS[i].getCategory(), newArrayFromQS[i].getCategory());
            assertEquals(origQS[i].getQuestionNumber(), newArrayFromQS[i].getQuestionNumber());
        }
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
    public void setTimeTakenShouldNotBeNegative() {
        try {
            //Should throw exception from negative number
            qsNoTimeCorrect.setTimeTaken(-88);
            fail("Expected exception has not been thrown");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is("ERROR: Time must be a positive integer."));
        }
    }

    @Test
    public void getCategory() {
        assertEquals(3, qsNoTimeCorrect.getCategory());
        assertEquals(4, qsWithTimeCorrect.getCategory());
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
}