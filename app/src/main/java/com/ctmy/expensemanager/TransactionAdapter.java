package com.ctmy.expensemanager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TransactionViewHolder(@NonNull View itemView){
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
