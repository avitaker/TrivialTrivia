package com.avinashdavid.trivialtrivia.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.avinashdavid.trivialtrivia.R;

import java.io.IOException;

public class ActivityWelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        setupWindowAnimations();

        ImageView imageView = (ImageView)findViewById(R.id.imageview_welcome_page);
        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

        ((ImageView)findViewById(R.id.imageview_start)).setImageDrawable(getRoundedDrawable("start.jpg"));
        ((ImageView)findViewById(R.id.imageview_statistics)).setImageDrawable(getRoundedDrawable("statistics.jpg"));
        ((ImageView)findViewById(R.id.imageview_instructions)).setImageDrawable(getRoundedDrawable("instructions.png"));
        ((ImageView)findViewById(R.id.imageview_acknowledgments)).setImageDrawable(getRoundedDrawable("acknowledgements.jpg"));

        CardView startCardview = (CardView)findViewById(R.id.card_view_start_game);
        startCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityQuiz.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        CardView statsCardView = (CardView)findViewById(R.id.card_view_statistics);
        statsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityStatistics.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.card_view_instructions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityInstructions.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.card_view_acknowledgments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityAcknowledgments.class);
                startActivity(intent);
            }
        });
    }

    @TargetApi(21)
    private void setupWindowAnimations(){
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setExitTransition(fade);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    private RoundedBitmapDrawable getRoundedDrawable(String filename){
        try {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), getAssets().open(filename));
            dr.setCornerRadius(500);
            return dr;
        }
        catch (IOException e){
//            Log.d("imageHandling", e.toString());
        }
        return null;
    }

}
