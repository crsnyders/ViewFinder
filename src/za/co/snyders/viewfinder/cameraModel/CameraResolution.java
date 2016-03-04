package za.co.snyders.viewfinder.cameraModel;
import java.io.*;
import za.co.snyders.viewfinder.*;

public class CameraResolution implements Serializable{
	private int width;
	private int height;
	
	public CameraResolution(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public CameraResolution(String width, String height){
		this(Integer.parseInt(width),Integer.parseInt(height));
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){

		return this.height;
	}
	
	public String getResolutuonString(){
		return Utils.unitChange(this.width,this.height);
	}
}

	/*if ((nWidth == 4680.0d && nHeight == 3456.0d) || (nWidth == 3456.0d && nHeight == 4680.0d)) {
		return "16 M";
	}
	if ((nWidth == 4680.0d && nHeight == 3072.0d) || (nWidth == 3072.0d && nHeight == 4680.0d)) {
		return "14 MP";
	}
	if ((nWidth == 4320.0d && nHeight == 3240.0d) || (nWidth == 3240.0d && nHeight == 4320.0d)) {
		return "14 M";
	}
	if ((nWidth == 4608.0d && nHeight == 2592.0d) || (nWidth == 2592.0d && nHeight == 4608.0d)) {
		return "12 MW";
	}
	if ((nWidth == 4320.0d && nHeight == 2880.0d) || (nWidth == 2880.0d && nHeight == 4320.0d)) {
		return "12 MP";
	}
	if ((nWidth == 4320.0d && nHeight == 2432.0d) || (nWidth == 2432.0d && nHeight == 4320.0d)) {
		return "10 MW";
	}
	if ((nWidth == 3648.0d && nHeight == 2736.0d) || (nWidth == 2736.0d && nHeight == 3648.0d)) {
		return "10 M";
	}
	if ((nWidth == 2832.0d && nHeight == 2832.0d) || (nWidth == 2832.0d && nHeight == 2832.0d)) {
		return "8 M";
	}
	if ((nWidth == 2592.0d && nHeight == 1944.0d) || (nWidth == 1944.0d && nHeight == 2592.0d)) {
		return "5 M";
	}
	if ((nWidth == 1984.0d && nHeight == 1488.0d) || (nWidth == 1488.0d && nHeight == 1984.0d)) {
		return "3 M";
	}
	if ((nWidth == 1920.0d && nHeight == 1080.0d) || (nWidth == 1080.0d && nHeight == 1920.0d)) {
		return "2 MW";
	}
	if ((nWidth == 1024.0d && nHeight == 768.0d) || (nWidth == 768.0d && nHeight == 1024.0d)) {
		return "1 M";
	}*/
