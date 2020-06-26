package com.gvt.dgt.threads;

/**
 * Copyright 2012 DGT
 * All Rights Reserved
 * 
 * Demonstration of calling the DLL for board communication from Java with JNA
 * 
 * $Id: WorkerThread.java 3023 2012-06-07 07:18:13Z jan $
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.DgtEBoard;

/**
 * Simple class that takes some input from the command line and in some cases will do some action against the DLL (show, hide, quit).
 * 
 * @author Jan
 */
public class WorkerThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(WorkerThread.class);

	// Reference to the DLL containing class. This should be done through an observer/observable
	private DgtEBoard dgtEBoard;

	/**
	 * Default constructor
	 */
	public WorkerThread() {
	}

	/**
	 * Constructor
	 * 
	 * @param demo2 The dll class we are referencing to.
	 */
	public WorkerThread(DgtEBoard dgtEBoard) {
		super("WorkerThread for " + dgtEBoard.toString());

		this.dgtEBoard = dgtEBoard;
	}

	/**
	 * The thread run method.
	 */
	@Override
	public void run() {
		while (true) {
			logger.debug("Enter command: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String cmd = null;
			try {
				cmd = br.readLine();
			} catch (IOException ioe) {
				logger.debug("IO error trying to read your command!");

				System.exit(1);
			}

			logger.debug("Processing command:{}", cmd);
			if (cmd.equals("quit")) {
				dgtEBoard.setRunning(false);
				break;
			} else if (cmd.equals("show")) {
				// shows the DLL window
				dgtEBoard.show();
			} else if (cmd.equals("hide")) {
				// hides the DLL window
				dgtEBoard.hide();
			} else {
				logger.debug("Command {} unknown!", cmd);
			}
		}
	}

}
