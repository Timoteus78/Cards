package com.learn.cards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CardFormActivity extends AppCompatActivity {

    public final static String MESSAGE_QUESTION = "com.learn.cards.QUESTION";
    public final static String MESSAGE_ANSWER = "com.learn.cards.ANSWER";
    public final static String MESSAGE_DELETE = "com.learn.cards.DELETE";
    private static final String TAG_UPDATE = "com.learn.cards.UPDATE";
    private static final String TAG_DELETE = "com.learn.cards.DELETE";
    private String idEdited = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String question = intent.getStringExtra(CardFragment.MESSAGE_QUESTION_EDIT);
        String answer = intent.getStringExtra(CardFragment.MESSAGE_ANSWER_EDIT);
        idEdited = intent.getStringExtra(CardFragment.MESSAGE_ID_EDIT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(question != null && answer!= null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update(idEdited);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            // show modal delete confirmation dialog here.
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Remove card");

            alert.setPositiveButton(
                    "Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CardFormActivity.this, MainActivity.class);
                            intent.putExtra(MESSAGE_DELETE, idEdited);
                            startActivity(intent);
                        }
                    }
            );

            alert.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }
            );

            alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void update(String idEdited) {
        Log.d(TAG_UPDATE, "update database and card here");
        Intent intent = new Intent(this, MainActivity.class);

        EditText questionField = (EditText) findViewById(R.id.questionField);
        String question = questionField.getText().toString();
        intent.putExtra(CardFragment.MESSAGE_QUESTION_EDIT, question);

        EditText answerField = (EditText) findViewById(R.id.answerField);
        String answer = answerField.getText().toString();
        intent.putExtra(CardFragment.MESSAGE_ANSWER_EDIT, answer);

        intent.putExtra(CardFragment.MESSAGE_ID_EDIT, idEdited);


        startActivity(intent);
    }

}
