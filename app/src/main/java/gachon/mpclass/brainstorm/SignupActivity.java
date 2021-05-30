package gachon.mpclass.brainstorm;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText email_join;
    private EditText pwd_join;
    private Button btn;
    private EditText nickname_join;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_join = (EditText) findViewById(R.id.s_email);
        pwd_join = (EditText) findViewById(R.id.s_password);
        btn = (Button) findViewById(R.id.signupbtn);
        nickname_join =(EditText) findViewById(R.id.s_nickname);

        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_join.getText().toString().trim();
                String pwd = pwd_join.getText().toString().trim();
                String nickname = nickname_join.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    String uid = task.getResult().getUser().getUid();
                                    UserModel.uid = uid;
                                    UserModel.email = email;
                                    UserModel.nickname = nickname;

                                    HashMap<String, String> user = new HashMap();
                                    user.put("uid",uid);
                                    user.put("email",email);
                                    user.put("nickname",nickname);

                                    FirebaseDatabase.getInstance().getReference().child("userinfo").child(uid).push().setValue(user);

                                    SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("nickName",UserModel.nickname);
                                    editor.commit();

                                    Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "회원가입 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });


    }
}