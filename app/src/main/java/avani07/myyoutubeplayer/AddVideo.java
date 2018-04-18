package avani07.myyoutubeplayer;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVideo extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText title,image,url;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("VideoToAdd");
        title = (EditText)findViewById(R.id.titleVideo);
        image = (EditText)findViewById(R.id.imageUrl);
        url = (EditText)findViewById(R.id.videoUrl);
        button = (Button) findViewById(R.id.addVideoButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(image.getText()) || TextUtils.isEmpty(url.getText())){
                    Toast.makeText(getApplicationContext(),"Enter the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                String key = databaseReference.push().getKey();
                Video addVideo = new Video(title.getText().toString(),image.getText().toString(),url.getText().toString());
                databaseReference.child(key).setValue(addVideo)
                    .addOnCompleteListener(AddVideo.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Your video will be verified and added shortly",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Failed, try again",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
            }
        });

    }
}
