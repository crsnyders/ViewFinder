package za.co.snyders.viewfinder.cameraModel;
import java.io.*;
import org.cybergarage.xml.*;
import za.co.snyders.viewfinder.cameraModel.*;
import java.util.*;

public class CameraInfo implements Serializable{
	private String videoUrl;
	private List<CameraResolution> resolutions = new ArrayList<>();
	private Flash flash= new Flash();
	private ZoomInfo zoomInfo;
	private Long avaiableShots;
	private int rotationt;
	
	
	public CameraInfo(Node cameraNode){
		Node cameraInfo = cameraNode.getNode("GETINFORMATIONRESULT");
		Node resolutionsNode = cameraInfo.getNode("Resolutions");
		Node flashNode = cameraInfo.getNode("Flash");
		Node zoomInfoNode = cameraInfo.getNode("ZoomInfo");
		
		for(int i =0;i< resolutionsNode.getNNodes();i++){
			if(resolutionsNode.getNode(i).getName().equals("Resolution")){
				resolutions.add(new CameraResolution(resolutionsNode.getNode(i).getNodeValue("Width"),resolutionsNode.getNode(i).getNodeValue("Height")));
			}
		}
		for(int i =0;i< flashNode.getNNodes();i++){
			if(flashNode.getNode(i).getName().equals("Support")){
				this.flash.addFlashSupport(flashNode.getNode(i).getValue());
				}
		}
		this.zoomInfo = new ZoomInfo(Integer.parseInt(zoomInfoNode.getNodeValue("DefaultZoom")),Integer.parseInt(zoomInfoNode.getNodeValue("MaxZoom")));
		this.rotationt = Integer.parseInt(cameraInfo.getNodeValue("ROTATION"));
		this.avaiableShots = Long.parseLong(cameraInfo.getNodeValue("AVAILSHOTS"));
		this.videoUrl = cameraNode.getNodeValue("GETIPRESULT");
	}
	
	public String getVideoUrl(){
		return this.videoUrl;
	}
	public ZoomInfo getZoomInfo(){
		return this.zoomInfo;
	}
	public int getRotation(){
		return this.rotationt;
	}
	public List<CameraResolution> getResolutions(){

		return this.resolutions;
	}
	
	public Long getAvailableShots(){
	return this.avaiableShots;
	}
	public Flash getFlash(){
		return this.flash;
	}
//					<GETINFORMATIONRESULT>
//         <Resolutions>
//            <Resolution>
//               <Width>4320</Width>
//               <Height>2432</Height>
//            </Resolution>
//            <Resolution>
//               <Width>1920</Width>
//               <Height>1080</Height>
//            </Resolution>
//         </Resolutions>
//         <Flash>
//            <Support>off</Support>
//            <Support>auto</Support>
//         </Flash>
//         <ZoomInfo>
//            <DefaultZoom>0</DefaultZoom>
//            <MaxZoom>11</MaxZoom>
//         </ZoomInfo>
//         <AVAILSHOTS>310</AVAILSHOTS>
//         <ROTATION>1</ROTATION>
//      </GETINFORMATIONRESULT>
//      <GETIPRESULT>http://192.168.1.104:9001/dcim/livestream.avi</GETIPRESULT>
}
