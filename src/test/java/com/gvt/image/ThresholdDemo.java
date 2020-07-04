package com.gvt.image;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import nu.pattern.OpenCV;

class Threshold {

	private static Logger logger = LoggerFactory.getLogger(Threshold.class);

	private Mat src = new Mat();
	private Mat srcGray = new Mat();
	private Mat copy = new Mat();
	public JFrame frame;
	private JLabel imgLabel;
	private static final int MAX_THRESHOLD = 500;
	private int maxCorners = 23;
	private int minCanny = 5;
	private int maxCanny = 75;
	private int squares = 6;

	// Data for chessboard
	private Point[][] centerBoxPoints = new Point[8][8];
	private Rect[][] squaresRectangles = new Rect[8][8];

	public Threshold(String[] args) {
		/// Load source image and convert it to gray
		String filename = args.length > 0 ? args[0] : getClass().getClassLoader().getResource("onlyChessboard.bmp").getFile();
		src = Imgcodecs.imread("C:/Users/hdacosta/Development/eclipse-workspace-jsf2/dgt-protocol/target/test-classes/onlyChessboard.bmp");
//		src = Imgcodecs.imread(filename);
		if (src.empty()) {
			System.err.println("Cannot read image: " + filename);
			System.exit(0);
		}

		Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);

		// Create and set up the window.
		frame = new JFrame("Shi-Tomasi corner detector demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		Image img = HighGui.toBufferedImage(src);
		addComponentsToPane(frame.getContentPane(), img);
		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		frame.pack();
		frame.setVisible(true);

		int timerDelay = 1000;
		new Timer(timerDelay, new ActionListener() {

			private int counter = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		}).start();
	}

	private void addComponentsToPane(Container pane, Image img) {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		JPanel componentsPanel = new JPanel();
		componentsPanel.setLayout(new GridLayout(3, 2));
		pane.add(componentsPanel, BorderLayout.BEFORE_FIRST_LINE);

		componentsPanel.add(addSlider1());
		componentsPanel.add(addSlider2());
		componentsPanel.add(addSlider3());

		imgLabel = new JLabel(new ImageIcon(img));
		pane.add(imgLabel, BorderLayout.CENTER);
	}

