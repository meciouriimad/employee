package com.example.employee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employee.adapter.AdapterEmployee;
import com.example.employee.adapter.OnItemClick;
import com.example.employee.adapter.OnItemLongClick;
import com.example.employee.adapter.SQLiteHelper;
import com.example.employee.models.Employee;

import java.util.ArrayList;
import java.util.Locale;

public class EmployeeScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton search,itemSwitcher,themeSwitcher,back,filtre_search;
    Button add;
    EditText search_edit;

    RelativeLayout shown;
    ImageView close,delete;

    TextView counter,title;

    boolean isGrid,isSearchMode=false;
    ArrayList<Employee> list,choose,result;
    SQLiteHelper sqLiteHelper;
    AdapterEmployee adapterEmployee;

    int filtreType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_screen);


        recyclerView = findViewById(R.id.recycler);
        itemSwitcher = findViewById(R.id.list_grid);
        themeSwitcher = findViewById(R.id.daynight);
        search = findViewById(R.id.search);
        add = findViewById(R.id.adde);
        counter = findViewById(R.id.counter);
        delete = findViewById(R.id.delete);
        close = findViewById(R.id.close);
        shown = findViewById(R.id.shown);
        search_edit = findViewById(R.id.search_employee);
        back = findViewById(R.id.return_home);
        filtre_search = findViewById(R.id.filtre_search);
        title = findViewById(R.id.title);



        SharedPreferences sharedPreferences = getSharedPreferences("lang",MODE_PRIVATE);


        isGrid = sharedPreferences.getBoolean("isGrid",false);


        themeSwitcher.setBackground(getDrawable(isDarkTheme(this)?R.drawable.ic_ight:R.drawable.ic_nightlight));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

         sqLiteHelper = new SQLiteHelper(this,"dbs",null,1);


        list = new ArrayList<>();
        choose = new ArrayList<>();
        result = new ArrayList<>();



        adapterEmployee = new AdapterEmployee(this,list,choose);



        recyclerView.setLayoutManager(isGrid ? layoutManager : gridLayoutManager);

        recyclerView.setAdapter(adapterEmployee);


            adapterEmployee.SetOnItemClick(new OnItemClick() {
                @Override
                public void OnClick(int position) {
                    if (!choose.isEmpty() && !choose.contains(list.get(position))){
                        choose.add(list.get(position));
                        adapterEmployee.notifyItemChanged(position);
                        System.out.println("add : "+position);
                    }
                    else if(choose.contains(list.get(position))){
                        choose.remove(list.get(position));
                        adapterEmployee.notifyItemChanged(position);
                        System.out.println("removed : "+position);
                    }
                    if (choose.isEmpty()){
                        shown.setVisibility(View.GONE);

                    }
                    System.out.println("cilcked : "+position);
                    System.out.println(list.get(position).getProfile());
                    counter.setText(""+choose.size());


                }
            });

            adapterEmployee.SetOnItemLongClick(new OnItemLongClick() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void OnClick(int position) {
                    System.out.println("Long cilcked : "+position);
                    if(!choose.contains(list.get(position)) && choose.isEmpty()){
                        choose.add(list.get(position));
                        adapterEmployee.notifyItemChanged(position);
                        System.out.println("add : "+position);
                        shown.setVisibility(View.VISIBLE);
                        counter.setText(""+choose.size());

                    }
                }
            });


                itemSwitcher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isGrid = !isGrid;
                        recyclerView.setLayoutManager(isGrid ? layoutManager : gridLayoutManager);
                        recyclerView.setAdapter(adapterEmployee);

                        SharedPreferences.Editor  editor = sharedPreferences.edit();
                        editor.putBoolean("isGrid",isGrid);
                        editor.commit();
                        view.setBackground(getDrawable(isGrid?R.drawable.ic_view_module:R.drawable.ic_view_list));
                    }
                });

                themeSwitcher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isDarkTheme(EmployeeScreen.this)) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }

                    }
                });


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmployeeScreen.this,AddEdit.class);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choose.clear();
                        adapterEmployee.notifyDataSetChanged();
                        shown.setVisibility(View.GONE);

                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqLiteHelper.removeEmployee(choose);
                        list.removeAll(choose);
                        choose.clear();
                        adapterEmployee.notifyDataSetChanged();
                        shown.setVisibility(View.GONE);


                    }
                });


                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isSearchMode = !isSearchMode;
                        view.setBackground(getDrawable(isSearchMode?R.drawable.ic_close:R.drawable.ic_search));
                        if (isSearchMode) {
                            showSearch();
                        } else {
                            hideSearch();
                        }
                    }
                });

                filtre_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       showMenuPopup();
                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();
                    }
                });


                search_edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String text = charSequence.toString();
                        Search(text);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


    }
    
    void showSearch(){
        themeSwitcher.setVisibility(View.GONE);

        title.setVisibility(View.GONE);
        search_edit.setVisibility(View.VISIBLE);
        filtre_search.setVisibility(View.VISIBLE);
        search_edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(search_edit, InputMethodManager.SHOW_IMPLICIT);
    }
    
    void hideSearch(){
        themeSwitcher.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        search_edit.setVisibility(View.GONE);
        filtre_search.setVisibility(View.GONE);
        search_edit.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_edit.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(sqLiteHelper.getEmployee());
        adapterEmployee.notifyDataSetChanged();
    }

    public static boolean isDarkTheme(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES;
        boolean dark = currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        System.out.println("dark : "+dark);
        return dark;
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void showMenuPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.choose));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = search_edit.getText().toString();
                Search(s);
                dialogInterface.dismiss();
            }
        });



        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.menu_layout, null);


        builder.setView(popupView);
        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        RadioGroup radioGroup = popupView.findViewById(R.id.filtre_choose);

        RadioButton firstname = popupView.findViewById(R.id.choose_firstname);
        RadioButton lastname = popupView.findViewById(R.id.choose_lastname);
        RadioButton number = popupView.findViewById(R.id.choose_number);
        RadioButton email = popupView.findViewById(R.id.choose_email);
        RadioButton ide = popupView.findViewById(R.id.choose_ide);

        switch (filtreType){
            case 1:
                firstname.setChecked(true);
                break;
            case 2:
                lastname.setChecked(true);
                break;
            case 3:
                number.setChecked(true);
                break;
            case 4:
                email.setChecked(true);
                break;
            case 5:
                ide.setChecked(true);
                break;


        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch(i){
                        case R.id.choose_firstname:
                            filtreType = 1;
                            break;
                        case R.id.choose_lastname:
                            filtreType = 2;
                            break;
                        case R.id.choose_number:
                            filtreType = 3;
                            break;

                        case R.id.choose_email:
                            filtreType = 4;
                            break;
                        case R.id.choose_ide:
                            filtreType = 5;
                            break;

                    };


            }
        });
    }

    void Search(String text){
        if(text.isEmpty()){
            adapterEmployee = new AdapterEmployee(this,list,choose);
            recyclerView.setAdapter(adapterEmployee);
        }else {
            result.clear();
            for (Employee e:list){
                if(filtreType == 1&& e.getFirstname().toLowerCase().startsWith(text.toLowerCase())){
                    result.add(e);
                }else if(filtreType == 2 && e.getLastname().toLowerCase().startsWith(text.toLowerCase())){
                    result.add(e);

                }else if(filtreType == 3 && e.getCall().toLowerCase().startsWith(text.toLowerCase())){
                    result.add(e);

                }else if(filtreType == 4 && e.getEmail().toLowerCase().startsWith(text.toLowerCase())){
                    result.add(e);

                }else if(filtreType == 5 && String.valueOf(e.getId()).toLowerCase().startsWith(text.toLowerCase())){
                    result.add(e);

                }

            }


            adapterEmployee = new AdapterEmployee(this,result,choose);
            recyclerView.setAdapter(adapterEmployee);
        }



    }

    @Override
    protected void onNightModeChanged(int mode) {

        super.onNightModeChanged(mode);

    }




}