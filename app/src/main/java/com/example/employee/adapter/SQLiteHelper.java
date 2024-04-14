package com.example.employee.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.employee.models.Employee;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private String DB_NAME;
    private int VERSION = 1;

     final String FIRST_NAME = "firstname";
     final String LAST_NAME = "lastname";
     final String CALL = "call";
     final String MESSAGE = "message";
     final String EMAIL = "email";
     final String PROFILE = "profile";

    public static final String EMPLOYEE = "employees";


    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        this.DB_NAME = name;
        this.VERSION = version;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE employees(id INTEGER PRIMARY KEY AUTOINCREMENT,firstname TEXT,lastname TEXT,call TEXT,message TEXT,email TEXT,profile TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS employees");
        onCreate(sqLiteDatabase);
    }

    public void addEmployee(Employee e){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(FIRST_NAME, e.getFirstname());
            values.put(LAST_NAME, e.getLastname());
            values.put(CALL, e.getCall());
            values.put(MESSAGE, e.getMessage());
            values.put(EMAIL, e.getEmail());
            values.put(PROFILE,e.getProfile());

            db.insert(EMPLOYEE, null, values);
            db.close();

        }

    public ArrayList<Employee> getEmployee(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Employee> employees = new ArrayList<>();

        Cursor cursor = db.query(EMPLOYEE, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            int c6 = cursor.getColumnIndex("id");
            int c0 = cursor.getColumnIndex(FIRST_NAME);
            int c1 = cursor.getColumnIndex(LAST_NAME);
            int c2 = cursor.getColumnIndex(CALL);
            int c3 = cursor.getColumnIndex(MESSAGE);
            int c4 = cursor.getColumnIndex(EMAIL);
            int c5 = cursor.getColumnIndex(PROFILE);

            Employee e = new Employee(String.valueOf(cursor.getString(c0)),String.valueOf(cursor.getString(c1)),String.valueOf(cursor.getString(c2)),String.valueOf(cursor.getString(c3)),String.valueOf(cursor.getString(c4)),String.valueOf(cursor.getString(c5)));
            e.setId(cursor.getInt(c6));
            employees.add(e);
        }

        return employees;


    }

    public void removeEmployee(ArrayList<Employee> choose){

        SQLiteDatabase db = getWritableDatabase();

        for (Employee e:choose){
            db.execSQL("DELETE FROM "+EMPLOYEE+" WHERE id = "+e.getId());
        }
        db.close();
    }

    public Employee getEmployeeById(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Employee e = null;
        Cursor cursor = sqLiteDatabase.query(EMPLOYEE, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) { // Check if cursor is not null and move it to the first row
            int c6 = cursor.getColumnIndex("id");
            int c0 = cursor.getColumnIndex(FIRST_NAME);
            int c1 = cursor.getColumnIndex(LAST_NAME);
            int c2 = cursor.getColumnIndex(CALL);
            int c3 = cursor.getColumnIndex(MESSAGE);
            int c4 = cursor.getColumnIndex(EMAIL);
            int c5 = cursor.getColumnIndex(PROFILE);

            e = new Employee(
                    cursor.getString(c0),
                    cursor.getString(c1),
                    cursor.getString(c2),
                    cursor.getString(c3),
                    cursor.getString(c4),
                    cursor.getString(c5)
            );
            e.setId(cursor.getInt(c6));
            cursor.close(); // Close the cursor after use
        }
        return e;
    }



    public void updateEmployee(int id, Employee e) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, e.getFirstname());
        values.put(LAST_NAME, e.getLastname());
        values.put(CALL, e.getCall());
        values.put(MESSAGE, e.getMessage());
        values.put(EMAIL, e.getEmail());
        values.put(PROFILE,e.getProfile());
        db.update(EMPLOYEE, values,   "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }



}
