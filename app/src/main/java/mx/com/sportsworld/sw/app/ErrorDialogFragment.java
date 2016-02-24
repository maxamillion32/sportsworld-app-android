package mx.com.sportsworld.sw.app;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ErrorDialogFragment extends SherlockDialogFragment {
	// Global field to contain the error dialog
	private Dialog mDialog;

	public ErrorDialogFragment() {
	}

	public static final ErrorDialogFragment newInstance(Dialog dialog) {
		final ErrorDialogFragment df = new ErrorDialogFragment();
		df.setDialog(dialog);
		return df;
	}

	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}
}
