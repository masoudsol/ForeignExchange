package com.foreignex.modules.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.foreignex.modules.models.Currency;
import com.foreignex.modules.models.CurrencyModel;
import com.foreignex.R;
import com.foreignex.modules.viewmodels.ExchangeViewModel;

public class ExchangeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ExchangeViewModel exchangeViewModel;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.edittext);
        final TextView textView = findViewById(R.id.textview);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0) {
                    textView.setText(exchangeViewModel.conversion(spinner.getSelectedItemPosition(),String.valueOf(charSequence)));
                } else {
                    textView.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String input = editText.getText().toString();
                ((TextView) view).setText(exchangeViewModel.getISOCode(i));

                if (!input.isEmpty()) {
                    textView.setText(exchangeViewModel.conversion(i,input));
                } else {
                    textView.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        exchangeViewModel = ViewModelProviders.of(this).get(ExchangeViewModel.class);
        exchangeViewModel.getCurrencyModelMutableLiveData().observe(this,new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> currencyModels) {
                adapter = new ArrayAdapter<String>(ExchangeActivity.this, R.layout.support_simple_spinner_dropdown_item, currencyModels);

                spinner.setAdapter(adapter);
            }
        });

        exchangeViewModel.getExcahngeRate();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        exchangeViewModel.getCurrencyModelMutableLiveData().removeObservers(this);
        spinner.setOnItemSelectedListener(null);
    }
}
