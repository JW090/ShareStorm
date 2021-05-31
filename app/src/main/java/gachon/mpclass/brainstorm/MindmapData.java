package gachon.mpclass.brainstorm;

import java.io.Serializable;

//마인드맵 노드 정보
public class MindmapData implements Serializable {

   public   String id;
    public String text_data;


    public MindmapData(String id, String text_data, Node rootNode){
        this.id = id;
        this.text_data = text_data;
    }

    public MindmapData(){

    }

    public void setId(String id){
        this.id = id;
    }
    public void setText_data(String text_data){
        this.text_data = text_data;
    }


    public String getId(){return id;}
    public String getText_data(){return text_data;}


}
