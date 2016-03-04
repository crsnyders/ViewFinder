// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package za.co.snyders.multimedia;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.util.*;
//import com.samsungimaging.remoteviewfinder.Logger;

// Referenced classes of package com.samsungimaging.asphodel.multimedia:
//            FFmpegScreen, FFmpegPicture

public class FFmpegJNI{


    private static int $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command[];
    public static final String ARG_VIDEO_STREAMING_FLUSH = "ffplay flush";
    public static final String ARG_VIDEO_STREAMING_QUIT = "ffplay quit";
    public static final String ARG_VIDEO_STREAMING_TOGGLE_PAUSE = "ffplay toggle_pause";
    private static final String DELIMITER= " ";
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

    static int[] $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()
    {
        int ai[] = $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command;
        if (ai != null)
        {
            return ai;
        }
        ai = new int[Command.values().length];
        try
        {
            ai[Command.FRAME_COUNT_GET.ordinal()] = 9;
        }
        catch (NoSuchFieldError nosuchfielderror8) { }
        try
        {
            ai[Command.SCREEN_CONFIG_CHANGE.ordinal()] = 8;
        }
        catch (NoSuchFieldError nosuchfielderror7) { }
        try
        {
            ai[Command.SURFACE_CHANGED.ordinal()] = 6;
        }
        catch (NoSuchFieldError nosuchfielderror6) { }
        try
        {
            ai[Command.SURFACE_CREATED.ordinal()] = 5;
        }
        catch (NoSuchFieldError nosuchfielderror5) { }
        try
        {
            ai[Command.SURFACE_DESTROYED.ordinal()] = 7;
        }
        catch (NoSuchFieldError nosuchfielderror4) { }
        try
        {
            ai[Command.VIDEO_STREAMING_FLUSH.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror3) { }
        try
        {
            ai[Command.VIDEO_STREAMING_QUIT.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror2) { }
        try
        {
            ai[Command.VIDEO_STREAMING_START.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror1) { }
        try
        {
            ai[Command.VIDEO_STREAMING_TOGGLE_PAUSE.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror) { }
        $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command = ai;
        return ai;
    }

    public FFmpegJNI()
    {
    }

    public static int construct(Bitmap bitmap, Bitmap bitmap1, int ai[][], int ai1[][], int i, int j){
        System.loadLibrary("ffplay-jni");
        m_ffmpegScreen = new FFmpegScreen(bitmap, bitmap1, ai, ai1, i, j);
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
        com/samsungimaging/asphodel/multimedia/FFmpegJNI;
        JVM INSTR monitorenter ;
        if (i >= m_pictureQueueLen) goto _L2; else goto _L1
_L1:
        Bitmap bitmap = m_ffmpegPicture[i].getPicture();
_L4:
        com/samsungimaging/asphodel/multimedia/FFmpegJNI;
        JVM INSTR monitorexit ;
        return bitmap;
_L2:
        bitmap = null;
        if (true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }

    private static int onInitPictureQueue(int i, int j, int k, int l)
    {
        Log.i("[FFmpegJNI]", (new StringBuilder("picture queue len : ")).append(i).toString());
        Log.i("[FFmpegJNI]", (new StringBuilder("picture width : ")).append(j).toString());
        Log.i("[FFmpegJNI]", (new StringBuilder("picture height : ")).append(k).toString());
        Log.i("[FFmpegJNI]", (new StringBuilder("picture type : ")).append(l).toString());
        m_pictureQueueLen = i;
        m_pictureWidth = j;
        m_pictureHeigtht = k;
        android.graphics.Bitmap.Config config;
        if (l == 27)
        {
            Log.i("[FFmpegJNI]", "RGB Type : ARGB8888");
            config = android.graphics.Bitmap.Config.ARGB_8888;
        } else
        if (l == 44)
        {
            Log.i("[FFmpegJNI]", "RGB Type : RGB565");
            config = android.graphics.Bitmap.Config.RGB_565;
        } else
        {
            Log.e("[FFmpegJNI]", "Unknown RGB Type!");
            return -1;
        }
        m_ffmpegPicture = new FFmpegPicture[m_pictureQueueLen];
        i = 0;
        do
        {
            if (i >= m_pictureQueueLen)
            {
                return 0;
            }
            m_ffmpegPicture[i] = new FFmpegPicture(m_pictureWidth, m_pictureHeigtht, config);
            i++;
        } while (true);
    }

    public static int request(Command command, int i, int j)
    {
        if (!m_run)
        {
            Log.e("[FFmpegJNI]", "You should call construct() function first!");
            return -1;
        }
        switch ($SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()[command.ordinal()])
        {
        default:
            Log.e("[FFmpegJNI]", "Unknown Command!");
            return -1;

        case 8: // '\b'
            Log.i("[FFmpegJNI]", (new StringBuilder("Command : ")).append(command.toString()).toString());
            m_ffmpegScreen.changeScreenConfig(i, j);
            return 0;

        case 9: // '\t'
            return m_frameCount;
        }
    }

    public static int request(Command command, SurfaceHolder surfaceholder)
    {
        if (!m_run)
        {
            Log.e("[FFmpegJNI]", "You should call construct() function first!");
            return -1;
        }
        switch ($SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()[command.ordinal()])
        {
        default:
            Log.e("[FFmpegJNI]", "Unknown Command!");
            return -1;

        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
            Log.i("[FFmpegJNI]", (new StringBuilder("Command : ")).append(command.toString()).toString());
            break;
        }
        m_ffmpegScreen.configureSurface(command, surfaceholder);
        return 0;
    }

    public static int request(Command command, String s)
    {
        int i;
        if (!m_run)
        {
            Log.e("[FFmpegJNI]", "You should call construct() function first!");
            return -1;
        }
        m_argv = s.split(DELIMITER);
        m_argc = m_argv.length;
        i = 0;
_L6:
        if (i < m_argc) goto _L2; else goto _L1
_L1:
        Log.i("[FFmpegJNI]", (new StringBuilder("m_argc : ")).append(m_argc).toString());
        $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()[command.ordinal()];
        JVM INSTR tableswitch 1 4: default 100
    //                   1 150
    //                   2 215
    //                   3 215
    //                   4 215;
           goto _L3 _L4 _L5 _L5 _L5
_L3:
        Log.e("[FFmpegJNI]", "Unknown Command!");
        return -1;
_L2:
        Log.i("[FFmpegJNI]", (new StringBuilder("m_argv[")).append(i).append("] : ").append(m_argv[i]).toString());
        i++;
          goto _L6
_L4:
        Log.i("[FFmpegJNI]", (new StringBuilder("Command : ")).append(command.toString()).toString());
        if (m_ffmpegMainThread != null) goto _L8; else goto _L7
_L7:
        m_ffmpegMainThread = new Thread(new Runnable() {

            public void run()
            {
                FFmpegJNI.ffplayMain(FFmpegJNI.m_argc, FFmpegJNI.m_argv);
                FFmpegJNI.m_run = false;
            }

        });
        m_ffmpegMainThread.start();
_L10:
        return 0;
_L8:
        Log.i("[FFmpegJNI]", "Video Streaming already started!");
        return -1;
_L5:
        Log.i("[FFmpegJNI]", (new StringBuilder("Command : ")).append(command.toString()).toString());
        ffplaySDL(m_argc, m_argv);
        if (command != Command.VIDEO_STREAMING_QUIT) goto _L10; else goto _L9
_L9:
        i = 0;
_L12:
        Log.i("[FFmpegJNI]", "wait for termination,...");
        try
        {
            Thread.sleep(100L);
        }
        // Misplaced declaration of an exception variable
        catch (Command command)
        {
            command.printStackTrace();
        }
        if (!m_run || i > 30) goto _L10; else goto _L11
_L11:
        i++;
          goto _L12
    }





}
