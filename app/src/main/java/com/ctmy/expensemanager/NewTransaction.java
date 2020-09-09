package com.ctmy.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.ctmy.expensemanager.FirebaseUtil.PROJECT_NAME;

public class NewTransaction extends AppCompatActivity {
    TextView tvTransDate;
    EditText etAmount;
    MultiAutoCompleteTextView atvDescription;
    Button btnSave;

    private String mReferenceFirebase = "transactions";
    private String mCurrentProjectId;
    private String mCurrentUserName;
    private String mCurrentDate;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Project mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mProject = (Project) intent.getSerializableExtra("NewTransaction");

        // get all data needed to save the transaction
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
        mCurrentProjectId = sharedPreferences.getString("project_id","");

        /* Get references to views from activity*/
        tvTransDate = (TextView) findViewById(R.id.tv_date);
        etAmount = (EditText) findViewById(R.id.etAmount);
        atvDescription = (MultiAutoCompleteTextView) findViewById(R.id.atvDescription);
        btnSave = (Button) findViewById(R.id.btn_ok);

        FirebaseUtil.openFbReference("transactions", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        FirebaseAuth firebaseAuth = FirebaseUtil.mFirebaseAuth;
        mCurrentUserName = firebaseAuth.getCurrentUser().getDisplayName();

        // Set date view to current date at start up
        Calendar date = Calendar.getInstance();
        mCurrentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date.getTime());
        tvTransDate.setText(mCurrentDate);

        tvTransDate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   showDatePickerDiaglog(v);
               }
           }
        );

        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                
                try{
                    saveTransaction();
                    Toast.makeText(NewTransaction.this, "Transaction saved", Toast.LENGTH_LONG).show();
                    cleanFields();
                }catch (Exception e){
                    Toast.makeText(NewTransaction.this, "Error saving transaction", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cleanFields() {
    }

    private void showDatePickerDiaglog(View v){
        DatePickerFragment ddf = new DatePickerFragment();

        ddf.setDateDialogFragmentListener(new DateDialogFragmentListener(){

            @Override
            public void dateDialogFragmentDateSet(Calendar date) {
                TextView tv = (TextView) findViewById(R.id.tv_date);
                String stringOfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date.getTime());
                tv.setText(stringOfDate);

            }
        });

        ddf.show(getSupportFragmentManager(), "datePickerTrans");
    }

    private void saveTransaction(){
        String id = mDatabaseReference.push().getKey();
        String date = tvTransDate.getText().toString();
        Double amount = Double.valueOf(etAmount.getText().toString());
        String description = atvDescription.getText().toString();
        Transaction transaction = new Transaction(id, date, amount, description, mCurrentUserName);
        mDatabaseReference.child(mCurrentProjectId + "/" + id).setValue(transaction);
    }
}