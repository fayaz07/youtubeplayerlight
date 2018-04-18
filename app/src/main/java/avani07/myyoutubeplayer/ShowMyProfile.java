package avani07.myyoutubeplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowMyProfile extends AppCompatActivity {

    private FirebaseUser user;
    private TextView name,mobile,email,emailVerified;
    private Button changePassword,verifyEmail;
    private DatabaseReference databaseReference;
    private UserInfo uInfo;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

        Intent intent = getIntent();

        uInfo = (UserInfo)intent.getSerializableExtra("u");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        name = (TextView)findViewById(R.id.user_profile_name);
        email = (TextView)findViewById(R.id.userProfileEmail);
        mobile = (TextView)findViewById(R.id.showMobile);
        emailVerified = (TextView)findViewById(R.id.showEmail);
        changePassword = (Button)findViewById(R.id.changePassword);
        verifyEmail = (Button)findViewById(R.id.verifyEmail);
        progressDialog.show();

        name.setText(uInfo.getName());
        email.setText(uInfo.getEmail());
        mobile.setText("Mobile:"+uInfo.getMobile());
        emailVerified.setText("Email:"+uInfo.getEmail());
        progressDialog.dismiss();

        if(user.isEmailVerified())
            verifyEmail.setVisibility(View.GONE);
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification();
                Toast.makeText(getApplicationContext(),"Check your inbox to verify your email",Toast.LENGTH_SHORT).show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowMyProfile.this,ChangePassword.class);
                intent1.putExtra("p",uInfo.getPassword());
                startActivity(intent1);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
