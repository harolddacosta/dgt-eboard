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
import java.util.concurrent.atomic.AtomicBoolean;

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
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
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
	private int minCanny = 125;
	private int maxCanny = 255;
	private int squares = 6;

	// Data for chessboard
	private Point[][] centerBoxPoints = new Point[8][8];
	private Rect[][] squaresRectangles = new Rect[8][8];
	private Rect[][] piecesInSquares = new Rect[8][8];

	private boolean chessboardInfoExtracted = false;
	private boolean whitePiecesBottom = false;

	private Mat[] whitePieces = new Mat[6];
	private Mat[] blackPieces = new Mat[6];

	AtomicBoolean executingUpdate = new AtomicBoolean(false);

	public Threshold(String[] args) {
		/// Load source image and convert it to gray
		String filename = args.length > 0 ? args[0] : getClass().getClassLoader().getResource("onlyChessboard.bmp").getFile();
		src = Imgcodecs.imread("C:/Users/hdacosta/Development/eclipse-workspace-jsf2/dgt-protocol/target/test-classes/onlyChessboard.bmp");
//		src = Imgcodecs.imread(filename);
		if (src.empty()) {
			System.err.println("Cannot read image: " + filename);
			System.exit(0);
		}

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
//		update();

		int timerDelay = 3000;
		new Timer(timerDelay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (executingUpdate.get() == false) {
					update();
				}
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
		executingUpdate.set(true);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Robot robot;

		try {
			robot = new Robot();

			BufferedImage screenFullImage = robot.createScreenCapture(new Rectangle(2555, 126, 793, 793));
			src = BufferedImage2Mat(screenFullImage);

			/// Copy the source image
			copy = src.clone();
			srcGray = new Mat();
			Imgproc.cvtColor(copy, srcGray, Imgproc.COLOR_BGR2GRAY);

			extractChessboardCoordinates();
//			showChessboardCoordinates(copy);

			double squareWidth = centerBoxPoints[1][0].x - centerBoxPoints[0][0].x;
			double squareHeight = centerBoxPoints[0][1].y - centerBoxPoints[0][0].y;

			squareWidth = squareWidth - (squareWidth * 0.01);
			squareHeight = squareHeight - (squareHeight * 0.01);

			Imgproc.threshold(srcGray, srcGray, minCanny, maxCanny, Imgproc.THRESH_BINARY_INV); // 125-255
			Imgproc.findContours(srcGray, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

			int minSize = (int) (squareWidth * 0.2);
			logger.trace("Contours size:{}", contours.size());
			piecesInSquares = new Rect[8][8];
			for (int i = 0; i < contours.size(); i++) {
				Rect rect = Imgproc.boundingRect(contours.get(i));
				if ((rect.height > minSize && rect.height < squareHeight) && (rect.width > minSize && rect.width < squareWidth)) {
//					Imgproc.rectangle(copy, rect, new Scalar(255, 255, 0));

					putPieceInsideSquare(rect);
				}
			}

			if (checkForInitialPosition()) {
				// restart a game, scan for pieces to compare later
				recognizePiecesColor();

				cropImagesToCompare();
			}

			createFEN();

			imgLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(srcGray)));
			frame.repaint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		executingUpdate.set(false);
	}

	private void createFEN() {
		logger.trace("Starting FEN diagram creation");

		printPiecesInSquaresDiagram();

		StringBuffer fen = new StringBuffer();

		int qtyBlackPawns = 0;
		int qtyWhitePawns = 0;
		int qtyBlackKings = 0;
		int qtyWhiteKings = 0;

		for (int y = 0; y < 8; ++y) {
			int emptySquares = 0;

			if (y > 0) {
				fen.append("/");
			}

			for (int x = 0; x < 8; ++x) {
				if (piecesInSquares[x][y] != null) {
					// 0=Pawn, 1=Rook, 2=Knight, 3=Bishop, 4=Queen, 5=King

					boolean pieceFound = false;

					// Check first black pieces
					logger.trace("Checkig first all black pieces");
					for (int pieceType = 0; pieceType < 6; ++pieceType) {
						if (pieceType == 0 && qtyBlackPawns == 8) {
							logger.trace("All black pawns found, following with the next piece");

							continue;
						}

						if (pieceType == 5 && qtyBlackKings == 1) {
							logger.trace("All black kings found, following with the next piece");

							continue;
						}

						boolean isPiece = isPiece(x, y, pieceType, blackPieces);

						if (isPiece) {
							if (emptySquares > 0) {
								// write first empty squares
								fen.append(emptySquares);

								emptySquares = 0;
							}

							switch (pieceType) {
							case 0:
								qtyBlackPawns++;
								fen.append("p");

								break;
							case 1:
								fen.append("r");

								break;
							case 2:
								fen.append("n");

								break;
							case 3:
								fen.append("b");

								break;
							case 4:
								fen.append("q");

								break;
							case 5:
								qtyBlackKings++;
								fen.append("k");

								break;
							default:
								break;
							}

							pieceFound = true;

							break;
						}
					}

					// Now check for white pieces
					if (!pieceFound) {
						logger.trace("Checkig then all white pieces");
						for (int pieceType = 0; pieceType < 6; ++pieceType) {
							if (pieceType == 0 && qtyWhitePawns == 8) {
								logger.trace("All white pawns found, following with the next piece");

								continue;
							}

							if (pieceType == 5 && qtyWhiteKings == 1) {
								logger.trace("All white kings found, following with the next piece");

								continue;
							}

							boolean isPiece = isPiece(x, y, pieceType, whitePieces);

							if (isPiece) {
								if (emptySquares > 0) {
									// write first empty squares
									fen.append(emptySquares);

									emptySquares = 0;
								}

								switch (pieceType) {
								case 0:
									qtyWhitePawns++;
									fen.append("P");

									break;
								case 1:
									fen.append("R");

									break;
								case 2:
									fen.append("N");

									break;
								case 3:
									fen.append("B");

									break;
								case 4:
									fen.append("Q");

									break;
								case 5:
									qtyWhiteKings++;
									fen.append("K");

									break;
								default:
									break;
								}

								break;
							}
						}
					}
				} else {
					emptySquares++;

					if (x == 7 && emptySquares == 8) {
						fen.append("8");
					} else if (x == 7 && emptySquares > 0) {
						fen.append(emptySquares);
					}
				}
			}
		}

		logger.debug("FEN diagram:{}", fen.toString());
	}

	private void printPiecesInSquaresDiagram() {
		StringBuffer line = new StringBuffer();

		for (int y = 0; y < 8; ++y) {
			line = new StringBuffer();
			for (int x = 0; x < 8; ++x) {
				if (piecesInSquares[x][y] != null) {
					line.append("1");
				} else {
					line.append("0");
				}
			}
			logger.trace("{}", line.toString());
		}
	}

	public boolean isPiece(int x, int y, int pieceType, Mat[] piecesToCompare) {
		boolean retValue = false;

		Mat pieceRecognized = new Mat();
		Imgproc.matchTemplate(srcGray.submat(squaresRectangles[x][y]), piecesToCompare[pieceType], pieceRecognized,
				Imgproc.TM_CCOEFF_NORMED);

		MinMaxLocResult mmr = Core.minMaxLoc(pieceRecognized);

		logger.trace("Checking for piece type:{} in [{}][{}] %:{}", pieceType, x, y, mmr.maxVal);

		if (mmr.maxVal > 0.7) {
			switch (pieceType) {
			case 1:
				logger.trace("Comparing Rook against [{}][{}] with %{}", x, y, mmr.maxVal);

				retValue = true;

				break;
			default:
				break;
			}
		}

		if (mmr.maxVal > 0.75) {
			switch (pieceType) {
			case 0:
				logger.trace("Comparing Pawn against [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 2:
				logger.trace("Comparing Knight against [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 3:
				logger.trace("Comparing Bishop against [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 4:
				logger.trace("Comparing Queen against [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 5:
				logger.trace("Comparing King against [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			default:
				break;
			}

			retValue = true;
		}

		return retValue;
	}

	private void cropImagesToCompare() {
		// Positions for array
		// 0=Pawn, 1=Rook, 2=Knight, 3=Bishop, 4=Queen, 5=King
		if (whitePiecesBottom) {
			whitePieces[0] = srcGray.submat(piecesInSquares[0][6]);
			whitePieces[1] = srcGray.submat(piecesInSquares[0][7]);
			whitePieces[2] = srcGray.submat(piecesInSquares[1][7]);
			whitePieces[3] = srcGray.submat(piecesInSquares[2][7]);
			whitePieces[4] = srcGray.submat(piecesInSquares[3][7]);
			whitePieces[5] = srcGray.submat(piecesInSquares[4][7]);

			blackPieces[0] = srcGray.submat(piecesInSquares[0][1]);
			blackPieces[1] = srcGray.submat(piecesInSquares[0][0]);
			blackPieces[2] = srcGray.submat(piecesInSquares[1][0]);
			blackPieces[3] = srcGray.submat(piecesInSquares[2][0]);
			blackPieces[4] = srcGray.submat(piecesInSquares[3][0]);
			blackPieces[5] = srcGray.submat(piecesInSquares[4][0]);
		} else {
			whitePieces[0] = srcGray.submat(piecesInSquares[0][1]);
			whitePieces[1] = srcGray.submat(piecesInSquares[0][0]);
			whitePieces[2] = srcGray.submat(piecesInSquares[1][0]);
			whitePieces[3] = srcGray.submat(piecesInSquares[2][0]);
			whitePieces[5] = srcGray.submat(piecesInSquares[3][0]);// Be aware, here the king appears first
			whitePieces[4] = srcGray.submat(piecesInSquares[4][0]);

			blackPieces[0] = srcGray.submat(piecesInSquares[0][6]);
			blackPieces[1] = srcGray.submat(piecesInSquares[0][7]);
			blackPieces[2] = srcGray.submat(piecesInSquares[1][7]);
			blackPieces[3] = srcGray.submat(piecesInSquares[2][7]);
			blackPieces[5] = srcGray.submat(piecesInSquares[3][7]); // Be aware, here the king appears first
			blackPieces[4] = srcGray.submat(piecesInSquares[4][7]);
		}
	}

	private void recognizePiecesColor() { // Check only pawn color
		Mat piece = srcGray.submat(piecesInSquares[0][1]);
		int qtyColorPixelsTop = 0;
		for (int a = 0; a < piece.rows(); a++) {
			for (int b = 0; b < piece.cols(); b++) {
				if (piece.get(a, b)[0] == 255) {
					qtyColorPixelsTop++;
				}
			}
		}

		///////////////////////
		piece = srcGray.submat(piecesInSquares[0][6]);
		int qtyColorPixelsBottom = 0;
		for (int a = 0; a < piece.rows(); a++) {
			for (int b = 0; b < piece.cols(); b++) {
				if (piece.get(a, b)[0] == 255) {
					qtyColorPixelsBottom++;
				}
			}
		}

		if (qtyColorPixelsTop > qtyColorPixelsBottom) {
			whitePiecesBottom = true;
		} else {
			whitePiecesBottom = false;
		}

		logger.trace("White pieces bottom? {}", whitePiecesBottom);
	}

	private boolean checkForInitialPosition() {
		boolean retValue = true;

		int qtyPieces = 0;
		for (int y = 0; y < 8 && retValue == true; ++y) {
			for (int x = 0; x < 8; ++x) {
				if (piecesInSquares[x][y] != null) {
					logger.trace("There is a piece in:{}:{}", x, y);

					if (y > 1 && y < 6) {
						retValue = false;

						break;
					}

					qtyPieces++;
				}
			}
		}

		if (qtyPieces < 32) {
			retValue = false;
		}

		logger.trace("Is initial position? {}", retValue);

		return retValue;
	}

	private void putPieceInsideSquare(Rect potencialPieceShape) {
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				if (potencialPieceShape.tl().x >= squaresRectangles[x][y].tl().x
						&& potencialPieceShape.br().x <= squaresRectangles[x][y].br().x
						&& potencialPieceShape.tl().y >= squaresRectangles[x][y].tl().y
						&& potencialPieceShape.br().y <= squaresRectangles[x][y].br().y) {
					logger.trace("potencialPieceShape rect:{},{},{},{}", potencialPieceShape.tl().x, potencialPieceShape.tl().y,
							potencialPieceShape.br().x, potencialPieceShape.br().y);
					logger.trace("squaresRectangles rect:{},{},{},{}", squaresRectangles[x][y].tl().x, squaresRectangles[x][y].tl().y,
							squaresRectangles[x][y].br().x, squaresRectangles[x][y].br().y);

					if (piecesInSquares[x][y] != null) { // already contains a piece, check for larger
						if (piecesInSquares[x][y].area() < potencialPieceShape.area()) {
							piecesInSquares[x][y] = potencialPieceShape;

							logger.trace("replacing a piece in {}:{}", x, y);
						}
					} else {
						piecesInSquares[x][y] = potencialPieceShape;

						logger.trace("putting a new piece in {}:{}", x, y);
					}
				}
			}
		}
	}

	private void extractChessboardCoordinates() {
		if (chessboardInfoExtracted) {
			return;
		}

		Monitor mon = MonitorFactory.start("Chessboard information extraction");

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

//				Calib3d.drawChessboardCorners(copy, squaresSize, corners, isChessboard);
			}
		}

		double squareWidth = corners.get(1, 0)[0] - corners.get(0, 0)[0];
		double squareHeight = corners.get(squareCorners, 0)[1] - corners.get(0, 0)[1];
		logger.trace("First coordinate:{}-{}", corners.get(0, 0)[0], corners.get(1, 0)[0]);
		logger.trace("Second coordinate:{}-{}", corners.get(0, 0)[1], corners.get(squareCorners, 0)[1]);
		logger.trace("Square width:{} Square height:{}", squareWidth, squareHeight);

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

		logger.trace("First corner found:{}-{}", firstLeftCorner, firstTopCorner);

		for (int y = 0; y < corners.rows(); ++y) {
			for (int x = 0; x < corners.cols(); ++x) {
				logger.trace("Corner y:{} x:{} {}", y, x, corners.get(y, x));
			}
		}

		double centerOfBoxX;
		double centerOfBoxY = firstTopCorner + (squareHeight / 2);
		double cornerInX;
		double cornerInY = firstTopCorner;
		Size boxSize = new Size(squareWidth, squareHeight);

		for (int y = 0; y < 8; ++y) {
			centerOfBoxX = firstLeftCorner + (squareWidth / 2);
			cornerInX = firstLeftCorner;

			for (int x = 0; x < 8; ++x) {
				centerBoxPoints[x][y] = new Point(centerOfBoxX, centerOfBoxY);
				squaresRectangles[x][y] = new Rect(new Point(cornerInX, cornerInY), boxSize);

				centerOfBoxX = centerOfBoxX + squareWidth;
				cornerInX = cornerInX + squareWidth;
			}

			centerOfBoxY = centerOfBoxY + squareHeight;
			cornerInY = cornerInY + squareHeight;
		}

		mon.stop();
		logger.info("Chessboard information extraction:{}", mon);

		chessboardInfoExtracted = true;
	}

	private void showChessboardCoordinates(Mat copy) {
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
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", byteArrayOutputStream);
//			byteArrayOutputStream.flush();

			return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
		}
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
