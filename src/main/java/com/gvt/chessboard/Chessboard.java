package com.gvt.chessboard;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Piece.Color;

public class Chessboard {

	public enum PlayMode {
		SAN, UCI
	}

	private static Logger logger = LoggerFactory.getLogger(Chessboard.class);

	private static final String[] algebraicAnnotationForCols = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
	private static final String[] algebraicAnnotationForRows = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

	private boolean whitePiecesBottom = false;
	private Square[][] squares = new Square[8][8];

	private Color playForColor;
	private PlayMode playMode = PlayMode.SAN;

	public Chessboard(boolean whitePiecesBottom) {
		this.whitePiecesBottom = whitePiecesBottom;

		initChessboard();
	}

	public Chessboard(boolean whitePiecesBottom, PlayMode playMode) {
		this.whitePiecesBottom = whitePiecesBottom;
		this.playMode = playMode;

		initChessboard();
	}

	public void setPiece(String fenPiece, int coordinateX, int coordinateY) {
		Piece piece = null;

		switch (fenPiece) {
		case "p":
			piece = new Pawn(Color.BLACK);
			break;
		case "P":
			piece = new Pawn(Color.WHITE);
			break;
		case "r":
			piece = new Rook(Color.BLACK);
			break;
		case "R":
			piece = new Rook(Color.WHITE);
			break;
		case "n":
			piece = new Knight(Color.BLACK);
			break;
		case "N":
			piece = new Knight(Color.WHITE);
			break;
		case "b":
			piece = new Bishop(Color.BLACK);
			break;
		case "B":
			piece = new Bishop(Color.WHITE);
			break;
		case "q":
			piece = new Queen(Color.BLACK);
			break;
		case "Q":
			piece = new Queen(Color.WHITE);
			break;
		case "k":
			piece = new King(Color.BLACK);
			break;
		case "K":
			piece = new King(Color.WHITE);
			break;
		default:
			break;
		}

		squares[coordinateX][coordinateY] = new Square(convertCoordinatesToAlgebraic(coordinateX, coordinateY, whitePiecesBottom), piece);
	}

	public void print(boolean onlyCoordinates) {
		logger.debug("************ Chessboard ***************");
		logger.debug("");

		StringBuilder row = new StringBuilder();

		if (whitePiecesBottom) {
			row.append("\ta\tb\tc\td\te\tf\tg\th");
			logger.debug("{}", row);

			for (int y = 0; y < 8; ++y) {
				row = new StringBuilder();
				row.append(8 - y);
				for (int x = 0; x < 8; ++x) {
					if (onlyCoordinates) {
						row.append("\t" + squares[x][y].getAlgebraicCoordinate());
					} else {
						row.append("\t" + squares[x][y]);
					}
				}

				logger.debug("{}", row);
			}

			row = new StringBuilder();
			row.append("\ta\tb\tc\td\te\tf\tg\th");
			logger.debug("{}", row);
		} else {
			row.append("\th\tg\tf\te\td\tc\tb\ta");
			logger.debug("{}", row);

			for (int y = 0; y < 8; ++y) {
				row = new StringBuilder();
				row.append(y + 1);
				for (int x = 0; x < 8; ++x) {
					if (onlyCoordinates) {
						row.append("\t" + squares[x][y].getAlgebraicCoordinate());
					} else {
						row.append("\t" + squares[x][y]);
					}
				}

				logger.debug("{}", row);
			}

			row = new StringBuilder();
			row.append("\th\tg\tf\te\td\tc\tb\ta");
			logger.debug("{}", row);
		}
	}

	public String compare(Chessboard previousChessboard) {
		logger.debug("Starting to compare");

		Square startingSquare = null;
		Square previousStateInfinalSquare = null;
		Square finalSquare = null;

		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				if (previousChessboard.squares[x][y].toString().equals(squares[x][y].toString())) {
//					logger.debug("Equal squares previous:{} current:{}", previousChessboard.squares[x][y], squares[x][y]);
				} else {
					logger.debug("Not equals previous:{} current:{}", previousChessboard.squares[x][y], squares[x][y]);

					if (!previousChessboard.squares[x][y].isEmpty() && squares[x][y].isEmpty()) {
						logger.debug("\t There was a piece here which one? {}", previousChessboard.squares[x][y]);
						startingSquare = previousChessboard.squares[x][y];
					} else {
						logger.debug("\t Final square? {}", squares[x][y].getAlgebraicCoordinate());
						previousStateInfinalSquare = previousChessboard.squares[x][y];
						finalSquare = squares[x][y];
					}

					if (startingSquare != null && finalSquare != null) {
						playForColor = startingSquare.getPiece().getColor();

						return examineMovement(startingSquare, previousStateInfinalSquare, finalSquare);
					}
				}
			}
		}

		return null;
	}

	public Square getSquare(int coordinateX, int coordinateY) {
		return squares[coordinateX][coordinateY];
	}

	public Square getSquare(String algebraicCoordinate) {
		String col = algebraicCoordinate.substring(0, 1);
		int row = Integer.parseInt(algebraicCoordinate.substring(1, 2));

		logger.trace("Col:{} row:{}", col, row);

		int colInArray = ArrayUtils.indexOf(algebraicAnnotationForCols, col);

		if (whitePiecesBottom) {
			return squares[colInArray][Math.abs(row - 8)];
		}

		return squares[Math.abs(colInArray - 7)][row - 1];
	}

	public Color getPlayForColor() {
		return playForColor;
	}

	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}

	private String examineMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare) {
		logger.trace("Piece in starting square:{} piece was before in final movement:{} final square:{}",
				startingSquare.getPiece().getFenLetter(),
				previousStateInfinalSquare.isEmpty() ? "none" : previousStateInfinalSquare.getPiece().getFenLetter(),
				finalSquare.getAlgebraicCoordinate());

		return startingSquare.getPiece().getMovement(startingSquare, previousStateInfinalSquare, finalSquare, playMode);
	}

	public static String convertCoordinatesToAlgebraic(int coordinateX, int coordinateY, boolean whitePiecesBottom) {
		if (whitePiecesBottom) {
			return algebraicAnnotationForCols[coordinateX] + algebraicAnnotationForRows[Math.abs(coordinateY - 7)];
		}

		return algebraicAnnotationForCols[Math.abs(coordinateX - 7)] + algebraicAnnotationForRows[coordinateY];
	}

	private void initChessboard() {
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				squares[x][y] = new Square(convertCoordinatesToAlgebraic(x, y, whitePiecesBottom));
			}
		}
	}

}
