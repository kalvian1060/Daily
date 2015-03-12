package com.aspsine.zhihu.daily.adapter.holder;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.entity.Story;
import com.aspsine.zhihu.daily.interfaces.OnItemClickListener;
import com.aspsine.zhihu.daily.interfaces.OnItemLongClickListener;
import com.aspsine.zhihu.daily.ui.widget.CirclePageIndicator;
import com.aspsine.zhihu.daily.ui.widget.MyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/3/11.
 */
public class HeaderViewPagerHolder extends RecyclerView.ViewHolder {
    public List<Story> mStories;
    private List<ImageView> mImages;
    private TextView title;
    public MyViewPager viewPager;
    private CirclePageIndicator indicator;
    private PagerAdapter mPagerAdapter;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private DisplayImageOptions mOptions;

    public HeaderViewPagerHolder(@Nullable View itemView) {
        super(itemView);
        // TODO findViewById
        title = (TextView) itemView.findViewById(R.id.title);
        viewPager = (MyViewPager) itemView.findViewById(R.id.viewPager);
        indicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        this.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public HeaderViewPagerHolder(@Nullable View itemView, OnItemClickListener onItemClickListener,
                                 OnItemLongClickListener onItemLongClickListener) {
        this(itemView);

        this.mOnItemClickListener = onItemClickListener;
        this.mOnItemLongClickListener = onItemLongClickListener;

        // TODO Blind action to views
    }

    public void bindData(List<Story> stories, View itemView) {
        mStories = stories;
        int size = mStories == null ? 0 : mStories.size();
        mImages = new ArrayList<ImageView>(size);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(finalI, v);
                }
            });
            mImages.add(imageView);
        }
    }

    public void notifyDataSetChanged() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new HeaderPagerAdapter();
            viewPager.setAdapter(mPagerAdapter);
            indicator.setViewPager(viewPager);
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    title.setText(String.valueOf(mStories.get(position).getTitle()));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            title.setText(String.valueOf(mStories.get(0).getTitle()));
            viewPager.startAutoScroll();
        } else {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    protected class HeaderPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mStories == null ? 0 : mStories.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImages.get(position);
            container.addView(imageView);
            ImageLoader.getInstance().displayImage(mStories.get(position).getImage(), imageView, mOptions);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImages.get(position));
        }
    }
}