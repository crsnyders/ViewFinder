package za.co.snyders.viewfinder.cameraModel;

import java.io.*;

public class ZoomInfo implements Serializable {
	
	private int defaultZoom;
	private int maxZoom;
	
	public ZoomInfo(int defaultZoom, int maxZoom){
		this.defaultZoom = defaultZoom;
		this.maxZoom = maxZoom;
	}
	
	public int getDefaultZoom(){
		return this.defaultZoom;
	}
	
	public int getMaxZoom(){
		return this.maxZoom;
	}
}
