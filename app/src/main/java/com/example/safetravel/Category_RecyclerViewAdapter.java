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
        holder.button.setText(category_files.get(position).getFileName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = category_files.get(position).getFilePath();

                try {
                    // Create an intent to view the file using its associated app
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("content://" + filePath); // Use "file://" for local file paths
                    intent.setDataAndType(uri, "application/pdf"); // Set the MIME type to PDF

                    // Set flags and permissions
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exceptions or show an error message if the file can't be opened.
                }

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
