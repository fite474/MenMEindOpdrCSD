package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;

public class StartingActivity extends AppCompatActivity
{
    private ImageView backgroundImageView;
    private ImageView logoImageView;
    private TextView fetchTrainTextView;

    private static int TIME_OUT = 2420;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        backgroundImageView = findViewById(R.id.start_backgrImageView);
        logoImageView = findViewById(R.id.start_logoImageView);
        fetchTrainTextView = findViewById(R.id.start_fetchTrainTextView);

        backgroundImageView.setImageResource(R.drawable.rasta);
        logoImageView.setImageResource(R.drawable.nug);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent (StartingActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

    }
}
