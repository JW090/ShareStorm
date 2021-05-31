package gachon.mpclass.brainstorm;


import com.google.firebase.database.DatabaseReference;

public class SaveNode {

    private String N;

    private DatabaseReference mDatabase;

    public String loadNodes(Runnable callback){
        readNodes(callback);
        return N;
    }

    public void readNodes(Runnable callback){

    }


}
