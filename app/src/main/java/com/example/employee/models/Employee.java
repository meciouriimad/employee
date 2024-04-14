package com.example.employee.models;

import android.graphics.Bitmap;

public class Employee implements Comparable<Employee>{
    private String firstname,lastname,call,message,email,profile;
    private int id = 0;


    public Employee(String firstname, String lastname, String call,String message, String email,String profile){
        this.firstname = firstname;
        this.lastname = lastname;
        this.call = call;
        this.email = email;
        this.message = message;
        this.profile = profile;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCall() {
        return call;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile(){return profile;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public int compareTo(Employee employee) {
        if (employee.getFirstname().equals(this.getFirstname()) &&
                employee.getLastname().equals(this.getLastname()) &&
                employee.getCall().equals(this.getCall()) &&
                employee.getMessage().equals(this.getMessage()) &&
                employee.getEmail().equals(this.getEmail())&&
                employee.getProfile().equals(this.getProfile())
        ) {
            System.out.println("\n equals");
            return 1;
        }
        return -1;
    }

}
