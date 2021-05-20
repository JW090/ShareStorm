package gachon.mpclass.brainstorm;

import java.io.Serializable;
import java.util.ArrayList;

// 채팅방 정보 저장하는 클래스
public class ChatModel implements Serializable {
    public String roomid; // 데이터베이스에 저장된 채팅방 키값
    public String roomname; // 채팅방 이름
    public ArrayList<String> user = new ArrayList<String>(); // 채팅방 인원
   // public ArrayList<String> uid = new ArrayList<String>();
}
