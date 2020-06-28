package com.gvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.windows.MainWindows;

/**
 * Application to make possible to play with dgteboards on any chess server without use bots
 * 
 * @author Harold Da Costa
 */
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	/**
	 * The main showing how the DLL can be called and how everything is organized.
	 * 
	 * @param args Command line arguments (not used)
	 * @throws Exception
	 */
	public static void main(String[] args) {
		new MainWindows();
	}

}
