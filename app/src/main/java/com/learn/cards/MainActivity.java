package com.learn.cards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.learn.cards.models.Question;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_FIREBASE = "FIREBASE";
    public static final String DATABASE_QUESTIONS =  "questions";
    public static final String AUTH_MESSAGE = "com.learn.cards.AUTH";
    private DatabaseReference mDatabase;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

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
            if (question != null && answer != null) {

                DatabaseReference ref = mDatabase.child(DATABASE_QUESTIONS).push();
                String questionUUID = ref.getKey();
                ref.setValue(new Question(question, answer));

                fragment.addItem(question, R.drawable.great_wall_of_china);
            }

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


/*        findViews();
        loadAnimations();
        changeCameraDistance();*/

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

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    public void flipCard(View view) {
        Log.d("[MAIN]", "flipCard haha");
        FragmentManager fm = getSupportFragmentManager();
        CardFragment fragment = (CardFragment)fm.findFragmentById(R.id.fragmentContainer);
        mCardBackLayout = fragment.getView().findViewById(R.id.cardView).findViewById(R.id.card_view).findViewById(R.id.card_back);
        mCardFrontLayout = fragment.getView().findViewById(R.id.cardView).findViewById(R.id.card_view).findViewById(R.id.card_front);
        loadAnimations();
        changeCameraDistance();
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }

}
