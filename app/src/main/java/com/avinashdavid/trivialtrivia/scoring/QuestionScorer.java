package com.avinashdavid.trivialtrivia.scoring;

import android.os.Parcel;
import android.os.Parcelable;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

/**
 * Created by avinashdavid on 11/2/16.
 */

public class QuestionScorer implements Parcelable {

    private IndividualQuestion question;
    private int timeTaken;
    private int mChosenAnswer;
    private boolean mCorrect;

    public static final int NO_ANSWER = -1;

    public QuestionScorer(IndividualQuestion question, int chosenAnswer){
        this.question = question;
        this.mChosenAnswer = chosenAnswer;
        this.mCorrect = (question.correctAnswer==chosenAnswer);
        this.timeTaken = 10;
    }

    public QuestionScorer(IndividualQuestion question, int timeTaken, int chosenAnswer){
        this.question = question;
        this.mChosenAnswer = chosenAnswer;
        this.timeTaken = timeTaken;
        this.mCorrect = (this.question.correctAnswer==chosenAnswer);
    }

    public QuestionScorer(Parcel in){
        this.question = in.readParcelable(IndividualQuestion.class.getClassLoader());
        this.timeTaken = in.readInt();
        this.mChosenAnswer = in.readInt();
        boolean[] correct = new boolean[1];
        in.readBooleanArray(correct);
        this.mCorrect = correct[0];
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(question, 1);
        parcel.writeInt(timeTaken);
        parcel.writeInt(mChosenAnswer);
        boolean[] correct = new boolean[1];
        correct[0] = mCorrect;
        parcel.writeBooleanArray(correct);
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

        @Override
        public Object[] newArray(int i) {
            return new QuestionScorer[i];
        }
    };

    public int getChosenAnswer() {
        return mChosenAnswer;
    }

    public boolean getQuestionEvaluation(){
        return mCorrect;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public IndividualQuestion getQuestion() {
        return question;
    }
}
