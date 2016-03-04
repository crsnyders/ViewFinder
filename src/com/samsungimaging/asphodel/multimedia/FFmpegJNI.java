// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.samsungimaging.asphodel.multimedia;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.util.*;
import java.util.*;

// Referenced classes of package com.samsungimaging.asphodel.multimedia:
//            FFmpegScreen, FFmpegPicture

public class FFmpegJNI /*implements Runnable*/{
    public enum Command{

		VIDEO_STREAMING_START, 
		VIDEO_STREAMING_TOGGLE_PAUSE, 
		VIDEO_STREAMING_FLUSH, 
		VIDEO_STREAMING_QUIT, 
		SURFACE_CREATED, 
		SURFACE_CHANGED, 
		SURFACE_DESTROYED, 
		SCREEN_CONFIG_CHANGE, 
		FRAME_COUNT_GET
		}

    public static final String ARG_VIDEO_STREAMING_FLUSH = "ffplay flush";
    public static final String ARG_VIDEO_STREAMING_QUIT = "ffplay quit";
    public static final String ARG_VIDEO_STREAMING_TOGGLE_PAUSE = "ffplay toggle_pause";
    private static final String DELIMITER = " ";
    private static final int FFMPEG_ARGB = 27;
    private static final int FFMPEG_RGB565LE = 44;
    public static final int SCREEN_TYPE_PHONE_LAND_CAMERA_LAND = 2;
    public static final int SCREEN_TYPE_PHONE_LAND_CAMERA_PORT = 3;
    public static final int SCREEN_TYPE_PHONE_PORT_CAMERA_LAND = 0;
    public static final int SCREEN_TYPE_PHONE_PORT_CAMERA_PORT = 1;
    private static final String TAG = "[FFmpegJNI]";
    private static int m_argc = 0;
    private static String m_argv[] = null;
    private static Thread m_ffmpegMainThread = null;
    private static FFmpegPicture m_ffmpegPicture[] = null;
    private static FFmpegScreen m_ffmpegScreen = null;
    private static int m_frameCount = 0;
    private static int m_pictureHeigtht = 0;
    private static int m_pictureQueueLen = 0;
    private static int m_pictureWidth = 0;
    private static boolean m_run = false;

    
	/*public void run(){
        FFmpegJNI.access$2(FFmpegJNI.access$0(), FFmpegJNI.access$1());
        FFmpegJNI.access$3(false);
    }*/

    public static int construct(Bitmap bitmap, Bitmap bitmap1, int screenPosition[][], int screenSize[][], int screenType, int cameraRotate){   
		Log.i(TAG,"Loading ffplay lib");
		try{
			System.loadLibrary("ffplay-jni");
		}catch(UnsatisfiedLinkError e){
			Log.e(TAG,"Could not load ffplay lib",e);
		}
		Log.i(TAG,"Ffplay loaded");
        m_ffmpegScreen = new FFmpegScreen(bitmap, bitmap1, screenPosition, screenSize, screenType, cameraRotate);
        m_run = true;
        return 0;
    }

    public static int destruct(){
        m_run = false;
        return 0;
    }

    private static native int ffplayMain(int i, String as[]);

    private static native int ffplaySDL(int i, String as[]);

    private static int onDisplay(int i){
        m_ffmpegScreen.drawScreen(onGetPicture(i));
        m_frameCount++;
        return 0;
    }

    private static Bitmap onGetPicture(int i){
		
		if(i>=m_pictureQueueLen){
			return null;
		}else{
			Bitmap bitmap = m_ffmpegPicture[i].getPicture();
			return bitmap;
		}
    }

