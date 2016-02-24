package mx.com.sportsworld.sw.fragment;

import mx.com.sportsworld.sw.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class WebFragment extends Fragment {
	private String url;
	private WebView web_view;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.web_view, container, false);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Bundle args = getArguments();
		web_view = (WebView) getActivity().findViewById(R.id.webView1);
		if(args != null){
			url = args.getString("url");
		}else{
			url ="http://upster.com.mx/";
		}
		web_view.loadUrl(url);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	//Sirve para guardar dentro del Bundle cierto estado, esto es la posicion actual en donde estamos
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}
	
	
}
