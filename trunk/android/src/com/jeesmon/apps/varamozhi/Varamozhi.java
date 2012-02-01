package com.jeesmon.apps.varamozhi;

public class Varamozhi {
	public static native String stringFromJNI(String input);
    
    static {
        System.loadLibrary("varamozhi-jni");
    }
}
