package com.example.wongweihsuan.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
Context context;
LayoutInflater layoutInflater;

public SliderAdapter(Context context){
    this.context=context;
}

public int[] slide_images={
       
};

public String[] slide_headings = {

        "WELCOME TO VATM",
        "CASHBACK AMOUNT",
        "GET MONEY"
    };

public String[] slide_descs={
        "Join the VATM. Enjoy getting cash back at home instead of going out.",
        "Choosing any amount what you want but less than 200.",
        "Deliver cash back money to you with your food."

};

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout) object;
    }

    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_header);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_des);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object ){
        container.removeView((RelativeLayout)object);
    }
}
