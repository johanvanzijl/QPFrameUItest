package qpframe.screens;

import java.io.IOException;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import qpframe.ui.field.QpEditField;
import qpframe.ui.field.QpLabelField;
import qpframe.ui.managers.QpGridFieldManager;
import qpframe.ui.managers.QpVerticalFieldManager;
import qpframe.ui.screen.QpScreen;
import qpframe.ui.screen.UiSettings;
import qpframe.util.QpConfig;
import qpframe.util.QpError;
import qpframe.util.QpPropStore;
import qpframe.util.QpProperties;


public class QpOptionScreen extends QpScreen {
	private QpVerticalFieldManager vertman;
	private int[] colWidth = { 0, 0 };
	private ObjectChoiceField objListfieldConn;
	private ObjectChoiceField objListfieldAuth;
	
	private QpEditField schemeField;
	private QpEditField fgcolorField;
	private QpEditField bgcolorField;
	private QpEditField fgtitleField;
	private QpEditField bgtitleField;
	private QpEditField bordercolField;
	private QpEditField fieldbgcolField;
	private QpEditField editbgcolField;
	
	
	private ButtonField _saveBtn;
	private ButtonField _backBtn;
	private ButtonField _resetBtn;
	private Object[] objlistAuth = new Object[2]; 
	
	public QpOptionScreen(String Appname, UiSettings settings){
		super(Appname, "Options", settings);
	}
	
