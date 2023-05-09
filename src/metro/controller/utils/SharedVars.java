package metro.controller.utils;

import android.app.Application;
 
public class SharedVars extends Application {
	public final static int BUTTON_REFRESH = 0x00000001;
	public final static int BUTTON_EXIT = 0x00000002;
	public final static int BUTTON_PREFERENCES = 0x00000003;
	public final static int OPTIONS_MENU = 0x00000004;
	public final static int PROGRESS_DIALOG = 0x00000005;
	public final static int REGISTER_DIALOG = 0x00000006;
	public final static int ERROR_NO_INTERNET = 0x00000007;
	public final static int METRO_MODULE = 0x00000008;
	public final static int BUS_MODULE = 0x00000009;
	public final static int TRAIN_MODULE = 0x0000000A;
	public final static int WRONG_LOGIN = 0x0000000B;
	public static final String PREFS_NAME = "MCPFile";
}
