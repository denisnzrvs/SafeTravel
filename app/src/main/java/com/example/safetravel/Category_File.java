package com.example.safetravel;

public class Category_File {
    String fileName;
    String filePath;

    public Category_File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}
