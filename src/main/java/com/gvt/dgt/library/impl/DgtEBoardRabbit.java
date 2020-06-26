package com.gvt.dgt.library.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.library.DgtEBoard;
import com.gvt.dgt.library.DgtEBoardLib;
import com.sun.jna.Native; //NOSONAR
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.MSG;

public class DgtEBoardRabbit implements DgtEBoard {

	private static Logger logger = LoggerFactory.getLogger(DgtEBoardRabbit.class);

	private boolean running = true;
	private DgtEBoardLib dll = null;

	/**
	 * Callback implementation function for the received status
	 */
	private DgtEBoardLib.CallbackFunctionCharPtr status = data -> {
		String str = data.getString(0);

		logger.debug("Received status:{}", str);
	};

	public DgtEBoardLib.CallbackFunctionCharPtr getStatus() {
		return status;
	}

	/**
	 * Callback implementation function for the evaluation of the received scan
	 */
	private DgtEBoardLib.CallbackFunctionCharPtr scan = data -> {
		String str = data.getString(0);

		logger.debug("Received a scan:{}", str);
	};

	public DgtEBoardLib.CallbackFunctionCharPtr getScan() {
		return scan;
	}

	/**
	 * Callback implementation function for the evaluation of the Magic Pieces
	 */
	private DgtEBoardLib.CallbackFunctionIntIntCharPtr magic = (int arg1, int arg2, Pointer data) -> {
		String str = data.getString(0);

		logger.debug("Found a magic piece: field {},{} - value {}", arg1, arg2, str);
	};

	public DgtEBoardLib.CallbackFunctionIntIntCharPtr getMagic() {
		return magic;
	}

	private DgtEBoardLib.CallbackFunctionInt type = data -> {
		logger.debug("Changing to: ");

		switch (data) {
		case 0:
			logger.debug("Chess");
			break;
		case 10:
			logger.debug("Chess960");
			break;
		case 20:
			logger.debug("International Draughts 10x10");
			break;
		case 21:
			logger.debug("English Draughts 8x8 (Checkers)");
			break;
		case 25:
			logger.debug("Russian Draughts 8x8");
			break;
		case 26:
			logger.debug("Brazilian Draughts 8x8");
			break;
		case 29:
			logger.debug("Turkish Draughts 8x8");
			break;
		default:
			logger.debug("Unknown value");
			break;
		}
	};

	public DgtEBoardLib.CallbackFunctionInt getType() {
		return type;
	}

	/**
	 * Method to set the correct dll version (32 or 64 bit).
	 * 
	 * @param model The wanted version
	 * @throws Exception Incorrect model given
	 */
	public void setDllModel(String model) {
		if (model.equals("64")) {
			dll = Native.load("dgtebdll64", DgtEBoardLib.class);
		} else if (model.equals("32")) {
			dll = Native.load("dgtebdll", DgtEBoardLib.class);
		} else {
			throw new IllegalArgumentException("No correct model given!!");
		}
	}

	/**
	 * Implementation of the Windows message loop, which is necessary for the processing of the messages generated in the DLL for the
	 * display. This method is not needed if a Swing user interface is used.
	 */
	public void msgLoop() {
		final User32 lib = User32.INSTANCE;
		int result;
		MSG msg = new MSG();

		while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
			if (running == Boolean.FALSE || result == -1) {
				logger.error("error in get message");

				break;
			}

			lib.TranslateMessage(msg);
			lib.DispatchMessage(msg);
		}
	}

	/**
	 * @param r false if the message and worker threads should stop running.
	 */
	public void setRunning(boolean r) {
		running = r;
	}

	/**
	 * Show the DLL dialog
	 */
	public void show() {
		dll._DGTDLL_ShowDialog(0);
	}

	/**
	 * Hide the DLL dialog
	 */
	public void hide() {
		dll._DGTDLL_HideDialog(0);
	}

	public DgtEBoardLib getDll() {
		return dll;
	}
}
