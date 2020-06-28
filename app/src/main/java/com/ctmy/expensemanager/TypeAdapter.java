package com.ctmy.expensemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TypeAdapter  extends ArrayAdapter<ProjectType> {

    public TypeAdapter(Context context, ArrayList<ProjectType> projectType){
        super(context, 0, projectType);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_type, parent, false);
        }

        ImageView imgType = convertView.findViewById(R.id.imgType);
        TextView tvDescType = convertView.findViewById(R.id.tv_desc_type);

        ProjectType currentItem = getItem(position);

        if(currentItem != null){
            imgType.setImageResource(currentItem.getTypeImage());
            tvDescType.setText(currentItem.getTypeName());
        }

        return convertView;
    }

}
