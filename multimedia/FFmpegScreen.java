// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package za.co.snyders.multimedia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.SurfaceHolder;
import com.samsungimaging.remoteviewfinder.Logger;

class FFmpegScreen
{

    private static int $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command[];
    private static final int SCREEN_TYPE_NUM = 4;
    private static final String TAG = "[FFmpegScreen]";
    private Bitmap m_background[];
    private Canvas m_canvas;
    private int m_curCameraRotate;
    private int m_curScreenType;
    private Matrix m_matrix;
    private int m_screenPositoin[][];
    private int m_screenSize[][];
    private SurfaceHolder m_surfaceHolder;

    static int[] $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()
    {
        int ai[] = $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command;
        if (ai != null)
        {
            return ai;
        }
        ai = new int[FFmpegJNI.Command.values().length];
        try
        {
            ai[FFmpegJNI.Command.FRAME_COUNT_GET.ordinal()] = 9;
        }
        catch (NoSuchFieldError nosuchfielderror8) { }
        try
        {
            ai[FFmpegJNI.Command.SCREEN_CONFIG_CHANGE.ordinal()] = 8;
        }
        catch (NoSuchFieldError nosuchfielderror7) { }
        try
        {
            ai[FFmpegJNI.Command.SURFACE_CHANGED.ordinal()] = 6;
        }
        catch (NoSuchFieldError nosuchfielderror6) { }
        try
        {
            ai[FFmpegJNI.Command.SURFACE_CREATED.ordinal()] = 5;
        }
        catch (NoSuchFieldError nosuchfielderror5) { }
        try
        {
            ai[FFmpegJNI.Command.SURFACE_DESTROYED.ordinal()] = 7;
        }
        catch (NoSuchFieldError nosuchfielderror4) { }
        try
        {
            ai[FFmpegJNI.Command.VIDEO_STREAMING_FLUSH.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror3) { }
        try
        {
            ai[FFmpegJNI.Command.VIDEO_STREAMING_QUIT.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror2) { }
        try
        {
            ai[FFmpegJNI.Command.VIDEO_STREAMING_START.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror1) { }
        try
        {
            ai[FFmpegJNI.Command.VIDEO_STREAMING_TOGGLE_PAUSE.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror) { }
        $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command = ai;
        return ai;
    }

    public FFmpegScreen(Bitmap bitmap, Bitmap bitmap1, int ai[][], int ai1[][], int i, int j)
    {
        m_surfaceHolder = null;
        m_background = null;
        m_screenPositoin = null;
        m_screenSize = null;
        m_curScreenType = 0;
        m_curCameraRotate = 0;
        m_canvas = null;
        m_matrix = null;
        m_matrix = new Matrix();
        m_background = new Bitmap[2];
        m_background[0] = bitmap;
        m_background[1] = bitmap1;
        m_screenPositoin = new int[4][];
        m_screenSize = new int[4][];
        int k = 0;
        do
        {
            if (k >= 4)
            {
                changeScreenConfig(i, j);
                return;
            }
            m_screenPositoin[k] = new int[2];
            if (ai != null)
            {
                m_screenPositoin[k][0] = ai[k][0];
                m_screenPositoin[k][1] = ai[k][1];
            }
            m_screenSize[k] = new int[2];
            if (ai1 != null)
            {
                m_screenSize[k][0] = ai1[k][0];
                m_screenSize[k][1] = ai1[k][1];
            }
            k++;
        } while (true);
    }

    public int changeScreenConfig(int i, int j)
    {
        m_curScreenType = i;
        m_curCameraRotate = j;
        return 0;
    }

    public void configureSurface(FFmpegJNI.Command command, SurfaceHolder surfaceholder)
    {
        switch ($SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command()[command.ordinal()])
        {
        default:
            Logger.e("[FFmpegScreen]", "Unknown Command!");
            // fall through

        case 5: // '\005'
        case 7: // '\007'
            return;

        case 6: // '\006'
            m_surfaceHolder = surfaceholder;
            break;
        }
    }

    public int drawScreen(Bitmap bitmap)
    {
        int i;
        int j;
        j = 0;
        i = 0;
        if (Thread.currentThread().isInterrupted())
        {
            break MISSING_BLOCK_LABEL_296;
        }
        m_curCameraRotate;
        JVM INSTR lookupswitch 4: default 60
    //                   0: 191
    //                   90: 205
    //                   180: 191
    //                   270: 205;
           goto _L1 _L2 _L3 _L2 _L3
_L1:
        Bitmap bitmap1;
        m_matrix.reset();
        if (m_curCameraRotate != 0)
        {
            m_matrix.postRotate(m_curCameraRotate, (float)bitmap.getWidth() / 2.0F, (float)bitmap.getHeight() / 2.0F);
        }
        m_matrix.postScale((float)m_screenSize[m_curScreenType][0] / (float)j, (float)m_screenSize[m_curScreenType][1] / (float)i);
        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m_matrix, true);
        synchronized (m_surfaceHolder)
        {
            m_canvas = m_surfaceHolder.lockCanvas();
            if (m_canvas != null)
            {
                break; /* Loop/switch isn't completed */
            }
        }
        return -1;
_L2:
        j = bitmap.getWidth();
        i = bitmap.getHeight();
        continue; /* Loop/switch isn't completed */
_L3:
        j = bitmap.getHeight();
        i = bitmap.getWidth();
        if (true) goto _L1; else goto _L4
_L4:
        m_canvas.drawBitmap(m_background[m_curScreenType % 2], 0.0F, 0.0F, null);
        m_canvas.drawBitmap(bitmap1, m_screenPositoin[m_curScreenType][0], m_screenPositoin[m_curScreenType][1], null);
        m_surfaceHolder.unlockCanvasAndPost(m_canvas);
        bitmap;
        JVM INSTR monitorexit ;
        break MISSING_BLOCK_LABEL_296;
        exception;
        bitmap;
        JVM INSTR monitorexit ;
        throw exception;
        return 0;
    }
}
