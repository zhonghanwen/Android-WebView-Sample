package com.zhw.webview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by zhonghanwen on 15/2/3.
 */
public class TitleBar extends LinearLayout {

    TextView mLeftTextView;
    TextView mRightTextView;
    public View mBackView;
    TextView mRightLine;
    TextView mLeftLine;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.titlebar_view, this);

        if (!isInEditMode()) {
            mLeftTextView = (TextView) findViewById(R.id.leftTextView);
            mRightTextView = (TextView) findViewById(R.id.rightTextView);
            mBackView = (View) findViewById(R.id.backView);
        }
    }





    public void setBackViewOnClickListener(OnClickListener listener) {
        mBackView.setOnClickListener(listener);
    }

    public void setBackViewIsGone() {
        mBackView.setVisibility(View.GONE);
        mLeftLine.setVisibility(View.GONE);
    }

    public void setBackClick(final Activity activity){
        mBackView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public void setLeftText(String text) {
        mLeftTextView.setText(text);
    }

    public void setLeftViewOnClickListener(OnClickListener listener) {
        mLeftTextView.setOnClickListener(listener);
    }

    public void setLeftViewIsGone() {
        mLeftTextView.setVisibility(View.INVISIBLE);
    }



    public void setRightText(String text) {
        mRightTextView.setText(text);
    }

    public void setRightView(int drawableId) {
    }

    public void setRightView(String text, int drawableId) {
        mRightTextView.setText(text);
    }

    public void setRightViewOnClickListener(OnClickListener listener) {
        mRightTextView.setOnClickListener(listener);
    }

    public void setRightViewIsGone() {
        mRightTextView.setVisibility(View.INVISIBLE);
    }

    public void setRightViewIsVisible() {
        mRightTextView.setVisibility(View.VISIBLE);
    }

    public void setRightViewClickable(boolean clickable){
        mRightTextView.setClickable(clickable);
    }
}
