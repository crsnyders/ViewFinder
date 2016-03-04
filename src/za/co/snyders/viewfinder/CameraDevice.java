package za.co.snyders.viewfinder;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import java.io.*;
import android.os.*;

public class CameraDevice implements Serializable {

	
	private final Device upnpDevice;
	private Action getInfomationAction;
	private Action setResolutionAction;
	private Action zoomINAction;
	private Action zoomOUTAction;
	private Action multiAFAAction;
	private Action shotAction;
	private Action shotWithGPSAction;
	private Action setLEDAction;
	private Action setFlashAction;

	public CameraDevice(Device upnpDevice) {
		this.upnpDevice = upnpDevice;
		
		Action getInfomation = this.upnpDevice.getAction("GetInfomation");
		Action setResolution = this.upnpDevice.getAction("SetResolution");
		Action zoomIN = this.upnpDevice.getAction("ZoomIN");
		Action zoomOUT = this.upnpDevice.getAction("ZoomOUT");
		Action multiAF = this.upnpDevice.getAction("MULTIAF");
		Action shot = this.upnpDevice.getAction("Shot");
		Action shotWithGPS = this.upnpDevice.getAction("ShotWithGPS");
		Action setLED = this.upnpDevice.getAction("SetLED");
		Action setFlash = this.upnpDevice.getAction("SetFlash");
		setGetInfomationAction(getInfomation);
		setSetResolutionAction(setResolution);
		setZoomINAction(zoomIN);
		setZoomOUTAction(zoomOUT);
		setMultiAFAAction(multiAF);
		setShotAction(shot);
		setShotWithGPSAction(shotWithGPS);
		setSetLEDAction(setLED);
		setSetFlashAction(setFlash);
	}

	public void setGetInfomationAction(Action getInfomationAction) {
		this.getInfomationAction = getInfomationAction;
	}

	public void setSetResolutionAction(Action setResolutionAction) {
		this.setResolutionAction = setResolutionAction;
	}

	public void setZoomINAction(Action zoomINAction) {
		this.zoomINAction = zoomINAction;
	}

	public void setZoomOUTAction(Action zoomOUTAction) {
		this.zoomOUTAction = zoomOUTAction;
	}

	public void setMultiAFAAction(Action multiAFAAction) {
		this.multiAFAAction = multiAFAAction;
	}

	public void setShotAction(Action shotAction) {
		this.shotAction = shotAction;
	}

	public void setShotWithGPSAction(Action shotWithGPSAction) {
		this.shotWithGPSAction = shotWithGPSAction;
	}

	public void setSetLEDAction(Action setLEDAction) {
		this.setLEDAction = setLEDAction;
	}

	public void setSetFlashAction(Action setFlashAction) {
		this.setFlashAction = setFlashAction;
	}

	public Action getGetInfomation() {
		return getInfomationAction;
	}

	public Action getMultiAF() {
		return multiAFAAction;
	}

	public Action getSetFlash() {
		return setFlashAction;
	}

	public Action getSetLED() {
		return setLEDAction;
	}

	public Action getSetResolution() {
		return setResolutionAction;
	}

	public Action getShot() {
		return shotAction;
	}

	public Action getShotWithGPS() {
		return shotWithGPSAction;
	}

	public Device getUpnpDevice() {
		return upnpDevice;
	}

	public Action getZoomIN() {
		return zoomINAction;
	}

	public Action getZoomOUT() {
		return zoomOUTAction;
	}
}
