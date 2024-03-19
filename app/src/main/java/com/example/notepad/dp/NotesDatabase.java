package com.example.notepad.dp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notepad.domain.model.Notes;
@Database(entities = {Notes.class}, version = 1)

public abstract class NotesDatabase extends RoomDatabase {

    public abstract  Notedao notedao();
}
