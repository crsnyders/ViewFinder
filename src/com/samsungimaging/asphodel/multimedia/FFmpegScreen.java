// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.samsungimaging.asphodel.multimedia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.SurfaceHolder;
import android.util.*;


class FFmpegScreen{

    private static final int SCREEN_TYPE_NUM = 4;
    private static final String TAG = "[FFmpegScreen]";
    private Bitmap m_background[];
    private Canvas m_canvas=null;
    private int m_curCameraRotate=0;
    private int m_curScreenType=0;
    private Matrix m_matrix = new Matrix();
    private int m_screenPositoin[][];
    private int m_screenSize[][];
    private static SurfaceHolder m_surfaceHolder =null;


    public FFmpegScreen(Bitmap bitmap, Bitmap bitmap1, int screenPosition[][], int screenSize[][], int screenType, int cameraRotate){
         
        this.m_background = new Bitmap[]{bitmap,bitmap1};
        this.m_screenPositoin = new int[SCREEN_TYPE_NUM][2];
        this.m_screenSize = new int[SCREEN_TYPE_NUM][2];
		
		for(int k=0;k<SCREEN_TYPE_NUM;k++){
			m_screenPositoin[k] = new int[2];
            if (screenPosition != null){
                this.m_screenPositoin[k][0] = screenPosition[k][0];
                this.m_screenPositoin[k][1] = screenPosition[k][1];
            }
            m_screenSize[k] = new int[2];
            if (screenSize != null){
                this.m_screenSize[k][0] = screenSize[k][0];
                this.m_screenSize[k][1] = screenSize[k][1];
            }
		}
        changeScreenConfig(screenType, cameraRotate);
		return;
    }

    public int changeScreenConfig(int screenType, int cameraRotate){
        this.m_curScreenType = screenType;
        this.m_curCameraRotate = cameraRotate;
        return 0;
    }
	
    public void configureSurface(FFmpegJNI.Command command, SurfaceHolder surfaceholder){
        switch (command){
        	case SURFACE_CREATED:
			case SURFACE_DESTROYED:
           		return;
			case SURFACE_CHANGED:
				Log.i(TAG,"Surface holder set");
           		m_surfaceHolder = surfaceholder;
            	break;
			default:
				Log.e(TAG, "Unknown Command!");
				return;
        }
    }
	
    public int drawScreen(Bitmap bitmap){
        int height=0;
        int width=0;
        
        if (Thread.currentThread().isInterrupted()) {
          Log.e(TAG,"Thread interrupted");
		  return -1;
        }
		
		switch(m_curCameraRotate){
			case 0:
			case 180:
				width = bitmap.getWidth();
				height = bitmap.getHeight();
				break;
			case 90:
			case 270:
				width = bitmap.getHeight();
				height = bitmap.getWidth();
		}
        this.m_matrix.reset();
        if (this.m_curCameraRotate != 0){
            this.m_matrix.postRotate(this.m_curCameraRotate, (float)bitmap.getWidth() / 2.0F, (float)bitmap.getHeight() / 2.0F);
        }
        this.m_matrix.postScale((float)this.m_screenSize[this.m_curScreenType][0] / (float)width, (float)this.m_screenSize[this.m_curScreenType][1] / (float)height);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), this.m_matrix, true);
		if(m_surfaceHolder!=null){
        synchronized (m_surfaceHolder){
            this.m_canvas = this.m_surfaceHolder.lockCanvas();
            if (this.m_canvas == null){
      			return -1;
            }
        }
		this.m_canvas.drawBitmap(this.m_background[this.m_curScreenType % 2], 0.0F, 0.0F, null);
        this.m_canvas.drawBitmap(bitmap1, this.m_screenPositoin[this.m_curScreenType][0], this.m_screenPositoin[this.m_curScreenType][1], null);
        this.m_surfaceHolder.unlockCanvasAndPost(this.m_canvas);
		return 0;
		}
		Log.w(TAG,"Surface holder null");
		return -1;
    }
}