	private JPanel addSlider1() {
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		sliderPanel.add(new JLabel("minCanny"));

		JSlider slider = new JSlider(0, MAX_THRESHOLD, minCanny);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				minCanny = source.getValue();
				logger.debug("Valor de minCanny:{}", minCanny);

				if (minCanny > maxCanny) {
					maxCanny = source.getValue();
				}

				update();
			}
		});
		sliderPanel.add(slider);

		return sliderPanel;
	}

	private JPanel addSlider2() {
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		sliderPanel.add(new JLabel("maxcanny:"));

		JSlider slider = new JSlider(0, MAX_THRESHOLD, maxCanny);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				maxCanny = source.getValue();
				logger.debug("Valor de maxCanny:{}", maxCanny);

				if (maxCanny < minCanny) {
					minCanny = source.getValue();
				}

				update();
			}
		});
		sliderPanel.add(slider);

		return sliderPanel;
	}

	private JPanel addSlider3() {
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		sliderPanel.add(new JLabel("squares:"));

		JSlider slider = new JSlider(0, MAX_THRESHOLD, squares);
		slider.setMajorTickSpacing(20);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				squares = source.getValue();
				logger.debug("Valor de squares:{}", squares);

				update();
			}
		});
		sliderPanel.add(slider);

		return sliderPanel;
	}

	public void update() {
		/// Parameters for Shi-Tomasi algorithm
//		maxCorners = Math.max(maxCorners, 1);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		MatOfPoint2f corners = new MatOfPoint2f();
		Mat hierarchy = new Mat();
//		double qualityLevel = 0.01;
//		double minDistance = 10;
//		int blockSize = 3, gradientSize = 3;
//		boolean useHarrisDetector = false;
//		double k = 0.04;

		Robot robot;
		try {
			robot = new Robot();

			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(2557, 125, 793, 793));
			src = BufferedImage2Mat(screenFullImage);

			/// Copy the source image
			copy = src.clone();
//		Mat dest = Mat.zeros(copy.size(), CvType.CV_8UC3);
			srcGray = new Mat();
			Imgproc.cvtColor(copy, srcGray, Imgproc.COLOR_BGR2GRAY);

			extractChessboardCoordinates();
			double squareWidth = centerBoxPoints[1][0].x - centerBoxPoints[0][0].x;
			double squareHeight = centerBoxPoints[0][1].y - centerBoxPoints[0][0].y;
//
			logger.debug("Square width:{}", squareWidth);
			logger.debug("Square height:{}", squareHeight);

			squareWidth = squareWidth - (squareWidth * 0.01);
			squareHeight = squareHeight - (squareHeight * 0.01);

//			Imgproc.Canny(srcGray, srcGray, minCanny, maxCanny); // 5, 75
			Imgproc.threshold(srcGray, srcGray, minCanny, maxCanny, Imgproc.THRESH_BINARY_INV); // 50-255
			Imgproc.findContours(srcGray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			// Draw contours in dest Mat
//		Imgproc.drawContours(dest, contours, -1, new Scalar(255, 0, 0));
//		Imgproc.cornerEigenValsAndVecs(cannyApplied, cannyApplied, blockSize, 1);

//		Mat finalDraw = new Mat(cannyApplied.size(), CvType.CV_8U, Scalar.all(255));
//

			int minSize = 2;
			for (int i = 0; i < contours.size(); i++) {
//			if (Imgproc.contourArea(contours.get(i)) > 10) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
//				if ((rect.height > minSize && rect.height < squareHeight) && (rect.width > minSize && rect.width < squareWidth)) {
				Imgproc.rectangle(copy, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(255, 255, 0));
//				}
//			}
			}

			/// Apply corner detection
//		Imgproc.goodFeaturesToTrack(srcGray, corners, maxCorners, qualityLevel, minDistance, new Mat(), blockSize,
//				gradientSize, useHarrisDetector, k);

			/// Draw corners detected
//		System.out.println("** Number of corners detected: " + corners.rows());
//		int[] cornersData = new int[(int) (corners.total() * corners.channels())];
//		corners.get(0, 0, cornersData);
//		int radius = 4;
//		for (int i = 0; i < corners.rows(); i++) {
//			Imgproc.circle(srcGray, new Point(cornersData[i * 2], cornersData[i * 2 + 1]), radius,
//					new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)), Imgproc.FILLED);
//		}

			imgLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(copy)));
			frame.repaint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void extractChessboardCoordinates() {
		Monitor mon = MonitorFactory.start("chessboard information extraction");

		centerBoxPoints = new Point[8][8];
		squaresRectangles = new Rect[8][8];

		MatOfPoint2f corners = new MatOfPoint2f();
		boolean squaresFound = false;
		boolean isChessboard = false;
		int squareCorners = 15;

		while (!squaresFound) {
			logger.debug("Is a chessboard?:{} in size:{}", isChessboard, squareCorners);

			if (squareCorners <= 0) {
				return;
			}

			--squareCorners;
			Size squaresSize = new Size(squareCorners, squareCorners);

			isChessboard = Calib3d.checkChessboard(srcGray, squaresSize);
			if (!isChessboard) {
				continue;
			}

			Calib3d.findChessboardCorners(srcGray, squaresSize, corners,
					Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
			if (corners.rows() > 0 && corners.cols() > 0) {
				squaresFound = true;
			}
		}

//			Calib3d.drawChessboardCorners(srcGray, squaresSize, corners, isChessboard);

		double squareWidth = corners.get(1, 0)[0] - corners.get(0, 0)[0];
		double squareHeight = corners.get(squareCorners, 0)[1] - corners.get(0, 0)[1];
		logger.trace("first coordinate:{}-{}", corners.get(0, 0)[0], corners.get(1, 0)[0]);
		logger.trace("second coordinate:{}-{}", corners.get(0, 0)[1], corners.get(squareCorners, 0)[1]);
		logger.trace("square width:{} square height:{}", squareWidth, squareHeight);

		boolean foundFirstCorner = false;
		double firstLeftCorner = corners.get(0, 0)[0];
		while (!foundFirstCorner) {
			if (firstLeftCorner - squareWidth >= 0) { // if there is space for other box
				firstLeftCorner = firstLeftCorner - squareWidth;
			} else {
				foundFirstCorner = true;
			}
		}

		foundFirstCorner = false;
		double firstTopCorner = corners.get(0, 0)[1];
		while (!foundFirstCorner) {
			if (firstTopCorner - squareHeight >= 0) { // if there is space for other box
				firstTopCorner = firstTopCorner - squareHeight;
			} else {
				foundFirstCorner = true;
			}
		}

		logger.trace("first corner found:{}-{}", firstLeftCorner, firstTopCorner);

		for (int y = 0; y < corners.rows(); ++y) {
			for (int x = 0; x < corners.cols(); ++x) {
				logger.debug("Corner y:{} x:{} {}", y, x, corners.get(y, x));
			}
		}

		double centerOfBoxX;
		double centerOfBoxY = firstTopCorner + (squareHeight / 2);
		int cornerInX;
		int cornerInY = (int) firstTopCorner;

		for (int y = 0; y < 8; ++y) {
			centerOfBoxX = firstLeftCorner + (squareWidth / 2);
			cornerInX = (int) firstLeftCorner;

			for (int x = 0; x < 8; ++x) {
				centerBoxPoints[x][y] = new Point(centerOfBoxX, centerOfBoxY);
				squaresRectangles[x][y] = new Rect(cornerInX, cornerInY, (int) squareWidth, (int) squareHeight);

				centerOfBoxX = centerOfBoxX + squareWidth;
				cornerInX = (int) (cornerInX + squareWidth);
			}

			centerOfBoxY = centerOfBoxY + squareHeight;
			cornerInY = (int) (cornerInY + squareHeight);
		}

		mon.stop();
		logger.info("Chessboard information extraction:{}", mon);

		showChessboardCoordinates();

//		return centerBoxPoints;
	}

	private void showChessboardCoordinates() {
		int squaresCount = 0;
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				Imgproc.circle(copy, // Matrix obj of the image
						centerBoxPoints[x][y], // Center of the circle
						5, // Radius
						new Scalar(255, 128, 0), // Scalar object for color
						2 // Thickness of the circle
				);

				Imgproc.rectangle(copy, squaresRectangles[x][y], new Scalar(255, 128, 255));
				Imgproc.putText(copy, String.valueOf(squaresCount + 1), centerBoxPoints[x][y], Imgproc.FONT_HERSHEY_COMPLEX_SMALL, 0.9,
						new Scalar(0, 255, 255));

				++squaresCount;
			}
		}
	}

	private Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();

		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
	}
}

public class ThresholdDemo {

	public static void main(String[] args) {
		// Load the native OpenCV library
		OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Threshold thres = new Threshold(args);
			}
		});
	}
}
