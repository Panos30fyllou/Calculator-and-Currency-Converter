package com.example.calculator;

import android.app.Dialog;
import android.view.View;

public class DropdownOnClickListener implements View.OnClickListener {

    public DropdownOnClickListener(Dialog toDialog){
        toDialog.setContentView(R.layout.dropdown);

    }

    @Override
    public void onClick(View v) {

    }
}
