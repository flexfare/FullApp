package com.flexfare.android.AsyncTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.flexfare.android.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by kodenerd on 8/8/17.
 */
@SuppressWarnings("deprecation")
public class ImageAsyncTask extends AsyncTask<String, String, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;
    Bitmap bitmap;

    public ImageAsyncTask(ImageView imageView)
    {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    protected Bitmap doInBackground(String... args) {
        try
        {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image)
    {
        if (imageViewReference != null)
        {
            ImageView imageView = imageViewReference.get();
            if (imageView != null)
            {
                if (bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.no_image);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }
}