	public void buildScreen(){
		this.deleteAll();
		
		int screenWidth = Display.getWidth() - 10;
		colWidth[0] = (int) (screenWidth * 0.6);
		colWidth[1] = (int) (screenWidth * 0.4);

		vertman = new QpVerticalFieldManager();
		vertman.setPreferredWidth(Display.getWidth() - 10);

		QpGridFieldManager gridman = new QpGridFieldManager(colWidth, vertman.getPreferredWidth());
		
		gridman.add(new QpLabelField("Scheme : "));
		schemeField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		schemeField.setText(QpConfig.get("scheme"));
		gridman.add(schemeField);
		
		FocusChangeListener listener = new FocusChangeListener() {
	         public void focusChanged(Field field, int context) {
	        	 QpEditField editField = (QpEditField) field;
	        	 if(context == FocusChangeListener.FOCUS_LOST) {
	        		 loadSchemeFields(editField.getText());
	        	 }	        	 
	       
	         }
	     };
	     
	     schemeField.setFocusListener(listener);

		
		
		gridman.add(new QpLabelField("Foreground Col : "));
		fgcolorField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		fgcolorField.setText(QpConfig.getScheme("fgcolor"));
		gridman.add(fgcolorField);
		
		gridman.add(new QpLabelField("Background Col : "));
		bgcolorField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		bgcolorField.setText(QpConfig.getScheme("bgcolor"));
		gridman.add(bgcolorField);
		
		gridman.add(new QpLabelField("FG Title Col : "));
		fgtitleField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		fgtitleField.setText(QpConfig.getScheme("fgtitle"));
		gridman.add(fgtitleField);
		
		gridman.add(new QpLabelField("BG Title Col : "));
		bgtitleField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		bgtitleField.setText(QpConfig.getScheme("bgtitle"));
		gridman.add(bgtitleField);
		
		gridman.add(new QpLabelField("Border Col : "));
		bordercolField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		bordercolField.setText(QpConfig.getScheme("bordercol"));
		gridman.add(bordercolField);
		
		gridman.add(new QpLabelField("Field Background : "));
		fieldbgcolField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		fieldbgcolField.setText(QpConfig.getScheme("fieldbgcol"));
		gridman.add(fieldbgcolField);
		
		gridman.add(new QpLabelField("Edit Field BG : "));
		editbgcolField = new QpEditField(colWidth[1], settings.getBordercol(), settings.getEditbgcol());
		editbgcolField.setText(QpConfig.getScheme("editbgcol"));
		gridman.add(editbgcolField);
		
		vertman.add(gridman);
		SeparatorField sep = new SeparatorField();
		sep = new SeparatorField();
		vertman.add(sep);
		
// Save button
		_saveBtn = new ButtonField("Save");
		_backBtn = new ButtonField("Cancel");
		_resetBtn = new ButtonField("Defaults");
		HorizontalFieldManager buttonMan = new HorizontalFieldManager();
		buttonMan.add(_saveBtn);
		buttonMan.add(_backBtn);
		buttonMan.add(_resetBtn);
		vertman.add(buttonMan);
		this.add(vertman);	
		
	}
	public void loadSchemeFields(String scheme) {
		if( ! scheme.equalsIgnoreCase(QpConfig.getActiveScheme())) {
			int answer = Dialog.ask(Dialog.YES + Dialog.NO, "Do you wish to reload Scheme dependent fields?");
			if( answer == Dialog.YES) {
				QpConfig.setActiveScheme(scheme);
				fgcolorField.setText(QpConfig.getScheme("fgcolor"));
				bgcolorField.setText(QpConfig.getScheme("bgcolor"));
				fgtitleField.setText(QpConfig.getScheme("fgtitle"));
				bgtitleField.setText(QpConfig.getScheme("bgtitle"));
				bordercolField.setText(QpConfig.getScheme("bordercol"));
				fieldbgcolField.setText(QpConfig.getScheme("fieldbgcol"));
				editbgcolField.setText(QpConfig.getScheme("editbgcol"));
			} else {
				QpConfig.setActiveScheme(scheme);
			}
		}

	}
	protected boolean trackwheelClick(int status, int time) {

		Field field = getLeafFieldWithFocus();
		if (field == _resetBtn) {
			try {
				QpProperties stdProps = QpProperties.loadProperties("/cnmconfig.properties");
				QpPropStore customProps = QpPropStore.loadPropStore("/custom.settings");
			    customProps.setPropTable(stdProps.getPropTable());
			    customProps.saveStore();
			} catch (IOException e) {
				Dialog.inform("Failed to Reset");
				return true;
			}
			Dialog.inform("Properties reset to Standard. Please Restart App.");
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			return true;
		}		
		if (field == _saveBtn) {
			QpConfig.set("scheme", schemeField.getText().toUpperCase() );
			QpConfig.setScheme("fgcolor", fgcolorField.getText().toUpperCase() );
			QpConfig.setScheme("bgcolor", bgcolorField.getText().toUpperCase() );
			QpConfig.setScheme("fgtitle", fgtitleField.getText().toUpperCase() );
			QpConfig.setScheme("bgtitle", bgtitleField.getText().toUpperCase() );
			QpConfig.setScheme("bordercol", bordercolField.getText().toUpperCase() );
			QpConfig.setScheme("fieldbgcol", fieldbgcolField.getText().toUpperCase() );
			QpConfig.setScheme("editbgcol", editbgcolField.getText().toUpperCase() );
			
			QpPropStore customProps;
			try {
				customProps = QpPropStore.loadPropStore("/custom.settings");
			    customProps.setPropTable(QpConfig.getHandler().get_conf().getPropTable());
			    customProps.saveStore();
			} catch (IOException e) {
				Dialog.inform("Could not save:" + e.getMessage());
				return true;
			}
			Dialog.inform("Options saved. Please Restart App.");
			close();
			return true;
		}
		if (field == _backBtn) {
			close();
			
			return true;
		}

		return super.trackwheelClick(status, time);
	}
	
	public void onDisplay(){
		buildScreen();
	}
	
	protected void makeMenu(Menu menu, int instance) {
		instance = 2;
		super.makeMenu(menu, instance);
		menu.add(clearErrorlog);
	}
    
    private MenuItem clearErrorlog = new MenuItem("Clear Error Log",100,10)
    {
        
        public void run() {
            try{
            	QpError.clearErrorLog();
            	Dialog.inform("Log cleared");
            	
            }catch( Throwable t){

            }
        }
        
        
    };
	
	protected boolean keyChar(char c, int status, int time) {
		if (c == Characters.ESCAPE) {
			close();
			
			return true;
		} else if (c == Characters.ENTER) {
	//		Field field = getLeafFieldWithFocus();
			return true;
		}

		return super.keyChar(c, status, time);

	}

}
