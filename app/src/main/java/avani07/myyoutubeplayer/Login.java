package avani07.myyoutubeplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText email,password;
    private Button signin,signup,forgot;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.unamefield_login);
        password =(EditText)findViewById(R.id.passwdfield_login);
        signin = (Button) findViewById(R.id.login_button_login);
        signup = (Button)findViewById(R.id.sign_up_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        forgot = (Button)findViewById(R.id.forgot_password_login);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user!=null){
            finish();
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail("Reset my password");
                Toast.makeText(getApplicationContext(),"Check your inbox to reset your password",Toast.LENGTH_SHORT).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                loginMethod();
            }
        });


    }

    private void loginMethod() {

        String email_id = email.getText().toString();
        String password_id = password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email_id,password_id)
           .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   progressDialog.dismiss();
                   if (task.isSuccessful()) {
                       user = FirebaseAuth.getInstance().getCurrentUser();
                       Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                       finish();
                   }else
                       Toast.makeText(getApplicationContext(),"Login failed, try again",Toast.LENGTH_SHORT).show();
               }
           });
    }
}
