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

public class ProjectTransaction extends AppCompatActivity {

    private Project project;
    TextView addTransaction;

    String mProjectTitle="Test";
    final String PROJECT_NAME = "com.ctmy.expensemanager.PROJECT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_transactions);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

       /* if(savedInstanceState != null){
            mProjectTitle = savedInstanceState.getString(PROJECT_NAME);
            Log.d("Null", "null object");
        }*/

        if(savedInstanceState == null){
            Log.d("Null","savedinstance");
        }
        addTransaction = (TextView) findViewById(R.id.btAdd);
        addTransaction.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent transIntent = new Intent(ProjectTransaction.this, NewTransaction.class);
                                                  startActivity(transIntent);
                                              }
                                          }

        );

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("Project");

        if(project == null){
            project = new Project();
        }else{
            mProjectTitle = project.getProjectName();
        }

        this.project = project;
        setTitle(getResources().getText(R.string.proj_trans_acitivity) + " " + mProjectTitle);

        initializeDisplayTransactions();
    }

    private void initializeDisplayTransactions(){
        RecyclerView rvTransactions = (RecyclerView)findViewById(R.id.Transactions);
        final TransactionAdapter transactionAdapter = new TransactionAdapter();
        rvTransactions.setAdapter(transactionAdapter);
        LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTransactions.setLayoutManager(transactionsLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PROJECT_NAME, project.getProjectName());
    }
    

}