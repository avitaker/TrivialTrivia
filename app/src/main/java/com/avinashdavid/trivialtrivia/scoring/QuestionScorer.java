package com.avinashdavid.trivialtrivia.scoring;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by avinashdavid on 11/2/16.
 */

public class QuestionScorer implements Parcelable {
    private int mQuestionNumber;
    private int mCategory;
    private int timeTaken;
    private int mCorrectAnswer;
    private int mChosenAnswer;
    private boolean mCorrect;

    public static final int NO_ANSWER = -1;

    public QuestionScorer(int questionNumber, int category, int correctAnswer, int chosenAnswer){
        this.mQuestionNumber = questionNumber;
        this.mCategory = category;
        this.mCorrectAnswer = correctAnswer;
        this.mChosenAnswer = chosenAnswer;
        this.mCorrect = (correctAnswer==chosenAnswer);
        this.timeTaken = 10;
    }

    public QuestionScorer(int questionNumber, int category, int timeTaken, int correctAnswer, int chosenAnswer){
        this.mQuestionNumber = questionNumber;
        this.mCategory = category;
        this.mCorrectAnswer = correctAnswer;
        this.mChosenAnswer = chosenAnswer;
        this.timeTaken = timeTaken;
        this.mCorrect = (correctAnswer==chosenAnswer);
    }

    public QuestionScorer(Parcel in){
        this.mQuestionNumber = in.readInt();
        this.mCategory = in.readInt();
        this.timeTaken = in.readInt();
        this.mCorrectAnswer = in.readInt();
        this.mChosenAnswer = in.readInt();
        byte trueOrNot = in.readByte();
        this.mCorrect = (trueOrNot == 0);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mQuestionNumber);
        parcel.writeInt(mCategory);
        parcel.writeInt(timeTaken);
        parcel.writeInt(mCorrectAnswer);
        parcel.writeInt(mChosenAnswer);
        byte correct = (byte) (( mCorrect ) ? 0 : 1);
        parcel.writeByte( correct );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator(){
        @Override
        public QuestionScorer createFromParcel(Parcel parcel) {
            return new QuestionScorer(parcel);
        }

        // There to deserialize an array of Parcelable QuestionScorers
        @Override
        public Object[] newArray(int i) {
            return new QuestionScorer[i];
        }
    };

    public int getChosenAnswer() {
        return mChosenAnswer;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }

    public boolean getQuestionEvaluation(){
        return mCorrect;
    }

    public int getCategory(){
        return mCategory;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        if( timeTaken >= 0 ){
            this.timeTaken = timeTaken;
        }
        else
            throw new IllegalStateException("ERROR: Time must be a positive integer.");
    }
}
