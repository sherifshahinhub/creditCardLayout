package com.example.mohammed.sherif.my_application_2;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView txt;
    ImageView lockImg;
    EditText cardNumber;
    EditText expirationDate;
    EditText code ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.save_info_text_id);
        lockImg = (ImageView) findViewById(R.id.save_info_img_id);
        btn = (Button) findViewById(R.id.button_id);
        cardNumber = (EditText) findViewById(R.id.card_number_id);
        expirationDate = (EditText) findViewById(R.id.expiration_date_id);
        code = (EditText) findViewById(R.id.security_code_id);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vaidateData()){
                    txt.setVisibility(View.VISIBLE);
                    lockImg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean vaidateData(){
        boolean validateCardData =  validateCreditcardData(cardNumber.getText().toString());
        boolean validateExpirationDate = validateExpirationDate(expirationDate.getText().toString());
        boolean validateSecurityCode = validateSecurityCode(code.getText().toString());
        if (validateCardData && validateExpirationDate && validateSecurityCode){
            String cardCompany = cardCompany(cardNumber.getText().toString());
            showDialog("Confermation message", "Your " + cardCompany +" Card info is saved securely");
            return true;
        }
        return false;
    }
    private String cardCompany(String cardNumber){
        if(cardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")){
            return "VISA";
        } else if(cardNumber.matches("^5[1-5][0-9]{14}$")){
            return "MASTERCARD";
        } else if(cardNumber.matches("^3[47][0-9]{13}$")){
            return "AMEX";
        } else if(cardNumber.matches("^3(?:0[0-5]|[68][0-9])[0-9]{11}$")){
            return "DINERS";
        } else if(cardNumber.matches("^6(?:011|5[0-9]{2})[0-9]{12}$")){
            return "DISCOVER";
        } else if(cardNumber.matches("^(?:2131|1800|35\\d{3})\\d{11}$")){
            return "JCB";
        } else {
            return "";
        }
    }

    protected void showDialog(String title, String messageContent){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(title);

        builder.setMessage(messageContent)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: handle the OK
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validateSecurityCode(String securityCode){
        if (securityCode != null && securityCode.matches("[0-9]{3}") || securityCode.matches("[0-9]{4}") || securityCode.matches("[0-9]{5}")) {
            return true;
        }
        showDialog("Error message", "Invalid security code");
        return false;
    }
    private boolean validateCreditcardData(String creditNumber){
        //System.out.println(creditNumber);
        if(creditNumber != null && luhnCheck(creditNumber)){
            return true;
        } else {
            showDialog("Error message", "Invalid Card number");
            return false;
        }
    }

    private boolean validateExpirationDate(String date){
        if (date != null && date.matches("([0-1][0-9])/([0-3][0-9])")) {
            return true;
        }
        showDialog("Error message", "Invalid expiration date");
        return false;
    }

    /**
     * Checks if the card is valid
     *
     * @param card
     *           {@link String} card number
     * @return result {@link boolean} true of false
     */
    public boolean luhnCheck(String card) {
        if (card == null)
            return false;
        char checkDigit = card.charAt(card.length() - 1);
        String digit = calculateCheckDigit(card.substring(0, card.length() - 1));
        return checkDigit == digit.charAt(0);
    }
    /**
     * Calculates the last digits for the card number received as parameter
     *
     * @param card
     *           {@link String} number
     * @return {@link String} the check digit
     */
    public String calculateCheckDigit(String card) {
        if (card == null)
            return null;
        String digit;
        /* convert to array of int for simplicity */
        int[] digits = new int[card.length()];
        for (int i = 0; i < card.length(); i++) {
            digits[i] = Character.getNumericValue(card.charAt(i));
        }

        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2)	{
            digits[i] += digits[i];

            /* taking the sum of digits grater than 10 - simple trick by substract 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }
        /* multiply by 9 step */
        sum = sum * 9;

        /* convert to string to be easier to take the last digit */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }


}
