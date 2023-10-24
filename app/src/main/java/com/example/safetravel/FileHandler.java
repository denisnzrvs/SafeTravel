package com.example.safetravel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    private Context context;
    private File innerDir;

    public FileHandler(Context context) {
        this.context = context;
        this.innerDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);;
    }

    public String getCurrentTripName() {
        String inactivePattern = ".*_inactive$"; // Match names ending with "_inactive"

        for (File file : innerDir.listFiles()) {
            if (file.isDirectory() && !file.getName().matches(inactivePattern)) {
                return file.getName();
            }
        }
        return null;
    }

    public ArrayList<String> getTripNames() {
        ArrayList<String> tripNames = new ArrayList<>();
        String inactivePattern = ".*_inactive$"; // Match names ending with "_inactive"

        for (File file : innerDir.listFiles()) {
            if (file.isDirectory() && file.getName().matches(inactivePattern)) {
                tripNames.add(file.getName());
            }
        }
        return tripNames;
    }

    public File getCurrentTripDir() {
        String currentTripName = getCurrentTripName();
        if (currentTripName != null) {
            return new File(innerDir, currentTripName);
        }
        return null;
    }

    public void openFile(File f) {
        context.startActivity(new Intent(context, PdfView.class).putExtra("file", f.getAbsolutePath()));
    }
}