    private static int onInitPictureQueue(int queueLength, int width, int height, int type){
        Log.i(TAG, (new StringBuilder("picture queue len : ")).append(queueLength).toString());
        Log.i(TAG, (new StringBuilder("picture width : ")).append(width).toString());
        Log.i(TAG, (new StringBuilder("picture height : ")).append(height).toString());
        Log.i(TAG, (new StringBuilder("picture type : ")).append(type).toString());
        m_pictureQueueLen = queueLength;
        m_pictureWidth = width;
        m_pictureHeigtht = height;
        android.graphics.Bitmap.Config config;
        if (type == FFMPEG_ARGB){
            Log.i(TAG, "RGB Type : ARGB8888");
            config = android.graphics.Bitmap.Config.ARGB_8888;
        } else
        if (type == FFMPEG_RGB565LE){
            Log.i(TAG, "RGB Type : RGB565");
            config = android.graphics.Bitmap.Config.RGB_565;
        } else{
            Log.e(TAG, "Unknown RGB Type!");
            return -1;
        }
        m_ffmpegPicture = new FFmpegPicture[m_pictureQueueLen];
        queueLength = 0;
        do{
            if (queueLength >= m_pictureQueueLen){
                return 0;
            }
            m_ffmpegPicture[queueLength] = new FFmpegPicture(m_pictureWidth, m_pictureHeigtht, config);
            queueLength++;
        } while (true);
    }

    public static int request(Command command, int i, int j) {
        if (!m_run){
            Log.e(TAG, "You should call construct() function first!");
            return -1;
        }
        switch (command){
		case SCREEN_CONFIG_CHANGE: 
			Log.i(TAG, (new StringBuilder("Command : ")).append(command.toString()).toString());
            m_ffmpegScreen.changeScreenConfig(i, j);
            return 0;

		case FRAME_COUNT_GET: 
            return m_frameCount;
			
		default:
			Log.e(TAG, "Unknown Command!");
			return -1;
        }
		
    }

    public static int request(Command command, SurfaceHolder surfaceholder){
        if (!m_run){
            Log.e(TAG, "You should call construct() function first!");
            return -1;
        }
        switch (command){
			case SURFACE_CREATED:
			case SURFACE_CHANGED:
			case SURFACE_DESTROYED:
				Log.i(TAG, (new StringBuilder("Command : ")).append(command.toString()).toString());
            	break;
			default:
				Log.e(TAG, "Unknown Command!");
				return -1;
        }
		Log.i(TAG,"Surface changed:"+surfaceholder);
        m_ffmpegScreen.configureSurface(command, surfaceholder);
        return 0;
    }

    public static int request(Command command, String s){
        if (!m_run){
            Log.e(TAG, "You should call construct() function first!");
            return -1;
        }
        m_argv = s.split(DELIMITER);
        m_argc = m_argv.length;
		for(int i =0;i< m_argv.length;i++){
			Log.i(TAG, (new StringBuilder("m_argv[")).append(i).append("] : ").append(m_argv[i]).toString());
		}
		
		switch(command){
				case VIDEO_STREAMING_START:
				Log.i(TAG, (new StringBuilder("Command : ")).append(command.toString()).toString());
				if (m_ffmpegMainThread != null){
					Log.i(TAG, "Video Streaming already started!");
					return -1;
				}else{
					m_ffmpegMainThread = new Thread(new Runnable() {
							public void run(){
								
								FFmpegJNI.ffplayMain(FFmpegJNI.m_argc, FFmpegJNI.m_argv);
								FFmpegJNI.m_run = false;
							}
						});
					m_ffmpegMainThread.start();
					return 0;
				}
					
				case VIDEO_STREAMING_TOGGLE_PAUSE:
				case VIDEO_STREAMING_FLUSH:
				case VIDEO_STREAMING_QUIT:
						Log.i(TAG, (new StringBuilder("Command : ")).append(command.toString()).toString());
						ffplaySDL(m_argc, m_argv);
					if (command != Command.VIDEO_STREAMING_QUIT){
						return 0;
					}else{
						
						int counter=0;
						while(counter<30||!m_run){
							Log.i(TAG, "wait for termination,...");
							try{
								Thread.sleep(100L);
							}catch (Exception e){
								Log.e(TAG,"Sleep thread interrupted",e);
							}
							counter++;
						}
						return 0;
					}
						
				default:
				Log.e(TAG, "Unknown Command!");
				return -1;
				
		}
    }
}
