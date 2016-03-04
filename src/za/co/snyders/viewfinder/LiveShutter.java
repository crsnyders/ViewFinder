package za.co.snyders.viewfinder;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import za.co.snyders.quickaction.*;
import static za.co.snyders.viewfinder.Constants.*;
import org.cybergarage.xml.*;
import org.cybergarage.upnp.xml.*;
import org.cybergarage.upnp.*;
import android.support.v4.content.*;
import za.co.snyders.viewfinder.cameraModel.*;
import com.samsungimaging.asphodel.multimedia.*;
import android.graphics.*;

public class LiveShutter extends Activity implements CameraInfoListener,ResChangeListener{

	private static final int ONE_MP = 1;
	private static final int TWO_MP = 2;
	private static final int THREE_Mp = 3;
	private static final int FIVE_Mp = 5;
	private static final int EIGHT_Mp = 8;
	private static final int Ten_Mp = 10;
	private static final int TWELVE_Mp = 12;
	private static final int FOURTEEN_Mp = 14;

	private static final int FLASH_OFF = 20;
	private static final int FLASH_AUTO = 21;

	private static final int TIMER_OFF = 30;
	private static final int TIMER_2 = 31;
	private static final int TIMER_10 = 32;

	private QuickAction resolutionQuickActions;
	private QuickAction flashQuickActions;
	private QuickAction timerQuickActions;
	
	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	
	private TextView totalShots;
	
	private String TAG = "LiveShutter";
	
	String ffplay = "ffplay -livestreaming -an -sync video -pix_fmt rgb565le -demuxprobesize 1024 ";

	
	private CameraInfo cameraInfo;
	
