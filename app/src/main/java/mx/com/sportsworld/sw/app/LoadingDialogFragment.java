package mx.com.sportsworld.sw.app;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class LoadingDialogFragment extends SherlockDialogFragment {

	private static final String FRAG_ARG_MESSAGE = "frag_arg_message";

	public LoadingDialogFragment() {
	}

	public static LoadingDialogFragment newInstance(String message) {
		final Bundle args = new Bundle();
		args.putString(FRAG_ARG_MESSAGE, message);
		final LoadingDialogFragment df = new LoadingDialogFragment();
		df.setArguments(args);
		return df;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.fragment_loading, null,
				false);

		final TextView txtMessage = (TextView) view
				.findViewById(R.id.txt_message);
		txtMessage.setText(getMessageFromArgs());

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setView(view);

		return builder.create();
	}

	private String getMessageFromArgs() {
		return getArguments().getString(FRAG_ARG_MESSAGE);
	}
}
