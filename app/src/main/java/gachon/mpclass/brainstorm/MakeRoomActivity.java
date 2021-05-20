package gachon.mpclass.brainstorm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeroom);

        Button enterbutton = (Button) findViewById(R.id.enter);
        EditText roomname = (EditText)findViewById(R.id.roomname);
        EditText name = (EditText)findViewById(R.id.nickname);

        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatroom = new ChatModel();
                chatroom.roomname = roomname.getText().toString();
                chatroom.user.add(name.getText().toString());

                chatroom.roomid = FirebaseDatabase.getInstance().getReference().child("chatroom").push().getKey();
                FirebaseDatabase.getInstance().getReference().child("chatroom").child(chatroom.roomid).setValue(chatroom);

                Intent intent = new Intent(getApplicationContext(),Chatting.class);
                intent.putExtra("roominfo",chatroom);
                startActivity(intent);
            }
        });

    }
}
