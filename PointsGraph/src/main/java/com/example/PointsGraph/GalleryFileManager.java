package com.example.PointsGraph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 24.07.13
 * Time: 2:23
 * Information about this garbage is coming soon
 */
public class GalleryFileManager {
    Context context;

    public GalleryFileManager(Context context) {
        this.context = context;
    }

    private String getAlbumName() {
        return context.getString(R.string.app_name);
    }

    private File getGallaryPath() {
        File file = getAlbumStorageDir(getAlbumName());
        file.mkdirs();
        return file;
    }

    private File getAlbumStorageDir(String albumName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return froyoAlbumDirFactory(albumName);
        } else {
            return baseAlbumDirFactory(albumName);
        }
    }

    private static final String CAMERA_DIR = "/dcim/";

    public File baseAlbumDirFactory(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }

    public File froyoAlbumDirFactory(String albumName) {
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }

    public static boolean savePNG(String prefix, String path, Bitmap bitmap) {
        boolean b = false;
        OutputStream outStream = null;
        File file = new File(path, prefix);

        try {
            outStream = new FileOutputStream(file);
            b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;

    }

    /**
     * Сохраняет битмап в галлерею.
     *
     * @param b исходный битмап
     * @return название файла, если успех, иначе null
     */
    public String savePNG(Bitmap b) {
        String galleryPath = getGallaryPath().getAbsolutePath();
        String fileName = Calendar.getInstance().getTimeInMillis() + ".PNG";
        boolean isSaved = savePNG(fileName, galleryPath, b);
        galleryAddPic(galleryPath + "/" + fileName);
        return isSaved ? fileName : null;
    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
