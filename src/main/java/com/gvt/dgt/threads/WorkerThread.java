package com.gvt.dgt.threads;

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
		logger.debug("Enter command: ");

		// shows the DLL window
		dgtEBoard.show();
	}

}
