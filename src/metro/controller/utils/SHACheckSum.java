package metro.controller.utils;

import java.security.MessageDigest;

import android.util.Log;

public class SHACheckSum {
	public static String Hash(String arg) throws Exception {
		Log.i(SHACheckSum.class.toString(),
				"public static String Hash(String arg)throws Exception");
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		if ((arg) != null) {
			md.update(arg.getBytes());
		}
		
		byte[] mdbytes = md.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
		}

		return hexString.toString();
	}
}