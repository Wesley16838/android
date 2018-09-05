package com.example.wongweihsuan.demo;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class welcome extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    private Button mNextBtn, mBackBtn,mstartBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mSlideViewPager = findViewById(R.id.slideViewPage);
        mDotLayout = findViewById(R.id.dotsLayout);

        mNextBtn=findViewById(R.id.nextBtn);
        mBackBtn=findViewById(R.id.preBtn);
        mstartBtn=findViewById(R.id.start);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });
//
        mBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });

    }

    public void addDotsIndicator(int position) {

        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.white));
    }


        }

        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addDotsIndicator(i);
                mCurrentPage=i;
                if(i==0){
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mstartBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.INVISIBLE);
                    mstartBtn.setVisibility(View.INVISIBLE);
                    mNextBtn.setText("Next");
                    mBackBtn.setText("");
                }else if(i==2){
                    mNextBtn.setEnabled(true);
                    mstartBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);
                    mNextBtn.setVisibility(View.INVISIBLE);
                    mstartBtn.setVisibility(View.VISIBLE);

                    mBackBtn.setText("Back");
                }else{
                    mNextBtn.setEnabled(true);
                    mBackBtn.setEnabled(true);
                    mBackBtn.setVisibility(View.VISIBLE);
                    mstartBtn.setVisibility(View.INVISIBLE);
                    mNextBtn.setVisibility(View.VISIBLE);
                    mNextBtn.setText("Next");
                    mBackBtn.setText("Back");
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

    public void onClick(View v) {

        Intent i = new Intent(welcome.this, login.class);
        startActivity(i);
    }
    }
