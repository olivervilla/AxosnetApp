package com.example.axosnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet.api.ReceiptApiService;
import com.example.axosnet.models.Receipt;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateReceipt extends AppCompatActivity {

    private TextView tv_title;
    private TextInputEditText te_provider, te_amount;
    private EditText te_comments;
    private RadioGroup rg_code;
    private RadioButton selectedCode;
    private Button submit;
    private Retrofit retrofit;
    private Boolean isUpdateRequest = false;
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_receipt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_title = findViewById(R.id.tv_title);
        te_provider = findViewById(R.id.tie_provider);
        te_amount = findViewById(R.id.tie_amount);
        te_comments = findViewById(R.id.et_comments);
        rg_code = findViewById(R.id.rg_code);
        submit = findViewById(R.id.btn_submit);
        selectedCode = findViewById(R.id.rb_usd);
        selectedCode.setChecked(true);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Intent intent = getIntent();
        if (intent.hasExtra("selected_receipt")) {
            isUpdateRequest = true;
            receipt = intent.getParcelableExtra("selected_receipt");
            tv_title.setText(R.string.uptade_receipt);
            updateFields(receipt);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkProvider() | !checkAmount()) {
                    return;
                }
                Double amount = Double.valueOf(te_amount.getText().toString());
                String providerName = te_provider.getText().toString();
                String currencyCode = selectedCode.getText().toString();
                String comments = te_comments.getText().toString();
                comments = comments.length() > 0 ? comments : null;
                SimpleDateFormat dateFormat = new
                        SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
                String date = dateFormat.format(Calendar.getInstance().getTime());

                Log.d("TAG", "onClick: " + comments + "|");

                ReceiptApiService service = retrofit.create(ReceiptApiService.class);

                Call<ResponseBody> responseCall;
                if (isUpdateRequest) {
                    responseCall = service.updateReceipt(receipt.getId(), providerName, amount, comments, date, currencyCode);
                } else {
                    responseCall = service.insertReceipt(providerName, amount, comments, date, currencyCode);
                }

                responseCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToastError();
                        Log.e("Error", "onFailure " + t.getMessage());
                    }
                });
            }
        });

        rg_code.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedCode = findViewById(radioGroup.getCheckedRadioButtonId());
            }
        });

    }

    private void updateFields(Receipt receipt) {
        te_provider.setText(receipt.getProvider());

        te_amount.setText(String.valueOf(receipt.getAmount()));

        // Selected code
        ArrayList<RadioButton> listRB = new ArrayList<>();
        for (int i = 0; i < rg_code.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rg_code.getChildAt(i);
            if (rb.getText().toString().equals(receipt.getCurrency_code())) {
                selectedCode = rb;
                selectedCode.setChecked(true);
            }
        }

        te_comments.setText(receipt.getComment());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToastError() {
        Toast.makeText(this, "Something went wrong, try again in few seconds", Toast.LENGTH_SHORT).show();
    }

    private boolean checkProvider() {
        if (te_provider.getText().length() < 4) {
            te_provider.setError("Provider name must have at least 5 characters");
            return false;
        }
        return true;
    }

    private boolean checkAmount() {
        if (te_amount.getText().length() < 1) {
            te_amount.setError("This field can´t be empty");
            return false;
        }
        return true;
    }
}