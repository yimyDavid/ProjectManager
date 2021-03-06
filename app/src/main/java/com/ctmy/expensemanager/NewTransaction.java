package com.ctmy.expensemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ctmy.expensemanager.FirebaseUtil.PROJECT_NAME;

public class NewTransaction extends AppCompatActivity {
    TextView tvTransDate;
    EditText etAmount;
    MultiAutoCompleteTextView atvDescription;
    Button btnSave;
    ImageView ivReceipt;
    ProgressBar pgvReceiptUpload;
    Boolean mIsImagelarge = false;

    private String mReferenceFirebase = "transactions";
    private String mCurrentProjectId;
    private String mCurrentUserName;
    private String mUrl = "";

    private Double mTotalExpenses;
    private Double mTotalIncomes;
    private Long mEpochTime;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabaseTotal;
    private DatabaseReference mDatabaseReferenceTotal;
    private Project mProject;
    private Transaction mTransaction;

    final String INCOMES = "ingresos";
    final String INCOME = "ingreso";
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int PICTURE_REQUEST = 11;
    String currentPhotoPath;
    Uri mPhotoURI;

    Logger log = LoggerFactory.getLogger(NewTransaction.class);

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        btnSave = (Button) findViewById(R.id.btn_ok);
        ivReceipt = (ImageView) findViewById(R.id.imgvReceipt);
        //ivReceipt.setTranslationZ(10);

        pgvReceiptUpload = (ProgressBar)findViewById(R.id.pbUploadReceipt);

        /* Get transaction object */
        Intent transIntent = getIntent();
        Transaction transaction = (Transaction) transIntent.getSerializableExtra("transaction");
        if(transaction == null){
            transaction = new Transaction();
            transaction.setDate(DateUtil.getEpochTimeStamp());
        }

        this.mTransaction = transaction;
        mEpochTime = mTransaction.getDate();
        log.info("onCrate " + mEpochTime);

        tvTransDate.setText(DateUtil.epochToDateString(mEpochTime));
        etAmount.setText(String.valueOf(transaction.getAmount()));
        atvDescription.setText(mTransaction.getDescription());
        showImage(mTransaction.getImageUrl());
        // set it to existing path so is not set to empty when editing
        mUrl = mTransaction.getImageUrl();

        FirebaseUtil.openFbReference("transactions", null);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        FirebaseAuth firebaseAuth = FirebaseUtil.mFirebaseAuth;
        mCurrentUserName = firebaseAuth.getCurrentUser().getDisplayName();

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

