package com.andromeda.filePicker.utils;

import com.andromeda.filePicker.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {

    private static final Map<String, FileType> fileTypeExtensions = new HashMap<>();

    static {
        for (FileType fileType : FileType.values()) {
            for (String extension : fileType.getExtensions()) {
                fileTypeExtensions.put(extension, fileType);
            }
        }
    }

    public static FileType getFileType(File file) {
        if (file.isDirectory()) {
            return FileType.DIRECTORY;
        }

        FileType fileType = fileTypeExtensions.get(FileUtil.getExtensionWithoutDot(file).toLowerCase());
        if (fileType != null) {
            return fileType;
        }

        return FileType.FILE;
    }

    public enum FileType {
        BACK(R.drawable.ic_format_directory, R.string.back),
        INTERNAL_STORAGE(R.drawable.ic_internaml_storage, R.string.internal_storage),
        SD_CARD(R.drawable.ic_sd_card, R.string.sd_storage),
        DIRECTORY(R.drawable.ic_format_directory, R.string.type_directory),
        FILE(R.drawable.ic_format_file, R.string.type_document),

        APK(R.drawable.ic_format_apk, R.string.type_apk, "apk"),
        CERTIFICATE(R.drawable.ic_format_certificate, R.string.type_certificate, "cer", "der", "pfx", "p12", "arm", "pem"),
        COMPRESS(R.drawable.ic_format_compress, R.string.type_archive, "cab", "7z", "alz", "arj", "bzip2", "bz2", "dmg", "gzip", "gz", "jar", "lz", "lzip", "lzma", "zip", "rar", "tar", "tgz"),
        WORD(R.drawable.ic_format_document, R.string.type_word, "doc", "docm", "docx", "dot", "mcw", "rtf", "pages", "odt", "ott"),
        DRAWING(R.drawable.ic_format_drawing, R.string.type_drawing, "ai", "cdr", "dfx", "eps", "svg", "stl", "wmf", "emf", "art", "xar"),
        JSON(R.drawable.ic_format_json, R.string.type_json, "json"),
        IMAGE(R.drawable.ic_format_image, R.string.type_image, "bmp", "gif", "ico", "jpeg", "jpg", "pcx", "png", "psd", "tga", "tiff", "tif", "xcf"),
        MUSIC(R.drawable.ic_format_music, R.string.type_music, "aiff", "aif", "wav", "flac", "m4a", "wma", "amr", "mp2", "mp3", "wma", "aac", "mid", "m3u"),
        PDF(R.drawable.ic_format_pdf, R.string.type_pdf, "pdf"),
        PRESENTATION(R.drawable.ic_format_presentation, R.string.type_power_point, "pptx", "keynote", "ppt", "pps", "pot", "odp", "otp"),
        SPREADSHEET(R.drawable.ic_format_spreadsheet, R.string.type_excel, "xls", "xlk", "xlsb", "xlsm", "xlsx", "xlr", "xltm", "xlw", "numbers", "ods", "ots"),
        VIDEO(R.drawable.ic_format_video, R.string.type_video, "avi", "mov", "wmv", "mkv", "3gp", "f4v", "flv", "mp4", "mpeg", "webm");


        private final int icon;
        private final int description;
        private final String[] extensions;

        FileType(int icon, int description, String... extensions) {
            this.icon = icon;
            this.description = description;
            this.extensions = extensions;
        }

        public String[] getExtensions() {
            return extensions;
        }

        public int getIcon() {
            return icon;
        }

        public int getDescription() {
            return description;
        }
    }
}
