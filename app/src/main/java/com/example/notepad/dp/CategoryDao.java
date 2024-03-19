package com.example.notepad.dp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notepad.domain.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM CATEGORY")
    List<Category> getAll();
    @Query("SELECT * FROM Category WHERE uid IN (:categoryId)")
    List<Category> loadAllByIds(int[] categoryId);

    @Insert
    void insertAll(Category... categories);
    @Delete
    void delete(Category category);
}
