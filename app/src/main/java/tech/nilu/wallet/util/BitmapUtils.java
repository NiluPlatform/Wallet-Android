package tech.nilu.wallet.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 2/4/18.
 */

public class BitmapUtils {
    public static Bitmap takeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache();

        if (view.getDrawingCache() == null) return null;

        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return snapshot;
    }

    public static File getBitmapFile(Context context, Bitmap bitmap) {
        try {
            File file = new File(context.getCacheDir(), "images");
            if (!file.exists())
                file.mkdirs();
            FileOutputStream stream = new FileOutputStream(file + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            return new File(file, "image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getBitmapIntent(Context context, View view) {
        Bitmap snapshot = takeScreenshot(view);
        File bitmapFile = getBitmapFile(context, snapshot);
        if (bitmapFile != null) {
            Uri contentUri = FileProvider.getUriForFile(context, "tech.nilu.wallet.fileprovider", bitmapFile);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            sendIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            return sendIntent;
        }
        return null;
    }

    public static Bitmap getAvatar(int size, String text) {
        Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(72);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2 + Math.abs(rect.height()) / 2, paint);
        return bmp;
    }
}