        ivReceipt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mIsImagelarge == false){
                    ivReceipt.setTranslationZ(10);
                    showImageLarge(mUrl);
                    mIsImagelarge = true;
                }else{
                    ivReceipt.setTranslationZ(0);
                    showImage(mUrl);
                    mIsImagelarge = false;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.attach_receipt_trans:
                dispatchTakePictureIntent();
                return true;
            case R.id.select_receipt_trans:
                dispatchSelectPictureIntent();
                return true;
            case R.id.cancel_trans:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            /*Uri imageUri = data.getData(); This didn't work, it was returning null
             * so I ended up saving the uri in a member variable(mPhotoURI) in dispatchTakePictureIntent() */
            uploadPicture(mPhotoURI);
        }else if(requestCode == PICTURE_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            uploadPicture(imageUri);
        }
    }

    private void cleanFields() {
        etAmount.setText("");
        atvDescription.setText("");
        etAmount.requestFocus();
        ivReceipt.setImageResource(0);
        pgvReceiptUpload.setProgress(0);

        // this will allow to enter a new transaction after editing another
        mTransaction = new Transaction();
        //this will reset the date after saving/editing the transaction.
        //It makes sure that new transaction is not saved with the previous trans date
        mEpochTime = DateUtil.getEpochTimeStamp();
        tvTransDate.setText(DateUtil.epochToDateString(mEpochTime));
        log.info("cleanFields " + tvTransDate.getText().toString());
        log.info("cleanFields " + mEpochTime);
        mUrl="";
    }

    private void showDatePickerDiaglog(View v){
        DatePickerFragment ddf = new DatePickerFragment();

        ddf.setDateDialogFragmentListener(new DateDialogFragmentListener(){

            @Override
            public void dateDialogFragmentDateSet(Calendar date) {

                /* New implementation*/
                mEpochTime = date.getTimeInMillis();
                String newDate = DateUtil.epochToDateString(mEpochTime);
                TextView tv = (TextView) findViewById(R.id.tv_date);
                tv.setText(newDate);
                log.info("DateDialog " + tv.getText().toString());
            }
        });

        ddf.show(getSupportFragmentManager(), "datePickerTrans");
    }
    //100 => 80  originalValue - newValue = 20
    //100 => 200 originalValue - newValue = -100
    private void saveTransaction(){
        //mTransaction.setDate(DateUtil.dateStringToEpoch(mLongCurrentDate, longPattern));
        mTransaction.setDate(mEpochTime);
        log.info("saveTransaction " + mTransaction.getDate());

        mTransaction.setAuthor(mCurrentUserName);
        mTransaction.setImageUrl(mUrl);
        if(mTransaction.getId() == null){
            mTransaction.setDescription(atvDescription.getText().toString());
            mTransaction.setAmount(Double.parseDouble(etAmount.getText().toString()));
            String id = mDatabaseReference.push().getKey();
            mTransaction.setId(id);
            mDatabaseReference.child(mCurrentProjectId + "/" + id).setValue(mTransaction);
            // new transaction so just save the amount in the text view
            getTotalsProject(mTransaction.getAmount(), mTransaction.getDescription());
        }else{
            if(!mTransaction.getDescription().equals(atvDescription.getText().toString())) {
                if ((mTransaction.getDescription().equals(INCOME) && atvDescription.getText().toString().equals(INCOMES)) ||
                    (mTransaction.getDescription().equals(INCOMES) && atvDescription.getText().toString().equals(INCOME))) {
                    mTransaction.setDescription(atvDescription.getText().toString());
                        if(mTransaction.getAmount() != Double.parseDouble(etAmount.getText().toString())){
                            Double originalAmount = mTransaction.getAmount();
                            Double newAmount = Double.parseDouble(etAmount.getText().toString());
                            Double amountToUpdate = (originalAmount - newAmount) * -1.0;
                            mTransaction.setAmount(newAmount);
                            mDatabaseReference.child(mCurrentProjectId + "/" + mTransaction.getId()).setValue(mTransaction);
                            getTotalsProject(amountToUpdate, mTransaction.getDescription());
                        }else{
                            mDatabaseReference.child(mCurrentProjectId + "/" + mTransaction.getId()).setValue(mTransaction);
                        }
                }else{
                        // We always going to add whatever is in the textview, and always subtract what is in the transaction object
                        getTotalsProject(Double.parseDouble(etAmount.getText().toString()), atvDescription.getText().toString());
                        getTotalsProject(mTransaction.getAmount()*-1, mTransaction.getDescription());

                        Double newAmount = Double.valueOf(etAmount.getText().toString());
                        mTransaction.setDescription(atvDescription.getText().toString());
                        mTransaction.setAmount(newAmount);
                        mDatabaseReference.child(mCurrentProjectId + "/" + mTransaction.getId()).setValue(mTransaction);
                }
            }else {
                Double originalAmount = mTransaction.getAmount();
                Double newAmount = Double.valueOf(etAmount.getText().toString());
                Double amountToUpdate = (originalAmount - newAmount) * -1.0;
                mTransaction.setAmount(newAmount);
                mDatabaseReference.child(mCurrentProjectId + "/" + mTransaction.getId()).setValue(mTransaction);
                getTotalsProject(amountToUpdate, mTransaction.getDescription());
            }
        }
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
               // Log.d("Totals", "Exp:" + mTotalExpenses + " Inc:" + mTotalIncomes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /* This is done here to reflect the total right away so another user looking at the list of projects can see
    * the up-to-date totals
    * */
    private void updateProjectTotal(Double amount, String transactionType){
        if(transactionType.toLowerCase().equals(INCOMES) || transactionType.toLowerCase().equals(INCOME)){
            Double newTotalIncomes = mTotalIncomes + amount;
            mDatabaseReferenceTotal.child(mCurrentProjectId).child("totalIncomes").setValue(newTotalIncomes);
        }else{
            Double newTotalExpenses = mTotalExpenses + amount;
            mDatabaseReferenceTotal.child(mCurrentProjectId).child("totalExpenses").setValue(newTotalExpenses);
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            // Create the File where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if(photoFile != null){
                mPhotoURI = FileProvider.getUriForFile(this, "com.ctmy.expensemanager.fileprovider", photoFile);
                //Log.d("Create:", String.valueOf(mPhotoURI) + currentPhotoPath);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile()throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir     /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchSelectPictureIntent(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICTURE_REQUEST);
    }

    private void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width*2/3)
                    .rotate(90)
                    .centerCrop()
                    .into(ivReceipt);
        }
    }

    private void showImageLarge(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width*2/3)
                    .rotate(90)
                    .centerInside()
                    .into(ivReceipt);
        }
    }


    private void uploadPicture(Uri imageUri){
        StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
        ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot.getMetadata() != null){
                    if(taskSnapshot.getMetadata().getReference() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUrl = uri.toString();
                                showImage(mUrl);
                                ivReceipt.setVisibility(View.VISIBLE);
                                pgvReceiptUpload.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }})
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        ivReceipt.setVisibility(View.GONE);
                        pgvReceiptUpload.setVisibility(View.VISIBLE);
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        //System.out.println("Upload is " + progress + "% done");
                        pgvReceiptUpload.setProgress((int) progress);
                    }
                });

    }
}