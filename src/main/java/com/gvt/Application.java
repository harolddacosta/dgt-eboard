package com.gvt;

import com.gvt.windows.MainWindows;

import nu.pattern.OpenCV;

/**
 * Application to make possible to play with dgteboards on any chess server without use bots
 * 
 * @author Harold Da Costa
 */
public class Application {

	/**
	 * The main showing how the DLL can be called and how everything is organized.
	 * 
	 * @param args Command line arguments (not used)
	 * @throws Exception
	 */
	public static void main(String[] args) {
		new MainWindows();

		OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
	}

}
