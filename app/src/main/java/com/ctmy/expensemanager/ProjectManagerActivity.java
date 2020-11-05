package com.ctmy.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    RecyclerView rvProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectmanager);

        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

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
                                //Log.d("Logout", "User Logged Out");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();
                return true;
            case R.id.send_log:
                Toast.makeText(this, "Sending Log", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeDisplayContent(){
        rvProjects = (RecyclerView) findViewById(R.id.projects);
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
        /*Called here instead of onCreate because user has not logged in yet and the list recyclerV shows nothing*/
        initializeDisplayContent();
        FirebaseUtil.attachListener();

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProjIntent = new Intent(ProjectManagerActivity.this, newProject.class);
                startActivity(newProjIntent);
            }
        });
        rvProjects.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }
}