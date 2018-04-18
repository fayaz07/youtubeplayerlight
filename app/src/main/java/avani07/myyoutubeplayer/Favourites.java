package avani07.myyoutubeplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favourites extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Video> videoList;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView = (RecyclerView) findViewById(R.id.favourites_list);
        progressDialog = new ProgressDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog.setMessage("Loading...");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favourites").child(firebaseUser.getUid());

    }
    //Recycler View
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Video,MainActivity.VideoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video,MainActivity.VideoViewHolder>
                (Video.class,R.layout.videos_list2,MainActivity.VideoViewHolder.class,databaseReference){
            @Override
            protected void populateViewHolder(MainActivity.VideoViewHolder viewHolder, Video model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(model.getImage());
                viewHolder.setUrl(model.getUrl());
                videoList.add(position,model);
                progressDialog.dismiss();
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        if(recyclerView.isAttachedToWindow())
            progressDialog.dismiss();

    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private MainActivity.RecyclerItemClickListener.OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;



        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, MainActivity.RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }


    public static class VideoViewHolder extends RecyclerView.ViewHolder{

        View view;
        public VideoViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setTitle(String t){
            TextView textView = (TextView)view.findViewById(R.id.titleVideo);
            textView.setText(t);
        }
        public void setUrl(String url){
            TextView textView2 = (TextView)view.findViewById(R.id.url);
            textView2.setText("www.youtube.com/"+url);
        }
        public void setImage(String image){
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            Picasso.get().load(image).into(imageView);
        }
    }

}
