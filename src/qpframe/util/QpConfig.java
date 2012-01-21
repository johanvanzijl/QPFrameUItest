package qpframe.util;

import java.io.IOException;

import net.rim.device.api.ui.Color;

public class QpConfig {

	private QpProperties _conf;
	private static QpConfig _handler;
	protected QpPropStore _store;
    protected static String scheme;
    
	protected QpConfig() {
		// Singleton should only be called once!
		try {
			set_conf(QpProperties.loadProperties("/cnmconfig.properties"));
			QpPropStore customProps = QpPropStore.loadPropStore("/custom.settings");
			if( customProps.getPropTable().size() > 0) {
			    this._conf.setPropTable(customProps.getPropTable());
			}
			
			set_store(QpPropStore.loadPropStore("/cnm.settings"));
		} catch (IOException e) {
			QpError.dieOnly(e); // can't survive without config.
		}
	}

	public static QpConfig getHandler() {
		if (_handler == null) {
			_handler = new QpConfig();
			QpConfig.scheme = _handler.get_conf().getProperty("scheme");
		}
		return _handler;
	}

	public static void setActiveScheme(String scheme) {
			QpConfig.scheme = scheme.toUpperCase();
	}
	
	public static String getActiveScheme() {
		return QpConfig.scheme;
}
	
	public static String get(String name) {
		return getHandler().get_conf().getProperty(name);
	}
	
	public static void set(String name, String value) {
		getHandler().get_conf().setProperty(name, value);
		return;
	}

	public static String getScheme(String name) {
		return get(name+"-"+scheme);
	}
	
	public static void setScheme(String name, String value) {
		set(name+"-"+scheme, value);
	}
	
	
	public static QpPropStore store() {
		return getHandler().get_store();
	}

	public static boolean getBool(String name) {
		return getHandler().get_conf().getBoolProp(name);
	}

	public static int getFGColor(String name) { // get foreground color

		String schemeName = name + "-" + QpConfig.scheme;
		int intColor = getHandler().get_conf().getHexProp(schemeName);

		if (intColor == -1) { // Error occurred
			intColor = Color.BLACK;
		}
		return intColor;
	}

	public static int getBGColor(String name) { // get background color
		
		String schemeName = name + "-" + QpConfig.scheme;
		int intColor = getHandler().get_conf().getHexProp(schemeName);

		if (intColor == -1) { // Error occurred
			intColor = Color.WHITE;
		}
		return intColor;
	}

	public void set_conf(QpProperties _conf) {
		this._conf = _conf;
	}

	public QpProperties get_conf() {
		return _conf;
	}

	public void set_store(QpPropStore _store) {
		this._store = _store;
	}

	public QpPropStore get_store() {
		return _store;
	}

}
