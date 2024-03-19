package com.example.notepad.dp;

import android.widget.WrapperListAdapter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notepad.domain.model.Category;
import com.example.notepad.domain.model.Notes;

@Database(entities = {Category.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract  CategoryDao categoryDao();

}
