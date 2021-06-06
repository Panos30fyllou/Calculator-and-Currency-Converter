package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;


public class Converter extends AppCompatActivity {

    final String apiKey = "9b95bbd1942db69b88e3";
    EditText convertFromEditText, convertToEditText;
    TextView resultTextView;
    String convertFromValue, convertToValue = "";
    EditText amountEditText;
    ArrayList<String> arrayList;
    Button convertButton;
    String result;
    String[] country = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHE", "CHF", "CHW", "CLF", "CLP", "CNY", "COP", "COU", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MXV", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "SSP", "STD", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "USN", "USS", "UYI", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XBA", "XBB", "XBC", "XBD", "XCD", "XDR", "XFU", "XOF", "XPD", "XPF", "XPT", "XTS", "XXX", "YER", "ZAR", "ZMW"};

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        String amountFromCalc = getIntent().getStringExtra("amount");

        convertFromEditText = findViewById(R.id.fromDropDown);
        convertToEditText = findViewById(R.id.toDropDown);
        convertFromValue = convertFromEditText.getText().toString();
        convertToValue = convertToEditText.getText().toString();
        convertButton = findViewById(R.id.convertButton);
        amountEditText = findViewById(R.id.amountEditText);
        amountEditText.setText(amountFromCalc);
        resultTextView = findViewById(R.id.resultTextView);

        progressBar = findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);

        arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(country));


    }

    public void countryClick(View view){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dropdown);
        dialog.getWindow().setLayout(500, 700);
        dialog.show();

        EditText editTextClicked = (EditText) view;

        EditText searchEditText = dialog.findViewById(R.id.searchEditText);
        ListView currencyListView = dialog.findViewById(R.id.currencyListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);


        currencyListView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new SearchTextWatcher(adapter));

        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (editTextClicked.getId() == convertFromEditText.getId()) {
                    convertFromEditText.setText(adapter.getItem(position));
                    convertFromValue = convertFromEditText.getText().toString();
                }
                else if (editTextClicked.getId() == convertToEditText.getId()) {
                    convertToEditText.setText(adapter.getItem(position));
                    convertToValue = convertToEditText.getText().toString();
                }
                dialog.dismiss();
            }
        });

    }

    public void convert(String from, String to, Double amount){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="
                + from
                + "_"
                + to +
                "&compact=ultra&apiKey=" + apiKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                //Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
                try {
                    jsonObject = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(), "Got response, " + jsonObject.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Result is ok", Toast.LENGTH_SHORT).show();
                    Double resultValue = Double.parseDouble(jsonObject.get(from + "_" + to).toString());
                    progressBar.setVisibility(View.INVISIBLE);
                    result = amount + " " + from + "\n = \n" + round(resultValue * amount,2) + " " + to;
                    resultTextView.setText(result);
                    //Toast.makeText(getApplicationContext(), "All good", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    public static double round(double valueToRound, int places){
        BigDecimal bigDecimal = BigDecimal.valueOf(valueToRound);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void openCalculator(View view){
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), CalculatorActivity.class));
    }

    public void convertButtonClicked(View view){
        resultTextView.setText("");
        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
        try{
            //Toast.makeText(getApplicationContext(), "Trying", Toast.LENGTH_SHORT).show();
            Double amount = Double.valueOf(amountEditText.getText().toString());
            //Toast.makeText(getApplicationContext(), "Double ok", Toast.LENGTH_SHORT).show();
            if(!convertFromValue.isEmpty() && !convertToValue.isEmpty()) {
                //Toast.makeText(getApplicationContext(), convertFromValue + ", " + convertToValue, Toast.LENGTH_SHORT).show();
                convert(convertFromValue, convertToValue, amount);
            }
            //Toast.makeText(getApplicationContext(), "Currency ok", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void swap(View view){
        String temp = convertFromEditText.getText().toString();
        convertFromEditText.setText(convertToEditText.getText().toString());
        convertToEditText.setText(temp);

        convertFromValue = convertFromEditText.getText().toString();
        convertToValue = convertToEditText.getText().toString();
    }
}