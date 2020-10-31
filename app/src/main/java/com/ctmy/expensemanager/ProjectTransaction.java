package com.ctmy.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.ctmy.expensemanager.FirebaseUtil.PROJECT_NAME;

public class ProjectTransaction extends AppCompatActivity  implements ValuesFromAdapter{

    private Project project;
    TextView addTransaction;
    TextView tvTotalExpenses;
    TextView tvTotalIncomes;

    String mProjectTitle="";
    String mProjectId="";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    // final String PROJECT_NAME = "com.ctmy.expensemanager.PROJECT_NAME";

    Double expenses = 0.0 ;
    Double incomes = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_transactions);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);
        tvTotalExpenses = (TextView) findViewById(R.id.tvTotalExpenses);
        tvTotalIncomes = (TextView) findViewById(R.id.tvTotalIncome);


        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("Project");

        addTransaction = (TextView) findViewById(R.id.btAdd);
        addTransaction.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent transIntent = new Intent(ProjectTransaction.this, NewTransaction.class);
                                                  /*if(project != null) {
                                                      transIntent.putExtra("NewTransaction", project);
                                                  }*/
                                                  startActivity(transIntent);
                                              }
                                          }

        );

        if(project == null){
            project = new Project();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
            mProjectTitle = sharedPreferences.getString("project_name", "");
            mProjectId = sharedPreferences.getString("project_id", "");
        }else{
            mProjectTitle = project.getProjectName();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("project_name", project.getProjectName());
            editor.putString("project_id", project.getProjectId());
            editor.commit();
        }

        initializeDisplayTransactions();
        setTitle(getResources().getText(R.string.proj_trans_acitivity) + " " + mProjectTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.project_menu, menu);
        return true;
    }

    private void initializeDisplayTransactions(){
        RecyclerView rvTransactions = (RecyclerView)findViewById(R.id.Transactions);
        final TransactionAdapter transactionAdapter = new TransactionAdapter(getApplicationContext(), this);
        rvTransactions.setAdapter(transactionAdapter);
        LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTransactions.setLayoutManager(transactionsLayoutManager);
    }

    @Override
    public void getTotalExpenses(HashMap<String, Double> totalAmount) {
        expenses = totalAmount.get("egresos");
        incomes =  totalAmount.get("ingresos");
        tvTotalExpenses.setText(Double.valueOf(expenses).toString());
        tvTotalIncomes.setText(Double.valueOf(incomes).toString());

        updateProjectTotals();
    }

    public void updateProjectTotals(){

        Log.d("value", tvTotalIncomes.getText().toString() + incomes);
        // Not working
        /* update total for main project */
        FirebaseUtil.openFbReference("projects", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            String projectId;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(project.getProjectId() != null) {
                    projectId = project.getProjectId();
                }else{
                    projectId = mProjectId;
                }
                mDatabaseReference.child(projectId).child("totalIncomes").setValue(incomes);
                mDatabaseReference.child(projectId).child("totalExpenses").setValue(expenses);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}