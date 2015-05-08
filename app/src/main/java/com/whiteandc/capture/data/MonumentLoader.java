package com.whiteandc.capture.data;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.whiteandc.capture.R;


public class MonumentLoader {
	private static final String CLASS = "MonumentLoader";

	public static void loadMonuments(SharedPreferences sharedpreferences){
		
		addMonument(sharedpreferences, "Teatro Albeniz", new int[]{R.drawable.albeniz});
		addMonument(sharedpreferences, "Alcazaba", new int[]{R.drawable.alcazaba_cartel, R.drawable.alcazaba_cuesta, R.drawable.alcazaba_dentro, R.drawable.alcazaba_entrada, R.drawable.alcazaba_fuente, R.drawable.alcazaba_puerta_columnas});
		addMonument(sharedpreferences, "Catedral", new int[]{R.drawable.catedral_1, R.drawable.catedral_2, R.drawable.catedral_3, R.drawable.catedral_4, R.drawable.catedral_5, R.drawable.catedral_6, R.drawable.catedral_7, R.drawable.catedral_8, R.drawable.catedral_9, R.drawable.catedral_10, R.drawable.catedral_11, R.drawable.catedral_12});
		addMonument(sharedpreferences, "Larios", new int[]{R.drawable.larios, R.drawable.larios_1, R.drawable.larios_2, R.drawable.larios_3, R.drawable.larios_4, R.drawable.larios_banco, R.drawable.larios_cara, R.drawable.larios_cara_1, R.drawable.larios_paloma, R.drawable.larios_pensador, R.drawable.larios_plaza});
		addMonument(sharedpreferences, "Mercado", new int[]{R.drawable.mercado});
		addMonument(sharedpreferences, "Plaza de la Merced", new int[]{R.drawable.merced_picasso, R.drawable.merced_picasso_1, R.drawable.merced_picasso_2, R.drawable.merced_picasso_3, R.drawable.picasso_casa, R.drawable.picasso_casa_3, R.drawable.plaza_merced, R.drawable.plaza_merced_1, R.drawable.plaza_merced_2, R.drawable.plaza_merced_3, R.drawable.plaza_merced_4, R.drawable.plaza_merced_5, R.drawable.plaza_merced_6, R.drawable.plaza_merced_7});
		addMonument(sharedpreferences, "Muelle Uno", new int[]{R.drawable.muelle_alcazaba, R.drawable.muelle_capilla, R.drawable.muelle_catedral, R.drawable.muelle_faro, R.drawable.muelle_faro2, R.drawable.muelle_palmeras, R.drawable.muelle_palmeras2, R.drawable.muelle_palmeras3});
		addMonument(sharedpreferences, "Museo Picasso", new int[]{R.drawable.museo_picasso_entrada, R.drawable.museo_picasso_entrada2, R.drawable.museo_picasso_patio});
		addMonument(sharedpreferences, "Piramide", new int[]{R.drawable.piramide, R.drawable.piramide_1, R.drawable.piramide_2, R.drawable.piramide_3});
		addMonument(sharedpreferences, "Teatro romano", new int[]{R.drawable.teatro_romano_alta, R.drawable.teatro_romano_baja, R.drawable.teatro_romano_baja2, R.drawable.teatro_romano_cartel, R.drawable.teatroromano_entrada, R.drawable.teatroromano_entrada_1});
		addMonument(sharedpreferences, "Test", new int[]{R.drawable.test1,R.drawable.test2});
	}

	private static void addMonument(SharedPreferences sharedpreferences, String name, int [] photos) {
		boolean captured= false;
		if(sharedpreferences.contains(name)){
			Log.i(CLASS, "Obteniendo valor de key: "+ name);
			String value= sharedpreferences.getString(name, "0");
			captured= value.equals("true");
		}else{
			Log.i(CLASS, "NO existe "+name+" en SharedPreferences, creando key");
			Editor editor = sharedpreferences.edit();
			editor.putString(name, "false");
			editor.commit(); 
		}
		Log.i(CLASS, "Anadiendo a la lista monumento: "+name+ ((captured)?" capturado": " NO capturado"));
		MonumentList.addItem(new Monument(name, photos,captured,"IMG_20140603_201814.jpg"));
	}
}
