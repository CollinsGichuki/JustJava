package com.example.android.class30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //Holds the value of the amount of coffee
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void decrement(View view) {
        if (quantity == 1){
            Toast.makeText(getApplicationContext(), "You cannot have less than one coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    public void increment(View view) {
        if (quantity>100){
            Toast.makeText(getApplicationContext(), "You cannot have less than one coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }
    public void display(int number){
        TextView textView  = findViewById(R.id.tv);
        textView.setText(""+number);
    }
    //This method generates the order information
    public void submitOrder(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy 'at' HH:mm:ss");
        String date = sdf.format(new Date());

        EditText nameEditText = findViewById(R.id.name_et);
        String name = nameEditText.getText().toString();

        EditText emailEditText = findViewById(R.id.email_et);
        String email = emailEditText.getText().toString();

        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checked);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checked);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String orderDetails = createOrderSummary(name, price, quantity, hasWhippedCream, hasChocolate, date);

        if (email.equals("") || name.equals("")){
            Toast.makeText(MainActivity.this, "Input all the text fields", Toast.LENGTH_SHORT).show();
        }else {
            sendMail(name, email, orderDetails);
        }

    }

    private void sendMail(String name, String email, String ticketDetails) {
        BackgroundMail.newBuilder(this)
                .withUsername("The email adress")
                .withPassword("Password")
                .withMailto(email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Coffee order for "+ name)
                .withBody(""+ticketDetails)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
        //Clear the edit text fields
        final EditText nameEditText = findViewById(R.id.name_et);
        nameEditText.getText().clear();
        nameEditText.setFocusable(false);
        nameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                nameEditText.setFocusableInTouchMode(true);

                return false;
            }
        });
        final EditText emailEditText = findViewById(R.id.email_et);
        emailEditText.getText().clear();
        emailEditText.setFocusable(false);
        emailEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                emailEditText.setFocusableInTouchMode(true);

                return false;
            }
        });
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checked);
        whippedCreamCheckBox.setChecked(false);
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checked);
        chocolateCheckBox.setChecked(false);
        TextView tv = findViewById(R.id.tv);
        tv.setText("1 ");



      /*  Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //Makes sue only email apps should handle this intent
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for "+ name);
        intent.putExtra(Intent.EXTRA_TEXT, ticketDetails);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivity(intent);
        }*/
    }

    private String createOrderSummary(String name, int price, int quantity, boolean hasWhippedCream, boolean hasChocolate, String date) {
        String orderMessage = "AMO'S COFFEE" + "\n" + "Name: " + name;
        if(hasWhippedCream){
            orderMessage += "\n" + "With whipped cream";
        }
        if(hasChocolate){
            orderMessage += "\n" + "With chocolate";
        }
        orderMessage += "\n" + "Quantity: " + quantity;
        orderMessage += "\n" + "Total: " + price + "ksh";
        orderMessage += "\n" + date;
        orderMessage += "\n" + "Thank you for drinking at Amo's Coffee";

        return orderMessage;
    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int basePrice = 200;
        if (hasWhippedCream){
            basePrice += 50;
        }
        if (hasChocolate){
            basePrice = basePrice + 50;
        }
        return basePrice * quantity;
    }
}
