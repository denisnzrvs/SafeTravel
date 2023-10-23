package com.example.safetravel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    private Context context;
    private File innerDir;

    public FileHandler(Context context) {
        this.context = context;
        this.innerDir = context.getFilesDir();
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

    public File getCurrentTripDir() {
        String currentTripName = getCurrentTripName();
        if (currentTripName != null) {
            return new File(innerDir, currentTripName);
        }
        return null;
    }

    public void openFile(File f) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }
}
