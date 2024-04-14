package com.example.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Region;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employee.models.Employee;

import java.util.Locale;

public class Home extends AppCompatActivity {

    Button admin;
    Spinner spinner;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        admin = findViewById(R.id.admin);
        spinner = findViewById(R.id.spinner);

        Intent intent = new Intent(this, EmployeeScreen.class);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_spinner, new String[]{"english", "العربية"});
        spinner.setAdapter(adapter);



        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("isAdmin",true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        spinner.setSelection(isLanguageArabic()?1:0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences sharedPreferences = getSharedPreferences("lang", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (position == 1 && !first) {
                    editor.putBoolean("us",false);
                    changeLanguage(true);


                } else if (position == 0 && !first){
                    editor.putBoolean("us",true);
                    changeLanguage(false);
                    Toast.makeText(Home.this, "english", Toast.LENGTH_SHORT).show();


                }
                editor.commit();

                first = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void changeLanguage(boolean isArabic) {
        Locale locale;
        if (isArabic) {
            locale = new Locale("ar");
        } else {
            // Set the default locale
            locale = new Locale("en");
        }

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Restart the activity to apply the language changes
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private boolean isLanguageArabic() {
        Locale currentLocale = getResources().getConfiguration().locale;
        return currentLocale.getLanguage().equals("ar");
    }


}