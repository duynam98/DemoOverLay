package com.duynam.demooverlay.ui.activity.activity_filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SaveImage extends AsyncTask<Bitmap, Void, Boolean> {

    private Context context;
    private OnSaveImage onSaveImage;

    public void setOnSaveImage(OnSaveImage onSaveImage) {
        this.onSaveImage = onSaveImage;
    }

    public SaveImage(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Bitmap... bitmaps) {
//        boolean saved;
//        OutputStream fos;
//        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ContentResolver resolver = context.getContentResolver();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, Calendar.getInstance().getTimeInMillis());
//            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "input_image");
//            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//            try {
//                fos = resolver.openOutputStream(imageUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            String imagesDir = Environment.getExternalStorageDirectory().toString() + File.separator + "input_image";
//
//            File file = new File(imagesDir);
//
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            File image = new File(imagesDir, name + ".jpg");
//            try {
//                fos = new FileOutputStream(image);
//                saved = bitmaps[0].compress(Bitmap.CompressFormat.PNG, 80, fos);
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        File folder = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "input_image");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String file_name = Calendar.getInstance().getTimeInMillis() + ".jpg";
        File file = new File(folder, file_name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString() + File.separator + "input_image" +
                        File.separator + file_name}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (onSaveImage != null) {
            onSaveImage.onSaveImage(aBoolean);
        }
    }

    public interface OnSaveImage {
        void onSaveImage(boolean isSave);
    }

}
