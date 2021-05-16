package gachon.mpclass.brainstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
            case R.id.menu_code:
                Toast.makeText(this, "방 코드 만들기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_chatroom:
                Intent intent = new Intent(this, NewActivity.class);
                startActivity(intent);
                Toast.makeText(this, "채팅방에 입장하였습니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_out:
                Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}