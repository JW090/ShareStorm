package gachon.mpclass.brainstorm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NodeFragment extends Fragment {

    public Node node;
    public Runnable onAddToLayout;

    private ImageButton btn_node,btn_start;
    private TextView node_text;

    private boolean root = false;

    private NodeFragment fragment;
    private Mindmap act;

    private String temp;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NodeFragment() {
        // Required empty public constructor
        node = new Node( this, "unknown");
    }

    public NodeFragment(Mindmap mindmap, String text) {

        this.node = new Node(this,text);
        this.act = mindmap;
    }

    public NodeFragment(Mindmap mindmap, Node node){
        this.node = node;
        this.node.fragment = this;

        this.act = mindmap;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NodeFragment newInstance(String param1, String param2) {
        NodeFragment fragment = new NodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_node,container,false);


        btn_node = rootView.findViewById(R.id.node_img);
        node_text = rootView.findViewById(R.id.data);
        btn_start = rootView.findViewById(R.id.btn_mind_root);

        MindmapData mdata = new MindmapData();


        btn_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act);
                builder.setTitle("단어 입력");

                final EditText input = new EditText(act);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String temp = input.getText().toString();

                        mdata.text_data = temp;
                        mdata.id = FirebaseDatabase.getInstance().getReference().child("Mindmap").push().getKey();

                        FirebaseDatabase.getInstance().getReference().child("Mindmap").child(mdata.id).setValue(mdata);
                        //node_text.setText(temp);
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

        setNode(temp);

        registerForContextMenu(btn_node);

        btn_node.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });



        // Inflate the layout for this fragment
        return rootView;


    }



    // 루트 노드로 만듬
    public void makeRoot()
    {
        root = true;
    }

    //수정삭제메뉴
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater mi = getActivity().getMenuInflater();
        if(v==btn_node){
            mi.inflate(R.menu.node_menu,menu);
        }

    }


    public boolean onContextItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){
            case R.id.edit: get_edit_text();
            return true;
            case R.id.remove: FirebaseDatabase.getInstance().getReference().child("MindMap").removeValue(); //act.remove_node(this);
            return true;
        }
        return false;
    }

    //수정누르면 텍스트 팝업으로 입력받기
    public void get_edit_text(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act);
        builder.setTitle("단어 입력");

        final EditText input = new EditText(act);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String edit = input.getText().toString();
                node_text.setText(edit);

                edit_text(edit);

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


    //텍스트 수정
    public void edit_text(String edit){

        node.data = edit;
        node_text.setText(edit);

    }

    //텍스트 설정
    public void setNode( String text){

        node.data = text;
        node_text.setText(text);

    }



}