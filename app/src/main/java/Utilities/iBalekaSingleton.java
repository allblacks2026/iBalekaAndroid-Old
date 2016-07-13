package Utilities;

import android.content.Context;

/**
 * Created by Okuhle on 5/16/2016.
 */
public class iBalekaSingleton {
    private static iBalekaSingleton ourInstance = new iBalekaSingleton();
    private LocalDbHelper dbHelperObject;
    private Context currentContext;

    public static iBalekaSingleton getInstance() {
        return ourInstance;
    }

    private void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;

    }

    public static void setContext(Context currentContext) {
        ourInstance.setCurrentContext(currentContext);
    }

    private iBalekaSingleton() {

    }

}
