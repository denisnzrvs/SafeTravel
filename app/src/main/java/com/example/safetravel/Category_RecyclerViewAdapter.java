package com.example.safetravel;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Category_RecyclerViewAdapter extends RecyclerView.Adapter<Category_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category_File> category_files;
    public Category_RecyclerViewAdapter(Context context, ArrayList<Category_File> category_files) {
        this.context = context;
        this.category_files = category_files;
    }
    @NonNull
    @Override
    public Category_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_file_row, parent, false);
        return new Category_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(category_files.get(position).getFileName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHandler fh = new FileHandler(context);
                fh.openFile(new File(category_files.get(position).getFilePath()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return category_files.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.textView2);
        }
    }
}
