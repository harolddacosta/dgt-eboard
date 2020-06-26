package com.gvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.library.DgtEBoard;
import com.gvt.dgt.library.impl.DgtEBoardRabbit;
import com.gvt.dgt.threads.WorkerThread;

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
	 */
	public static void main(String[] args) {
		String model = System.getProperty("sun.arch.data.model");
		logger.debug("We have found a {} bit environment!", model);

		// Load the correct DLL
		DgtEBoard eboard = new DgtEBoardRabbit();
		try {
			eboard.setDllModel(model);
		} catch (Exception e) {
			logger.error("Problem setting the dll", e);

			throw e;
		}

		// Now start the DLL
		eboard.getDll()._DGTDLL_Init();
		logger.debug("DLL Version:{}", eboard.getDll()._DGTDLL_GetVersion());

		// Register the callbacks
		eboard.getDll()._DGTDLL_RegisterStatusFunc(eboard.getStatus());
		eboard.getDll()._DGTDLL_RegisterScanFunc(eboard.getScan());
		eboard.getDll()._DGTDLL_RegisterMagicPieceFunc(eboard.getMagic());
		eboard.getDll()._DGTDLL_RegisterGameTypeChangedFunc(eboard.getType());

		// Start a thread doing things
		Thread worker = new WorkerThread(eboard);
		worker.start();

		// Start the windows message pump.
		eboard.msgLoop();

		eboard.getDll()._DGTDLL_Exit();
	}

}
