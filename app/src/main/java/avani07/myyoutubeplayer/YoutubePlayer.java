package avani07.myyoutubeplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;

public class YoutubePlayer extends YouTubeBaseActivity implements NavigationView.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private YouTubePlayerView youtubePlayer;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private Button play,pause,next,prev,forward,rewind, addfavButton;
    private LinearLayout buttonLayout;
    private DatabaseReference userInfoDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TextView nooftimesplayed,lastplayeddur,videoName,urlView;
    private String[] myPlayList,myPlayListNames;
    private ProgressDialog progressDialog;
    private TextView nameView,EmailView;
    private View view;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Intent intent = getIntent();
        Video videoCurrent = (Video)intent.getSerializableExtra("vo");

        pos = 0;
        youtubePlayer = (YouTubePlayerView) findViewById(R.id.myplayer);
        progressDialog = new ProgressDialog(this);
        //videoListToPlay.add();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        videoName = (TextView)findViewById(R.id.videoName);
        userInfoDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        userInfoDatabase.keepSynced(true);
        urlView = (TextView)findViewById(R.id.urlView);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater inf = getLayoutInflater();
        view = inf.inflate(R.layout.nav_header,null);
        nameView = (TextView)view.findViewById(R.id.navigationname);
        EmailView = (TextView)view.findViewById(R.id.navigationemail);

        play = (Button)findViewById(R.id.playButton);
        pause = (Button)findViewById(R.id.pauseButton);
        next = (Button)findViewById(R.id.nextButton);
        prev = (Button)findViewById(R.id.prevButton);
        forward = (Button)findViewById(R.id.forButton);
        rewind = (Button)findViewById(R.id.rewindButton);
        buttonLayout = (LinearLayout)findViewById(R.id.buttonYoutube);



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
                //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

            }
        });
/*addfavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = addFav.push().getKey();
                addFav.child(key).setValue(videoCurrent)
                   .addOnCompleteListener(YoutubePlayer.this, new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           addfavButton.setText("Added to Favourites");
                           Toast.makeText(getApplicationContext(),"Added to favourites",Toast.LENGTH_SHORT).show();
                       }
                });
            }
        });
        */

        myPlayList = new String[]{"61floBUAiTY", "RLmeisD3ZgM", "XpSicjw4v_U", "KMWS5y2gZ6E", "bXxoZIL-U74", "biCQbLCSd9U", "xKcFZ3MXEv4"};
        //myPlayList.add(videoCurrent.getUrl());
        /*myPlayList(0,"61floBUAiTY");
        myPlayList.set(1,"RLmeisD3ZgM");
        myPlayList.set(2,"XpSicjw4v_U");
        myPlayList.set(3,"KMWS5y2gZ6E");
        myPlayList.set(4,"bXxoZIL-U74");
        myPlayList.set(5,"biCQbLCSd9U");
        myPlayList.set(6,"xKcFZ3MXEv4");
        myPlayListNames.set(0,"'Tu Hai Ki Nahi' FULL VIDEO Song");
        myPlayListNames.set(1,"Hum Tum Ko Nigahon Mein ");
        myPlayListNames.set(2,"Mar Jaawan Mit Jaawan");
        myPlayListNames.set(3,"The Journey of Bharat - Mahesh Babu");
        myPlayListNames.set(4,"ROCKABYE - Clean Bandit | Shirley Setia");
        myPlayListNames.set(5,"Shirley Setia & KHS");
        myPlayListNames.set(6,"Titanium | Cover by Shirley Setia ");
*/

        myPlayListNames = new String[]{
                "'Tu Hai Ki Nahi' FULL VIDEO Song",
                "Hum Tum Ko Nigahon Mein ",
                "Mar Jaawan Mit Jaawan",
                "The Journey of Bharat - Mahesh Babu",
                "ROCKABYE - Clean Bandit | Shirley Setia",
                "Shirley Setia & KHS",
                "Titanium | Cover by Shirley Setia "
        };

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override

            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                //Toast.makeText(getApplicationContext(),"initialize sucess",Toast.LENGTH_SHORT).show();
                videoName.setText(videoCurrent.getTitle());
                urlView.setText("www.youtube.com/"+videoCurrent.getUrl());

                youTubePlayer.loadVideo(videoCurrent.getUrl());

                //youTubePlayer.loadVideos();
                play.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                youTubePlayer.play();
                            }
                        }
                );
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        youTubePlayer.pause();
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (pos>=0 && pos<=5){
                            pos++;
                            youTubePlayer.loadVideo(myPlayList[pos]);
                            videoName.setText(myPlayListNames[pos]);
                            urlView.setText("www.youtube.com/"+myPlayList[pos]);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Playlist finished",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pos>=1 && pos<=6){
                            pos--;
                            youTubePlayer.loadVideo(myPlayList[pos]);
                            videoName.setText(myPlayListNames[pos]);
                            urlView.setText("www.youtube.com/"+myPlayList[pos]);
                        }else {
                            Toast.makeText(getApplicationContext(),"Playlist just started",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        youTubePlayer.seekToMillis(youTubePlayer.getCurrentTimeMillis()+5000);
                    }
                });
                rewind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        youTubePlayer.seekToMillis(youTubePlayer.getCurrentTimeMillis()-5000);
                    }
                });
                youTubePlayer.setOnFullscreenListener( e -> buttonLayout.setVisibility(INVISIBLE));

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(),"initialize failed",Toast.LENGTH_SHORT).show();
            }
        };

        youtubePlayer.initialize("AIzaSyB_19ysEKsaqgwwc44EMMmmD45vhLa4kco",onInitializedListener);

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


    }



//Navigation Drawer

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_about) {
            startActivity(new Intent(YoutubePlayer.this,About.class));
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(YoutubePlayer.this,Login.class));
            finish();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        if (id == R.id.nav_profilesection) {
            progressDialog.show();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
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
                            Intent intent = new Intent(YoutubePlayer.this,ShowMyProfile.class);
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
            startActivity(new Intent(YoutubePlayer.this,AddVideo.class));
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeButtonYoutubePlayer:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    public void actionHomeClicked(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}