	private Map<Integer,CameraResolution> resmap = new HashMap<>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_shutter);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.surface =(SurfaceView)findViewById(R.id.surface);
		this.totalShots = (TextView)findViewById(R.id.totalShots);
		// set defaults
		reskinButton(findViewById(R.id.timer), this.getResources().getDrawable(R.drawable.custom_timer_off));
		reskinButton(findViewById(R.id.flash), this.getResources().getDrawable(R.drawable.custom_flash_off));
		reskinButton(findViewById(R.id.resolution), this.getResources().getDrawable(R.drawable.custom_size_1));

		resolutionQuickActions = new QuickAction(this);
		flashQuickActions = new QuickAction(this);
		timerQuickActions = new QuickAction(this);

		
		for (ActionItem action : getFlashActionItems()) {
			flashQuickActions.addActionItem(action);
		}
		for (ActionItem action : getTimerActionItems()) {
			timerQuickActions.addActionItem(action);
		}

		resolutionQuickActions.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);
				reskinButton(findViewById(R.id.resolution), actionItem.getIcon());
				Intent res = new Intent(SET_RESOLUTION_ACTION);
				res.putExtra(RESOLUTIONX,resmap.get(actionItem.getActionId()).getWidth());
				res.putExtra(RESOLUTIONY,resmap.get(actionItem.getActionId()).getHeight());
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(res);
			}
		});

		flashQuickActions.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);
				reskinButton(findViewById(R.id.flash), actionItem.getIcon());
				String flashMode = "OFF";
				switch(actionItem.getActionId()){
					case FLASH_OFF:
						flashMode ="OFF";
						break;
					case FLASH_AUTO:
						flashMode ="AUTO";
						break;
				}
				Intent flash = new Intent(SET_FLASH_ACTION);
				flash.putExtra(FLASHMODE,flashMode);
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(flash);
			}
		});
		timerQuickActions.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);
				reskinButton(findViewById(R.id.timer), actionItem.getIcon());
				
				String timerSetting = "OFF";
				switch(actionItem.getActionId()){
						case TIMER_OFF:
						timerSetting ="OFF";
							break;
						case TIMER_2:
						timerSetting ="2";
							break;
						case TIMER_10:
						timerSetting ="10";
							break;
				}
				Intent timer = new Intent(SET_TIMER_ACTION);
				timer.putExtra(LEDTIME,timerSetting);
				LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(timer);
		
			}
		});
		
		
	
		IntentFilter statusIntentFilter = new IntentFilter(Constants.CAMERA_FOUND);
		statusIntentFilter.addAction(AVAILSHOTS);

	LocalBroadcastManager.getInstance(this).registerReceiver(new UrlReciever(this,this),statusIntentFilter);
		
		Log.v(TAG,"STARTINGSERVICE");
		Intent test = new Intent(this,MyControlPoint.class);
		test.setAction(START_SEARCH);
		this.startService(test);
		
		
		int nHolder533 = convertDipToPx(getApplicationContext(), 533);
        int nHolder320 = convertDipToPx(getApplicationContext(), 320);
        int nHolder196 = convertDipToPx(getApplicationContext(), 196);
        int nCanvasY = convertDipToPx(getApplicationContext(), 113);
        int nCanvasX = nHolder533 / 2 - nHolder196 / 2;
        int nZoomland = convertDipToPx(getApplicationContext(), 78);
        int nZoomport = convertDipToPx(getApplicationContext(), 78);
        int nZoomWidth = convertDipToPx(getApplicationContext(), 133);
		
		Bitmap bgbitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg), nHolder533, nHolder320, true);
		Bitmap bgbitmap1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg1), nHolder320, nHolder533, true);
		
		int phonecamtype = 3;
		int[][]arrayOfInt1 = new int[4][2];
		int[][] arrayOfInt2 = new int[4][2];
		arrayOfInt1[0][0] = 0;
		arrayOfInt1[0][1] = nCanvasY;
		arrayOfInt1[1][0] = 0;
		arrayOfInt1[1][1] = 0;
		arrayOfInt1[2][0] = 0;
		arrayOfInt1[2][1] = 0;
		arrayOfInt1[3][0] = nCanvasX;
		arrayOfInt1[3][1] = 0;
		arrayOfInt2[0][0] = nHolder320;
		arrayOfInt2[0][1] = nHolder196;
		arrayOfInt2[1][0] = nHolder320;
		arrayOfInt2[1][1] = nHolder533;
		arrayOfInt2[2][0] = nHolder533;
		arrayOfInt2[2][1] = nHolder320;
		arrayOfInt2[3][0] = nHolder196;
		arrayOfInt2[3][1] = nHolder320;
		
		FFmpegJNI.construct(bgbitmap1, bgbitmap, arrayOfInt1, arrayOfInt2, phonecamtype - 1, 0);
		surfaceHolder = this.surface.getHolder();	
	}

	@Override
	protected void onDestroy() {
		FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_QUIT,"Stop playback");
		super.onDestroy();
	}
	public static int convertDipToPx(Context paramContext, int paramInt){
		return (int)(0.5F + paramContext.getResources().getDisplayMetrics().density * paramInt);
	}

	private void reskinButton(View view, Drawable image) {
		view.setBackgroundDrawable(image);
		view.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_live_shutter, menu);
		return true;
	}

	public void zoomIn(View view) {
		Log.d(TAG, "zoomin");
		Intent zoom = new Intent(ZOOM_IN_ACTION);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(zoom);
	}

	public void zoomOut(View view) {
		Log.d(TAG, "zoomout");
		Intent zoom = new Intent(ZOOM_OUT_ACTION);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(zoom);
	}

	public void takeShot(View view) {
		Log.d(TAG, "takeShot");
		Intent shutter = new Intent(SHUTTER_ACTION);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(shutter);
	}

	public void changeTimer(View view) {
		timerQuickActions.show(view);
		timerQuickActions.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
	}

	public void changeFlash(View view) {
		flashQuickActions.show(view);
		flashQuickActions.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
	}

	public void changeResolution(View view) {
		resolutionQuickActions.show(view);
		resolutionQuickActions.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
	}

	private void setupResolutions(){
		for (ActionItem action : getResolutionActionItems()) {
			for(CameraResolution res: this.cameraInfo.getResolutions()){
				if(Utils.getResIndex(res.getResolutuonString())==action.getActionId()){
					resolutionQuickActions.addActionItem(action);
					resmap.put(action.getActionId(),res);
			}
			}
		}
	}
	private Vector<ActionItem> getResolutionActionItems() {
		Vector<ActionItem> actionItems = new Vector<ActionItem>();
		actionItems.add(new ActionItem(ONE_MP, "", getResources().getDrawable(R.drawable.custom_size_1)));
		actionItems.add(new ActionItem(TWO_MP, "", getResources().getDrawable(R.drawable.custom_size_2)));
		actionItems.add(new ActionItem(THREE_Mp, "", getResources().getDrawable(R.drawable.custom_size_3)));
		actionItems.add(new ActionItem(FIVE_Mp, "", getResources().getDrawable(R.drawable.custom_size_5)));
		actionItems.add(new ActionItem(EIGHT_Mp, "", getResources().getDrawable(R.drawable.custom_size_8)));
		actionItems.add(new ActionItem(Ten_Mp, "", getResources().getDrawable(R.drawable.custom_size_10)));
		actionItems.add(new ActionItem(TWELVE_Mp, "", getResources().getDrawable(R.drawable.custom_size_12)));
		actionItems.add(new ActionItem(FOURTEEN_Mp, "", getResources().getDrawable(R.drawable.custom_size_14)));
		return actionItems;
	}

	private Vector<ActionItem> getFlashActionItems() {
		Vector<ActionItem> actionItems = new Vector<ActionItem>();
		actionItems.add(new ActionItem(FLASH_OFF, "", getResources().getDrawable(R.drawable.custom_flash_off)));
		actionItems.add(new ActionItem(FLASH_AUTO, "", getResources().getDrawable(R.drawable.custom_flash_auto)));
		return actionItems;
	}

	private Vector<ActionItem> getTimerActionItems() {
		Vector<ActionItem> actionItems = new Vector<ActionItem>();
		actionItems.add(new ActionItem(TIMER_OFF, "", getResources().getDrawable(R.drawable.custom_timer_off)));
		actionItems.add(new ActionItem(TIMER_2, "", getResources().getDrawable(R.drawable.custom_timer_2_sec)));
		actionItems.add(new ActionItem(TIMER_10, "", getResources().getDrawable(R.drawable.custom_timer_10_sec)));
		return actionItems;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void cameraInfoFound(CameraInfo cameraInfo) {
		FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CHANGED,surfaceHolder);
		FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_START, buildFFPlayString(cameraInfo.getVideoUrl()));
		this.cameraInfo=cameraInfo;
		this.totalShots.setText(String.valueOf(this.cameraInfo.getAvailableShots()));
		setupResolutions();
	}
	
	public void newAvailableShots(String shots){
		this.totalShots.setText(shots);
	}
	
	public String buildFFPlayString(String videoUrl){
		String streamCommand = ffplay;
		if(videoUrl.toLowerCase().contains("mp4")){
			streamCommand=streamCommand.concat("-f mp4 ");
		}else if(videoUrl.toLowerCase().contains("avi")){
			streamCommand=streamCommand.concat("-f avi ");
		}
		return streamCommand.concat(videoUrl);
	}
	
	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int width, int height){
		Log.e("INRY", new java.lang.Throwable().getStackTrace()[0].getFileName() + "(" + String.valueOf(new java.lang.Throwable().getStackTrace()[0].getLineNumber()) + ") - " + "surfaceChanged() - width:" + String.valueOf(width) + ", height:" + String.valueOf(height));
		FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CHANGED, paramSurfaceHolder);
	}
}
 class UrlReciever extends BroadcastReceiver{
	CameraInfoListener listener;
	ResChangeListener reschangeListener;
	private String TAG = "UrlReciever";
	public UrlReciever(CameraInfoListener listener,ResChangeListener reschangeListener){
		this.listener= listener;
		this.reschangeListener=reschangeListener;
		}
		@Override
		public void onReceive(Context p1, Intent p2) {
			Log.v(TAG,"Intent Acton recieved: "+p2.getAction());
			if(p2.getAction().equals(CAMERA_FOUND)){
			
			CameraInfo cameraInfo =(CameraInfo)p2.getSerializableExtra(CAMERA_DEVICE);
			this.listener.cameraInfoFound(cameraInfo);
			}
			if(p2.getAction().equals(AVAILSHOTS)){
				this.reschangeListener.newAvailableShots(p2.getStringExtra(AVAILSHOTS_COUNT));
			}
		}
	}
