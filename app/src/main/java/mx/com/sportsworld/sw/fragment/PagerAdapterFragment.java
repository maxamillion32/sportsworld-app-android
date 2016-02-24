package mx.com.sportsworld.sw.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import mx.com.sportsworld.sw.R;

/**
 * Created by LGarcia on 05/06/2015.
 */
public class PagerAdapterFragment extends PagerAdapter {

    Context context;
    int[] images;
    LayoutInflater inflater;

    public PagerAdapterFragment(Context context, int[] images){

        this.context = context;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((RelativeLayout)o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        ImageView imgImages;
        RelativeLayout layout;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.que_es_upster, container, false);
        layout = (RelativeLayout) itemView.findViewById(R.id.pagerLayout);
        //imgImages = (ImageView) itemView.findViewById(R.id.imgUpster);

        Drawable iy;


        //imgImages.setImageResource(images[position]);
        layout.setBackgroundResource(images[position]);


        ((ViewPager)container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager)container).removeView((RelativeLayout)object);
    }
}


























