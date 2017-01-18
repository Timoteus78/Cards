package com.learn.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        item.setCardName(title);
        item.setImageResourceId(drawable);

        listitems.add(item);
        //MyRecyclerView.getAdapter().notifyItemInserted(listitems.size() -1);
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
            holder.questionTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(R.drawable.chichen_itza);
            holder.coverImageView.setTag(R.drawable.chichen_itza);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView questionTextView;
        public ImageView coverImageView;

        public MyViewHolder(View v) {
            super(v);
            questionTextView = (TextView) v.findViewById(R.id.questionTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
        }
    }

    public void initializeList() {
        for(Question question: questionSet){
            CardModel item = new CardModel();
            item.setCardName(question.getQuestion());
            item.setImageResourceId(R.drawable.chichen_itza);
            listitems.add(item);
        }
    }

}
