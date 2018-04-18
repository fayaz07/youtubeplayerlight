package avani07.myyoutubeplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText email,password,mobilenumber,name;
    private Button signup,signin;
    private CheckBox agree;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering, please wait...");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

        email = (EditText)findViewById(R.id.email_reg);
        password = (EditText)findViewById(R.id.pwd_reg);
        mobilenumber = (EditText)findViewById(R.id.phone_reg);
        name = (EditText)findViewById(R.id.uname_reg);
        signin = (Button)findViewById(R.id.login_button);
        signup = (Button)findViewById(R.id.registerme);
        agree = (CheckBox)findViewById(R.id.agree_reg);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupMethod();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

    }

    private void signupMethod() {
        String name_s = name.getText().toString();
        String password_s = password.getText().toString();
        String email_s = email.getText().toString();
        String mobile_s = mobilenumber.getText().toString();

        if (agree.isChecked()){
            if (TextUtils.isEmpty(name_s)) {
                Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password_s)) {
                Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email_s)) {
                Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mobile_s)) {
                Toast.makeText(getApplicationContext(), "Enter mobile", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email_s,password_s)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()) {
                    UserInfo userInfo = new UserInfo(name_s,email_s,password_s,mobile_s);
                    user = firebaseAuth.getCurrentUser();
                    String key = user.getUid();
                    databaseReference.child(key).setValue(userInfo)
                     .addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                         //Do nothing
                         progressDialog.dismiss();
                         Toast.makeText(getApplicationContext(), "Registration Success,now signin to get started", Toast.LENGTH_SHORT).show();
                         firebaseAuth.signOut();
                         startActivity(new Intent(Register.this,Login.class));
                         finish();
                       }
                    });
                  }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration Failed,try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                  }
                });
        }
    }
}
