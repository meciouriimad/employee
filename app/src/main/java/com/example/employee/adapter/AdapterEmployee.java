package com.example.employee.adapter;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employee.R;
import com.example.employee.models.Employee;

import java.util.ArrayList;

public class AdapterEmployee extends RecyclerView.Adapter<HolderEmployee> {

    ArrayList<Employee> list;
    ArrayList<Employee> choose;
    Context context;
    OnItemClick onItemClick;
    OnItemLongClick onItemLongClick;
    RecyclerView recyclerView;

    public AdapterEmployee(Context context, ArrayList<Employee> list,ArrayList<Employee> choose){
        this.context = context;
        this.list = list;
        this.choose = choose;
    }

    public void SetOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    public void SetOnItemLongClick(OnItemLongClick onItemLongClick){
        this.onItemLongClick = onItemLongClick;
    }

    @NonNull
    @Override
    public HolderEmployee onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        recyclerView = (RecyclerView) parent;
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
           v = LayoutInflater.from(context).inflate(R.layout.employee_grid_model,parent,false);
        }else {
            v = LayoutInflater.from(context).inflate(R.layout.employee_model,parent,false);
        }


        return new HolderEmployee(v,context);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderEmployee holder, @SuppressLint("RecyclerView") int position) {



        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
        holder.itemView.startAnimation(animation);


        holder.bind(list.get(position));

        if(choose.contains(list.get(position))){
            holder.itemView.setBackground(getDrawable(context,R.drawable.employee_item_selected));
        }else {
            holder.itemView.setBackground(getDrawable(context,R.drawable.employee_item));

        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClick!=null){
                    onItemLongClick.OnClick(position);
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClick!=null){
                    onItemClick.OnClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }




}
