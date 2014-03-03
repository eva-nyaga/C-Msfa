package com.myitprovider.crownm.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crownm.R;





public class ListViewAdapter extends ArrayAdapter<ListViewItemModel> {

	public ListViewAdapter(Context context) {
		super(context, 0);
	}

	public void addHeader(int title) { //expects The Title for the header as an arugment to it
		add(new ListViewItemModel(title, -1, true));//add the object to the Bottom of the array
	}

	public void addItem(int title, int icon) {
		add(new ListViewItemModel(title, icon, false));
	}

	public void addItem(ListViewItemModel itemModel) {
		add(itemModel);
	}

	@Override
	public int getViewTypeCount() {  //Returns the number of types of Views that will be created by getView(int, View, ViewGroup).
		return 2; //we will create 2 types of views
	}

	@Override
	public int getItemViewType(int position) { //framework calls getItemViewType for row n, the row it is about to display.
		//Get the type of View that will be created by getView(int, View, ViewGroup) for the specified item.
		return getItem(position).isHeader ? 0 : 1; // get position passes (n) and accertain  is its a header  or not
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader;
	}

	public static class ViewHolder {
		public final TextView textHolder;
		public final ImageView imageHolder;

		public ViewHolder(TextView text1, ImageView image1) {
			this.textHolder = text1;
			this.imageHolder = image1;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
      //Abstract View --> Get a View that displays the data at the specified position in the data set.
		ListViewItemModel item = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			int layout = R.layout.navigation_list_view_row;
			if (item.isHeader)
				layout = R.layout.navigation_list_view_row_header;

			view = LayoutInflater.from(getContext()).inflate(layout, null);

			TextView text1 = (TextView) view.findViewById(R.id.menurow_title);
			ImageView image1 = (ImageView) view.findViewById(R.id.menurow_icon);
			view.setTag(new ViewHolder(text1, image1));
		}

		if (holder == null && view != null) {
			Object tag = view.getTag();
			if (tag instanceof ViewHolder) {
				holder = (ViewHolder) tag;
			}
		}

		if (item != null && holder != null) {
			if (holder.textHolder != null)

				holder.textHolder.setText(item.title);

			if (holder.imageHolder != null) {
				if (item.iconRes > 0) {

					holder.imageHolder.setVisibility(View.VISIBLE);
					holder.imageHolder.setImageResource(item.iconRes);
				} else {
					holder.imageHolder.setVisibility(View.GONE);
				}
			}
		}

		return view;
	}

}
