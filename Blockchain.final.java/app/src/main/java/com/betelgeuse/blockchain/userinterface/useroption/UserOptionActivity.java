package com.betelgeuse.blockchain.userinterface.useroption;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.betelgeuse.blockchain.H;
import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.core.indicator.Period;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListener;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;
import com.betelgeuse.blockchain.userinterface.information.InformationActivity;
import com.betelgeuse.blockchain.userinterface.model.CurrencyModel;
import com.betelgeuse.blockchain.userinterface.model.InformationToUserSettingModel;
import com.betelgeuse.blockchain.userinterface.model.UserOptionModel;
import com.betelgeuse.blockchain.userinterface.model.modelListener.CurrencyModelListListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);
        /*--------------------------------------------------------------------*/
        Switch vibration = findViewById(R.id.vibration);
        Switch notification = findViewById(R.id.notification);
        RadioGroup period = findViewById(R.id.period);
        RadioGroup history = findViewById(R.id.history);
        Spinner fromCurrencies = findViewById(R.id.fromCurrencies);
        Spinner toCurrencies = findViewById(R.id.toCurrencies);
        Button save = findViewById(R.id.save);
        CurrencySpinnerAdapter fromCurrenciesAdapter = new CurrencySpinnerAdapter(this, null);
        CurrencySpinnerAdapter toCurrenciesAdapter = new CurrencySpinnerAdapter(this, null);
        UserOptionController userOptionController = new UserOptionController(UserOptionActivity.this);
        /*--------------------------------------------------------------------*/

        userOptionController.readCurrencies(new CurrencyModelListListener() {
            @Override
            public void onSuccess(List<CurrencyModel> currencyModels) {
                fromCurrenciesAdapter.setData(currencyModels);
                toCurrenciesAdapter.setData(currencyModels);
                fromCurrencies.setAdapter(fromCurrenciesAdapter);
                toCurrencies.setAdapter(toCurrenciesAdapter);

                String informationToUserOptionModel = getIntent().getStringExtra("informationToUserOptionModel");
                if (informationToUserOptionModel ==null) {
                    fromCurrencies.setSelected(true);
                    toCurrencies.setSelected(true);
                }
                if (informationToUserOptionModel != null) {
                    InformationToUserSettingModel model = new Gson().fromJson(informationToUserOptionModel, InformationToUserSettingModel.class);
                    userOptionController.readUserOption(model.email, model.fromCurrency, model.toCurrency, userOption -> {
                        H.debugLog(this.getClass().getSimpleName(), "readUserOption", userOption.id);
                        int indexFc =  getIndexOfSpinnerItem(fromCurrencies,userOption.fromCurrency);
                        fromCurrencies.setSelection(indexFc);  fromCurrencies.setSelected(false);
                        int indexTc =  getIndexOfSpinnerItem(toCurrencies,userOption.toCurrency);
                        toCurrencies.setSelection(indexTc); toCurrencies.setSelected(false);



                        for (RadioButton radioButton : getRadioButtons(history)
                        ) {
                            if (radioButton.getText().equals(String.valueOf(userOption.history))) {
                                radioButton.setChecked(true);
                            }
                        }
                        for (RadioButton radioButton : getRadioButtons(period)
                        ) {
                            if (radioButton.getText().equals(String.valueOf(userOption.period.getCode()))) {
                                radioButton.setChecked(true);
                            }
                        }

                        vibration.setChecked(userOption.vibration);
                        notification.setChecked(userOption.notification);
                    });
                }
            }
        });



        save.setOnClickListener((View saveButton) -> {
            CurrencyModel fromCurrency = (CurrencyModel) fromCurrencies.getSelectedItem();
            CurrencyModel toCurrency = (CurrencyModel) toCurrencies.getSelectedItem();
            RadioButton selectedHistory = findViewById(history.getCheckedRadioButtonId());
            RadioButton selectedPeriod = findViewById(period.getCheckedRadioButtonId());


            userOptionController.createUserOption(new UserOptionModel("kaymak__serkan35@hotmail.com", fromCurrency.code, toCurrency.code,
                    Period.get(Integer.parseInt((String) selectedPeriod.getText())),
                    Integer.parseInt((String) selectedHistory.getText()),
                    vibration.isChecked() ? true : false,
                    notification.isChecked() ? true : false
            ));

            goBackActivity();


        });
    }

    private void goBackActivity() {
        onBackPressed();
    }

    private <Activity> void changeActivity(Class<Activity> activity, boolean addBackStack, @Nullable String data) {
        Intent intent =
                new Intent(UserOptionActivity.this, activity);
        if (data != null) intent.putExtra("email", data);
        if (!addBackStack) {
            finish();
        }
        startActivity(intent);
    }

    private List<RadioButton> getRadioButtons(RadioGroup radioGroup) {
        int count = radioGroup.getChildCount();
        ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                radioButtons.add((RadioButton) o);
            }
        }
        H.debugLog(this.getClass().getSimpleName(), "getRadioButtons", String.valueOf(radioButtons.size()));
        return radioButtons;
    }

    private int getIndexOfSpinnerItem(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            CurrencyModel currencyModel = (CurrencyModel) spinner.getItemAtPosition(i);
            if (currencyModel.code.equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }



}