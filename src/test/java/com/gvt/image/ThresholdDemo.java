package com.gvt.image;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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

import nu.pattern.OpenCV;

class Threshold {

	private static Logger logger = LoggerFactory.getLogger(Threshold.class);

	private Mat src = new Mat();
	private Mat srcGray = new Mat();
	private Mat cannyApplied = new Mat();
	private JFrame frame;
	private JLabel imgLabel;
	private static final int MAX_THRESHOLD = 100;
	private int maxCorners = 23;
	private double minCanny = 5;
	private double maxCanny = 75;
	private int squares = 16;
	private Random rng = new Random(12345);

	public Threshold(String[] args) {
		/// Load source image and convert it to gray
		String filename = args.length > 0 ? args[0]
				: getClass().getClassLoader().getResource("onlyChessboard.bmp").getFile();
		src = Imgcodecs.imread(filename);
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
		update();
	}

	private void addComponentsToPane(Container pane, Image img) {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		pane.add(panel, BorderLayout.BEFORE_FIRST_LINE);

		panel.add(addSlider1());
		panel.add(addSlider2());
		panel.add(addSlider3());

		imgLabel = new JLabel(new ImageIcon(img));
		pane.add(imgLabel, BorderLayout.CENTER);
	}

	private JPanel addSlider1() {
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		sliderPanel.add(new JLabel("minCanny"));

		JSlider slider = new JSlider(0, MAX_THRESHOLD, maxCorners);
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

		JSlider slider = new JSlider(0, MAX_THRESHOLD, maxCorners);
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

		JSlider slider = new JSlider(0, MAX_THRESHOLD, maxCorners);
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

	private void update() {
		/// Parameters for Shi-Tomasi algorithm
		maxCorners = Math.max(maxCorners, 1);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfPoint2f corners = new MatOfPoint2f();
		Mat hierarchy = new Mat();
		double qualityLevel = 0.01;
		double minDistance = 10;
		int blockSize = 3, gradientSize = 3;
		boolean useHarrisDetector = false;
		double k = 0.04;

		/// Copy the source image
		Mat copy = src.clone();
		Imgproc.cvtColor(copy, srcGray, Imgproc.COLOR_BGR2GRAY);

//		Imgproc.Canny(srcGray, srcGray, minCanny, maxCanny); // 5, 75
//		Imgproc.findContours(cannyApplied, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//		Imgproc.cornerEigenValsAndVecs(cannyApplied, cannyApplied, blockSize, 1);
		Size squaresSize = new Size(squares, squares);
		Calib3d.findChessboardCorners(srcGray, squaresSize, corners,
				Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
		logger.debug("Is a chessboard:{}", Calib3d.checkChessboard(srcGray, squaresSize));
		Calib3d.drawChessboardCorners(srcGray, squaresSize, corners, Calib3d.checkChessboard(srcGray, squaresSize));

		for (int x = 0; x < corners.cols(); ++x) {
			for (int y = 0; y < corners.rows(); ++y) {
				logger.debug("Corner {}", corners.get(y, x));
			}
		}

		Mat finalDraw = new Mat(cannyApplied.size(), CvType.CV_8U, Scalar.all(255));

		for (int i = 0; i < contours.size(); i++) {
			if (Imgproc.contourArea(contours.get(i)) > 60) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
//				if ((rect.height > 35 && rect.height < 60) && (rect.width > 35 && rect.width < 60)) {
//				Imgproc.rectangle(copy, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
//						new Scalar(0, 0, 255));
//				}
			}
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

		imgLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(srcGray)));
		frame.repaint();
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
				new Threshold(args);
			}
		});
	}
}
