package com.ctmy.expensemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class newProject extends AppCompatActivity {
    private ArrayList<ProjectType> mProjectTypes;
    private TypeAdapter mAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    private Long mEpochTime;

    // view's fields
    EditText txtTitle;
    TextView tvDueDate;
    String spProjType;
    Long creationDate;
    Button btSaveProject;

    // List of project types
    ArrayList mbadprojectype;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUtil.openFbReference("projects", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle = (EditText) findViewById(R.id.tv_proj_name);
        tvDueDate = (TextView) findViewById(R.id.dueDate);
        btSaveProject = (Button) findViewById(R.id.btn_create_proj);

        // get the current epoch time
        mEpochTime = DateUtil.getEpochTimeStamp();
        tvDueDate.setText(DateUtil.epochToDateString(mEpochTime));

        initList();

        //getProjectTypeList(); /* will use hard coded values for now */
        Spinner spinnerProjType = findViewById(R.id.spn_proj_type);

        mAdapter = new TypeAdapter(this, mProjectTypes);
        spinnerProjType.setAdapter(mAdapter);

        spinnerProjType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProjectType clickedItem = (ProjectType) parent.getItemAtPosition(position);
                spProjType = clickedItem.getTypeName();
                //Toast.makeText(newProject.this, spProjType + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        btSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    saveProject();
                    Toast.makeText(newProject.this, "Project saved", Toast.LENGTH_LONG).show();
                    cleanFields();
                }catch (Exception e){
                    Toast.makeText(newProject.this, "Error saving project", Toast.LENGTH_LONG).show();
                }
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
        mProjectTypes.add(new ProjectType("Celebracion", R.mipmap.ic_cake_white_24dp));
        mProjectTypes.add(new ProjectType("Construccion", R.mipmap.ic_domain_white_24dp));
        mProjectTypes.add(new ProjectType("Convivencia", R.mipmap.ic_local_dining_white_24dp));

    }

    private void getProjectTypeList(){
        FirebaseUtil.openFbReference("project_type", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
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
                   //Log.d(TAG, String.valueOf(val));
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showDatePickerDialog(View v){

        DatePickerFragment ddf = new DatePickerFragment();

        ddf.setDateDialogFragmentListener(new DateDialogFragmentListener(){

            @Override
            public void dateDialogFragmentDateSet(Calendar date) {
                mEpochTime = date.getTimeInMillis();
                String newDate = DateUtil.epochToDateString(mEpochTime);
                TextView tvDate = (TextView) findViewById(R.id.dueDate);
                tvDate.setText(newDate);
            }
        });

        ddf.show(getSupportFragmentManager(), "datePickerNewProj");

    }

    private void saveProject(){
        String projectTitle = txtTitle.getText().toString();
        if(projectTitle.equals("") || projectTitle.isEmpty()){
            String projectId = mDatabaseReference.push().getKey();
            creationDate = DateUtil.getEpochTimeStamp();
            Project project = new Project(projectId, projectTitle, mEpochTime, creationDate, spProjType, 0.00, 0.00);
            mDatabaseReference.child(projectId).setValue(project);
        }else{
            Toast.makeText(newProject.this, R.string.reqProjectNameToast, Toast.LENGTH_LONG).show();
        }

    }

    private void cleanFields(){
        txtTitle.setText("");
        //tvDueDate.setText("");
    }

}