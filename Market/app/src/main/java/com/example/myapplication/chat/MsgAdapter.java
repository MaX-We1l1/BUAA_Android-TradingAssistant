package com.example.myapplication.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.Message;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Message> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View view){
            super(view);
            leftLayout=(LinearLayout)view.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            leftMsg=(TextView)view.findViewById(R.id.left_msg);
            rightMsg=(TextView)view.findViewById(R.id.right_msg);
        }
    }
    public MsgAdapter(List<Message> msgList){
        mMsgList=msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){               //onCreateViewHolder()用于创建ViewHolder实例
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
        return new ViewHolder(view);                                                   //把加载出来的布局传到构造函数中，再返回
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, int position){                     //onBindViewHolder()用于对RecyclerView子项的数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
        Message msg=mMsgList.get(position);
        if(getType(msg) == Message.TYPE_RECEIVED){                                         //增加对消息类的判断，如果这条消息是收到的，显示左边布局，是发出的，显示右边布局
            Holder.leftLayout.setVisibility(View.VISIBLE);
            Holder.rightLayout.setVisibility(View.GONE);
            Holder.leftMsg.setText(msg.getContent());
        }else if(getType(msg) == Message.TYPE_SENT) {
            Holder.rightLayout.setVisibility(View.VISIBLE);
            Holder.leftLayout.setVisibility(View.GONE);
            Holder.rightMsg.setText(msg.getContent());
        }
    }
    @Override
    public int getItemCount(){
        return mMsgList.size();
    }

    public int getType(Message msg) {
        if (msg.getSenderId() == MainActivity.getCurrentUserId()) {
            return Message.TYPE_SENT;
        } else {
            return Message.TYPE_RECEIVED;
        }
    }
}
