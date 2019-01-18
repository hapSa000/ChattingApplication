package in.test.com.chattingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.test.com.chattingapplication.MyAdapter.MyListAdapter;

public class ChattingWindow extends AppCompatActivity {
ArrayList arrayList=new ArrayList();
ListView listView;
FirebaseDatabase database;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_window);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Information");
        listView=findViewById(R.id.listChat);
        SharedPreferences sharedPreferences=getSharedPreferences("veer",MODE_PRIVATE);
        final String id=sharedPreferences.getString("id",null);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    mypojo mypojo=data.getValue(mypojo.class);
                    if (!mypojo.getId().equals(id)){
                        arrayList.add(mypojo);
                    }


                }
                MyListAdapter adapter = new MyListAdapter(ChattingWindow.this,R.layout.mylist,arrayList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
Toast.makeText(ChattingWindow.this,"DAtabase Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id=item.getItemId();
            switch (id)
            {

                case R.id.logout:
                    Toast.makeText(this,"logout",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=getSharedPreferences("veer",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent2 = new Intent(ChattingWindow.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;


            }

            return true;
        }
    }

