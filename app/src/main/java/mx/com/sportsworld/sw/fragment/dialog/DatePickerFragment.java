package mx.com.sportsworld.sw.fragment.dialog;
/**	
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class DatePickerFragment.
 */
public class DatePickerFragment extends DialogFragment {
	
	/** The tag. */
	public static String TAG="Date Picker";
	
	/** The ondate set. */
	OnDateSetListener ondateSet;

	/**
	 * Instantiates a new date picker fragment.
	 */
	public DatePickerFragment() {
	}

	/**
	 * Sets the call back.
	 *
	 * @param ondate the new call back
	 */
	public void setCallBack(OnDateSetListener ondate) {
		ondateSet = ondate;
	}

	/** The day. */
	private static int year, month, day;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
	}
	

}