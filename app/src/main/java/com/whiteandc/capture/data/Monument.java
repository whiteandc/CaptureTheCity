package com.whiteandc.capture.data;


import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

public class Monument implements Comparable{
	
	private String name= null;
	private int[] photos= null;
	private boolean captured= false;
	private String capturedImg= null;
<<<<<<< HEAD
	private String description=  "";
    private Drawable mainPicture = null;

	public Monument(String name, int[] photoList, boolean captured, String capturedImage, String description){
=======
	private String description= "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private LatLng latLng= null;
    private Drawable mainPicture = null;

	public Monument(String name, int[] photoList, boolean captured, String capturedImage, LatLng latLng){
>>>>>>> upstream/master
		this.name= name;
		this.photos= photoList;
		this.captured= captured;
<<<<<<< HEAD
		this.capturedImg= capturedImage;
        this.description = description;
=======
		capturedImg= capturedImage;
        this.latLng= latLng;
>>>>>>> upstream/master
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

	public String getCapturedImg() {
		return capturedImg;
	}

    public Drawable getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Drawable mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getDescription() {
        return description;
    }
}
