package com.example.vikikkdi.askit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vikikkdi on 10/20/17.
 */

public class ValueAdapter extends BaseAdapter implements Filterable {
    private ArrayList<String> mStringList;
    private ArrayList<String> mStringFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;
    public ValueAdapter(ArrayList<String> mStringList, Context context) {
        this.mStringList = mStringList;
        this.mStringFilterList = mStringList;
        mInflater = LayoutInflater.from(context);
        getFilter();
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder viewHolder;
        if (convertView == null) {
            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.txt_listitem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.nameTv.setText(mStringList.get(position).toString());
        return convertView;
    }

    private class Holder {
        TextView nameTv;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<String>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (mStringFilterList.get(i).contains(constraint)) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mStringList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}