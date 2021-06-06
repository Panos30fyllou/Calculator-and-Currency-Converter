package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends AppCompatActivity {

    TextView operationTextView;
    EditText inputEditText;

    int num1 = 0;
    int num2 = 0;
    String operator = "";
    int res = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationTextView = findViewById(R.id.textViewOperation);
        inputEditText = findViewById(R.id.editTextInput);
    }


    public void clearEverything(View view){
        clearInput(inputEditText);
        clearOperation(operationTextView);
    }

    public void clearInput(View view){
        inputEditText.setText("0");
        num1 = num2 = 0;
        operator = "";
        res = 0;
    }

    public void clearOperation(View view) {
        operationTextView.setText("");
    }

    public void numClicked(View view){
        Button button = (Button) view;
        String newNum = button.getText().toString();
        if(operationTextView.getText().toString().contains("="))
            clearEverything(view);
        inputEditText.setText(String.format("%s%s", inputEditText.getText().toString().equals("0") || (operator != "" && num2 == 0) ? "" : inputEditText.getText(), newNum));
        if(operator.equals(""))
            num1 = Integer.parseInt(inputEditText.getText().toString());
        else
            num2 = Integer.parseInt(inputEditText.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    public void operatorClicked(View view){
        Button button = (Button) view;
        if (operator.equals("")){
            num1 = Integer.parseInt(inputEditText.getText().toString());
            operator = button.getText().toString();
            operationTextView.setText(num1 + " " + operator);
        }else{
            //Toast.makeText(getApplicationContext(), String.valueOf(num2), Toast.LENGTH_SHORT).show();
            if(num2 != 0) {
                switch (operator){
                    case("+"):
                        res = num1 + num2;
                        break;
                    case("-"):
                        res = num1 - num2;
                        break;
                    case("*"):
                        res = num1 * num2;
                        break;
                    case("/"):
                        res = num1 / num2;
                        break;
                }
                operator = button.getText().toString();
                operationTextView.setText(res + " " + operator);
                num1 = res;
                num2 = 0;
                inputEditText.setText(String.valueOf(res));
            }else{
                operator = button.getText().toString();
                operationTextView.setText(num1 + " " + operator);
            }
        }
    }

    public void showRes(View view){
        if(num1 != 0 || num2 != 0) {
            //Toast.makeText(getApplicationContext(), num1 + ", " + num2, Toast.LENGTH_SHORT).show();
            switch (operator) {
                case ("+"):
                    res = num1 + num2;
                    break;
                case ("-"):
                    res = num1 - num2;
                    break;
                case ("*"):
                    res = num1 * num2;
                    break;
                case ("/"):
                    if (num2 != 0)
                        res = num1 / num2;
                    else
                        Toast.makeText(getApplicationContext(), "Can't divide by 0", Toast.LENGTH_SHORT).show();
                    break;
            }


            if (view.getId() == R.id.button_Equals && !operationTextView.getText().toString().contains("=")){
                if(num2 == 0)
                    res = num1;
                operationTextView.setText(String.format("%s %s =", operationTextView.getText().toString(), inputEditText.getText().toString()));
            }
        }
        inputEditText.setText("");
        inputEditText.setText(String.valueOf(res));
    }

    public void openConverter(View view){
        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), Converter.class);
        intent.putExtra("amount", inputEditText.getText().toString());
        startActivity(intent);
    }
}