package com.avinashdavid.trivialtrivia.questions;

import android.os.Parcel;
import com.avinashdavid.trivialtrivia.MockParcel;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

public class IndividualQuestionTest {

    private IndividualQuestion basicIQ;
    private IndividualQuestion secondIQ;
    private String[] basicAnswers = { "Small", "Medium", "Large", "Really Large"};

    @Before
    public void setUp() {
        basicIQ = new IndividualQuestion(2, "science", "How big is a photon?", basicAnswers, 0);
        secondIQ = new IndividualQuestion(10, "general", "What is the size of a plane?", basicAnswers, 3);
    }

    @Test
    public void getCategoryList() {
        ArrayList<String> expectedList = new ArrayList<>(Arrays.asList("general", "science", "world", "history", "entertainment", "sports"));
        ArrayList<String> actualCategories = IndividualQuestion.getCategoryList();

        for ( int i = 0; i < expectedList.size(); i++ ){
            assertEquals( actualCategories.get(i), expectedList.get(i));
        }
    }

    @Test
    public void writeToParcelThenCreateFromParcel() {
        //Create a mock parcel and write the contents of qsWithTimeCorrect to Parcel
        Parcel parcel = MockParcel.obtain();
        basicIQ.writeToParcel( parcel, basicIQ.describeContents() );
        parcel.setDataPosition(0);

        //Create a new IndividualQuestion from the parcel
        IndividualQuestion createdFromParcel = (IndividualQuestion) IndividualQuestion.CREATOR.createFromParcel(parcel);

        //Compare what wat created from the Parcel
        assertEquals( "Question = [ questionNumber: 2, question: How big is a photon?, category: 1, correct: 0 ]", createdFromParcel.toString());
    }

    @Test
    public void newArrayOfTheParcelableClass() {
        IndividualQuestion[] origIQ = { basicIQ, secondIQ };
        IndividualQuestion[] newArrayFromIQ = (IndividualQuestion[]) IndividualQuestion.CREATOR.newArray(2);
        System.arraycopy(basicIQ, 0, secondIQ,0,2);

        for ( int i = 0; i < 2; i++ ) {
            assertEquals(origIQ[i].toString(), newArrayFromIQ[i].toString());
        }
    }


}