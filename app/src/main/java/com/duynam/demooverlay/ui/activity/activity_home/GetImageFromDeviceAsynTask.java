package com.duynam.demooverlay.ui.activity.activity_home;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GetImageFromDeviceAsynTask extends AsyncTask<Void, Void, List<String>> {

    private Context context;
    private List<String> stringList ;
    private OnLoadImage onLoadImage;

    public GetImageFromDeviceAsynTask(Context context, OnLoadImage onLoadImage) {
        this.context = context;
        this.onLoadImage = onLoadImage;
        stringList = new ArrayList<>();
    }

    public void setOnLoadImage(OnLoadImage onLoadImage) {
        this.onLoadImage = onLoadImage;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> list_patchImage = new ArrayList<>();
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int count = cursor.getCount();
        String[] arrPath = new String[count];
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = cursor.getString(dataColumnIndex);
            list_patchImage.add(arrPath[i]);
            Log.e("PATH", list_patchImage.get(i));
        }
        cursor.close();
        return list_patchImage;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        if (onLoadImage != null){
            onLoadImage.onLoadFinish(strings);
        }
    }

    public interface OnLoadImage{
        void onLoadFinish(List<String> data);
    }

}
