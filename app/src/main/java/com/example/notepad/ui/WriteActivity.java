package com.example.notepad.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.MainActivity;
import com.example.notepad.R;
import com.example.notepad.domain.model.Category;
import com.example.notepad.domain.model.Notes;
import com.example.notepad.dp.AppDatabase;
import com.example.notepad.dp.NotesDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity {
    ImageView buttenSave;
    EditText edititle, editnote;
    NotesDatabase notesDatabase;
    AppDatabase appDatabase;
    AutoCompleteTextView categoryChoose;
    TextView txtTime;
    List<Category> list = new ArrayList<>();
    Notes notes;
    int categoryPosition = -1;
    int notesId = -1;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        notesId = getIntent().getIntExtra("notes_id", -1);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        notesDatabase = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "note-name").allowMainThreadQueries().build();
        if (notesId>=0){
            notes = notesDatabase.notedao().loadNoteById(notesId);
        }
        list = appDatabase.categoryDao().getAll();

        if (notes !=null){
            txtTime.setText(notes.getTime());
            edititle.setText(notes.getTitle());
            editnote.setText(notes.getNote());
            categoryChoose.setText(getCategoryName(notes.getCategoryId() ));
        }
        categoryChoose.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,getCategoryName(list)));
        txtTime.setText(getTimeNow());
        categoryChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryPosition = position;
            }
        });


        buttenSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edititle.getText().toString();
                String note = editnote.getText().toString();

                if (title.isEmpty() || note.isEmpty()){
                    Toast.makeText(WriteActivity.this, "Bo`sh maydonlarni to`ldiring", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categoryPosition == -1){
                    Toast.makeText(WriteActivity.this, "Category Tanlang", Toast.LENGTH_SHORT).show();
                    return;
                }
                Notes notes = new Notes(title,note,getTime(),list.get(categoryPosition).getuId());
                if (notesId >= 0) {
                    notesDatabase.notedao().updateNote(notesId,notes.getTitle(),notes.getTime(),notes.getNote(),list.get(categoryPosition).getuId());
                }else{
                    notesDatabase.notedao().insertAll(notes);
                }


                Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        buttenSave = findViewById(R.id.buttenSave);
        edititle = findViewById(R.id.editText2);
        editnote = findViewById(R.id.comment);
        categoryChoose = findViewById(R.id.categoryChoose);
        txtTime = findViewById(R.id.time);
        back = findViewById(R.id.back);
    }
    public String getCategoryName(int id){
        for ( int i = 0; i < list.size(); i++){
            if (list.get(i).uId == id){
                categoryPosition = i;
                return list.get(i).category_Name;
            }
        }
        return " ";

    }

    public String getTime(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
    }
    public String getTimeNow(){
        String currentDate = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
    }
    public List<String> getCategoryName(List<Category> list){
        List<String> categories = new ArrayList<>();
        for (Category category: list){
            categories.add(category.getCategory_Name());
        }
        return categories;
    }
}