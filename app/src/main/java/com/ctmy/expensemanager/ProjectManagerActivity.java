package com.ctmy.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProjectManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectmanager);

        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User Logged Out");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeDisplayContent(){
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.projects);
        final ProjectAdapter projectAdapter = new ProjectAdapter();
        rvProjects.setAdapter(projectAdapter);
        LinearLayoutManager projectsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProjects.setLayoutManager(projectsLayoutManager);
    }

    @Override
    protected void onPause(){
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUtil.openFbReference("projects", this);
        initializeDisplayContent();
        FirebaseUtil.attachListener();
    }
}