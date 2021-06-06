package com.example.calculator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchTextWatcher implements TextWatcher {

    ArrayAdapter<String> adapter;
    public SearchTextWatcher(ArrayAdapter<String> myAdapter){
        adapter = myAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int before, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int before, int count) {
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
