package qpframe.util;

import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

//NB: A record store can be a maximum of 64 KB!

public class QpStore {

	protected String _storename;
	protected RecordStore rs;
	protected static Vector stores = new Vector();

	/**
	 * Construct a CnmStore. Do Not Use unless you want to implement your own
	 * error Handling,etc.
	 * 
	 * @param name
	 *            A String
	 * @throws RecordStoreFullException
	 *             , RecordStoreNotFoundException, RecordStoreException
	 */
	public QpStore(String name) {
		_storename = name;
		stores.addElement(this);
	}

	/**
	 * Returns a new CnmStore. Safer/Preferred way of opening a store.
	 * 
	 * @param name
	 *            A String
	 * @throws RecordStoreFullException
	 *             , RecordStoreNotFoundException, RecordStoreException
	 */
	public static QpStore getStore(String name) {

		QpStore store;
		// see if store exists already
		for (int n = 0; n < stores.size(); n++) {
			store = (QpStore) stores.elementAt(n);
			if (store.getName().equals(name)) {
				return store;
			}
		}

		// If store not found then create store.
		store = new QpStore(name);

		try {

			store.openRecStore();
		} catch (RecordStoreFullException e) {
			// Try to delete and recreate
			QpStore.deleteRecordStore(name);
			try {
				store.openRecStore(); // Try to open it again.
			} catch (Exception e1) {
				// Time to die
				QpError.dieOnly(e1);
			}
		} catch (RecordStoreException e) {
			// Good one to die on
			QpError.dieOnly(e);
		}
		return store;
	}

	/**
	 * Opens the record store.
	 * 
	 * @throws RecordStoreFullException
	 *             , RecordStoreNotFoundException, RecordStoreException
	 */
	public void openRecStore() throws RecordStoreFullException,
			RecordStoreException {
		// The second parameter indicates that the record store
		// should be created if it does not exist
		try {
			rs = RecordStore.openRecordStore(_storename, true);
		} catch (RecordStoreNotFoundException e) {
			// Ignore as it should be auto created, but then maybe die?
			QpError.dieOnly(e);
		}

	}

	public String getName() {
		return _storename;
	}

	public void closeRecStore() throws RecordStoreException {

		try {
			rs.closeRecordStore();
		} catch (RecordStoreNotOpenException e) {
			// We should be able to ignore safely
		}

	}

	public static void closeAllStores() {
		QpStore store;
		for (int n = 0; n < stores.size(); n++) {
			store = (QpStore) stores.elementAt(n);
			try {
				store.closeRecStore();
			} catch (RecordStoreException e) {
				// TODO Do Nothing atm, ignore this error. <- Figure out way to
				// handle this
			}
		}
	}

	public void deleteRecStore() {
		if (RecordStore.listRecordStores() != null) {
			try {
				RecordStore.deleteRecordStore(_storename);
			} catch (Exception e) {
				// This is an error we hopefully want to ignore.
			}
		}
	}

	public static void deleteRecordStore(String name) {
		try {
			QpStore store;
			for (int n = 0; n < stores.size(); n++) {
				store = (QpStore) stores.elementAt(n);
				if( store.getName().equals(name) ){
					try {
						store.closeRecStore();
					} catch (RecordStoreException e) {
						// TODO Do Nothing atm, ignore this error. <- Figure out way to
						// handle this
					}
				}
				
			}
			RecordStore.deleteRecordStore(name);
		} catch (Exception e) {
			// This is an error we want to ignore.
			e.printStackTrace();
		}
	}

	public void writeRecord(String str) {
		byte[] rec = str.getBytes();

		try {
			rs.addRecord(rec, 0, rec.length);
		} catch (Exception e) {
			QpError.die(e);
		}
	}

	public Vector getRecords() {
		Vector records = new Vector();
		try {
			for (int i = 1; i <= rs.getNumRecords(); i++) {
				byte[] byteRec = rs.getRecord(i);
				String strRec = new String(byteRec);
				records.addElement(strRec);
			}
		} catch (Exception e) {
			QpError.die(e);
		}
		return records;
	}
	
	

}
