package com.android.systemui.statusbar.phone;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

class NotificationWallpaper extends FrameLayout {

    private final String TAG = "NotificationWallpaperUpdater";

    private final String NOTIF_WALLPAPER_IMAGE_PATH = "/data/data/com.teamsourcery.sourcerytools/files/notification_wallpaper.jpg";
    private final String NOTIF_WALLPAPER_IMAGE_PATH_LAND = "/data/data/com.teamsourcery.sourcerytools/files/notification_wallpaper_land.jpg";

    private ImageView mNotificationWallpaperImage;
    private int mScreenOrientation;

    Bitmap bitmapWallpaper;

    float wallpaperAlpha = Settings.System.getFloat(getContext()
            .getContentResolver(), Settings.System.NOTIF_WALLPAPER_ALPHA, 1.0f);

    public NotificationWallpaper(Context context, AttributeSet attrs) {
        super(context);

        setNotificationWallpaper();
    }

    public void setNotificationWallpaper() {
        File portrait = new File(NOTIF_WALLPAPER_IMAGE_PATH);
        File landscape = new File(NOTIF_WALLPAPER_IMAGE_PATH_LAND);
	mScreenOrientation = getContext().getResources().getConfiguration().orientation;
        boolean isPortrait =  mScreenOrientation == Configuration.ORIENTATION_PORTRAIT;

	//Lets make sure we don't just keep stacking images on top of one another
	removeAllViews();
	
	if (isPortrait) {
            if (portrait.exists()) {
                mNotificationWallpaperImage = new ImageView(getContext());
                mNotificationWallpaperImage.setScaleType(ScaleType.CENTER);
                addView(mNotificationWallpaperImage, -1, -1);
                bitmapWallpaper = BitmapFactory.decodeFile(NOTIF_WALLPAPER_IMAGE_PATH);
                Drawable d = new BitmapDrawable(getResources(), bitmapWallpaper);
                d.setAlpha((int) (wallpaperAlpha * 255));
                mNotificationWallpaperImage.setImageDrawable(d);
            }
        } else {
            if (landscape.exists()) {
                mNotificationWallpaperImage = new ImageView(getContext());
                mNotificationWallpaperImage.setScaleType(ScaleType.CENTER);
                addView(mNotificationWallpaperImage, -1, -1);
                bitmapWallpaper = BitmapFactory.decodeFile(NOTIF_WALLPAPER_IMAGE_PATH_LAND);
                Drawable d = new BitmapDrawable(getResources(), bitmapWallpaper);
                d.setAlpha((int) (wallpaperAlpha * 255));
                mNotificationWallpaperImage.setImageDrawable(d);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (bitmapWallpaper != null)
            bitmapWallpaper.recycle();

        System.gc();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        //super.onConfigurationChanged(newConfig);
	if (newConfig.orientation != mScreenOrientation) {
		setNotificationWallpaper();
	}
    }

}