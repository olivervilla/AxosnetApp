package com.example.axosnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet.api.ReceiptApiService;
import com.example.axosnet.models.Receipt;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiptActivity extends AppCompatActivity {

    private TextView vtProvider, vtAmount, vtCode, vtDate, vtComments;
    private Button btnDelete, btnEdit;
    private Receipt receipt;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        receipt = intent.getParcelableExtra("selected_receipt");

        fillLayout();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to delete this receipt?");
                builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReceiptApiService service = retrofit.create(ReceiptApiService.class);
                        Call<ResponseBody> responseCall = service.deleteReceipt(receipt.getId());
                        responseCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                showToastError();
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.dialog_no, null).show();
            }
        });

        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceiptActivity.this, CreateReceipt.class);
                intent.putExtra("selected_receipt", receipt);
                finish();
                startActivity(intent);
            }
        });
    }

    private void fillLayout() {
        vtProvider = findViewById(R.id.tv_provider);
        vtProvider.setText(receipt.getProvider());

        vtAmount = findViewById(R.id.tv_amount);
        vtAmount.setText(String.valueOf(receipt.getAmount()));

        vtCode = findViewById(R.id.tv_currency_code);
        vtCode.setText(receipt.getCurrency_code());

        vtDate = findViewById(R.id.tv_date);
        vtDate.setText(receipt.getEmission_date());

        vtComments = findViewById(R.id.tv_comments);
        vtComments.setText(receipt.getComment());
    }

    private void showToastError() {
        Toast.makeText(this, "Something went wrong, try again in few seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}