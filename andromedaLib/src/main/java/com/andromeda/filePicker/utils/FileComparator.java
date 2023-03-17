package com.andromeda.filePicker.utils;

import java.util.Comparator;

public class FileComparator implements Comparator<AndromedaFile> {
    @Override
    public int compare(AndromedaFile f1, AndromedaFile f2) {
        if (f1 == f2) {
            return 0;
        }
        if (f1.getFile().isDirectory() && f2.getFile().isFile()) {
            // Show directories above files
            return -1;
        }
        if (f1.getFile().isFile() && f2.getFile().isDirectory()) {
            // Show files below directories
            return 1;
        }
        // Sort the directories alphabetically
        return f1.getFile().getName().compareToIgnoreCase(f2.getFile().getName());
    }
}
