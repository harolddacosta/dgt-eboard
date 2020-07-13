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
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nu.pattern.OpenCV;

class ChessboardCoordinatesExtraction {

	private static Logger logger = LoggerFactory.getLogger(ChessboardCoordinatesExtraction.class);

	public JFrame frame;
	private JLabel imgLabel;

	ChessboardRecognition chessboardRecognition = new ChessboardRecognition(null);

	AtomicBoolean executingUpdate = new AtomicBoolean(false);

	public ChessboardCoordinatesExtraction(String[] args) {
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
		int count = 1;
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
			Robot robot = new Robot();

			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(1048, 168, 756, 764));
//			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(2637, 169, 457, 457));
			Mat src = chessboardRecognition.bufferedImage2Mat(screenFullImage);
			Mat copy = src.clone();
			Mat srcGray = new Mat();
			Imgproc.cvtColor(copy, srcGray, Imgproc.COLOR_BGR2GRAY);

			chessboardRecognition.setSrcGray(srcGray);
			copy = chessboardRecognition.extractChessboardCoordinates(copy);
			chessboardRecognition.showChessboardCoordinates(copy);

			imgLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(copy)));
			frame.repaint();

			executingUpdate.set(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class ChessboardCoordinatesExtractionDemo {

	public static void main(String[] args) {
		// Load the native OpenCV library
		OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ChessboardCoordinatesExtraction(args);
			}
		});
	}
}
