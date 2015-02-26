package com.whiteandc.capture.data;


public class Monument implements Comparable{
	
	private String name= null;
	private int[] photos= null;
	private boolean captured= false;
	private String capturedImg= null;
	private String description= "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";

	public Monument(String name, int[] photoList, boolean captured, String capturedImage){
		this.name= name;
		photos= photoList;
		this.captured= captured;
		capturedImg= capturedImage;
	}
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Object another) {
		return name.compareTo(((Monument) another).name);
	}
	public int[] getPhotos() {
		return photos;
	}
	public void setPhotos(int[] photos) {
		this.photos = photos;
	}
	public boolean isCaptured() {
		return captured;
	}
	public void setIsCaptured(boolean b) {
		captured=b;
	}
	public String getDescription() {
		return description; 
	}
	public String getCapturedImg() {
		return capturedImg;
	}
	

}
