package qpframe.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

import net.rim.device.api.system.EventLogger;

public class QpPropStore extends QpProperties {

	private QpStore store;

	/* [1] */
	public static QpPropStore loadPropStore(String filePath)
			throws IOException {
		QpPropStore result = new QpPropStore();
		result.openStore(filePath);
		return result;
	}
	
	public static QpPropStore loadPropStore(String filePath, Hashtable props) throws IOException {
		QpPropStore result = new QpPropStore();
		result.openStore(filePath);
		result.setPropTable(props);
		return result;
    }

	protected void openStore(String filePath) {

		store = QpStore.getStore(filePath);
		Vector records = store.getRecords();
		try {
			store.closeRecStore();
		} catch (RecordStoreException e) {
			String message = "Can't close Store: " + filePath + " Error:"
					+ e.getMessage();
			QpError.logAndDisplay("CnmPropStore", message, EventLogger.ERROR);
		}
		for (int i = 0; i < records.size(); i++) {
			String[] kv = QpStringUtils.split(QpStringUtils
					.chomp((String) records.elementAt(i)), '=', 2);
			if (kv.length == 1) {
				setProperty(kv[0], "");
			}
			if (kv.length == 2) {
				setProperty(kv[0], kv[1]);
			}
		}

	}

	
	public void saveStore() {
		String propVal;
		String recVal;

		store.deleteRecStore();
		try {
			store.openRecStore();
		} catch (RecordStoreFullException e) {
			// Serious on this object
			QpError.dieOnly(e);
		} catch (RecordStoreException e) {
			QpError.dieOnly(e);
		}
		Enumeration names = this.getEnumeratedNames();
		while (names.hasMoreElements()) {
			String propName = (String) names.nextElement();
			if (propName != null) {
				propVal = this.getProperty(propName);
				if (propVal.equals("")) {
					recVal = propName;
				} else {
					recVal = propName + "=" + propVal;
				}
				store.writeRecord(recVal);
			}

		}

		try {
			store.closeRecStore();
		} catch (RecordStoreException e) {
			QpError.dieOnly(e);
		}

	}

}
