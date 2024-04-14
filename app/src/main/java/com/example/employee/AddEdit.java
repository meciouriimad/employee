package com.example.employee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.employee.adapter.SQLiteHelper;
import com.example.employee.models.Employee;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEdit extends AppCompatActivity {

    CircleImageView circleImageView;
    EditText first_name,last_name,number,email;
    Button envoyer;
    ImageButton back;
    final int PICK_IMAGE_REQUEST = 1;
    String path = "";
    boolean isEditMode;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        circleImageView = findViewById(R.id.add_image);
        first_name = findViewById(R.id.add_firstname);
        last_name = findViewById(R.id.add_lastname);
        number = findViewById(R.id.add_number);
        email = findViewById(R.id.add_email);

        envoyer = findViewById(R.id.envoyer);
        back = findViewById(R.id.back);

        isEditMode = getIntent().getBooleanExtra("isEditMode",false);



        SQLiteHelper sqLiteHelper = new SQLiteHelper(this,"dbs",null,1);

        if(isEditMode){
            id = getIntent().getIntExtra("id",0);
            Employee e = sqLiteHelper.getEmployeeById(id);

            first_name.setText(e.getFirstname());
            last_name.setText(e.getLastname());
            number.setText(e.getCall());
            email.setText(e.getEmail());
            File file = new File(e.getProfile());
            if(file.exists()){
                Uri uri = Uri.fromFile(file);
                path = e.getProfile();
                circleImageView.setImageURI(uri);
            }
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fistname,lastname,numbere,emaile;
                fistname = first_name.getText().toString().trim();
                lastname = last_name.getText().toString().trim();
                numbere = number.getText().toString().trim();
                emaile = email.getText().toString().trim();
                if (fistname.isEmpty()||lastname.isEmpty()||numbere.isEmpty()||emaile.isEmpty()||path.isEmpty()){
                    Toast.makeText(AddEdit.this, "enter all data", Toast.LENGTH_SHORT).show();
                }else {
                    Employee e = new Employee(fistname,lastname,numbere,numbere,emaile,path);

                    if (isEditMode){
                        sqLiteHelper.updateEmployee(id,e);
                    }
                    else{
                        sqLiteHelper.addEmployee(e);

                    }

                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                path = getPath(uri);
                circleImageView.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public String getPath(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                result = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}