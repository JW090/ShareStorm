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

public class HomeActivity extends AppCompatActivity {

    Button start_button;
    EditText starting_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start_button = findViewById(R.id.btn_start);
        starting_word = findViewById(R.id.start_text);



        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = starting_word.getText().toString();

                Intent intent = new Intent(getApplicationContext(),Mindmap.class);
                intent.putExtra("Start",text);
                startActivity(intent);

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_newchat:
                Toast.makeText(this, "새로운 채팅방 생성", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, MakeRoomActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_chatroom:
                Intent intent2 = new Intent(this, EnterActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}