package com.ctmy.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.HashMap;

import static com.ctmy.expensemanager.FirebaseUtil.PROJECT_NAME;

public class ProjectTransaction extends AppCompatActivity  implements ValuesFromAdapter{

    private Project project;
    TextView addTransaction;
    TextView tvTotalExpenses;
    TextView tvTotalIncomes;

    String mProjectTitle="";
    // final String PROJECT_NAME = "com.ctmy.expensemanager.PROJECT_NAME";

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
                                                  transIntent.putExtra("NewTransaction", project);
                                                  startActivity(transIntent);
                                              }
                                          }

        );

        if(project == null){
            project = new Project();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
            mProjectTitle = sharedPreferences.getString("project_name", "");
        }else{
            mProjectTitle = project.getProjectName();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("project_name", project.getProjectName());
            editor.putString("project_id", project.getProjectId());
            editor.commit();
        }

        setTitle(getResources().getText(R.string.proj_trans_acitivity) + " " + mProjectTitle);

        initializeDisplayTransactions();
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
        Double expenses = totalAmount.get("egresos");
        Double incomes =  totalAmount.get("ingresos");
        tvTotalExpenses.setText(Double.valueOf(expenses).toString());
        tvTotalIncomes.setText(Double.valueOf(incomes).toString());
    }
}