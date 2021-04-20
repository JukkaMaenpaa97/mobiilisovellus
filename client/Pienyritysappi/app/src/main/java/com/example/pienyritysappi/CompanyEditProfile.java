package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

public class CompanyEditProfile extends AppCompatActivity {

     private static final Pattern PUHELIN_PATTERN =
            Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");

     private EditText textInputCompanyName;
     private EditText textInputEmail;
     private EditText textInputPhone;
     private EditText textInputAddress;
     private EditText textInputPostalCode;
     private EditText textInputCity;
     private EditText textInputPassword;
     private EditText textInputPasswordConfirm;
     private EditText textInputCompanyId;
     private EditText textInputCompanyDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);

        textInputAddress = findViewById(R.id.companyAddressEditText);
        textInputCity = findViewById(R.id.companyCityEditText);
        textInputCompanyDescription = findViewById(R.id.companyDescriptionEditText);
        textInputCompanyId = findViewById(R.id.companyIdEditText);
        textInputCompanyName = findViewById(R.id.companyNameEditText);
        textInputEmail = findViewById(R.id.companyEmailEditText);
        textInputPassword = findViewById(R.id.companyPasswordEditText);
        textInputPasswordConfirm = findViewById(R.id.companyPasswordConfirmEditText);
        textInputPhone = findViewById(R.id.companyPhoneEditText);
        textInputPostalCode = findViewById(R.id.companyPostalCodeEditText);


        textInputAddress.setEnabled(false);
        textInputCity.setEnabled(false);
        textInputCompanyDescription.setEnabled(false);
        textInputCompanyId.setEnabled(false);
        textInputCompanyName.setEnabled(false);
        textInputPassword.setEnabled(false);
        textInputPasswordConfirm.setEnabled(false);
        textInputPostalCode.setEnabled(false);
        textInputPhone.setEnabled(false);
        textInputEmail.setEnabled(false);

    }

    public void profileButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void homeButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private boolean checkAddress()
    {
        String companyAddress  = textInputAddress.getText().toString().trim();

        if(companyAddress.isEmpty())
        {
            textInputAddress.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else
        {
            textInputAddress.setError(null);
            return true;
        }
    }

    private boolean checkPostalCode()
    {
        String postalCode;
        postalCode = textInputPostalCode.getText().toString().trim();

        if(postalCode.isEmpty())
        {
            textInputPostalCode.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if(postalCode.length() > 5 || postalCode.length() < 5)
        {
            textInputPostalCode.setError("Postinumeron pitää olla viisi numeroa pitkä");
            return false;
        }

        try
        {
            Integer.parseInt(postalCode);
            textInputPostalCode.setError(null);
            return true;
        }
        catch (NumberFormatException e)
        {
            System.out.println("String ei ole numero");
            textInputPostalCode.setError("Postinumerossa ei voi olla kirjaimia");

        }
        return false;
    }

    private boolean checkCity()
    {
        String companyCity = textInputCity.getText().toString().trim();

        if(companyCity.isEmpty())
        {
            textInputCity.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        try
        {
            Integer.parseInt(companyCity);
            textInputPostalCode.setError("Kaupungin nimessä ei voi olla numeroita");
            return false;
        }

        // TAA EI TOIMI VIELA
        catch (NumberFormatException e)
        {
            System.out.println("Stringissä ei ole numeroita");
            textInputPostalCode.setError(null);

        }
        return true;
    }

    private boolean checkPhoneNumber()
    {
        String companyPhone = textInputPhone.getText().toString().trim();

        if(companyPhone.isEmpty()) {
            textInputPhone.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!PUHELIN_PATTERN.matcher(companyPhone).matches())
        {
            textInputPhone.setError("Puhelinnumero ei kelpaa");
            return false;
        }
        else
        {
            textInputPhone.setError(null);
            return true;
        }

    }

    private boolean checkCompanyName()
    {
        String companyNameInput = textInputCompanyName.getText().toString().trim();

        if(companyNameInput.isEmpty())
        {
            textInputCompanyName.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else
        {
            textInputCompanyName.setError(null);
            return true;
        }
    }

    private boolean checkEmail()
    {
        String emailInput = textInputEmail.getText().toString().trim();

        if(emailInput.isEmpty())
        {
            textInputEmail.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            textInputEmail.setError("Sähköposti ei ole pätevä");
            return false;
        }
        else
        {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean checkPassword()
    {
        String password = textInputPassword.getText().toString().trim();
        String passwordConfirm = textInputPasswordConfirm.getText().toString().trim();

        if(password.isEmpty())
        {
            textInputPassword.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!password.equals(passwordConfirm))
        {
            textInputPassword.setError("Salasanat eivat täsmää");
            return false;
        }
        else
        {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void editInformationButtonClicked(View view)
    {
        textInputAddress.setEnabled(true);
        textInputCity.setEnabled(true);
        textInputCompanyDescription.setEnabled(true);
        textInputCompanyId.setEnabled(true);
        textInputCompanyName.setEnabled(true);
        textInputPassword.setEnabled(true);
        textInputPasswordConfirm.setEnabled(true);
        textInputPostalCode.setEnabled(true);
        textInputPhone.setEnabled(true);
        textInputEmail.setEnabled(true);
    }

    public void saveInformationButtonClicked(View view)
    {
        if(!checkPassword() | !checkEmail() | !checkCompanyName() | !checkAddress() | !checkCity() | !checkPostalCode() | !checkPhoneNumber())
        {
            return;
        }

        textInputAddress.setEnabled(false);
        textInputCity.setEnabled(false);
        textInputCompanyDescription.setEnabled(false);
        textInputCompanyId.setEnabled(false);
        textInputCompanyName.setEnabled(false);
        textInputPassword.setEnabled(false);
        textInputPasswordConfirm.setEnabled(false);
        textInputPostalCode.setEnabled(false);
        textInputPhone.setEnabled(false);
        textInputEmail.setEnabled(false);
    }
}