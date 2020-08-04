package com.ctmy.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class project_transactions extends AppCompatActivity {

    private Project project;
    TextView addTransaction;

    String mProjectTitle="Test";
    final String PROJECT_NAME = "project_name";

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
                                                  Intent transIntent = new Intent(project_transactions.this, new_transaction.class);
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
            Log.d("project","project is null");
        }

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
        LinearLayoutManager transationsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTransactions.setLayoutManager(transationsLayoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(PROJECT_NAME, mProjectTitle);
        Log.d("Name", mProjectTitle);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        mProjectTitle = savedInstanceState.getString(PROJECT_NAME);
        Log.d("restore", "Entered Restored");
    }

}