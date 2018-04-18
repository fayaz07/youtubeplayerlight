package avani07.myyoutubeplayer;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private List<Video> videoList;
    private RecyclerView recyclerView;

    private DatabaseReference databaseReference,userInfoDatabase;
    private TextView textView;
    private ProgressDialog progressDialog;
    private NetworkInfo info;
    private ConnectivityManager connectivityManager;
    boolean isNetworkactive;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    int n;
    int k;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private TextView nameView,EmailView;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        videoList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Video");
        databaseReference.keepSynced(true);
        userInfoDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        userInfoDatabase.keepSynced(true);
        connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo();

        isNetworkactive = info!=null && info.isConnectedOrConnecting();
               System.out.print(isNetworkactive);
        progressDialog.setMessage("Loading...");

        LayoutInflater inf = getLayoutInflater();
        view = inf.inflate(R.layout.nav_header,null);
        nameView = (TextView)view.findViewById(R.id.navigationname);
        EmailView = (TextView)view.findViewById(R.id.navigationemail);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView = (TextView) findViewById(R.id.textView);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        k=0;

        if (user==null){
            startActivity(new Intent(MainActivity.this,Register.class));
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        UserInfo info2 = new UserInfo();
        userInfoDatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot v: dataSnapshot.getChildren()){
                    UserInfo userInfo = (UserInfo) v.getValue(UserInfo.class);
                    //Toast.makeText(getApplicationContext(),userInfo.getEmail()+"\n"+user.getEmail(),Toast.LENGTH_LONG).show();
                    if (userInfo.getEmail().toString().equals(user.getEmail())){
                        info2.setName(userInfo.getName());
                        info2.setPassword(userInfo.getPassword());
                        info2.setMobile(userInfo.getMobile());
                        info2.setEmail(userInfo.getEmail());
                        nameView.setText(info2.getName());
                        EmailView.setText(info2.getEmail());
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();


        //Navigation Bar

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                        //Toast.makeText(getApplicationContext(),"Drawer Slided",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        drawerView.bringToFront();
                        drawerView.requestLayout();
                        // Respond when the drawer is opened
                        //Toast.makeText(getApplicationContext(),"Drawer opened",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        //Toast.makeText(getApplicationContext(),"Drawer closed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                        //Toast.makeText(getApplicationContext(),"Drawer State changed",Toast.LENGTH_SHORT).show();
                    }
                });



        //Recycler View

        recyclerView.addOnItemTouchListener(
           new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
              @Override public void onItemClick(View view, int position) {
                  // do whatever
                  String urll = videoList.get(position).getUrl();

                  Video send = new Video();
                  send.setUrl(videoList.get(position).getUrl());
                  send.setTitle(videoList.get(position).getTitle());
                  send.setImage(videoList.get(position).getImage());

                  Intent intent = new Intent(MainActivity.this,YoutubePlayer.class);

                  intent.putExtra("vo", send);
                  startActivity(intent);
              }

              @Override public void onLongItemClick(View view, int position) {
              }
           })
        );


    }



    //Navigation Drawer

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,About.class));
            return true;
        }
       if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
            return true;
        }
        if (id == R.id.nav_profilesection) {
            progressDialog.show();
            user = FirebaseAuth.getInstance().getCurrentUser();
            UserInfo info = new UserInfo();
            userInfoDatabase.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot v: dataSnapshot.getChildren()){
                        UserInfo userInfo = (UserInfo) v.getValue(UserInfo.class);
                        //Toast.makeText(getApplicationContext(),userInfo.getEmail()+"\n"+user.getEmail(),Toast.LENGTH_LONG).show();
                        if (userInfo.getEmail().toString().equals(user.getEmail())){

                            info.setName(userInfo.getName());
                            info.setPassword(userInfo.getPassword());
                            info.setMobile(userInfo.getMobile());
                            info.setEmail(userInfo.getEmail());
                            Intent intent = new Intent(MainActivity.this,ShowMyProfile.class);
                            intent.putExtra("u",info);
                            startActivity(intent);
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

                }
            });

        }
        if (id == R.id.nav_addvideo) {
            startActivity(new Intent(MainActivity.this,AddVideo.class));
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //Recycler View

    @Override
    protected void onStart() {
        super.onStart();
        n=0;
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (k==0) {
            startActivity(new Intent(MainActivity.this, Splash.class));
            k++;
        }

        if (isNetworkactive)
            progressDialog.show();
        else
        {
            Toast.makeText(getApplicationContext(),"Network Connection not active",Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseRecyclerAdapter<Video,VideoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Video,VideoViewHolder>
                (Video.class,R.layout.videos_list,VideoViewHolder.class,databaseReference){
            @Override
            protected void populateViewHolder(VideoViewHolder viewHolder, Video model, int position) {

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

    @Override
    public void onClick(View v) {

    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;



        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
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



    @Override
    public void onBackPressed() {
        n++;
        if (n==1)
            Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        if (n==2)
            finish();
    }

}