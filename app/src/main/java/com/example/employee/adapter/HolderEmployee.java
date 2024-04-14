package com.example.employee.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employee.AddEdit;
import com.example.employee.R;
import com.example.employee.models.Employee;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderEmployee extends RecyclerView.ViewHolder {
    TextView nom,prenom,call,message,email,idemp;
    CircleImageView circleImageView;
    ImageButton edit;
    LinearLayout btn_call,btn_message,btn_email;
    Context context;

    public HolderEmployee(@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        nom = itemView.findViewById(R.id.nom);
        prenom = itemView.findViewById(R.id.prenom);
        email = itemView.findViewById(R.id.email);
        call = itemView.findViewById(R.id.call);
        message =itemView.findViewById(R.id.messagee);
        circleImageView = itemView.findViewById(R.id.pdp);
        edit = itemView.findViewById(R.id.edit);
        btn_call = itemView.findViewById(R.id.btn_call);
        btn_email = itemView.findViewById(R.id.btn_email);
        btn_message = itemView.findViewById(R.id.btn_message);
        idemp = itemView.findViewById(R.id.idemployee);


    }

    public void bind(Employee e){
        nom.setText(e.getFirstname());
        prenom.setText(e.getLastname());
        email.setText(e.getEmail());
        call.setText(e.getCall());
        message.setText(e.getMessage());
        idemp.setText(String.valueOf(e.getId()));


        File imgFile = new File(e.getProfile());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEdit.class);
                intent.putExtra("isEditMode",true);
                intent.putExtra("id",e.getId());
                context.startActivity(intent);


            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + e.getMessage()));
                context.startActivity(intent);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + e.getEmail()));
                context.startActivity(intent);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + e.getCall()));
                context.startActivity(intent);


            }
        });


        if(imgFile.exists()){

            Uri imageUri = Uri.fromFile(imgFile);

            // Set the image to the ImageView
            circleImageView.setImageURI(imageUri);

        } else {

            // For example, display a placeholder image or show an error message
        }
    }
}
