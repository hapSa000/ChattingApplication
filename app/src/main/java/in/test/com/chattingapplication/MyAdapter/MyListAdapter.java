package in.test.com.chattingapplication.MyAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.test.com.chattingapplication.ChatRoom;
import in.test.com.chattingapplication.ChattingWindow;
import in.test.com.chattingapplication.R;
import in.test.com.chattingapplication.mypojo;

public class MyListAdapter extends ArrayAdapter {
   private ArrayList<mypojo> values;
   Context context;
   int resource;



    public MyListAdapter(Context context, int resource, ArrayList<mypojo> list) {
        super(context,resource,list);
        this.context=context;
        this.resource=resource;
        values=list;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.mylist, null, false);
        TextView textView = view1.findViewById(R.id.user);

        LinearLayout linearLayout = view1.findViewById(R.id.lineR);
        final mypojo mypojo = values.get(position);
        Log.d("1234", "getView: "+values.size());
        textView.setText(mypojo.getName());
        ImageView profil_Image =view1.findViewById(R.id.imageView);
        Glide.with(context).load(mypojo.getImageUrl()).into(profil_Image);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoom.class);
                intent.putExtra("reciverId", mypojo.getId());
                intent.putExtra("name", mypojo.getName());
                context.startActivity(intent);

            }
        });
return view1;

    }
}
