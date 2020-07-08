package com.gvt.image;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Chessboard;
import com.gvt.windows.MainWindows;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * Simple class that takes some input from the command line and in some cases will do some action against the DLL (show, hide, quit).
 * 
 * @author Jan
 */
public class ChessboardRecognition extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ChessboardRecognition.class);

	private Mat src = new Mat();
	private Mat srcGray = new Mat();
	private Mat copy = new Mat();

	// Data for chessboard
	private Point[][] centerBoxPoints = new Point[8][8];
	private Rect[][] squaresRectangles = new Rect[8][8];
	private Rect[][] piecesInSquares = new Rect[8][8];

	private boolean chessboardInfoExtracted = false;
	private boolean whitePiecesBottom = false;

	private Mat[] whitePieces = new Mat[6];
	private Mat[] blackPieces = new Mat[6];

	public String currentFEN;
	public String previousFEN;
	public String toCompareFEN;

	public Chessboard currentChessboard;
	public Chessboard previousChessboard;

	private long lastCallTime;
	private long currentCallTime;

	AtomicBoolean executingUpdate = new AtomicBoolean(false);

	@Override
	public void run() {
		super.run();

		while (true) {
//			if (!executingUpdate.get()) {
			update();
//			}
		}
	}

	public void update() {
		executingUpdate.set(true);

		List<MatOfPoint> contours = new ArrayList<>();
		Robot robot;

		try {
			robot = new Robot();

			BufferedImage screenFullImage = robot.createScreenCapture(MainWindows.snipIt.getSelectedBounds());
			src = bufferedImage2Mat(screenFullImage);

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

			int minSize = (int) (squareWidth * 0.2);

			Imgproc.threshold(srcGray, srcGray, 125, 255, Imgproc.THRESH_BINARY_INV); // 125-255
			Imgproc.findContours(srcGray, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

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
		} catch (Exception e) {
			logger.error("Problem", e);
		}

		executingUpdate.set(false);
	}

	private void createFEN() {
		logger.trace("Starting FEN diagram creation");

		printPiecesInSquaresDiagram();

		StringBuilder fen = new StringBuilder();

		int qtyBlackPawns = 0;
		int qtyWhitePawns = 0;
		int qtyBlackKings = 0;
		int qtyWhiteKings = 0;

		currentChessboard = new Chessboard(whitePiecesBottom);

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
					logger.trace("Checking first all black pieces");
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
								currentChessboard.setPiece("p", x, y);

								break;
							case 1:
								fen.append("r");
								currentChessboard.setPiece("r", x, y);

								break;
							case 2:
								fen.append("n");
								currentChessboard.setPiece("n", x, y);

								break;
							case 3:
								fen.append("b");
								currentChessboard.setPiece("b", x, y);

								break;
							case 4:
								fen.append("q");
								currentChessboard.setPiece("q", x, y);

								break;
							case 5:
								qtyBlackKings++;

								fen.append("k");
								currentChessboard.setPiece("k", x, y);

								break;
							default:
								break;
							}

							pieceFound = true;
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
									currentChessboard.setPiece("P", x, y);

									break;
								case 1:
									fen.append("R");
									currentChessboard.setPiece("R", x, y);

									break;
								case 2:
									fen.append("N");
									currentChessboard.setPiece("N", x, y);

									break;
								case 3:
									fen.append("B");
									currentChessboard.setPiece("B", x, y);

									break;
								case 4:
									fen.append("Q");
									currentChessboard.setPiece("Q", x, y);

									break;
								case 5:
									qtyWhiteKings++;

									fen.append("K");
									currentChessboard.setPiece("K", x, y);

									break;
								default:
									break;
								}
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

		if (previousFEN == null) {
			previousFEN = fen.toString();
			currentFEN = previousFEN;
			toCompareFEN = currentFEN;

			previousChessboard = currentChessboard;

			lastCallTime = System.currentTimeMillis();
			currentCallTime = lastCallTime;

			logger.info("FEN diagram:{}", fen);
			currentChessboard.print(false);
		} else {
			previousFEN = currentFEN;
			lastCallTime = currentCallTime;
		}

		if (!StringUtils.equals(previousFEN, fen.toString())) {
			currentFEN = fen.toString();
			currentCallTime = System.currentTimeMillis();

			long timeDifference = currentCallTime - lastCallTime;

//			if (timeDifference < 100) {
			logger.debug("Difference between calls:{} {} resultado:{}", lastCallTime, currentCallTime, currentCallTime - lastCallTime);
			logger.info("FEN diagram:{}", fen);
//				logger.debug("move piece:{}", MainWindows.dgtEBoard.getDll()._DGTDLL_PlayWhiteMove("d4"));

			currentChessboard.print(false);
			String play = currentChessboard.compare(previousChessboard);
			logger.info("Play:{}", play);

			previousChessboard = currentChessboard;
			toCompareFEN = currentFEN;
//			}
//			MainWindows.dgtEBoard.getDll()._DGTDLL_DisplayClockMessage("Pd4", 3000);

//			logger.debug("Its possible move? {}", MainWindows.dgtEBoard.getDll()._DGTDLL_WritePosition(currentFEN));
		}
	}

	private void printPiecesInSquaresDiagram() {
		for (int y = 0; y < 8; ++y) {
			StringBuilder line = new StringBuilder();
			for (int x = 0; x < 8; ++x) {
				if (piecesInSquares[x][y] != null) {
					line.append("1");
				} else {
					line.append("0");
				}
			}

			logger.trace("{}", line);
		}
	}

	private boolean isPiece(int x, int y, int pieceType, Mat[] piecesToCompare) {
		boolean retValue = false;

		Mat pieceRecognized = new Mat();
		Imgproc.matchTemplate(srcGray.submat(squaresRectangles[x][y]), piecesToCompare[pieceType], pieceRecognized,
				Imgproc.TM_CCOEFF_NORMED);

		MinMaxLocResult mmr = Core.minMaxLoc(pieceRecognized);

		logger.trace("Checking for piece type:{} in [{}][{}] %:{}", pieceType, x, y, mmr.maxVal);

		if (mmr.maxVal > 0.7) {
			switch (pieceType) {
			case 1:
				logger.trace("Found Rook in [{}][{}] with %{}", x, y, mmr.maxVal);

				retValue = true;

				break;
			default:
				break;
			}
		}

		if (mmr.maxVal > 0.75) {
			switch (pieceType) {
			case 0:
				logger.trace("Found Pawn in [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 2:
				logger.trace("Found Knight in [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 3:
				logger.trace("Found Bishop in [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 4:
				logger.trace("Found Queen in [{}][{}] with %{}", x, y, mmr.maxVal);

				break;
			case 5:
				logger.trace("Found King in [{}][{}] with %{}", x, y, mmr.maxVal);

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
		for (int y = 0; y < 8 && retValue; ++y) {
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
		boolean pieceFound = false;

		for (int y = 0; y < 8 && !pieceFound; ++y) {
			for (int x = 0; x < 8 && !pieceFound; ++x) {
				if (potencialPieceShape.tl().x >= squaresRectangles[x][y].tl().x
						&& potencialPieceShape.br().x <= squaresRectangles[x][y].br().x
						&& potencialPieceShape.tl().y >= squaresRectangles[x][y].tl().y
						&& potencialPieceShape.br().y <= squaresRectangles[x][y].br().y) {
					if (logger.isTraceEnabled()) {
						logger.trace("potencialPieceShape rect:{},{},{},{}", potencialPieceShape.tl().x, potencialPieceShape.tl().y,
								potencialPieceShape.br().x, potencialPieceShape.br().y);
						logger.trace("squaresRectangles rect:{},{},{},{}", squaresRectangles[x][y].tl().x, squaresRectangles[x][y].tl().y,
								squaresRectangles[x][y].br().x, squaresRectangles[x][y].br().y);
					}

					if (piecesInSquares[x][y] != null) { // already contains a piece, check for larger
						if (piecesInSquares[x][y].area() < potencialPieceShape.area()) {
							piecesInSquares[x][y] = potencialPieceShape;

							logger.trace("replacing a piece in {}:{}", x, y);
						}
					} else {
						piecesInSquares[x][y] = potencialPieceShape;

						logger.trace("putting a new piece in {}:{}", x, y);
					}

					pieceFound = true;
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
			logger.trace("Is a chessboard?:{} in size:{}", isChessboard, squareCorners);

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
		logger.debug("Chessboard information extraction:{}", mon);

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

	private Mat bufferedImage2Mat(BufferedImage image) throws IOException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", byteArrayOutputStream);

			return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
		}
	}

}
