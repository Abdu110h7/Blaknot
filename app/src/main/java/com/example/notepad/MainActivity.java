package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.notepad.adapter.CategoryAdapter;
import com.example.notepad.adapter.CategoryInterface;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.adapter.NoteInterface;
import com.example.notepad.domain.model.Category;
import com.example.notepad.domain.model.Notes;
import com.example.notepad.dp.AppDatabase;
import com.example.notepad.dp.NotesDatabase;
import com.example.notepad.ui.WriteActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CategoryInterface, NoteInterface, TextWatcher {
    RecyclerView categoryRecyclerView, notesRecyclerView;
    List<Category> categoryList;
    CategoryAdapter categoryAdapter;
    DrawerLayout drawerLayout;
    ImageView buttenMenu;
    AppDatabase appDatabase;
    NotesDatabase notesDatabase;
    FloatingActionButton buttenAdd;
    NoteAdapter noteAdapter;
    List<Notes> list;
    RelativeLayout mainTopBar;
    LinearLayout deleteTopBar;
    ImageView deleteButten, cancelButten;
    EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        notesDatabase = Room.databaseBuilder(getApplicationContext(),
               NotesDatabase.class, "note-name").allowMainThreadQueries().build();

        searchEdit.addTextChangedListener(this);

         list = notesDatabase.notedao().getAll();
        noteAdapter = new NoteAdapter(list,this);
        categoryList = appDatabase.categoryDao().getAll();
        categoryAdapter = new CategoryAdapter(categoryList,this);
        categoryRecyclerView.setAdapter(categoryAdapter);
        notesRecyclerView .setAdapter(noteAdapter);
        buttenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        buttenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        categoryRecyclerView = findViewById(R.id.recyclerView);
        categoryList = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawerLayout);
        buttenMenu = findViewById(R.id.menu_button);
        buttenAdd = findViewById(R.id.buttenAdd);
        notesRecyclerView = findViewById(R.id.notes_RecyclerView);
        deleteTopBar = findViewById(R.id.deleteTopBar);
        mainTopBar = findViewById(R.id.relativeLayout);
        cancelButten = findViewById(R.id.butten_cancel);
        deleteButten = findViewById(R.id.butten_delete);
        searchEdit = findViewById(R.id.editText);


    }

    @Override
    public void addCategory() {
        Dialog dialog =new Dialog(this);
        dialog.setContentView(R.layout.add_category_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(DrawerLayout.LayoutParams.MATCH_PARENT,DrawerLayout.LayoutParams.WRAP_CONTENT);
        EditText categoryName = dialog.findViewById(R.id.categoryName);
        MaterialButton saveButton = dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryName.getText().toString();
                appDatabase.categoryDao().insertAll(new Category(name));
                categoryAdapter.refreshItem(appDatabase.categoryDao().getAll());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void selectCategory(int[] id) {
        list = notesDatabase.notedao().loadAllByCategory(id);
        noteAdapter.changeWithCategory(list);
    }

    @Override
    public void getAllCategory() {
        list = notesDatabase.notedao().getAll();
        noteAdapter.changeWithCategory(list);

    }

    @Override
    public void onLongClick(Notes notes, int position) {
        ShowDeleteTopBar();
        deleteButten .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesDatabase.notedao().delete(notes);
                noteAdapter.cancelLongClick();
                noteAdapter.refreshItem(notesDatabase.notedao().getAll(), position);
                ShowMainTopBar();
            }
        });

        cancelButten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMainTopBar();
                 noteAdapter.cancelLongClick();
            }
        });
    }

    @Override
    public void onLongClickMore(List<Notes> list) {
        ShowDeleteTopBar();
        deleteButten .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesDatabase.notedao().deleteSelect(list);
                noteAdapter.refreshItem(notesDatabase.notedao().getAll(),-1 );
                ShowMainTopBar();
            }
        });

    }

    public void ShowDeleteTopBar(){
        mainTopBar.setVisibility(View.GONE);
        deleteTopBar.setVisibility(View.VISIBLE);

    }
    public void ShowMainTopBar(){
        mainTopBar.setVisibility(View.VISIBLE);
        deleteTopBar.setVisibility(View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()){
            noteAdapter.refreshItem(notesDatabase.notedao().getAll(),-1);
        }else {
            String title = s.toString();
            List<Notes> searches = notesDatabase.notedao().loadBySearch(title);
            noteAdapter.refreshItem(searches,-1);

        }

    }
}