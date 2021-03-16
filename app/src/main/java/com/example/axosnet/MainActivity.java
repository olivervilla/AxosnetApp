package com.example.axosnet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.axosnet.adapters.ReceiptsAdapter;
import com.example.axosnet.api.ReceiptApiService;
import com.example.axosnet.models.Receipt;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ReceiptsAdapter.OnClickItemListener {

    private Retrofit retrofit;
    private List<Receipt> receipts;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getReceipts();

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateReceipt.class);
                startActivity(i);
            }
        });

        refreshLayout = findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReceipts();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getReceipts();
    }

    private void getReceipts() {
        ReceiptApiService service = retrofit.create(ReceiptApiService.class);
        Call<String> responseCall = service.getAllReceipts();
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Receipt>>() {
                        }.getType();
                        receipts = gson.fromJson(response.body(), listType);
                        createRecycler();
                    }
                } else {
                    Log.e("Error", "onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToastError();
                Log.e("Error", "onFailure " + t.getMessage());
            }
        });
    }

    private void createRecycler() {
        if (receipts != null) {
            ReceiptsAdapter adapter = new ReceiptsAdapter(receipts, this, this);
            RecyclerView recyclerView = findViewById(R.id.rv);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("Receipts Adapter", "receipts null");
        }
    }

    private void showToastError() {
        Toast.makeText(this, "Something went wrong, try again in few seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra("selected_receipt", receipts.get(position));
        startActivity(intent);
    }
}