package com.ctmy.expensemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newProject extends AppCompatActivity {
    private ArrayList<ProjectType> mProjectTypes;
    private TypeAdapter mAdapter;
    TextView tvSelectedDate;

    private static final String TAG = "DocSnippets";

    private FirebaseDatabase mFiredatabaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    // List of project types
    ArrayList mbadprojectype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvSelectedDate = findViewById(R.id.tv_due_date);
        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initList();

        getProjectTypeList();
        Spinner spinnerProjType = findViewById(R.id.spn_proj_type);

        mAdapter = new TypeAdapter(this, mProjectTypes);
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

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void initList() {
        mProjectTypes = new ArrayList<>();
        mProjectTypes.add(new ProjectType("Cumpleanos", R.mipmap.ic_cake_white_24dp));
        mProjectTypes.add(new ProjectType("Construccion", R.mipmap.ic_domain_white_24dp));

    }

    private void getProjectTypeList(){
        FirebaseUtil.openFbReference("project_type");
        mFiredatabaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //long res = snapshot.getChildrenCount();
               /* HashMap result;
                result = (HashMap) snapshot.getValue();*/
               mbadprojectype = new ArrayList();
               String val;
               for(DataSnapshot types: snapshot.getChildren()){
                   val = (String) types.getValue();
                   mbadprojectype.add(val);
                   Log.d(TAG, String.valueOf(val));
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void showDatePickerDiaglog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

}