package com.util.core.photopreview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<PhotoModel> photoModels = new ArrayList<>();

	public List<PhotoModel> getPhotoModels() {
		return photoModels;
	}

	public void setPhotoModels(List<PhotoModel> photoModels) {
		this.photoModels = photoModels;
	}
	
	
}
