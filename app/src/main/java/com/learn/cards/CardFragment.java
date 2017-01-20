package com.learn.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learn.cards.models.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CardFragment extends Fragment {

    private DatabaseReference mDatabase;
    public static final String DATABASE_QUESTIONS =  "questions";
    private Set<Question> questionSet = new HashSet<>();
    ArrayList<CardModel> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("All Cards");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card_view, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot questions = dataSnapshot.child(DATABASE_QUESTIONS);
                for(DataSnapshot child : questions.getChildren()){
                    Question question = child.getValue(Question.class);
                    questionSet.add(question);
                }
                initializeList();

                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
                MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                MyRecyclerView.setHasFixedSize(true);
                if (listitems.size() > 0 & MyRecyclerView != null) {
                    MyRecyclerView.setAdapter(new MyAdapter(listitems));
                }
                MyRecyclerView.setLayoutManager(MyLayoutManager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void addItem(String title, int drawable) {
        CardModel item  = new CardModel();
        item.setQuestion(title);
        item.setImageResourceId(drawable);
        listitems.add(item);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<CardModel> list;

        public MyAdapter(ArrayList<CardModel> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.questionTextView.setText(list.get(position).getQuestion());
            holder.answerTextView.setText(list.get(position).getAnswer());
            holder.coverImageView.setImageResource(R.drawable.chichen_itza);
            holder.coverImageView.setTag(R.drawable.chichen_itza);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView questionTextView;
        public TextView answerTextView;
        public ImageView coverImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View cardFlipRoot = v.findViewById(R.id.card_flip_root);
                    View back = v.findViewById(R.id.card_back);
                    View front = v.findViewById(R.id.card_front);

                    FlipAnimation flipAnimation = new FlipAnimation(front, back);
                    if(front.getVisibility() == View.GONE) {
                        flipAnimation.reverse();
                    }
                    cardFlipRoot.startAnimation(flipAnimation);
                }
            });
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            questionTextView = (TextView) itemView.findViewById(R.id.questionTextView);
            answerTextView = (TextView) itemView.findViewById(R.id.answerTextView);
            coverImageView = (ImageView) itemView.findViewById(R.id.coverImageView);
        }
    }

    public void initializeList() {
        for(Question question: questionSet){
            CardModel item = new CardModel();
            item.setQuestion(question.getQuestion());
            item.setImageResourceId(R.drawable.chichen_itza);
            item.setAnswer(question.getAnswer());
            listitems.add(item);
        }
    }

}
