package com.ctmy.expensemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    ArrayList<Transaction> projTransactions;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferene;
    private ChildEventListener mChildListener;

    public TransactionAdapter(){
        FirebaseUtil.openFbReference("transactions/-MCAUjjie8CJ84dfLGfH", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReferene = FirebaseUtil.mDatabaseReference;
        projTransactions = new ArrayList<>();

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transaction transaction = snapshot.getValue(Transaction.class);
                Log.d("TRANS: ", transaction.getDescription() + transaction.getDate());
                //project.setProjectId(dataSnapshot.getKey());
                projTransactions.add(transaction);
                notifyItemInserted(projTransactions.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReferene.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.transaction_list, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = projTransactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return projTransactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvDescription;
        TextView tvDate;
        TextView tvAmount;

        public TransactionViewHolder(@NonNull View itemView){
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_trans_desc);
            tvDate = (TextView) itemView.findViewById(R.id.tv_trans_date);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_trans_amount);
        }

        @Override
        public void onClick(View v) {

        }

        public void bind(Transaction transaction) {
            tvDescription.setText(transaction.getDescription());
            tvDate.setText(transaction.getDate());
            tvAmount.setText(String.valueOf(transaction.getAmount()));
        }
    }
}
