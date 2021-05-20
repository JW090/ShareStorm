package gachon.mpclass.brainstorm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Chatting extends AppCompatActivity {

    private ChatModel roominfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting);

        roominfo = (ChatModel)getIntent().getSerializableExtra("roominfo");
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