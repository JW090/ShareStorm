package gachon.mpclass.brainstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity2 extends AppCompatActivity {

    Button start_button;
    EditText starting_word;

    String roomid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start_button = findViewById(R.id.btn_start);
        starting_word = findViewById(R.id.start_text);

        roomid = (String) getIntent().getStringExtra("roomid");


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = starting_word.getText().toString();

                Intent intent = new Intent(getApplicationContext(),Mindmap2.class);
                intent.putExtra("Start",text);
                intent.putExtra("roomid",roomid);
                startActivity(intent);

            }
        });


    }
}