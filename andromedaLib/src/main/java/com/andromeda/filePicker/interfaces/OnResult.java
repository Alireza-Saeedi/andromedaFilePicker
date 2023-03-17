package com.andromeda.filePicker.interfaces;

import com.andromeda.filePicker.utils.AndromedaFile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Alireza Saeedi.
 * @since 2023-03-16 2:58 PM
 */
public interface OnResult extends Serializable {
    void onChooseFiles(ArrayList<AndromedaFile> files);
}
