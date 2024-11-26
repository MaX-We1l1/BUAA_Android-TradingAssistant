package com.example.myapplication.profile.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Comment;
import com.example.myapplication.database.Hobby;
import com.example.myapplication.profile.FavoriteAdapter;
import com.example.myapplication.square.CommodityDetailActivity;

import java.util.List;

public class CommentAdapter {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }


}
