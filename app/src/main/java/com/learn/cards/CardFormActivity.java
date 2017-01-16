package com.learn.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CardFormActivity extends AppCompatActivity {

    public final static String MESSAGE_QUESTION = "com.learn.cards.QUESTION";
    public final static String MESSAGE_ANSWER = "com.learn.cards.ANSWER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCardForm();
            }
        });
    }

    private void submitCardForm() {
        Intent intent = new Intent(this, MainActivity.class);

        EditText questionField = (EditText) findViewById(R.id.questionField);
        String question = questionField.getText().toString();
        intent.putExtra(MESSAGE_QUESTION, question);

        EditText answerField = (EditText) findViewById(R.id.answerField);
        String answer = answerField.getText().toString();
        intent.putExtra(MESSAGE_ANSWER, answer);


        startActivity(intent);
    }

}
