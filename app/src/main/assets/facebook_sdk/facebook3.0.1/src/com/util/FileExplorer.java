package com.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.android.R;

public class FileExplorer extends DialogFragment implements OnItemClickListener{
	
	private List<String> item = null;
	private List<String> path = null;
	private String root;
	private TextView myPath;
	private ListView lView = null;
	public static OnFileSelectedListener listener = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().setTitle("Selecciona un archivo");
		ViewGroup linLay = (ViewGroup)inflater.inflate(R.layout.explorer_layout, null);
		myPath = (TextView)linLay.findViewById(R.id.path);
		lView = (ListView)linLay.findViewById(R.id.listafiles);
        root = Environment.getExternalStorageDirectory().getPath();
        lView.setOnItemClickListener(this);
        getDir(root);
		
		return linLay;
	}
	
	private void getDir(String dirPath)
    {
    	myPath.setText("Location: " + dirPath);
    	item = new ArrayList<String>();
    	path = new ArrayList<String>();
    	File f = new File(dirPath);
    	File[] files = f.listFiles();
    	
    	if(!dirPath.equals(root))
    	{
    		item.add(root);
    		path.add(root);
    		item.add("../");
    		path.add(f.getParent());	
    	}
    	
    	for(int i=0; i < files.length; i++)
    	{
    		File file = files[i];
    		
    		if(!file.isHidden() && file.canRead()){
    			path.add(file.getPath());
        		if(file.isDirectory()){
        			item.add(file.getName() + "/");
        		}else{
        			item.add(file.getName());
        		}
    		}	
    	}

    	ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity(), R.layout.rowexplorer, item);
    	lView.setAdapter(fileList);	
    }

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		// TODO Auto-generated method stub
			File file = new File(path.get(position));	
				if (file.isDirectory())
				{
					if(file.canRead()){
						getDir(path.get(position));
					}else{
						new AlertDialog.Builder(getActivity())
							.setIcon(R.drawable.ic_launcher)
							.setTitle("[" + file.getName() + "] folder can't be read!")
							.setPositiveButton("OK", null).show();	
					}	
				}else {
					if(listener!=null){
						listener.OnFileSelected(file);
						getDialog().dismiss();
					}else{
						new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.ic_launcher)
						.setTitle("[" + file.getName() + "]")
						.setPositiveButton("OK", null).show();
					}
				}
	}
	
	public interface OnFileSelectedListener{
		public void OnFileSelected(File file);
	}
}
