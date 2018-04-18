package avani07.myyoutubeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ChangePassword extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private EditText t1,t2,t3;
    private FirebaseUser user;
    private Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String old = intent.getStringExtra("p");
        t1 = (EditText)findViewById(R.id.oldPassword);
        t2 = (EditText)findViewById(R.id.newp1);
        t3 = (EditText)findViewById(R.id.newp2);
        change = (Button)findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(t1.getText()) || TextUtils.isEmpty(t2.getText()) || TextUtils.isEmpty(t3.getText())){
                    Toast.makeText(getApplicationContext(),"Enter the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (t1.getText().equals(old)){
                    if (t2.getText().equals(t3.getText())){
                        user.updatePassword(t2.getText().toString())
                            .addOnCompleteListener(ChangePassword.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getApplicationContext(),"Failed, contact admin",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                    }
                }
            }
        });


    }

}
