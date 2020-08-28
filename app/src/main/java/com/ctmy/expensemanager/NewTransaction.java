package com.ctmy.expensemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

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

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get all data needed to save the transaction
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PROJECT_NAME, 0);
        mCurrentProjectId = sharedPreferences.getString("project_id","");

        /* Get references to views from activity*/
        tvTransDate = (TextView) findViewById(R.id.tv_date);
        etAmount = (EditText) findViewById(R.id.etAmount);
        atvDescription = (MultiAutoCompleteTextView) findViewById(R.id.atvDescription);

        FirebaseUtil.openFbReference("transactions", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        FirebaseAuth firebaseAuth = FirebaseUtil.mFirebaseAuth;
        mCurrentUserName = firebaseAuth.getCurrentUser().getDisplayName();

        tvTransDate = (TextView) findViewById(R.id.tv_date);
        btnSave = (Button) findViewById(R.id.btn_ok);

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
                saveTransaction();
            }
        });
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
        Transaction transaction = new Transaction(id, date, amount, description);
        mDatabaseReference.child(mCurrentProjectId + "/" + id).setValue(transaction);
        //TODO: complete mehtod

    }
}