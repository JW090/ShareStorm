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

        //제목줄 제목글시를 채팅방 이름으로
        getSupportActionBar().setTitle(roomname);

        et = findViewById(R.id.et);
        listView = findViewById(R.id.listview);
        btn = findViewById(R.id.btn);

        adapter = new ChatAdapter(messageItems, getLayoutInflater());
        listView.setAdapter(adapter);


        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(messageItem);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size() - 1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
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
                //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
                String nickName = UserModel.nickname;
                String message= et.getText().toString();

                //메세지 작성 시간 문자열로..
                Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

                //firebase DB에 저장할 값(MessageItem객체) 설정
                MessageItem messageItem= new MessageItem(nickName,message,time);

                //'char'노드에 MessageItem객체를 통해
                //key = FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").push().getKey();
                FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").push().setValue(messageItem);


                //EditText에 있는 글씨 지우기
                et.setText("");

                //소프트키패드를 안보이도록..
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


            }
        });
    }

/*    public void clickSend(View view) {
        //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
        String nickName= UserModel.nickname;
        String message= et.getText().toString();

        //메세지 작성 시간 문자열로..
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

        //firebase DB에 저장할 값(MessageItem객체) 설정
        MessageItem messageItem= new MessageItem(nickName,message,time);
        //'char'노드에 MessageItem객체를 통해
        FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("message").push().setValue(messageItem);

        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이도록..
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
*/


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
            case R.id.menu_chatcode:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("roomcode", roominfo.roomid);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplication(), "코드가 복사되었습니다",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_exitroom:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}