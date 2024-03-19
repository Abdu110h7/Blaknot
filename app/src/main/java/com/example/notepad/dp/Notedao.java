package com.example.notepad.dp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.domain.model.Category;
import com.example.notepad.domain.model.Notes;

import java.util.List;
@Dao

public interface Notedao {
    @Query("SELECT * FROM Notes")
    List<Notes> getAll();

    @Insert
    void insertAll(Notes... notes);

    @Query("SELECT * FROM Notes WHERE categoryId IN (:categoryId)")
    List<Notes> loadAllByCategory(int[] categoryId);

    @Query("SELECT * FROM Notes WHERE id IN (:id)")
    Notes loadNoteById(int id);
    @Query("SELECT * FROM Notes WHERE title LIKE '%' || :title || '%'")
    List<Notes> loadBySearch(String title);

   @Query("UPDATE Notes SET title = :title,time = :time,note = :note,categoryId =:categoryId WHERE id = :id")
    void updateNote(int id,String title,String time, String note, int categoryId);

    @Delete
    void delete(Notes notes);

    @Delete
    void deleteSelect(List<Notes> notes);
}
