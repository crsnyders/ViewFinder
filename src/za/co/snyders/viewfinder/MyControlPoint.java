package za.co.snyders.viewfinder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.xml.ActionData;
import org.cybergarage.xml.Node;
import za.co.snyders.*;
import android.widget.*;
import android.app.*;
import android.content.*;
import android.os.*;

import static za.co.snyders.viewfinder.Constants.*;
import android.support.v4.content.*;
import android.util.*;
import org.cybergarage.upnp.*;
import java.util.concurrent.*;
import org.cybergarage.upnp.control.*;
import za.co.snyders.viewfinder.cameraModel.*;
import android.nfc.*;

public class MyControlPoint extends  IntentService  implements DeviceChangeListener,IntentHandler  {

	
	private static final String TAG = "MyControlPoint";
	private ControlPoint p;
	private CameraDevice cameraDevice;
	
	public MyControlPoint(){
		this("Something");
	}
	
	public MyControlPoint(String something){
		super(something);
		IntentFilter statusIntentFilter = new IntentFilter(Constants.ZOOM_IN_ACTION);
		statusIntentFilter.addAction(Constants.ZOOM_OUT_ACTION);
		statusIntentFilter.addAction(Constants.SHUTTER_ACTION);
		statusIntentFilter.addAction(Constants.SHUTTER_WITH_GPS_ACTION);
		statusIntentFilter.addAction(Constants.SET_FLASH_ACTION);
		statusIntentFilter.addAction(Constants.SET_RESOLUTION_ACTION);
		statusIntentFilter.addAction(Constants.SET_TIMER_ACTION);
		

		LocalBroadcastManager.getInstance(this).registerReceiver(new IntentManager(this),statusIntentFilter);
	}
	
		
	/*@Override
	public void cameraDevcieFound() {
		
		Node node  = ((ActionData) this.cameraDevice.getGetInfomation().getActionNode().getUserData()).getControlResponse().getBodyNode();
		String streamUrl = node.getNodeValue("GETIPRESULT");
		Log.v(TAG,"Fetched urlStrema: "+streamUrl);
		Intent localIntent = new Intent(CAMERA_FOUND)
            .putExtra(Constants.CAMERA_DEVICE, streamUrl);
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
	}	*/
	
