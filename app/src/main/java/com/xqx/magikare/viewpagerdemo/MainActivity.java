package com.xqx.magikare.viewpagerdemo;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private PagerAdapter viewPagerAdapter;
    /*父容器，内部为动态添加的示意点*/
    private LinearLayout lyDot ;
    /*示意点不是的颜色值*/
    private String unSelectColor = "#476990";
    /*示意点的颜色值*/
    private String SelectColor = "#1d2939" ;

    /*轮播图图片资源*/
    private ArrayList<View> viewPagerData;

    /*Viewpager当前位置*/
    private int currentPosition = 0;

    ImageView imageView;
    /*延迟发送实现轮播*/
    private Handler handler;

    /* 当前activity是否存活，当为false的时候结束viewpager轮播线程*/
    private boolean actIsAlive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        /*数据初始化*/
        initData();
        initDots();
        initViewpager();
        initHandler();
    }

    private void initView() {
        lyDot = (LinearLayout) findViewById(R.id.lyDot);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actIsAlive = true; //
        autoViewPager();
    }

    /**
     * viewpager自动播放
     */
    private void autoViewPager() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (actIsAlive) {
                    try {
                        sleep(3000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 动态创建轮播图位置点显示
     */
    private void initDots() {
        // 动态添加轮播图位置点 , 默认第0个位置 为当前轮播图的颜色
        for (int i = 0; i < viewPagerData.size(); i++) {
            imageView = new ImageView(this);
            if (i==0) {
                imageView.setBackgroundColor(Color.parseColor(SelectColor));
            }else{
                imageView.setBackgroundColor(Color.parseColor(unSelectColor));
            }
            imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(8), dip2px(8)));
            setMargins(imageView,dip2px(2),0,dip2px(2),0);
            lyDot.addView(imageView);
        }
    }

    private void initData() {
        viewPagerData = new ArrayList<>();
        ImageView imageView = new ImageView(this);
        /*添加图片资源，实际开发中为for循环即可 ，这里demo麻烦了*/
        // 第一张图片
        imageView.setBackgroundResource(R.mipmap.aaa);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        viewPagerData.add(imageView);

        // 第二张图片
        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundResource(R.mipmap.bbb);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        viewPagerData.add(imageView2);

        // 第三张图片
        ImageView imageView3 = new ImageView(this);
        imageView3.setBackgroundResource(R.mipmap.ccc);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        viewPagerData.add(imageView3);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置view的margin
     * @param v           view
     * @param l           left
     * @param t           top
     * @param r           right
     * @param b           bottom
     */
    public void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private void initViewpager() {
        //数据适配器
        viewPagerAdapter = new PagerAdapter() {
            private int mChildCount = 0;

            @Override
            public void notifyDataSetChanged() {
                mChildCount = getCount();
                super.notifyDataSetChanged();
            }

            @Override
            public int getItemPosition(Object object) {
                if (mChildCount > 0) {
                    mChildCount--;
                    return POSITION_NONE;
                }
                return super.getItemPosition(object);
            }

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return viewPagerData.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //是从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(viewPagerData.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(viewPagerData.get(arg1));
                return viewPagerData.get(arg1);
            }
        };

        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(7);
//        viewpager.setPageTransformer(true, new ScaleInTransformer());
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                for (int i = 0; i < lyDot.getChildCount(); i++) {
                    if (i == currentPosition) {
                        lyDot.getChildAt(i).setBackgroundColor(Color.parseColor("#1d2939"));
                    } else {
                        lyDot.getChildAt(i).setBackgroundColor(Color.parseColor("#476990"));
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // 没有滑动的时候 切换页面
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (currentPosition==viewPagerData.size()-1){
                        currentPosition = 0 ;
                        viewpager.setCurrentItem(0,false);
                    }else{
                        currentPosition ++;
                        viewpager.setCurrentItem(currentPosition,true);
                    }
                }
            }
        };
    }

}
