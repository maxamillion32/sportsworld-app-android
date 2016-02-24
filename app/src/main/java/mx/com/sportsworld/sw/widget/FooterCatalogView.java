package com.upster.app.widget;

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

//Footer de la lista de catálogo

/**
 * The Class FooterCatalogView.
 */
public class FooterCatalogView extends LinearLayout {
	
	/** The root view. */
	private View rootView = null;
	
	/** The txt_footer_saldo. */
	TextView txt_footer_saldo;
	
	/** The txt_saldo_redimir. */
	TextView txt_saldo_redimir;
	
	/** The txt_saldo_restante. */
	TextView txt_saldo_restante;

	/**
	 * Instantiates a new footer catalog view.
	 * 
	 * @param context
	 *            the context
	 */
	public FooterCatalogView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Inits the.
	 */
	public void init() {
		rootView = LayoutInflater.from(getContext()).inflate(
				R.layout.footer_catalog, null);
		txt_footer_saldo = (TextView) rootView
				.findViewById(R.id.txt_catalog_saldo);
		txt_saldo_redimir = (TextView) rootView
				.findViewById(R.id.txt_catalog_redimir);
		txt_saldo_restante = (TextView) rootView
				.findViewById(R.id.txt_catalog_restante);
		addView(rootView);
	}

	/**
	 * Sets the saldo.
	 * 
	 * @param texto
	 *            the new saldo
	 */
	public void setSaldo(String texto) {
		txt_footer_saldo.setText(texto);
	}

	/**
	 * Sets the redimir.
	 * 
	 * @param texto
	 *            the new redimir
	 */
	public void setRedimir(String texto) {
		txt_saldo_redimir.setText(texto);
	}

	/**
	 * Sets the restante.
	 * 
	 * @param texto
	 *            the new restante
	 */
	public void setRestante(String texto) {
		txt_saldo_restante.setText(texto);
	}
}
