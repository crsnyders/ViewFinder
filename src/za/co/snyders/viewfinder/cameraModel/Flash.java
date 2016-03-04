package za.co.snyders.viewfinder.cameraModel;
import java.io.*;
import java.util.*;

public class Flash implements Serializable{
	private List<String> support = new ArrayList<>();
	
	public Flash(String ...support){
		this.support.addAll(Arrays.asList(support));
	}
	
	public Flash(){}
	
	public void addFlashSupport(String support){
		this.support.add(support);
	}
	
	public List<String> getSupport(){
		return this.support;
	}
}
