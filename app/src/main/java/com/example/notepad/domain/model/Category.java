package com.example.notepad.domain.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
  @PrimaryKey(autoGenerate = true)
     public int uId;
  @ColumnInfo(name = "category_name")
  public String category_Name;



    public Category(String category_Name) {

        this.category_Name = category_Name;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getCategory_Name() {
        return category_Name;
    }

    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }
}
