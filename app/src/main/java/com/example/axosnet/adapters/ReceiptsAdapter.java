package com.example.axosnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.axosnet.R;
import com.example.axosnet.models.Receipt;

import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder> {
    private List<Receipt> data;
    private LayoutInflater inflater;
    private Context context;
    private OnClickItemListener mOnClickItemListener;

    public ReceiptsAdapter(List<Receipt> data, OnClickItemListener onClickItemListener, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.mOnClickItemListener = onClickItemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceiptsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.receipt, parent, false);
        return new ViewHolder(view, mOnClickItemListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vtProvider, vtValue, vtCode, vtDate;
        OnClickItemListener onClickItemListener;

        public ViewHolder(@NonNull View itemView, OnClickItemListener onClickItemListener) {
            super(itemView);
            vtProvider = itemView.findViewById(R.id.provider);
            vtValue = itemView.findViewById(R.id.amount);
            vtCode = itemView.findViewById(R.id.currency_code);
            vtDate = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
            this.onClickItemListener = onClickItemListener;
        }

        void bindData(final Receipt item) {
            vtProvider.setText(item.getProvider());
            vtValue.setText(String.valueOf(item.getAmount()));
            vtDate.setText(item.getEmission_date());
            vtCode.setText(item.getCurrency_code());
        }

        @Override
        public void onClick(View view) {
            onClickItemListener.onClickItem(getAdapterPosition());
        }
    }

    public interface OnClickItemListener {
        void onClickItem(int position);
    }
}