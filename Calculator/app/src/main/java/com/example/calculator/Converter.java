package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

    final String apiKey = "9b95bbd1942db69b88e3";       //API Key that is used to access the API and get the exchange rates
    //Array of various countries' currencies' codes
    private final ArrayList<String> countryCodes = new ArrayList<>(Arrays.asList("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BOV", "BRL", "BSD", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHE", "CHF", "CHW", "CLF", "CLP", "CNY", "COP", "COU", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MXV", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "SSP", "STD", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "USN", "USS", "UYI", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XBA", "XBB", "XBC", "XBD", "XCD", "XDR", "XFU", "XOF", "XPD", "XPF", "XPT", "XTS", "XXX", "YER", "ZAR", "ZMW"));

    private TextView convertFromTextView, convertToTextView, resultTextView, fromResTextView, toResTextView, amountResTextView, equalsTextView;    //The 2 first TextViews are the ones from where the user's selection is taken and sent to the API to calculate the amount to be printed in the third one.
    private String convertFromValue, convertToValue;                            //These Strings contain what the 2 above TextViews contain
    private EditText amountEditText;                                            //In this EditText, the default value is taken from the calculator in the previous activity. It can be changed by the user. The value will be converted to the currency chosen.
    private String result;                                                      //After the API's response, the result is calculated and stored in this string
    private ProgressBar progressBar;                                            //Progress bar that is visible while the user waits fot the API's response

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        String amountFromCalc = getIntent().getStringExtra("amount");       //The amount from the calculator is being retrieved and stored
        amountEditText = findViewById(R.id.amountEditText);
        amountEditText.setText(amountFromCalc);                                   //Then it is set as the default value in the amount EditText

        convertFromTextView = findViewById(R.id.fromDropDown);
        convertToTextView = findViewById(R.id.toDropDown);
        convertFromValue = convertFromTextView.getText().toString();                //convertFromValue and convertToValue get their values from their TextViews. The TextViews default values are EUR and USD
        convertToValue = convertToTextView.getText().toString();
        resultTextView = findViewById(R.id.resultTextView);
        fromResTextView = findViewById(R.id.fromResTextView);
        equalsTextView = findViewById(R.id.equalsTextView);
        toResTextView = findViewById(R.id.toResTextView);
        amountResTextView = findViewById(R.id.amountResTextView);
        progressBar = findViewById(R.id.progressBar);

    }

    //Is called when the convertFrom or convertTo TextView is clicked, to show the dropdown and allow the user to pick a different currency code
    public void showDropdown(View view){
        //A dialog window is shown
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dropdown);
        dialog.getWindow().setLayout(800, 1000);
        dialog.show();

        ListView currencyListView = dialog.findViewById(R.id.currencyListView); //This ListView will contain all the currency codes from the ArrayList countryCodes
        EditText searchEditText = dialog.findViewById(R.id.searchEditText);     //The user will type here to search a currency code and narrow the results

        //The adapter passes the ArrayList to the ListView with a specified layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryCodes);
        currencyListView.setAdapter(adapter);
        searchEditText.addTextChangedListener(new SearchTextWatcher(adapter));

        //textViewClicked will be either convertFromTextView or convertToTextView
        TextView textViewClicked = (TextView) view;
        currencyListView.setOnItemClickListener((parent, view1, position, id) -> {  //When an item from the list of currency codes is clicked
            if (textViewClicked.getId() == convertFromTextView.getId()) {           //If convertFromTextView opened the dialog, the user's code selection is set to the convertFromTextView
                convertFromTextView.setText(adapter.getItem(position));
                convertFromValue = convertFromTextView.getText().toString();
            }
            else if (textViewClicked.getId() == convertToTextView.getId()) {        //If convertToTextView opened the dialog, the user's code selection is set to the convertToTextView
                convertToTextView.setText(adapter.getItem(position));
                convertToValue = convertToTextView.getText().toString();
            }
            dialog.dismiss();                                                       //Dialog closes
        });
    }

    public void requestRatioFromAPI(String from, String to, Double amount){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="
                + from
                + "_"
                + to +
                "&compact=ultra&apiKey=" + apiKey;                                                  //Using this url, the API will be requested to return the exchange ratio for the the 'from', 'to' currencies
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {      //A StringRequest is created containing the url. On the API's response
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);                                              //A JSONObject is created, containing the whole response
                Double resultValue = Double.parseDouble(jsonObject.get(from + "_" + to).toString());//The ratio is extracted as an Object from the JSONObject, converted to String, and then parsed to Double, to be stored in the resultValue
            progressBar.setVisibility(View.INVISIBLE);                                              //Now that we have received and stored the response from the API, the progressBar can be dismissed
                //result = amount + " " + from + "\n = \n" + round(resultValue * amount,2) + " " + to;
                amountResTextView.setText(String.valueOf(amount));
                fromResTextView.setText(String.format(" %s", from));
                equalsTextView.setVisibility(View.VISIBLE);
                resultTextView.setText(String.valueOf(round(resultValue * amount, 2)));
                toResTextView.setText(String.format(" %s", to));
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
    }

    public static double round(double valueToRound, int places){
        BigDecimal bigDecimal = BigDecimal.valueOf(valueToRound);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void openCalculator(View view){
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), Calculator.class));
    }

    public void convertButtonClicked(View view){
        amountResTextView.setText("");
        fromResTextView.setText("");
        toResTextView.setText("");
        resultTextView.setText("");
        equalsTextView.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
        try{
            //Toast.makeText(getApplicationContext(), "Trying", Toast.LENGTH_SHORT).show();
            Double amount = Double.valueOf(amountEditText.getText().toString());
            //Toast.makeText(getApplicationContext(), "Double ok", Toast.LENGTH_SHORT).show();
            if(!convertFromValue.isEmpty() && !convertToValue.isEmpty()) {
                //Toast.makeText(getApplicationContext(), convertFromValue + ", " + convertToValue, Toast.LENGTH_SHORT).show();
                requestRatioFromAPI(convertFromValue, convertToValue, amount);
            }
            //Toast.makeText(getApplicationContext(), "Currency ok", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void swap(View view){
        String temp = convertFromTextView.getText().toString();
        convertFromTextView.setText(convertToTextView.getText().toString());
        convertToTextView.setText(temp);

        convertFromValue = convertFromTextView.getText().toString();
        convertToValue = convertToTextView.getText().toString();
    }
}