package com.gvt.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.DgtEBoard;
import com.gvt.dgt.rabbit.DgtEBoardRabbit;
import com.gvt.dgt.threads.WorkerThread;
import com.gvt.graphic.SnipIt;
import com.gvt.image.ChessboardRecognition;

public class MainWindows {

	private static Logger logger = LoggerFactory.getLogger(MainWindows.class);

	// TODO esto se debería cambiar por standalone instead of statics
	public static DgtEBoard dgtEBoard;
	public static SnipIt snipIt;
	public static ChessboardRecognition chessboardRecognition;

	public static JLabel label = new JLabel("Test application");

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
				frame.setLayout(new BorderLayout());
				frame.setSize(600, 600);

				JButton selectChessboard = new JButton("Select chessboard");
				selectChessboard.addActionListener(e -> {
					if (chessboardRecognition != null) {
						chessboardRecognition.stop();
					}

					chessboardRecognition = new ChessboardRecognition(dgtEBoard);

					snipIt = new SnipIt();
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

				JPanel subPanel = new JPanel();
				subPanel.add(runDgtEBoard);
				subPanel.add(selectChessboard);

//				label = new JLabel("PRUEBA");
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(new Font("Calibri", Font.BOLD, 100));

				frame.getContentPane().add(subPanel, BorderLayout.NORTH);
				frame.getContentPane().add(label, BorderLayout.CENTER);
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
//		dgtEBoard.getDll()._DGTDLL_UseFEN((byte) 1);
		dgtEBoard.getDll()._DGTDLL_UseSAN((byte) 1);

		// Register the callbacks
		dgtEBoard.getDll()._DGTDLL_RegisterStatusFunc(dgtEBoard.getStatus());
		dgtEBoard.getDll()._DGTDLL_RegisterScanFunc(dgtEBoard.getScan());
		dgtEBoard.getDll()._DGTDLL_RegisterMagicPieceFunc(dgtEBoard.getMagic());
		dgtEBoard.getDll()._DGTDLL_RegisterGameTypeChangedFunc(dgtEBoard.getType());

		dgtEBoard.getDll()._DGTDLL_RegisterBlackMoveInputFunc(dgtEBoard.getBlackMoveInput());
		dgtEBoard.getDll()._DGTDLL_RegisterWhiteMoveInputFunc(dgtEBoard.getWhiteMoveInput());

		// Start a thread doing things
		Thread worker = new WorkerThread(dgtEBoard);
		worker.start();

		dgtEBoard.msgLoop();
	}

}
