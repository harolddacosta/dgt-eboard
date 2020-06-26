package com.gvt.dgt.library;

/**
 * Copyright 2012 DGT
 * All Rights Reserved
 * 
 * Demonstration of calling the DLL for board communication from Java with JNA
 * 
 * $Id: Dgtebdll64Lib.java 3022 2012-06-06 14:09:30Z jan $
 */
import com.sun.jna.Callback;//NOSONAR
import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * JNA Proxy interface for the dgtebdll DLL (64bit version).
 * 
 * @author Jan
 */
public interface DgtEBoardLib extends Library {

	// The interfaces for the different types of callback functions in use
	interface CallbackFunction extends Callback {

		void callback();
	}

	interface CallbackFunctionCharPtr extends Callback {

		void callback(Pointer data);
	}

	interface CallbackFunctionInt extends Callback {

		void callback(int data);
	}

	interface CallbackFunctionBool extends Callback {

		void callback(boolean data);
	}

	interface CallbackFunctionIntIntCharPtr extends Callback {

		void callback(int arg1, int arg2, Pointer data);
	}

	// Direct call for functionality inside dll
	public int _DGTDLL_GetVersion();

	public int _DGTDLL_GetWxWidgetsVersion();

	public int _DGTDLL_Init();

	public int _DGTDLL_Exit();

	public int _DGTDLL_ShowDialog(int int1);

	public int _DGTDLL_HideDialog(int int1);

	public int _DGTDLL_WriteCOMPort(int int1);

	public int _DGTDLL_WriteCOMPortString(String charPtr1);

	public int _DGTDLL_WritePosition(String charPtr1);

	public int _DGTDLL_PlayWhiteMove(String charPtr1);

	public int _DGTDLL_PlayBlackMove(String charPtr1);

	public int _DGTDLL_WriteDebug(byte bool1);

	public int _DGTDLL_DisplayClockMessage(String charPtr1, int int1);

	public int _DGTDLL_EndDisplay(int int1);

	public int _DGTDLL_SetNRun(String charPtr1, String charPtr2, int int1);

	public int _DGTDLL_ClockMode(int int1);

	public int _DGTDLL_SetAutoRotation(byte bool1);

	public int _DGTDLL_UseFEN(byte bool1);

	public int _DGTDLL_UseSAN(byte bool1);

	public int _DGTDLL_SetGameType(int int1);

	public int _DGTDLL_AllowTakebacks(byte bool1);

	// Registration of call back functions
	public int _DGTDLL_RegisterStatusFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterScanFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterStableBoardFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterWClockFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterBClockFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterResultFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterNewGameFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterWhiteMoveInputFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterBlackMoveInputFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterWhiteTakebackFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterBlackTakebackFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterWhiteMoveNowFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterBlackMoveNowFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterStartSetupFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterStopSetupWTMFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterStopSetupBTMFunc(DgtEBoardLib.CallbackFunctionCharPtr fn);

	public int _DGTDLL_RegisterGameTypeChangedFunc(DgtEBoardLib.CallbackFunctionInt fn);

	public int _DGTDLL_RegisterAllowTakebacksChangedFunc(DgtEBoardLib.CallbackFunctionBool fn);

	public int _DGTDLL_RegisterMagicPieceFunc(DgtEBoardLib.CallbackFunctionIntIntCharPtr fn);
}