	@Override
	public void handleIntent(Intent intent) {
		onHandleIntent(intent);
		}
	@Override
	protected void onHandleIntent(Intent p1) {
		Log.v(TAG,"Recieved Intent: "+p1);
		switch(p1.getAction()){
			case START_SEARCH:
				Log.v(TAG,"Starting search for device");
				p= new ControlPoint();
				p.addDeviceChangeListener(this);
				p.start();
				Log.v(TAG,"Started");
				break;
			case STOP_SEARCH:
				p.stop();
				break;
			case ZOOM_IN_ACTION:
				Action zoomin  = this.cameraDevice.getZoomIN();
				AsyncTask<Action,Void,Boolean> task = doAction(zoomin);
				
				try{
					if (task.get(30,TimeUnit.SECONDS)) {
						String status = zoomin.getStatus().getDescription();
						Log.v(TAG,"Zoomin action :"+status);
						
						ArgumentList outArgList = zoomin.getOutputArgumentList();
						for(Argument arg : outArgList){
							String name = arg.getName();
							String value = arg.getValue();
							Log.v(TAG,name+" : "+value);
							if (name.equals("CURRENTZOOM")) {
								Toast.makeText(getApplicationContext(),"Current Zoom: "+value,Toast.LENGTH_SHORT).show();
							}
						}
					}
				}catch(Exception e){
					Log.e(TAG,"Could not do zoom in",e);
				}
				break;
			case ZOOM_OUT_ACTION:
				Action zoomOut  = this.cameraDevice.getZoomOUT();
				try{
					if (doAction(zoomOut).get(30,TimeUnit.SECONDS)) {
						String status = zoomOut.getStatus().getDescription();
						Log.v(TAG,"Zoomin action :"+status);

						ArgumentList outArgList = zoomOut.getOutputArgumentList();
						for(Argument arg : outArgList){
							String name = arg.getName();
							String value = arg.getValue();
							Log.v(TAG,name+" : "+value);
							if (name.equals("CURRENTZOOM")) {
								Toast.makeText(getApplicationContext(),"Current Zoom: "+value,Toast.LENGTH_SHORT).show();
							}
						}
					}
				}catch(Exception e){
					Log.e(TAG,"Could not do zoom in",e);
				}
				break;
			case SHUTTER_ACTION:
				Action shutterAction = this.cameraDevice.getShot();
				doShot(shutterAction);
				break;
			case SHUTTER_WITH_GPS_ACTION:
				Action shutterWithGPSAction = this.cameraDevice.getShotWithGPS();
				/*long nvalue = (long) (this.currentLocation.getLatitude() * 3600.0d);
				long evalue = (long) (this.currentLocation.getLongitude() * 3600.0d);
				shutterWithGPSAction.setArgumentValue("GPSINFO", "N" + nvalue + "XE" + evalue);*/
				doShot(shutterWithGPSAction);
				break;
			case SET_FLASH_ACTION:
				Log.v(TAG,"FLASHMODE: "+p1.getStringExtra(FLASHMODE).trim().toLowerCase());
				Action setFlashAction = this.cameraDevice.getSetFlash();
				setFlashAction.setArgumentValue("FLASHMODE",p1.getStringExtra(FLASHMODE).trim().toLowerCase());
				try{
					if(doAction(setFlashAction).get(30,TimeUnit.SECONDS)){
						//Log.v(TAG,((ActionData)setFlashAction.getActionNode().getUserData()).getControlResponse().getBodyNode().toString());
					}
				}catch(Exception e){
					Log.e(TAG,"Could not set flash Action",e);
				}
				
				break;
			case SET_TIMER_ACTION:
				Log.v(TAG,"LEDTIME: "+p1.getStringExtra(LEDTIME).trim().toLowerCase());
				Action setLedAction = this.cameraDevice.getSetLED();
				setLedAction.setArgumentValue("LEDTIME",p1.getStringExtra(LEDTIME).trim().toLowerCase());
				try{
					if(doAction(setLedAction).get(30,TimeUnit.SECONDS)){
						doAction(this.cameraDevice.getMultiAF()).get(30,TimeUnit.SECONDS);
					}
				}catch(Exception e){
					Log.e(TAG,"Could not set flash Action",e);
				}
				
				
				break;
			case SET_RESOLUTION_ACTION:
				
				Action setRes = this.cameraDevice.getSetResolution();

				setRes.setArgumentValue("RESOLUTION", String.format("%dx%d", p1.getIntExtra(RESOLUTIONX,0),p1.getIntExtra(RESOLUTIONY,0)));
				try{
				if (doAction(setRes).get(30,TimeUnit.SECONDS)) {
					Node availableShots= ((ActionData) setRes.getActionNode().getUserData()).getControlResponse().getBodyNode();
					Log.i(TAG,availableShots.toString());
					if(availableShots.getNode("u:SetResolutionResponse")!=null){
						if(availableShots.getNode("u:SetResolutionResponse").getNode("AVAILSHOTS")!=null){
							String aval =availableShots.getNode("u:SetResolutionResponse").getNodeValue("AVAILSHOTS");
							Intent avalableShotCount = new Intent(AVAILSHOTS);
							avalableShotCount.putExtra(AVAILSHOTS_COUNT,aval);
							Log.i(TAG,"Available Shots: "+aval);
							LocalBroadcastManager.getInstance(this).sendBroadcast(avalableShotCount);
						}
					}
					}
				}catch(Exception e){
					Log.e(TAG,"Could not set flash Action",e);
				}
				break;
				
				
		}
	}
		
	private AsyncTask<Action,Void,Boolean> doAction(Action action){
			return  new AsyncTask<Action,Void,Boolean>(){

				@Override
				protected Boolean doInBackground(Action[] p1) {
					return p1[0].postControlAction();
				}
				
			}
			.execute(action);
		}
		
