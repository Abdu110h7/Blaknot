package com.example.notepad.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.domain.model.Category;
import com.example.notepad.domain.model.Notes;
import com.example.notepad.ui.WriteActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    List<Notes> list;
    NoteInterface noteInterface;

    String[] colorArray = {"#F1A882", "#F0DDAF", "#99BBD2", "#F8EBDC", "#E7D8C2"};
    boolean isLongClick = false;
    List<Notes> deletes = new ArrayList<>();


    public NoteAdapter(List<Notes> list, NoteInterface noteInterface) {
        this.list = list;
        this.noteInterface = noteInterface;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.select_radio_butten.setVisibility(View.GONE);
        holder.note_card.setAlpha(1f);
        holder.note_card.setStrokeWidth(0);
        holder.note_card.setStrokeColor(Color.parseColor("#5170F5"));

        holder.note.setText(list.get(position).getNote());
        holder.title.setText(list.get(position).getTitle());
        holder.time.setText(list.get(position).getTime());
        holder.note_card.setCardBackgroundColor(Color.parseColor(colorArray[position %5]));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongClick){
                    holder.select_radio_butten.setVisibility(View.VISIBLE);
                    holder.note_card.setAlpha(0.8f);
                    holder.note_card.setStrokeWidth(5);
                    holder.note_card.setStrokeColor(Color.parseColor("#5170F5"));
                    deletes.add(list.get(holder.getAdapterPosition()));
                    noteInterface.onLongClickMore(deletes);
                }else {
                    Intent intent = new Intent(holder.imageView.getContext(), WriteActivity.class);
                    intent.putExtra("notes_id", list.get(holder.getAdapterPosition()).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.select_radio_butten.setVisibility(View.VISIBLE);
                holder.note_card.setAlpha(0.8f);
                holder.note_card.setStrokeWidth(5);
                holder.note_card.setStrokeColor(Color.parseColor("#5170F5"));
                deletes.add(list.get(holder.getAdapterPosition()));
                isLongClick = true;
                noteInterface.onLongClick(list.get(holder.getAdapterPosition()),holder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, note, time;
        ImageView imageView;
        MaterialCardView note_card;
        RadioButton select_radio_butten;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_Title);
            note = itemView.findViewById(R.id.note_desc);
            time = itemView.findViewById(R.id.note_time);
            imageView = itemView.findViewById(R.id.note_Image);
            note_card = itemView.findViewById(R.id.note_card);
            select_radio_butten = itemView.findViewById(R.id.select_radio_butten);
        }
    }
    public void changeWithCategory(List<Notes> list){
       this.list = list;
       notifyDataSetChanged();

    }

    public void refreshItem(List<Notes> list, int position){
        if (position == -1){
            this.list = list;
            notifyDataSetChanged();
        }else {
            notifyItemRemoved(position);
            this.list = list;
        }
    }
    public void cancelLongClick(){
        isLongClick = false;
        notifyDataSetChanged();
    }

}
