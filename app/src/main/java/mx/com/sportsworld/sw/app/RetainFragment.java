package mx.com.sportsworld.sw.app;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class RetainFragment extends Fragment {

	private Object mObject;
	private static final String FRAG_TAG_RETAIN = "frag_tag_retain";

	public RetainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public Object getObject() {
		return mObject;
	}

	public void setObject(Object object) {
		mObject = object;
	}

	public static final Fragment findOrCreateInstance(FragmentManager fm) {
		Fragment f = fm.findFragmentByTag(FRAG_TAG_RETAIN);
		if (f == null) {
			f = new RetainFragment();
			fm.beginTransaction().add(f, FRAG_TAG_RETAIN).commit();
		}
		return f;
	}

	public static final Fragment findOrCreateInstance(FragmentManager fm,
			String tag) {
		Fragment f = fm.findFragmentByTag(tag);
		if (f == null) {
			f = new RetainFragment();
			fm.beginTransaction().add(f, tag).commit();
		}
		return f;
	}

}
