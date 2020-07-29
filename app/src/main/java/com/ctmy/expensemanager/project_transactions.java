package com.ctmy.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class project_transactions extends AppCompatActivity {

    private Project project;
    TextView addTransaction;

    String mProjectTitle;
    final String PROJECT_NAME = "project_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_transactions);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

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
        Project project = (Project) intent.getSerializableExtra("Project");

        if(project == null){
            project = new Project();
        }

        this.project = project;
        setTitle(getResources().getText(R.string.proj_trans_acitivity) + " " + project.getProjectName());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(PROJECT_NAME, mProjectTitle);
        savedInstanceState.putSerializable("project", project);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        project = (Project) savedInstanceState.getSerializable("project");
    }

}