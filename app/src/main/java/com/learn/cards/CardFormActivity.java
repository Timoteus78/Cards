package com.learn.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CardFormActivity extends AppCompatActivity {

    public final static String MESSAGE_QUESTION = "com.learn.cards.QUESTION";
    public final static String MESSAGE_ANSWER = "com.learn.cards.ANSWER";
    private static final String TAG_UPDATE = "com.learn.cards.UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String question = intent.getStringExtra(CardFragment.MESSAGE_QUESTION_EDIT);
        String answer = intent.getStringExtra(CardFragment.MESSAGE_ANSWER_EDIT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(question != null && answer!= null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update();
                }
            });
            EditText questionField = (EditText) findViewById(R.id.questionField);
            questionField.setText(question);
            EditText answerField = (EditText) findViewById(R.id.answerField);
            answerField.setText(answer);
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit();
                }
            });
        }
;
    }

    private void submit() {
        Intent intent = new Intent(this, MainActivity.class);

        EditText questionField = (EditText) findViewById(R.id.questionField);
        String question = questionField.getText().toString();
        intent.putExtra(MESSAGE_QUESTION, question);

        EditText answerField = (EditText) findViewById(R.id.answerField);
        String answer = answerField.getText().toString();
        intent.putExtra(MESSAGE_ANSWER, answer);


        startActivity(intent);
    }

    private void update() {
        Log.d(TAG_UPDATE, "update database and card here");
    }

}
