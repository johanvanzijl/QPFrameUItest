package qpframe.ui.test;

import qpframe.screens.QpOptionScreen;
import qpframe.ui.screen.UiSettings;
import qpframe.util.QpConfig;
import net.rim.device.api.ui.UiApplication;

public class QPTestApp extends UiApplication  {
	
	
	public static void main(String[] args) {
		QPTestApp theApp = new QPTestApp();
			


	    	// Enter the event dispatcher.
	  	theApp.enterEventDispatcher(); 
			
	}
	
    public QPTestApp() {
		UiSettings settings = new UiSettings();
		
		settings.setBgcolor(QpConfig.getBGColor("bgcolor"));
		settings.setFgcolor(QpConfig.getFGColor("fgcolor"));
		settings.setFgtitle(QpConfig.getFGColor("fgtitle"));
		settings.setBgtitle(QpConfig.getBGColor("bgtitle"));
		settings.setBordercol(QpConfig.getBGColor("bordercol"));
		settings.setFieldbgcol(QpConfig.getFGColor("fieldbgcol"));
		settings.setEditbgcol(QpConfig.getFGColor("editbgcol"));
		
		QpOptionScreen options = new QpOptionScreen(QpConfig.get("AppName"), settings);
		pushScreen(options);
	}

	    
	
}