	private void doShot(Action shutterAction){
		try{
			if(doAction(this.cameraDevice.getMultiAF()).get(30,TimeUnit.SECONDS)){
				try{
					if(doAction(shutterAction).get(30,TimeUnit.SECONDS)){
						Node shotNode = ((ActionData) shutterAction.getActionNode().getUserData()).getControlResponse().getBodyNode();
						Log.v(TAG,shotNode.toString());
						/*if (ccnode.getName().equals("AFSHOTRESULT")) {
						 String	FileURL = ccnode.getValue();
						 } else if (ccnode.getName().equals(ActionNode.AVAILSHOTS)) {
						 String AvailShots = ccnode.getValue();
						 }*/
					}
				}catch(Exception e){
					Log.e(TAG,"Could not take shot",e);
				}
			}			
		}catch(Exception e){
			Log.e(TAG,"Could not do auto focus",e);
		}
		
}
		@Override
		public void deviceAdded(Device device) {
			Log.v(TAG,"Device Added: "+device.getFriendlyName()+":"+device.getUDN()+" : "+device.getDeviceType()+" : "+device.getLocation());
			if(device.getLocation().contains("SamsungDmsDesc.xml")){
				Log.v(TAG,"DEVICE FOUND");
				initDevice(device);
			}
		}

		@Override
		public void deviceRemoved(Device device) {
			Log.v(TAG,"Device Removed: "+device.getFriendlyName()+":"+device.getUDN()+" : "+device.getDeviceType()+" : "+device.getLocation());
			if(this.cameraDevice.getUpnpDevice().getUDN().equals(device.getUDN())){
				
			}
		}

		private void initDevice(Device device){
			Log.v(TAG,"Attempting to get device type");
			String deviceType = device.getDeviceType();
			Log.v(TAG,"Got device Type: "+deviceType);
			
			ServiceList list =device.getServiceList();
			Service s0 =list.getService(0);
			Service s1 =list.getService(1);
			s0.setSCPDURL(device.getLocation().replace("SamsungDmsDesc.xml","")+s0.getSCPDURL());
			s1.setSCPDURL(device.getLocation().replace("SamsungDmsDesc.xml","")+s1.getSCPDURL());
			if (deviceType.contains("MediaServer")) {
				Log.v(TAG,"Fetching Actions");
				
				Action getInfomation = device.getAction("GetInfomation");
				Action setResolution = device.getAction("SetResolution");
				Action zoomIN = device.getAction("ZoomIN");
				Action zoomOUT = device.getAction("ZoomOUT");
				Action multiAF = device.getAction("MULTIAF");
				Action shot = device.getAction("Shot");
				Action shotWithGPS = device.getAction("ShotWithGPS");
				Action setLED = device.getAction("SetLED");
				Action setFlash = device.getAction("SetFlash");
				Log.v(TAG,"Actions fetched: "+device.getLocation());
				if (getInfomation != null && setResolution != null && zoomIN != null && zoomOUT != null && multiAF != null && shot != null && shotWithGPS != null && setLED != null
					&& setFlash != null) {
					
					Log.v(TAG,"Fetched All Actions");
					this.cameraDevice = new CameraDevice(device);
						
					
					Log.v(TAG,"ATTEMPTING TO GET INFO");
					if(this.cameraDevice.getGetInfomation().postControlAction()){
						Log.v(TAG,"FETCHED CAMERA INFO");
						Node node  = ((ActionData) this.cameraDevice.getGetInfomation().getActionNode().getUserData()).getControlResponse().getBodyNode();
						CameraInfo info = new CameraInfo(node.getNode("u:GetInfomationResponse"));
						Intent localIntent = new Intent(CAMERA_FOUND)
							//.putExtra(Constants.CAMERA_DEVICE, node.getNode("u:GetInfomationResponse").getNodeValue("GETIPRESULT"));
							.putExtra(Constants.CAMERA_DEVICE,info);
						LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
					}
							
				}
			}
		}

}

class IntentManager extends BroadcastReceiver {

	private IntentHandler handler;
	
	public IntentManager(IntentHandler handler){
		this.handler = handler;

	}
	
	@Override
	public void onReceive(Context p1, Intent p2) {
		handler.handleIntent(p2);
	}
	
	
}

