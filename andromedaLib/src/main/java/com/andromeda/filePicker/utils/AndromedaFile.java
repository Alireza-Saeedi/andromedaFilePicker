package com.andromeda.filePicker.utils;

import java.io.File;
import java.io.Serializable;

/**
 * @author Alireza Saeedi.
 * @since 2023-03-15 9:44 PM
 */
public class AndromedaFile implements Serializable  {
    private File file;
    private String extension;
    private FileTypeUtils.FileType fileType;
    private boolean selected;

    public AndromedaFile() {
    }

    public AndromedaFile(File file, String extension, FileTypeUtils.FileType fileType) {
        this.file = file;
        this.extension = extension;
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public FileTypeUtils.FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileTypeUtils.FileType fileType) {
        this.fileType = fileType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
