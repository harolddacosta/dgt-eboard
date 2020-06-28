package com.gvt.windows;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.DgtEBoard;
import com.gvt.dgt.rabbit.DgtEBoardRabbit;
import com.gvt.dgt.threads.WorkerThread;
import com.gvt.graphic.SnipIt;

public class MainWindows {

	private static Logger logger = LoggerFactory.getLogger(MainWindows.class);

	private DgtEBoard dgtEBoard;

	public MainWindows() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				}

				JFrame frame = new JFrame();
//				frame.setUndecorated(true);
				// This works differently under Java 6
//				frame.setBackground(new Color(0, 0, 0, 0));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new FlowLayout());
				frame.setSize(600, 600);

				JButton selectChessboard = new JButton("Select chessboard");
				selectChessboard.addActionListener(e -> {
					SnipIt snipIt = new SnipIt();
				});

				JButton runDgtEBoard = new JButton("Run DgtEBoard");
				runDgtEBoard.addActionListener(e -> {
					if (dgtEBoard == null) {
						Thread dgtEBoardThread = new Thread() {

							@Override
							public void run() {
								startDgtEBoard();
							}
						};

						dgtEBoardThread.start();
					}
				});

				frame.getContentPane().add(selectChessboard);
				frame.getContentPane().add(runDgtEBoard);
				frame.setVisible(true);
			}
		});
	}

	private void startDgtEBoard() {
		String model = System.getProperty("sun.arch.data.model");
		logger.debug("We have found a {} bit environment!", model);

		// Load the correct DLL
		dgtEBoard = new DgtEBoardRabbit();
		try {
			dgtEBoard.setDllModel(model);
		} catch (Exception e) {
			logger.error("Problem setting the dll", e);

			throw e;
		}

		// Now start the DLL
		dgtEBoard.getDll()._DGTDLL_Init();
		logger.debug("DLL Version:{}", dgtEBoard.getDll()._DGTDLL_GetVersion());

		// Register the callbacks
		dgtEBoard.getDll()._DGTDLL_RegisterStatusFunc(dgtEBoard.getStatus());
		dgtEBoard.getDll()._DGTDLL_RegisterScanFunc(dgtEBoard.getScan());
		dgtEBoard.getDll()._DGTDLL_RegisterMagicPieceFunc(dgtEBoard.getMagic());
		dgtEBoard.getDll()._DGTDLL_RegisterGameTypeChangedFunc(dgtEBoard.getType());

		// Start a thread doing things
		Thread worker = new WorkerThread(dgtEBoard);
		worker.start();

		dgtEBoard.msgLoop();
	}
}
