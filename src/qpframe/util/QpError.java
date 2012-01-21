package qpframe.util;

import java.util.Date;
import java.util.Vector;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.AddressException;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.Transport;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.component.Dialog;

// The purpose of this class is to handle errors:
// Might log, die or display
// Tobe implemented
public class QpError {
	protected static QpError _handler;
	protected static QpStore errorLog;
	
	static String APPLICATION_NAME = "CNMobileWFApp";
    static String APPLICATION_GUID_STRING = "za.cncorp.cnm.wf.CNMobileWFApp.GUID";
    public static long APPLICATION_GUID = 0x2e8d63e822b54a65L;

	protected QpError() {
		// Initialize the log, etc.
		EventLogger.register(APPLICATION_GUID, APPLICATION_NAME, EventLogger.VIEWER_STRING);
		errorLog = QpStore.getStore("CnmErrorLog");
		try {
			errorLog.openRecStore();
		} catch (RecordStoreFullException e) {
			QpStore.deleteRecordStore("CnmErrorLog");
			try {
				errorLog.openRecStore();
			} catch (RecordStoreFullException e1) {
				QpError.dieOnly(e1);
			} catch (RecordStoreException e1) {
				QpError.dieOnly(e1);
			}
		} catch (RecordStoreException e) {
			QpError.dieOnly(e);
		}
		// Handle the errors we cannot possibly log

	}

	public static QpError getHandler() {
		if (_handler == null) {
			_handler = new QpError();
		}
		return _handler;
	}

	public static void die(Exception e) {
		// log and die
	}

	public static void dieOnly(Object e) {
		// Only die with message..
	//	Exception exc = (Exception) e;
	}

	public static void die(String error) {
		// log and die
		
	}

	public static void log(Exception e, int level) {
		// log only
		EventLogger.logEvent(APPLICATION_GUID, e.toString().getBytes(), level);
		Date date = new Date();
		errorLog.writeRecord(date.toString() + ": " + e.toString());
	}

	public static void log(String error, int level) {
		// log only
		EventLogger.logEvent(APPLICATION_GUID, error.getBytes(), level);
		Date date = new Date();
		errorLog.writeRecord(date.toString() + ": " + error);
	}

	public static void logAndDisplay(String Context, String error, int level) {
		log(error, level);
		display(error);
	}

	// Should only be called when not running in thread
	public static void display(Exception e) {
		Dialog.inform(e.getMessage());
	}

	public static void display(String error) {
		Dialog.inform(error);
	}
	
	public static void reportlog( ){
		try {
			Store store = Session.getDefaultInstance().getStore();
			 
			//retrieve the sent folder
			Folder[] folders = store.list(Folder.SENT);
			Folder sentfolder = folders[0];
			
			String address = QpConfig.get("Support");
			String del = ";";
			char c = del.charAt(0);
			String[] addresses = QpStringUtils.split(address, c, 10);
			Address[] addr = new Address[addresses.length];
			for( int k = 0; k < addresses.length; k++){
				addr[k] = new Address(addresses[k], "Support" + String.valueOf(k));
			}
			
			Message mess = new Message(sentfolder);
			try {
				mess.addRecipients(Message.RecipientType.TO, addr);
				mess.setSubject("Error Log");
				
				
				mess.setContent(buildMessageText());
				
				Transport.send(mess);
			//	clearErrorLog();
				
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String buildMessageText(){
		String bodyText = null;
		// Device Info
		bodyText = "Device Name: " + DeviceInfo.getDeviceName() + "\n";
		bodyText = bodyText + "Software Version: " + DeviceInfo.getSoftwareVersion() + "\n";
		bodyText = bodyText + "Device ID: : " + DeviceInfo.getDeviceId() + "\n";
		
		
		// Get the error log
		Vector logs = errorLog.getRecords();
		
		for(int i = 0; i < logs.size(); i++){
			bodyText = bodyText + "\n" + logs.elementAt(i).toString();
		}
		return bodyText;
	}
	
	public static void clearErrorLog(){
		QpStore.deleteRecordStore("CnmErrorLog");
		QpStore.deleteRecordStore("CnmErrorLog");  // For some reason you have to do this twice
		errorLog = QpStore.getStore("CnmErrorLog");
	}

}
