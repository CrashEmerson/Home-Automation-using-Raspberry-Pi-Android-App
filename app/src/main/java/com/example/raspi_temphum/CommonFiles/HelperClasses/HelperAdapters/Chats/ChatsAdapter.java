package com.example.raspi_temphum.CommonFiles.HelperClasses.HelperAdapters.Chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raspi_temphum.R;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    ArrayList<ChatsHelperClass> chatsHelperClassArrayList;

    public ChatsAdapter(ArrayList<ChatsHelperClass> chatsHelperClassArrayList) {
        this.chatsHelperClassArrayList = chatsHelperClassArrayList;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_text_layout, parent, false);

        ChatsViewHolder chatsViewHolder = new ChatsViewHolder(view);
        return chatsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        ChatsHelperClass chats = chatsHelperClassArrayList.get(position);

        holder.chatsTxtView.setText(chats.getChatsText());
        holder.chatsTimeTxtView.setText(chats.getChatsTime());
    }

    @Override
    public int getItemCount() {
        return chatsHelperClassArrayList.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView chatsTxtView, chatsTimeTxtView;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            chatsTxtView = itemView.findViewById(R.id.chatsTxtView);
            chatsTimeTxtView = itemView.findViewById(R.id.chatsTimeTxtView);
        }
    }

}
