package com.whiteandc.capture.data;


import android.graphics.drawable.Drawable;

public class Monument implements Comparable{
	
	private String name= null;
	private int[] photos= null;
	private boolean captured= false;
	private String capturedImg= null;
	private String description=  "";
    private Drawable mainPicture = null;

	public Monument(String name, int[] photoList, boolean captured, String capturedImage, String description){
		this.name= name;
		this.photos= photoList;
		this.captured= captured;
		this.capturedImg= capturedImage;
        this.description = description;
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
