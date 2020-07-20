package com.ctmy.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProjectManagerActivity extends AppCompatActivity {

    ConstraintLayout clProjRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectmanager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clProjRow = (ConstraintLayout) findViewById(R.id.clProjCard);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProjIntent = new Intent(ProjectManagerActivity.this, newProject.class);
                startActivity(newProjIntent);
            }
        });

       /* clProjRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent projectIntent = new Intent(ProjectManagerActivity.this, project_transactions.class);
                startActivity(projectIntent);
            }
        });*/

        initializeDisplayContent();

    }

    private void initializeDisplayContent(){
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.projects);
        final ProjectAdapter projectAdapter = new ProjectAdapter();
        rvProjects.setAdapter(projectAdapter);
        LinearLayoutManager projectsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProjects.setLayoutManager(projectsLayoutManager);
    }
}