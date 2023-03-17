package com.andromeda.filePicker.example;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andromeda.filePicker.AndromedaFilePicker;
import com.andromeda.filePicker.utils.AndromedaFile;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setLocale(this, "fa");
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);

        new AndromedaFilePicker(this)
                .setOnResult(files -> {
                    if (files != null && files.size() > 0) {
                        List<AndromedaFile> selectedFiles = files;
                        //write your codes
                    }
                })
                .show();

        Button btn_multipleImage = findViewById(R.id.btn_multipleImage);
        btn_multipleImage.setOnClickListener(view -> {
            new AndromedaFilePicker(this)
                    .withFilterRegex(false, false, ".*\\.(jpe?g|png)")
                    .multiple(true, 2)
                    .setOnResult(files -> {
                        result.setText("");
                        if (files != null && files.size() > 0) {
                            for (AndromedaFile file : files) {
                                File f = file.getFile();
                                result.append(f.getAbsolutePath() + "\r\n\r\n");
                            }
                        }
                    })
                    .show();
        });

        Button btn_aImage = findViewById(R.id.btn_aImage);
        btn_aImage.setOnClickListener(view -> {
            new AndromedaFilePicker(this)
                    .withFilterRegex(false, false, ".*\\.(jpe?g|png)")
                    .setOnResult(files -> {
                        result.setText("");
                        if (files != null && files.size() > 0)
                            result.append(files.get(0).getFile().getAbsolutePath());
                    })
                    .show();
        });

        Button btn_pdf = findViewById(R.id.btn_pdf);
        btn_pdf.setOnClickListener(view -> {
            new AndromedaFilePicker(this)
                    .withFilter(false, false, "pdf")
                    .setOnResult(files -> {
                        result.setText("");
                        if (files != null && files.size() > 0)
                            result.append(files.get(0).getFile().getAbsolutePath());
                    })
                    .show();
        });

        Button btn_anyFile = findViewById(R.id.btn_anyFile);
        btn_anyFile.setOnClickListener(view -> {
            new AndromedaFilePicker(this)
                    .setOnResult(files -> {
                        result.setText("");
                        if (files != null && files.size() > 0)
                            result.append(files.get(0).getFile().getAbsolutePath());
                    })
                    .show();
        });
    }


    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}