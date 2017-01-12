package com.learn.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learn.cards.models.Question;

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
            if (question != null) {

                DatabaseReference ref = mDatabase.child(DATABASE_QUESTIONS).push();
                String questionUUID = ref.getKey();
                ref.setValue(new Question(question, "my answer"));

                fragment.addItem(question, R.drawable.great_wall_of_china);
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
            Intent intent = new Intent();
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
