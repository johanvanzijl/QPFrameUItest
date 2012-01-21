package qpframe.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;

public class QpProperties {

	private Hashtable propTable = new Hashtable();

	/* [1] */
	public static QpProperties loadProperties(String filePath)
			throws IOException {
		QpProperties result = new QpProperties();
		//CnmError.log(result.getClass().toString() + " : " + filePath, EventLogger.INFORMATION);
		InputStream stream = result.getClass().getResourceAsStream(filePath);
		InputStreamReader reader = new InputStreamReader(stream);

		StringBuffer sBuf = new StringBuffer();
		char[] buff = new char[1024];

		/* [2] */
		int pos = reader.read(buff, 0, 1024);
		while (pos != -1) {
			sBuf.append(buff, 0, pos);
			pos = reader.read(buff, 0, 1024);
		}

		/* [3] */
		String[] lines = QpStringUtils.split(sBuf.toString(), '\n', 0);
		for (int i = 0; i < lines.length; i++) {
			String[] kv = QpStringUtils.split(QpStringUtils.chomp(lines[i]),
					'=', 2);
			if (kv.length == 1) {
				result.setProperty(kv[0], "");
			}
			if (kv.length == 2) {
				result.setProperty(kv[0], kv[1]);
			}
		}

		return result;
	}

	public Hashtable getPropTable() {
		 return propTable;
	}
	
	public void setPropTable(Hashtable propTable) {
		this.propTable = propTable;
	}
	
	
	public void setProperty(String key, String val) {
		this.propTable.put(key, val);
	}

	public String getProperty(String key) {
		String val = (String) this.propTable.get(key);
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	public int getPropertyCount() {
		return this.propTable.size();
	}

	public Enumeration getEnumeratedNames() {
		return this.propTable.keys();
	}

	public String[] getPropertyNames() {
		String[] result = new String[this.propTable.size()];
		int c = 0;
		for (Enumeration e = this.propTable.keys(); e.hasMoreElements();) {
			result[c] = (String) e.nextElement();
			c++;
		}
		return result;
	}

	public int getHexProp(String key) {

		try {
			String strProp = getProperty(key);
			return Integer.parseInt(strProp, 16);
		} catch (Exception e) {
			return -1; // we assume config incorrect
		}
	}

	public int getIntProp(String key) {

		try {
			String strProp = getProperty(key);
			return Integer.parseInt(strProp);
		} catch (Exception e) {
			return -1; // we assume config incorrect
		}
	}

	public boolean getBoolProp(String key) {

		String strProp = getProperty(key);
		if (strProp.equals("on") || strProp.equals("true")
				|| strProp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public void setBoolProp(String key, boolean value) {

		if (value) {
			this.setProperty(key, "on");
		} else {
			this.setProperty(key, "off");
		}

	}

}
