package gachon.mpclass.brainstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            case R.id.menu_out:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}