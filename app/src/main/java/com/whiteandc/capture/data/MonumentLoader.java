package com.whiteandc.capture.data;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.whiteandc.capture.R;


public class MonumentLoader {
	private static final String CLASS = "MonumentLoader";
    public static final String FUENTE_CIBELES = "Fuente de Cibeles";
    public static final String PUERTA_ALCALA = "Puerta de Alcalá";
    public static final String CALLE_ALCALA = "Calle de Alcalá";
    public static final String CATEDRAL_ALMUDENA = "Catedral de la Almudena";
    public static final String TEMPLO_DEBOD = "Templo de Debod";
    public static final String PALACIO_REAL = "Palacio Real";
    public static final String PLAZA_MAYOR = "Plaza mayor";
    public static final String RETIRO = "Parque del Retiro";
    public static final String SOL = "Puerta del Sol";
    public static final String PALACIO_COMU = "Palacio de Comunicaciones";
    public static final String TEATRO = "Teatro Real";
    public static final String TELEFERICO = "Teleférico";


	public static void loadMonuments(SharedPreferences sharedpreferences){
		
		/*addMonument(sharedpreferences, "Teatro Albeniz", new int[]{R.drawable.albeniz});
		addMonument(sharedpreferences, "Alcazaba", new int[]{R.drawable.alcazaba_cartel, R.drawable.alcazaba_cuesta, R.drawable.alcazaba_dentro, R.drawable.alcazaba_entrada, R.drawable.alcazaba_fuente, R.drawable.alcazaba_puerta_columnas});
		addMonument(sharedpreferences, "Catedral", new int[]{R.drawable.catedral_1, R.drawable.catedral_2, R.drawable.catedral_3, R.drawable.catedral_4, R.drawable.catedral_5, R.drawable.catedral_6, R.drawable.catedral_7, R.drawable.catedral_8, R.drawable.catedral_9, R.drawable.catedral_10, R.drawable.catedral_11, R.drawable.catedral_12});
		addMonument(sharedpreferences, "Larios", new int[]{R.drawable.larios, R.drawable.larios_1, R.drawable.larios_2, R.drawable.larios_3, R.drawable.larios_4, R.drawable.larios_banco, R.drawable.larios_cara, R.drawable.larios_cara_1, R.drawable.larios_paloma, R.drawable.larios_pensador, R.drawable.larios_plaza});
		addMonument(sharedpreferences, "Mercado", new int[]{R.drawable.mercado});
		addMonument(sharedpreferences, "Plaza de la Merced", new int[]{R.drawable.merced_picasso, R.drawable.merced_picasso_1, R.drawable.merced_picasso_2, R.drawable.merced_picasso_3, R.drawable.picasso_casa, R.drawable.picasso_casa_3, R.drawable.plaza_merced, R.drawable.plaza_merced_1, R.drawable.plaza_merced_2, R.drawable.plaza_merced_3, R.drawable.plaza_merced_4, R.drawable.plaza_merced_5, R.drawable.plaza_merced_6, R.drawable.plaza_merced_7});
		addMonument(sharedpreferences, "Muelle Uno", new int[]{R.drawable.muelle_alcazaba, R.drawable.muelle_capilla, R.drawable.muelle_catedral, R.drawable.muelle_faro, R.drawable.muelle_faro2, R.drawable.muelle_palmeras, R.drawable.muelle_palmeras2, R.drawable.muelle_palmeras3});
		addMonument(sharedpreferences, "Museo Picasso", new int[]{R.drawable.museo_picasso_entrada, R.drawable.museo_picasso_entrada2, R.drawable.museo_picasso_patio});
		addMonument(sharedpreferences, "Piramide", new int[]{R.drawable.piramide, R.drawable.piramide_1, R.drawable.piramide_2, R.drawable.piramide_3});
		addMonument(sharedpreferences, "Teatro romano", new int[]{R.drawable.teatro_romano_alta, R.drawable.teatro_romano_baja, R.drawable.teatro_romano_baja2, R.drawable.teatro_romano_cartel, R.drawable.teatroromano_entrada, R.drawable.teatroromano_entrada_1});
		addMonument(sharedpreferences, "Test", new int[]{R.drawable.test1,R.drawable.test2});*/
        addMonument(sharedpreferences, FUENTE_CIBELES, new int[]{R.drawable.cibeles_480},  new LatLng(40.419385, -3.692996));
        addMonument(sharedpreferences, PUERTA_ALCALA, new int[]{R.drawable.alcala2},  new LatLng(40.419992, -3.688640));
        addMonument(sharedpreferences, CALLE_ALCALA, new int[]{R.drawable.calle_alcala},  new LatLng(40.418659, -3.697051));
        addMonument(sharedpreferences, CATEDRAL_ALMUDENA, new int[]{R.drawable.catedral_almudena},  new LatLng(40.415621, -3.714648));
        addMonument(sharedpreferences, TEMPLO_DEBOD, new int[]{R.drawable.templo_debod},  new LatLng(40.424034, -3.717801));
        addMonument(sharedpreferences, PALACIO_REAL, new int[]{R.drawable.palacio_real, R.drawable.palacio_real1,
                                                                R.drawable.palacio_real2, R.drawable.palacio_real3,
                                                                R.drawable.palacio_real4},
                                                                new LatLng(40.417967, -3.714291));
        addMonument(sharedpreferences, PLAZA_MAYOR, new int[]{R.drawable.plaza_mayor, R.drawable.plaza_mayor1,
                                                                R.drawable.plaza_mayor2, R.drawable.plaza_mayor3,
                                                                R.drawable.plaza_mayor4, R.drawable.plaza_mayor5},
                                                                new LatLng(40.415520, -3.707417));
        addMonument(sharedpreferences, RETIRO, new int[]{R.drawable.retiro, R.drawable.retiro1,
                                                        R.drawable.retiro2, R.drawable.retiro3,
                                                        R.drawable.retiro4, R.drawable.retiro5,
                                                        R.drawable.retiro6, R.drawable.retiro7,
                                                        R.drawable.retiro8, R.drawable.retiro9,
                                                        R.drawable.retiro10, R.drawable.retiro11,
                                                        R.drawable.retiro12, R.drawable.retiro13,
                                                        R.drawable.retiro14},
                                                        new LatLng(40.415249, -3.684521));
        addMonument(sharedpreferences, SOL, new int[]{R.drawable.puerta_del_sol, R.drawable.puerta_del_sol1,
                                                        R.drawable.puerta_del_sol2, R.drawable.puerta_del_sol3,
                                                        R.drawable.puerta_del_sol4, R.drawable.puerta_del_sol5,
                                                        R.drawable.puerta_del_sol6, R.drawable.puerta_del_sol7,
                                                        R.drawable.puerta_del_sol8},
                                                        new LatLng(40.416935, -3.703539));
        addMonument(sharedpreferences, PALACIO_COMU, new int[]{R.drawable.palacio_de_comunicaciones, R.drawable.palacio_de_comunicaciones1,
                                                        R.drawable.palacio_de_comunicaciones2, R.drawable.palacio_de_comunicaciones3,
                                                        R.drawable.palacio_de_comunicaciones4, R.drawable.palacio_de_comunicaciones5,
                                                        R.drawable.palacio_de_comunicaciones6, R.drawable.palacio_de_comunicaciones7},
                                                        new LatLng(40.418891, -3.692039));
        addMonument(sharedpreferences, TEATRO, new int[]{R.drawable.teatro_real, R.drawable.teatro_real1,
                                                        R.drawable.teatro_real2, R.drawable.teatro_real3,
                                                        R.drawable.teatro_real4, R.drawable.teatro_real5,
                                                        R.drawable.teatro_real6, R.drawable.teatro_real7,
                                                        R.drawable.teatro_real8},
                                                        new LatLng(40.418450, -3.710599));
        addMonument(sharedpreferences, TELEFERICO, new int[]{R.drawable.teleferico, R.drawable.teleferico1,
                                                        R.drawable.teleferico3, R.drawable.teleferico4},
                                                        new LatLng(40.425269, -3.717168));

		//addMonument(sharedpreferences, "Test", new int[]{R.drawable.test1,R.drawable.test2});
	}

	private static void addMonument(SharedPreferences sharedpreferences, String name, int[] photos, LatLng latLng) {
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
		MonumentList.addItem(new Monument(name, photos,captured,"IMG_20140603_201814.jpg", latLng));
	}
}
