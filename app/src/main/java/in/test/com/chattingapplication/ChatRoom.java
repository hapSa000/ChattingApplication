package in.test.com.chattingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.test.com.chattingapplication.MyAdapter.MessageAdapter;

public class ChatRoom extends AppCompatActivity {
ListView listView;
FirebaseDatabase database;
DatabaseReference reference;
ImageView imagesendd;
EditText editMessage;
ArrayList<MessagePojo> arrayList=new ArrayList<>();
String reciverid,senderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("message");
        getDataFromFirebase();
        Intent intent=getIntent();
        reciverid = intent.getStringExtra("reciverId");
        String name=intent.getStringExtra("name");
        setTitle(name);
        SharedPreferences sharedPreferences=getSharedPreferences("veer",MODE_PRIVATE);
        senderid=sharedPreferences.getString("id", null);
        listView=findViewById(R.id.listView);

        imagesendd=findViewById(R.id.imgsend);
        editMessage=findViewById(R.id.editMessage);
imagesendd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String message = editMessage.getText().toString();
        MessagePojo pojo=new MessagePojo();
        pojo.setMessage(message);
        pojo.setSenderId(senderid);
        pojo.setReciverId(reciverid);
        reference.push().setValue(pojo);
editMessage.setText(null);
        getDataFromFirebase();
    }


});
    }


    private void getDataFromFirebase()
    {
        Log.d("123", "getDataFromFirebase: "+senderid+reciverid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    MessagePojo pojo =  data.getValue(MessagePojo.class);
                    if (pojo.getReciverId().equals(reciverid) && pojo.getSenderId().equals(senderid) || pojo.getSenderId().equals(reciverid) && pojo.getReciverId().equals(senderid)) {
                        arrayList.add(pojo);
                    }
                }
                MessageAdapter adapter = new MessageAdapter(ChatRoom.this,R.layout.message_item, arrayList);
            listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoom.this, "DataBase Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
