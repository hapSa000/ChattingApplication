package in.test.com.chattingapplication.MyAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.test.com.chattingapplication.ChatRoom;
import in.test.com.chattingapplication.MessagePojo;
import in.test.com.chattingapplication.R;
import in.test.com.chattingapplication.mypojo;

public class MessageAdapter extends ArrayAdapter {
        private ArrayList<MessagePojo> values;
Context context;
int resource;

        public MessageAdapter(Context context, int resource, ArrayList<MessagePojo> list) {
            super(context,resource,list);
            this.context=context;
            this.resource=resource;
            values=list;

        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater.inflate(R.layout.message_item, null, false);
            TextView textView = view1.findViewById(R.id.messageText);

            final MessagePojo pojo = values.get(position);

            textView.setText(pojo.getMessage());

            return view1;

        }
}

