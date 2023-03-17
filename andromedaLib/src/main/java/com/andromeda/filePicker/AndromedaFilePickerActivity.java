package com.andromeda.filePicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.filePicker.adapters.AndromedaDirectoryAdapter;
import com.andromeda.filePicker.interfaces.OnItemClickListener;
import com.andromeda.filePicker.interfaces.OnResult;
import com.andromeda.filePicker.utils.AndromedaFile;
import com.andromeda.filePicker.utils.ExtFileFilter;
import com.andromeda.filePicker.utils.FileTypeUtils;
import com.andromeda.filePicker.utils.FileUtil;
import com.andromeda.filePicker.utils.RegexFileFilter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AndromedaFilePickerActivity extends AppCompatActivity {

    private TextView tv_andromeda_header_text;
    private RecyclerView rv_a_andromeda;
    private TextView tv_andromeda_empty;
    private FloatingActionButton fab_andromeda_selectedFinish;
    private AndromedaDirectoryAdapter andromedaDirectoryAdapter;
    private final List<AndromedaFile> currentDirectoryFiles = new ArrayList<>();
    private final ArrayList<AndromedaFile> selectedFiles = new ArrayList<>();
    private File currentDirectory;
    private int step = 0;
    private Context context = AndromedaFilePickerActivity.this;
    private FileFilter fileFilter;
    private String startPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andromeda_file_picker);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(AndromedaFilePickerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(AndromedaFilePickerActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            else
                init();
        } else
            checkSDK30AndAfter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                //deny permission for storage access
                Toast.makeText(this, getString(R.string.deny_permission), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkSDK30AndAfter() {
        if (!Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 200);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 200);
            }
        } else {
            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    init();
                } else {
                    Toast.makeText(this, getString(R.string.deny_permission), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private void init() {
        boolean dirOnly = getIntent().getBooleanExtra("dirOnly", false);
        boolean allowHidden = getIntent().getBooleanExtra("allowHidden", false);
        String[] suffixes = getIntent().getStringArrayExtra("suffixes");
        String pattern = getIntent().getStringExtra("pattern");
        boolean enableMultiple = getIntent().getBooleanExtra("enableMultiple", false);
        int maxItemSelected = getIntent().getIntExtra("maxItemSelected", 0);
        startPath = getIntent().getStringExtra("startPath");

        //init filters
        if (pattern != null) {
            fileFilter = new RegexFileFilter(dirOnly, allowHidden, pattern, Pattern.CASE_INSENSITIVE);
        } else {
            if (suffixes == null || suffixes.length == 0) {
                fileFilter = dirOnly ?
                        file -> file.isDirectory() && (!file.isHidden() || allowHidden) : file ->
                        !file.isHidden() || allowHidden;
            } else
                fileFilter = new ExtFileFilter(dirOnly, allowHidden, suffixes);
        }

        findViews();
        tv_andromeda_header_text.setText((enableMultiple) ? String.format(getString(R.string.choose_files), selectedFiles.size(), maxItemSelected) : getString(R.string.choose_file));
        andromedaDirectoryAdapter = new AndromedaDirectoryAdapter(context, currentDirectoryFiles).setEnableMultiple(enableMultiple, maxItemSelected);
        rv_a_andromeda.setAdapter(andromedaDirectoryAdapter);
        andromedaDirectoryAdapter.setOnItemClickListener((andromedaFile, position) -> {
            if (andromedaFile.getFileType() == FileTypeUtils.FileType.BACK) {
                onBackPressed();
                selectedFiles.clear();
            } else if (andromedaFile.getFile().isDirectory()) {
                selectedFiles.clear();
                step++;
                currentDirectory = new File(andromedaFile.getFile().getAbsolutePath());
                setFileList();
            } else {
                if (enableMultiple) {
                    if (maxItemSelected > selectedFiles.size() || andromedaFile.isSelected()) {
                        if (andromedaFile.isSelected()) {
                            selectedFiles.remove(andromedaFile);
                            andromedaFile.setSelected(false);
                        } else {
                            andromedaFile.setSelected(true);
                            selectedFiles.add(andromedaFile);
                        }
                        andromedaDirectoryAdapter.notifyItemChanged(position);
                    } else
                        Toast.makeText(context, getString(R.string.maxSelected), Toast.LENGTH_SHORT).show();
                } else {
                    selectedFiles.clear();
                    selectedFiles.add(andromedaFile);
                    setIntentDataAndExit();
                }
            }
            tv_andromeda_header_text.setText((enableMultiple) ? String.format(getString(R.string.choose_files), selectedFiles.size(), maxItemSelected) : getString(R.string.choose_file));
            if (enableMultiple) {
                if (selectedFiles.size() > 0)
                    fab_andromeda_selectedFinish.show();
                else
                    fab_andromeda_selectedFinish.hide();
            }
        });

        setFileList();

        fab_andromeda_selectedFinish.setOnClickListener(view -> {
            setIntentDataAndExit();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setFileList() {
        tv_andromeda_empty.setVisibility(View.GONE);
        if (step == 0) {
            runOnUiThread(() -> {
                currentDirectoryFiles.clear();
                if (startPath != null) {
                    currentDirectory = new File(startPath);
                    currentDirectoryFiles.addAll(FileUtil.filesInDirectory(context, currentDirectory, fileFilter));
                } else {
                    //set storages
                    currentDirectoryFiles.addAll(FileUtil.storages(context));
                }
                rv_a_andromeda.getAdapter().notifyDataSetChanged();
            });
        } else {
            runOnUiThread(() -> {
                //find files after click
                currentDirectoryFiles.clear();
                currentDirectoryFiles.add(new AndromedaFile(new File("..."), "", FileTypeUtils.FileType.BACK));
                currentDirectoryFiles.addAll(FileUtil.filesInDirectory(context, currentDirectory, fileFilter));
                andromedaDirectoryAdapter.notifyDataSetChanged();
                tv_andromeda_empty.setVisibility((currentDirectoryFiles.size() == 1) ? View.VISIBLE : View.GONE);
            });
        }
    }

    private void setIntentDataAndExit() {
        if (AndromedaFilePicker.onResult != null)
            AndromedaFilePicker.onResult.onChooseFiles(selectedFiles);
      /*  Intent resultIntent = new Intent();
        resultIntent.putExtra("files", selectedFiles);
        setResult(Activity.RESULT_OK, resultIntent);*/
        finish();
    }

    private void findViews() {
        rv_a_andromeda = findViewById(R.id.rv_a_andromeda);
        rv_a_andromeda.setLayoutManager(new LinearLayoutManager(AndromedaFilePickerActivity.this, RecyclerView.VERTICAL, false));
        tv_andromeda_empty = findViewById(R.id.tv_andromeda_empty);
        tv_andromeda_empty.setVisibility(View.GONE);
        fab_andromeda_selectedFinish = findViewById(R.id.fab_andromeda_selectedFinish);
        fab_andromeda_selectedFinish.hide();
        tv_andromeda_header_text = findViewById(R.id.tv_andromeda_header_text);
    }

    @Override
    public void onBackPressed() {
        if (step == 0)
            super.onBackPressed();
        else {
            step--;
            currentDirectory = new File(Objects.requireNonNull(currentDirectory.getParent()));
            setFileList();
        }
    }

}