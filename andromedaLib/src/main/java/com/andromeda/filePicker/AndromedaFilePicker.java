package com.andromeda.filePicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.andromeda.filePicker.interfaces.OnResult;

import java.io.File;

/**
 * @author Alireza Saeedi.
 * @since 2023-03-15 6:05 PM
 */
public class AndromedaFilePicker {
    private final Context context;
    public static int ANDROMEDA_FILE_CODE = 217;
    private boolean dirOnly;
    private boolean allowHidden;
    private String[] suffixes;
    private String pattern;
    private boolean enableMultiple = false;
    private int maxItemSelected = 0;
    private String startPath;
    public static OnResult onResult;

    public AndromedaFilePicker(Context context) {
        this.context = context;
    }

    /**
     * filter files in list with suffix
     * for example .withFilter(false,false , "png","jpg")
     *
     * @param dirOnly
     * @param allowHidden show hidden files
     * @param suffixes    suffixes allow files
     * @return
     */
    public AndromedaFilePicker withFilter(boolean dirOnly, final boolean allowHidden, String... suffixes) {
        this.dirOnly = dirOnly;
        this.allowHidden = allowHidden;
        this.suffixes = suffixes;
        return this;
    }

    /**
     * filter files in list with regex
     * for example .withFilterRegex(false, false, ".*\\.(jpe?g|png)")
     *
     * @param dirOnly
     * @param allowHidden
     * @param pattern
     * @return
     */
    public AndromedaFilePicker withFilterRegex(boolean dirOnly, boolean allowHidden, String pattern) {
        this.dirOnly = dirOnly;
        this.allowHidden = allowHidden;
        this.pattern = pattern;
        return this;
    }

    /**
     * select multiple item by user
     * for example .multiple(true , 4)
     *
     * @param enableMultiple  true or false
     * @param maxItemSelected set max item selected
     * @return
     */
    public AndromedaFilePicker multiple(boolean enableMultiple, int maxItemSelected) {
        this.enableMultiple = enableMultiple;
        this.maxItemSelected = maxItemSelected;
        return this;
    }

    /**
     * Specifies start directory for picker,
     * which will be shown to user at the beginning
     * for example start from download folder  .withStartPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath())
     *
     * @param startPath start path
     * @return
     */
    public AndromedaFilePicker withStartPath(String startPath) {
        if (startPath == null || startPath.equals(""))
            return this;
        File startFile = new File(startPath);
        if (!startFile.exists())
            return this;

        if (startFile.isDirectory())
            this.startPath = startPath;
        else
            this.startPath = startFile.getParent();
        return this;
    }

    /**
     * Show or hide hidden files in picker
     */
    public AndromedaFilePicker withHiddenFiles(boolean show) {
        allowHidden = show;
        return this;
    }

    /**
     * get list of selected files
     *
     * @param onResult
     * @return
     */
    public AndromedaFilePicker setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    public void show() {
        Intent intent = new Intent(context, AndromedaFilePickerActivity.class);
        intent.putExtra("dirOnly", dirOnly);
        intent.putExtra("allowHidden", allowHidden);
        intent.putExtra("suffixes", suffixes);
        intent.putExtra("pattern", pattern);
        intent.putExtra("enableMultiple", enableMultiple);
        intent.putExtra("maxItemSelected", maxItemSelected);
        intent.putExtra("startPath", startPath);
        // intent.putExtra("onResult", onResult);
        // ((Activity) context).startActivityForResult(intent, ANDROMEDA_FILE_CODE);
        ((Activity) context).startActivity(intent);
    }
}
