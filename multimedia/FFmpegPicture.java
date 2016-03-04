// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package za.co.snyders.multimedia;

import android.graphics.Bitmap;
import android.util.*;

class FFmpegPicture{

    private static final String LOG_TAG = "[FFmpegPicture]";
    private Bitmap m_bitmap;

    public FFmpegPicture(int i, int j, android.graphics.Bitmap.Config config){
        m_bitmap = null;
        Log.i(LOG_TAG, "picture creation");
        m_bitmap = Bitmap.createBitmap(i, j, config);
    }

    public Bitmap getPicture(){
        return m_bitmap;
    }
}
