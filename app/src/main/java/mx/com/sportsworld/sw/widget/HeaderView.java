package mx.com.sportsworld.sw.widget;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;

/**
 * The Class HeaderView.
 */
public class HeaderView extends LinearLayout {
	
	/** The root view. */
	private View rootView = null;
	
	/** The level contaniner. */
	private View levelContaniner = null;
	
	/** The goal container. */
	private View goalContainer = null;
	
	/** The txt name. */
	private TextView txtName = null;
	
	/** The txt objetive. */
	private TextView txtObjetive = null;
	
	/** The txt level. */
	private TextView txtLevel = null;
	
	/**
	 * Instantiates a new header view.
	 * 
	 * @param context
	 *            the context
	 */
	public HeaderView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Inits the.
	 */
	public void init(){
		rootView = LayoutInflater.from(getContext()).inflate(R.layout.header_rutinas_selection, null);
		goalContainer=rootView.findViewById(R.id.goalContainer);
		levelContaniner = rootView.findViewById(R.id.levelContainer);
		txtName = (TextView)rootView.findViewById(R.id.userNameLabel);
		txtObjetive = (TextView)rootView.findViewById(R.id.objetiveNameLabel);
		txtLevel = (TextView)rootView.findViewById(R.id.levelNameLabel);
		addView(rootView);
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name){
		txtName.setText(name);
	}
	
	/**
	 * Sets the objetive.
	 * 
	 * @param objetive
	 *            the new objetive
	 */
	public void setObjetive(String objetive){
		txtObjetive.setText(objetive);
		if(objetive.trim().length() > 0)
			goalContainer.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Sets the level.
	 * 
	 * @param level
	 *            the new level
	 */
	public void setLevel(String level){
		txtLevel.setText(level);
		if(level.trim().length() > 0)
			levelContaniner.setVisibility(View.VISIBLE);
	}
	
}
