package com.andromeda.filePicker.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * @author Alireza Saeedi.
 * @since 2023-03-16 2:01 PM
 */
public class FileThumbnail {

    public Bitmap image(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public Bitmap musicCoverPicture(String path) {
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        mr.setDataSource(path);
        byte[] byte1 = mr.getEmbeddedPicture();
        mr.release();
        if (byte1 != null)
            return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        else
            return null;
    }

    public Bitmap videoThumbnail(String path) {
        //slow speed
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    public Drawable apkLogo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path, 0);
        pi.applicationInfo.sourceDir = path;
        pi.applicationInfo.publicSourceDir = path;
        return pi.applicationInfo.loadIcon(pm);
    }

}
