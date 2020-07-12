package com.gvt.image;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nu.pattern.OpenCV;

class ChessboardInitialPositionRecognition {

	private static Logger logger = LoggerFactory.getLogger(ChessboardInitialPositionRecognition.class);

	public JFrame frame;
	private JLabel imgLabel;

	ChessboardRecognition chessboardRecognition = new ChessboardRecognition(null);

	AtomicBoolean executingUpdate = new AtomicBoolean(false);

	public ChessboardInitialPositionRecognition(String[] args) {
		// Create and set up the window.
		frame = new JFrame("Shi-Tomasi corner detector demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		frame.setPreferredSize(new Dimension(1000, 1100));
		frame.pack();
		frame.setVisible(true);

		int timerDelay = 300;
		new Timer(timerDelay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (executingUpdate.get() == false) {
					update();
				}
			}
		}).start();
	}

	private void addComponentsToPane(Container pane) {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		JPanel componentsPanel = new JPanel();
		componentsPanel.setLayout(new GridLayout(3, 2));
		pane.add(componentsPanel, BorderLayout.BEFORE_FIRST_LINE);

		imgLabel = new JLabel();
		pane.add(imgLabel, BorderLayout.CENTER);
	}

	public void update() {
		try {
			List<MatOfPoint> contours = new ArrayList<>();
			Robot robot = new Robot();

			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(2487, 124, 760, 760));
//			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(2637, 169, 457, 457));
			Mat src = chessboardRecognition.bufferedImage2Mat(screenFullImage);
			Mat copy = src.clone();
			Mat srcGray = new Mat();
			Imgproc.cvtColor(copy, srcGray, Imgproc.COLOR_BGR2GRAY);

			chessboardRecognition.setSrcGray(srcGray);
			chessboardRecognition.extractChessboardCoordinates(null);

			double squareWidth = chessboardRecognition.getCenterBoxPoints()[1][0].x - chessboardRecognition.getCenterBoxPoints()[0][0].x;
			double squareHeight = chessboardRecognition.getCenterBoxPoints()[0][1].y - chessboardRecognition.getCenterBoxPoints()[0][0].y;

			squareWidth = squareWidth - (squareWidth * 0.01);
			squareHeight = squareHeight - (squareHeight * 0.01);

			int minSize = (int) (squareWidth * 0.2);

			chessboardRecognition.extractContours(contours);
			chessboardRecognition.setRectForPieces(contours, squareWidth, squareHeight, minSize);

			for (int y = 0; y < 8; ++y) {
				for (int x = 0; x < 8; ++x) {
					try {
						Imgproc.rectangle(chessboardRecognition.getSrcGray(), chessboardRecognition.getPiecesInSquares()[x][y],
								new Scalar(255, 128, 255));
					} catch (Exception e) {
//						logger.error("Error");
					}
				}
			}

			if (chessboardRecognition.checkForInitialPosition()) {
				logger.debug("Initial position? Yes, its initial position");
			} else {
				logger.debug("Initial position? ******** NO, it is not initial position");
			}

			if (copy != null) {
				imgLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(chessboardRecognition.getSrcGray())));
			}

			frame.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class ChessboardInitialPositionRecognitionDemo {

	public static void main(String[] args) {
		// Load the native OpenCV library
		OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ChessboardInitialPositionRecognition(args);
			}
		});
	}
}
