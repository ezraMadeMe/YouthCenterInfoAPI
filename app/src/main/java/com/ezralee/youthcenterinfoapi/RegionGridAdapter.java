package com.ezralee.youthcenterinfoapi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegionGridAdapter extends BaseAdapter {

    Context context;
    TextView tvRegion;
    String[] regions = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};


    public RegionGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return regions.length;
    }

    @Override
    public Object getItem(int i) {
        return regions[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        tvRegion = new TextView(context.getApplicationContext());
        tvRegion.setText(regions[i]);
        tvRegion.setTextSize(28);
        tvRegion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvRegion.setTag(false);

        return tvRegion;
    }
}
