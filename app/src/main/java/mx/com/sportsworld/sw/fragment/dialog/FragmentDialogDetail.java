package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentDialogDetail.
 */
public class FragmentDialogDetail extends DialogFragment {
	
	/** The img_detail_pic. */
	private ImageView img_detail_pic;
	
	/** The txt_detalle_nombre. */
	private TextView txt_detalle_nombre;
	
	/** The txt_detalle_description. */
	private TextView txt_detalle_description;
	
	/** The item pojo. */
	private static AwardItemPojo itemPojo;
	
	/** The bitmap. */
	private static Bitmap bitmap;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * New instance.
	 *
	 * @param pojo the pojo
	 * @param bit the bit
	 * @return the fragment dialog detail
	 */
	public static FragmentDialogDetail newInstance(AwardItemPojo pojo,
			Bitmap bit) {
		itemPojo = pojo;
		bitmap = bit;
		return new FragmentDialogDetail();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.alert_dialog_detalle, null);
		img_detail_pic = (ImageView) view.findViewById(R.id.img_detail_pic);
		txt_detalle_nombre = (TextView) view
				.findViewById(R.id.txt_detalle_nombre);
		txt_detalle_description = (TextView) view
				.findViewById(R.id.txt_detalle_description);

		if (bitmap != null)
			img_detail_pic.setImageBitmap(bitmap);

		txt_detalle_nombre.setText(itemPojo.getPremio());
		txt_detalle_description.setText(itemPojo.getDescription());
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		bitmap = null;
		itemPojo = null;
		dialgoFragment = null;
		super.onDestroy();
	}
}
