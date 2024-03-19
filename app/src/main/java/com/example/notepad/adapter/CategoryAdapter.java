package com.example.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.domain.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

   List<Category> categorylist;
   CategoryInterface categoryInterface;
   int isSelect = -1;

    public CategoryAdapter(List<Category> categorylist, CategoryInterface categoryInterface) {
        this.categorylist = categorylist;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
       return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (categorylist .size() > 0){
            if (position == 0) {
                holder.cateogryText.setText("Add");
                holder.categoryicon.setVisibility(View.VISIBLE);
                holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                holder.cateogryRoot.setBackgroundResource(R.drawable.backrgound_category_selected);
            }else if (position == 1){
                holder.cateogryText.setText("All");
                holder.categoryicon.setVisibility(View.GONE);
                if (isSelect == -1){
                    holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                    holder.cateogryRoot.setBackgroundResource(R.drawable.backrgound_category_selected);
                }else {
                    holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.black));
                    holder.cateogryRoot.setBackgroundResource(R.drawable.backgraund_category_unselected);
                }

            }else {
                holder.cateogryText.setText(categorylist.get(position - 2).category_Name);
                holder.categoryicon.setVisibility(View.GONE);
                if (isSelect == position){
                    holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
                    holder.cateogryRoot.setBackgroundResource(R.drawable.backrgound_category_selected);
                }else {
                    holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    holder.cateogryRoot.setBackgroundResource(R.drawable.backgraund_category_unselected);
                }
            }
        }else {
            holder.cateogryText.setText("Add");
            holder.categoryicon.setVisibility(View.VISIBLE);
            holder.cateogryText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.white));
            holder.cateogryRoot.setBackgroundResource(R.drawable.backrgound_category_selected);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0){
                    categoryInterface.addCategory();
                }else if (holder.getAdapterPosition() == 1){
                    categoryInterface.getAllCategory();
                    isSelect = -1;
                    notifyDataSetChanged();

                }else {
                    isSelect = holder.getAdapterPosition();
                    categoryInterface.selectCategory(new int[]{categorylist.get(holder.getAdapterPosition() - 2).getuId()});
                    notifyDataSetChanged();
                }
            }
        });

    }





    @Override
    public int getItemCount() {
        if (categorylist.size() > 0){
            return categorylist.size() + 2;
        }else {
            return categorylist.size() + 1;
        }

    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cateogryRoot;
        TextView cateogryText;
        ImageView categoryicon;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cateogryRoot = itemView.findViewById(R.id.category_root);
            cateogryText = itemView.findViewById(R.id.category_text);
            categoryicon = itemView.findViewById(R.id.ic_category);
        }
    }

    public void refreshItem(List<Category> list){
        categorylist = list;
        notifyDataSetChanged();

    }
}
