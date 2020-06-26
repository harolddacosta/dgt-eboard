package com.gvt.dgt.library;

public interface DgtEBoard {

	DgtEBoardLib getDll();

	/**
	 * Method to set the correct dll version (32 or 64 bit).
	 * 
	 * @param model The wanted version
	 * @throws Exception Incorrect model given
	 */
	void setDllModel(String model);

	/**
	 * Implementation of the Windows message loop, which is necessary for the processing of the messages generated in the DLL for the
	 * display. This method is not needed if a Swing user interface is used.
	 */
	void msgLoop();

	/**
	 * @param r false if the message and worker threads should stop running.
	 */
	void setRunning(boolean r);

	/**
	 * Show the DLL dialog
	 */
	void show();

	/**
	 * Hide the DLL dialog
	 */
	void hide();

	DgtEBoardLib.CallbackFunctionCharPtr getStatus();

	DgtEBoardLib.CallbackFunctionCharPtr getScan();

	DgtEBoardLib.CallbackFunctionIntIntCharPtr getMagic();

	DgtEBoardLib.CallbackFunctionInt getType();

}