package com.ctmy.expensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class newProject extends AppCompatActivity {
    private ArrayList<ProjectType> mProjList;
    private TypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initList();

        Spinner spinnerProjType = findViewById(R.id.spn_proj_type);

        mAdapter = new TypeAdapter(this, mProjList);
        spinnerProjType.setAdapter(mAdapter);

        spinnerProjType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProjectType clickedItem = (ProjectType) parent.getItemAtPosition(position);
                String clickedProjType = clickedItem.getTypeName();
                Toast.makeText(newProject.this, clickedProjType + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        mProjList = new ArrayList<>();
        mProjList.add(new ProjectType("Cumpleanos", R.mipmap.ic_cake_white_24dp));
        mProjList.add(new ProjectType("Construccion", R.mipmap.ic_domain_white_24dp));

    }
}