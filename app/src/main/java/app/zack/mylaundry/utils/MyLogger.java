package app.zack.mylaundry.utils;

import android.util.Log;

import java.util.Hashtable;

/**
 * Created by Zack on 2016/6/30.
 */
public class MyLogger {
    private final static boolean isDebug = true;

    public static String TAG = "UBroad";
    private final static int logLevel = Log.VERBOSE;

    private static Hashtable<String, MyLogger> sLoggerTable = new Hashtable<String, MyLogger>();
    private String mClassName;
    private static MyLogger log;
    private static final String ZACK = "@Zack@ ";

    private MyLogger(String name) {
        mClassName = name;
    }

    @SuppressWarnings("unused")
    public static MyLogger getLogger(String className) {
        MyLogger classLogger = (MyLogger) sLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new MyLogger(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }

    public static MyLogger log() {
        if (log == null) {
            log = new MyLogger(ZACK);
        }
        return log;
    }


    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return mClassName + "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }

    public void i(Object str) {
        if (isDebug) {
            if (logLevel <= Log.INFO) {
                String name = getFunctionName();
                if (name != null) {
                    Log.i(TAG, name + " - " + str);
                } else {
                    Log.i(TAG, str.toString());
                }
            }
        }

    }

    public void d(Object str) {
        if (isDebug) {
            if (logLevel <= Log.DEBUG) {
                String name = getFunctionName();
                if (name != null) {
                    Log.d(TAG, name + " - " + str);
                } else {
                    Log.d(TAG, str.toString());
                }
            }
        }
    }

    public void v(Object str) {
        if (isDebug) {
            if (logLevel <= Log.VERBOSE) {
                String name = getFunctionName();
                if (name != null) {
                    Log.v(TAG, name + " - " + str);
                } else {
                    Log.v(TAG, str.toString());
                }
            }
        }
    }

    public void w(Object str) {
        if (isDebug) {
            if (logLevel <= Log.WARN) {
                String name = getFunctionName();
                if (name != null) {
                    Log.w(TAG, name + " - " + str);
                } else {
                    Log.w(TAG, str.toString());
                }
            }
        }
    }

    public void e(Object str) {
        if (isDebug) {
            if (logLevel <= Log.ERROR) {
                String name = getFunctionName();
                if (name != null) {
                    Log.e(TAG, name + " - " + str);
                } else {
                    Log.e(TAG, str.toString());
                }
            }
        }
    }

    public void e(Exception ex) {
        if (isDebug) {
            if (logLevel <= Log.ERROR) {
                Log.e(TAG, "error", ex);
            }
        }
    }

    public void e(String log, Throwable tr) {
        if (isDebug) {
            String line = getFunctionName();
            Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
        }
    }
}
