package com.example.notepad.adapter;

import com.example.notepad.domain.model.Notes;

import java.util.List;

public interface NoteInterface {
    void onLongClick(Notes notes, int position);

    void onLongClickMore(List<Notes> list);
}
