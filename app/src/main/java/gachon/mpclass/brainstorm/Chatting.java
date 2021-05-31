package gachon.mpclass.brainstorm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.database.core.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class Chatting extends AppCompatActivity {
    EditText et;
    ListView listView;
    Button btn;

    ArrayList<MessageItem> messageItems = new ArrayList<>();
    ChatAdapter adapter;

    private ChatModel roominfo;
    public String roomid;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting);

        roominfo = (ChatModel) getIntent().getSerializableExtra("roominfo");
        roomid = roominfo.roomid;
        String roomname = roominfo.roomname;

        // 제목을 방 이름으로
        getSupportActionBar().setTitle(roomname);

        et = findViewById(R.id.et);
        listView = findViewById(R.id.listview);
        btn = findViewById(R.id.btn);

        adapter = new ChatAdapter(messageItems, getLayoutInflater());
        listView.setAdapter(adapter);


        // 메세지가 추가되면 실시간으로 반영
        FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 채팅방 리스트뷰에 추가된 메세지 내용 붙이기
                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);
                messageItems.add(messageItem);
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String nickName = UserModel.nickname;
                String message= et.getText().toString();

                // 현재 시간 저장
                Calendar calendar= Calendar.getInstance();
                String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

                MessageItem messageItem= new MessageItem(nickName,message,time);

                // 데이터베이스에 닉네임, 메세지, 시간 정보 저장
                FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").push().setValue(messageItem);

                et.setText("");

                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


            }
        });
    }


    // 채팅방 안에서 메뉴
    // 채팅방 코드 복사하기, 채팅방 나가기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_chatcode: // 채팅방 코드 복사
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("roomcode", roominfo.roomid);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplication(), "코드가 복사되었습니다",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_exitroom: //채팅방 나가기

                // 데이터베이스에서 유저 삭제
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("user").child(uid).removeValue();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_mindmap:
                Intent intent2 = new Intent(getApplicationContext(), HomeActivity2.class);
                intent2.putExtra("roomid",roomid);
                startActivity(intent2);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}