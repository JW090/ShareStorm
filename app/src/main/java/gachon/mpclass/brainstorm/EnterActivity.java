package gachon.mpclass.brainstorm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class EnterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        Button enterbutton = (Button) findViewById(R.id.enter);
        EditText roomid = (EditText)findViewById(R.id.roomcode);
        EditText name = (EditText)findViewById(R.id.nickname);

        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = roomid.getText().toString();
                String myname = name.getText().toString();

                FirebaseDatabase.getInstance().getReference().child("chatroom").child(id).child("user").push().setValue(myname)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                ChatModel chatroom = new ChatModel();

                chatroom.roomid = id;
                chatroom.roomname = FirebaseDatabase.getInstance().getReference().child("chatroom").child(id).child("roomname").getKey();
                chatroom.user.add(FirebaseDatabase.getInstance().getReference().child("chatroom").child(id).child("user").getKey());

                System.out.println(chatroom.user);

                Intent intent = new Intent(getApplicationContext(),Chatting.class);
                intent.putExtra("roominfo",chatroom);
                startActivity(intent);
            }
        });

    }
}
