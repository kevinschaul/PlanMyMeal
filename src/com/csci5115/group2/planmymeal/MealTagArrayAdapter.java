package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MealTagArrayAdapter extends ArrayAdapter<Tag>
{

	private final Context context;
	private LinkedList<Tag> values;

	public MealTagArrayAdapter(Context context, LinkedList<Tag> tags) {
		super(context, R.layout.row_tag, tags);
		this.context = context;
		this.values = tags;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_tag, parent, false);
	    TextView tagName = (TextView) rowView.findViewById(R.id.row_tag_tagName);
	    
	    Tag tag = values.get(position);
	    tagName.setText(tag.getName());

	    return rowView;
	}
}
