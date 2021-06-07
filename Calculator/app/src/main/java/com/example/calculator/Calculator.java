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

public class Calculator extends AppCompatActivity {

    TextView operationTextView;     //The TextView where the operation that will be executed is shown
    EditText inputEditText;         //The EditText where the userInput is created

    int num1 = 0;                   //The first number of the operation
    int num2 = 0;                   //The second number of the operation
    String operator = "";           //The operator
    int res = 0;                    //The result of the operation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        operationTextView = findViewById(R.id.operationTextView);
        inputEditText = findViewById(R.id.inputEditText);
    }


    //Clears both Input and Operation views
    public void clearEverything(View view){
        clearInput(inputEditText);
        clearOperation(operationTextView);
    }

    //Clears Input EditText, resets the operator to "" and sets num1, num2 and res to 0
    public void clearInput(View view){
        inputEditText.setText("0");
        num1 = num2 = 0;
        operator = "";
        res = 0;
    }

    //Clears Operation EditText
    public void clearOperation(View view) {
        operationTextView.setText("");
    }

    //Is called when a button that contains a number is clicked
    public void numClicked(View view){
        Button buttonClicked = (Button) view;                       //The button that was clicked (view) is casted as a Button named buttonClicked
        String numOfButton = buttonClicked.getText().toString();    //The number that is written on the button is converted to string and stored to the String numOfButton
        if(operationTextView.getText().toString().contains("="))    //If the operation already contains '=', this means that a new operation is being written. Therefore, the input EditText is being cleared
            clearEverything(view);
        //Input EditText is being updated. If input is 0 (has no value yet), then the EditText must be cleared, and then the numOfButton is written.
        // Example if input is 0, the new input is not 05 but 5.
        // Also, if an operator exists in the operation, and the num2 is 0 (has no value yet), that means that the num2 is now being typed.
        // So the EditText must be cleared, and then the numOfButton follows.
        // If these are false, then the input remains the same, and numOfButton follows.
        inputEditText.setText(String.format("%s%s", inputEditText.getText().toString().equals("0") || (!operator.equals("") && num2 == 0) ? "" : inputEditText.getText(), numOfButton));
        if(operator.equals(""))//If there is not an operator in the operation, then the num1 must be updated. Else, the num2 must be updated
            num1 = Integer.parseInt(inputEditText.getText().toString());
        else
            num2 = Integer.parseInt(inputEditText.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    public void operatorClicked(View view){
        Button buttonClicked = (Button) view;  //The button that was clicked (view) is casted as a Button named buttonClicked
        if (operator.equals("")){       //If there is not an operator in the operation
            num1 = Integer.parseInt(inputEditText.getText().toString());    //num1 is updated
            operator = buttonClicked.getText().toString();                  //operator gets the value of the buttonClicked Text, converted to String
            operationTextView.setText(num1 + " " + operator);               //Operation TextView is updated
        }else{                          //If an operator already existed in the operation, that means that this operation must be executed first, and then put the new operator next to the result
            if(num2 != 0) {             //If the num2 is not 0 (has value), the operation is executed and the result is stored in res
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
                operator = buttonClicked.getText().toString();      //The operator is updated with the one that is written on the buttonClicked
                operationTextView.setText(res + " " + operator);    //The operation TextView is updated with the result (res) of the operation executed, with the new operator next to it
                num1 = res;                                         //Now num1 is equal to the result of the operation
                num2 = 0;                                           //and num2 is reset to 0
                inputEditText.setText(String.valueOf(res));         //Input EditText value is set equal to res
            }else{                                                  //If num2 is 0 (has no value yet)
                operator = buttonClicked.getText().toString();      //Operator is updated with the operator that is written on the buttonClicked
                operationTextView.setText(num1 + " " + operator);   //Operation TextView is updated with num1 followed by the operator
            }
        }
    }

    //Calculates and prints the result to the InputEditText
    public void showRes(View view){
        if(num1 != 0 || num2 != 0) {        //If one of num1 or num2 has a value, the operation is executed
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
                    if (num2 != 0)          //If num2 is 0, the division can't be done, so a message is printed
                        res = num1 / num2;
                    else
                        Toast.makeText(getApplicationContext(), "Can't divide by 0", Toast.LENGTH_SHORT).show();
                    break;
            }

            if (view.getId() == R.id.button_Equals && !operationTextView.getText().toString().contains("=")){ //If '=' was clicked and the operation doesn't already contain '='
                if(num2 == 0)               //If num2 has no value yet, set res as num1
                    res = num1;
                operationTextView.setText(String.format("%s %s =", operationTextView.getText().toString(), inputEditText.getText().toString()));    //Update operationTextView with the input
            }
        }
        inputEditText.setText("");                      //Reset Input EditText
        inputEditText.setText(String.valueOf(res));     //Set Input EditText value equal to res
    }

    //Opens Converter Activity
    public void openConverter(View view){
        finishAffinity();                                                       //Closes open Activities
        Intent intent = new Intent(getApplicationContext(), Converter.class);   //Creates new Intent
        intent.putExtra("amount", inputEditText.getText().toString());    //Passes the value in the input EditText to the new Activity
        startActivity(intent);                                                  //Starts new Activity
    }
}