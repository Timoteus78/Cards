package com.learn.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learn.cards.models.Question;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_FIREBASE = "FIREBASE";
    public static final String DATABASE_QUESTIONS =  "questions";
    public static final String AUTH_MESSAGE = "com.learn.cards.AUTH";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCardForm(view);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        CardFragment fragment = (CardFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new CardFragment();

            Intent intent = getIntent();
            String question = intent.getStringExtra(CardFormActivity.MESSAGE_QUESTION);
            String answer = intent.getStringExtra(CardFormActivity.MESSAGE_ANSWER);
            String questionEdited = intent.getStringExtra(CardFragment.MESSAGE_QUESTION_EDIT);
            String answerEdited = intent.getStringExtra(CardFragment.MESSAGE_ANSWER_EDIT);
            String idEdited = intent.getStringExtra(CardFragment.MESSAGE_ID_EDIT);
            String deleteId = intent.getStringExtra(CardFormActivity.MESSAGE_DELETE);
            if (question != null && answer != null) {
            //Add new card
                DatabaseReference ref = mDatabase.child(DATABASE_QUESTIONS).push();
                ref.setValue(new Question(question, answer));
            }else if (questionEdited != null && answerEdited != null && idEdited != null) {
            //Update card
                DatabaseReference ref = mDatabase.child(DATABASE_QUESTIONS + '/' + idEdited);
                ref.setValue(new Question(questionEdited, answerEdited));
            } else if (deleteId != null) {
            //Remove card
                DatabaseReference ref = mDatabase.child(DATABASE_QUESTIONS + '/' + deleteId);
                ref.setValue(null, new DatabaseReference.CompletionListener (){
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError == null) {
                            Toast.makeText(MainActivity.this, "Item removed successfully.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(AUTH_MESSAGE, "logout");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createCardForm (View view) {
        Intent intent = new Intent(this, CardFormActivity.class);
        startActivity(intent);
    }

}
