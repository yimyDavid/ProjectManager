package com.ctmy.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private Double mTotalExpenses;
    private Double mTotalIncomes;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabaseTotal;
    private DatabaseReference mDatabaseReferenceTotal;
    private Project mProject;

    final String INCOMES = "ingresos";
    final String INCOME = "ingreso";

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
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.transaction_menu, menu);
        return true;
    }

    private void cleanFields() {
        etAmount.setText("");
        atvDescription.setText("");
        etAmount.requestFocus();
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
        Double amount = Double.parseDouble(etAmount.getText().toString());
        String description = atvDescription.getText().toString();
        Transaction transaction = new Transaction(id, date, amount, description, mCurrentUserName);
        mDatabaseReference.child(mCurrentProjectId + "/" + id).setValue(transaction);

        getTotalsProject(amount, description);
    }

    private void getTotalsProject(final Double amount, final String transactionType){
        FirebaseUtil.openFbReference("projects", null);
        mFirebaseDatabaseTotal = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReferenceTotal = FirebaseUtil.mDatabaseReference;

        mDatabaseReferenceTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.child(mCurrentProjectId).getValue(Project.class);
                mTotalExpenses = project.getTotalExpenses();
                mTotalIncomes = project.getTotalIncomes();

                updateProjectTotal(amount, transactionType);
                Log.d("Totals", "Exp:" + mTotalExpenses + " Inc:" + mTotalIncomes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProjectTotal(Double amount, String transactionType){
        if(transactionType.toLowerCase().equals(INCOMES) || transactionType.toLowerCase().equals(INCOME)){
            Double newTotalIncomes = mTotalIncomes + amount;
            mDatabaseReferenceTotal.child(mCurrentProjectId).child("totalIncomes").setValue(newTotalIncomes);
        }else{
            Double newTotalExpenses = mTotalExpenses + amount;
            mDatabaseReferenceTotal.child(mCurrentProjectId).child("totalExpenses").setValue(newTotalExpenses);
        }

    }
}