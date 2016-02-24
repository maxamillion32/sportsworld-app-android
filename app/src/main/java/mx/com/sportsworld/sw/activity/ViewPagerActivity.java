package mx.com.sportsworld.sw.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.PagerAdapterFragment;

/**
 * Created by LGarcia on 05/06/2015.
 */
public class ViewPagerActivity extends Activity {

    ViewPager viewPager;
    PagerAdapter adapter;
    int [] images;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager_upster);


        images = new int[] {R.drawable.pantalla_estas_listo, R.drawable.pantalla_filosofia, R.drawable.pantalla_que_buscamos };
        if(getScreenOrientation() == 1){
            images = new int[] {R.drawable.pantalla_estas_listo, R.drawable.pantalla_filosofia, R.drawable.pantalla_que_buscamos };
        }
        if(getScreenOrientation() == 2){
            images = new int[] {R.drawable.pantalla_estas_listo_land, R.drawable.pantalla_filosofia_land, R.drawable.pantalla_que_buscamos_land };
        }

        viewPager = (ViewPager) findViewById(R.id.pagerUpster);

        adapter = new PagerAdapterFragment(ViewPagerActivity.this, images);

        viewPager.setAdapter(adapter);
    }
    public int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
}
