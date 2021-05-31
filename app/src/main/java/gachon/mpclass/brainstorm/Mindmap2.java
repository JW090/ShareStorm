package gachon.mpclass.brainstorm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Mindmap2 extends AppCompatActivity {

    private Random randomFragmentMargins;
    private ArrayList<NodeFragment> nodeFragments;
    public NodeFragment movingFragment;

    public NodeFragment fragment;

    private Draw draw;

    private int prevX, prevY;

    private ImageButton btn_root;
    private TextView str;

    private String temp;

    String roomid;

    //메인
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mindmap);

        roomid = (String) getIntent().getStringExtra("roomid");

        getSupportActionBar().hide();

        randomFragmentMargins = new Random();

        /*ViewGroup drawViewContainer = (ViewGroup)findViewById(R.id.node_container);
         draw = new Draw(this);
         drawViewContainer.addView(draw);*/

        final Mindmap2 mindmap = this;

        nodeFragments = new ArrayList<>();

        prevX=prevY=-1;

        btn_root = findViewById(R.id.btn_mind_root);
        str = findViewById(R.id.Root_Node);

        Intent i = getIntent();
        String starting_word = i.getStringExtra("start");
        str.setText(starting_word);

        MindmapData mdata = new MindmapData();


        btn_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mindmap);
                builder.setTitle("단어 입력");

                final EditText input = new EditText(mindmap);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String temp = input.getText().toString();

                        mdata.text_data = temp;
                        mdata.id = FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("mindmap").push().getKey();


                        FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("mindmap").child(mdata.id).setValue(mdata);

                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        registerForContextMenu(btn_root);

        btn_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        //오류부분
        FirebaseDatabase.getInstance().getReference().child("chatroom").child(roomid).child("mindmap").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //새로추가된 값 가져오기
                MindmapData mindmapData = snapshot.getValue(MindmapData.class);

                String temp = mindmapData.text_data;

                //데이터 확인용 - Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                //add_Node(fragment,temp);


                // node_text.setText(temp);

            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                //remove_node(fragment);

            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*Bundle bundle = new Bundle();
        bundle.putString("roomid",roomid);
        fragment.setArguments(bundle);
*/

    }


    //수정삭저장메뉴
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater mi = getMenuInflater();
        if(v==btn_root){
            mi.inflate(R.menu.mindmap_menu,menu);
        }

    }
    //메뉴
    public boolean onContextItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){
            case R.id.edit_root: edit_root();
                return true;

            case R.id.save: shareMindmap();
                return true;

        }
        return false;
    }

    //루트텍스트수정
    public void edit_root(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어 입력");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String edit = input.getText().toString();
                str.setText(edit);

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    //상단바와 타이틀바의 길이를 합한 값값
    public int getBarsHeight(){

        View decorView = getWindow().getDecorView();
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        View contentView = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int[] location = new int [2];
        contentView.getLocationInWindow(location);
        int titleBarHeight = location[1]=statusBarHeight;

        return statusBarHeight + titleBarHeight;
    }


    private NodeFragment createNodeFragment(NodeFragment nodeFragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NodeFragment fragment = nodeFragment;

        fragmentTransaction.add(R.id.node_container,fragment);
        fragmentTransaction.commit();

        return fragment;
    }

    //노드 추가
    public NodeFragment add_Node(final NodeFragment parent, String text){

        final NodeFragment fragment = createNodeFragment(new NodeFragment(this,text));

        nodeFragments.add(fragment);


        if (parent!=null){

            parent.node.add_Child(fragment.node);
        }

        fragment.onAddToLayout = new Runnable() {
            @Override
            public void run() {
                int x_margin = randomFragmentMargins.nextInt(200)-99;
                int y_margin = randomFragmentMargins.nextInt(200)-99;

                if (parent !=null){
                    x_margin+=parent.node.x_margin;
                    y_margin+=parent.node.y_margin;
                }

                moveTo(fragment,x_margin,y_margin);

            }
        };

        Bundle bundle = new Bundle();
        bundle.putString("roomid",roomid);
        fragment.setArguments(bundle);

        return fragment;

    }

    private NodeFragment _createNodeFragmentHierarchy(final Node node)
    {
        final NodeFragment fragment = createNodeFragment(new NodeFragment(this, node));



        fragment.onAddToLayout = new Runnable() {
            @Override
            public void run() {
                moveTo(fragment, node.x_margin, node.y_margin);
            }
        };

        nodeFragments.add(fragment);

        for (Node child : node.child_node)
        {
            _createNodeFragmentHierarchy(child);
        }




        return fragment;
    }

    public NodeFragment createNodeFragmentHierarchy(Node root)
    {
        NodeFragment rootFragment = _createNodeFragmentHierarchy(root);
        rootFragment.makeRoot();

        return rootFragment;
    }


    //노드삭제
    public void remove_node(NodeFragment fragment){

        for (Node child : fragment.node.child_node){
            remove_node(child.fragment);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();

        nodeFragments.remove(fragment);

    }

    //노드 옮기기
    public static void moveTo(NodeFragment fragment, int x, int y){
        moveTo(fragment.node, fragment.getView(),x,y);
    }
    public static void moveTo(Node node, View view, int x, int y){
        node.x_margin = x;
        node.y_margin = y;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        params.setMargins(x,y,0,0);
        view.requestLayout();
    }
    public static void move(NodeFragment fragment, int dx, int dy)
    {
        move(fragment.node, fragment.getView(), dx, dy);
    }
    public static void move(Node node, View view, int dx, int dy)
    {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        moveTo(node, view, params.leftMargin + dx, params.topMargin + dy);
    }

    //노드 리스트 얻기
    public ArrayList<NodeFragment> getNodeFragments(){
        return nodeFragments;
    }

    //노드 한번에 이동
    public static void moveRecursively(NodeFragment fragment, int dx, int dy){

        try{
            move(fragment,dx,dy);
        }catch (Exception e){

        }
/*
        for(Node child:fragment.node.child_node){
            moveRecursively(child.fragment,dx,dy);
        }*/
    }

    //선택된 노드 이동
    public boolean onTouchEvent(MotionEvent event){

        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevX = x;
                prevY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int dltX = x - prevX, dltY = y-prevY;

                if (movingFragment !=null){
                    moveRecursively(movingFragment,dltX,dltY);
                }
                else
                {
                    for (NodeFragment fragment: nodeFragments){
                        move(fragment,dltX,dltY);
                    }
                }

                prevX=x;
                prevY=y;
                break;

            case MotionEvent.ACTION_UP:
                if (movingFragment!=null){
                    movingFragment=null;
                }

                prevX=prevY=-1;
                break;
        }

        return false;

    }

    private SaveNode saveNode;

    private View mindMaplayout;

    //비트맵으로 캡쳐
    public Bitmap captureMindmap(){

        mindMaplayout = findViewById(R.id.node_container);

        mindMaplayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mindMaplayout.getDrawingCache());
        mindMaplayout.setDrawingCacheEnabled(false);

        return bitmap;
    }

    // 비트맵을 Base64로 인코딩
    public static String convertBitmapToBase64(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }
    // Base64로부터 비트맵을 디코딩
    public static Bitmap convertBase64ToBitmap(String base64)
    {
        byte[] byteArray = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    // 마인드맵을 캡쳐하여 Base64로 인코딩하여 반환
    public String captureMipMapAsBase64()
    {
        return convertBitmapToBase64(captureMindmap());
    }

    //마인드맵 공유
    public void shareMindmap(){

        Bitmap icon = captureMindmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*"+"application/*");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"title");
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/PNG");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.PNG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    //데이터베이스 실시간 업데이트 확인



}