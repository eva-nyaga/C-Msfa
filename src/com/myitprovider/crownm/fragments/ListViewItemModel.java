package com.myitprovider.crownm.fragments;

public class ListViewItemModel {

	public int title;
	public int iconRes;
	public boolean isHeader;

	public ListViewItemModel(int title, int iconRes, boolean header) {
		this.title = title;
		this.iconRes = iconRes;
		this.isHeader = header;
	}

	public ListViewItemModel(int title, int iconRes) {
		this(title, iconRes, false);
	}
	
	

}
