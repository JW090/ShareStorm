package gachon.mpclass.brainstorm;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Node {


    public Node root_Node;
    public int x_margin,y_margin;


    public ArrayList<Node> child_node;
    public String data;

    public NodeFragment fragment;


    public Node(NodeFragment fragment,String data){

        this.data = data;
        this.root_Node = null;
        this.child_node = new ArrayList<>();
        this.fragment = fragment;
    }

    public void add_Child(Node child){

        child.root_Node = this;
        child_node.add(child);

    }


}
