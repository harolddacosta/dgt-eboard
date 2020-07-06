package com.gvt.dgt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.dgt.chessboard.Piece.Color;

public class Chessboard {

	private static Logger logger = LoggerFactory.getLogger(Chessboard.class);

	private static final String[] algebraicAnnotationForCols = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
	private static final String[] algebraicAnnotationForRows = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

	private boolean whitePiecesBottom = false;
	private Square[][] squares = new Square[8][8];

	public Chessboard(boolean whitePiecesBottom) {
		this.whitePiecesBottom = whitePiecesBottom;

		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				squares[x][y] = new Square(convertCoordinatesToAlgebraic(whitePiecesBottom, x, y));
			}
		}
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

		Square square = new Square(convertCoordinatesToAlgebraic(whitePiecesBottom, coordinateX, coordinateY), piece);

		squares[coordinateX][coordinateY] = square;
	}

	public void print() {
		logger.debug("************ Chessboard ***************");
		logger.debug("");

		StringBuilder row = new StringBuilder();

		row.append("\ta\tb\tc\td\te\tf\tg\th");
		logger.debug("{}", row);
		for (int y = 0; y < 8; ++y) {
			row = new StringBuilder();
			row.append(y + 1);
			for (int x = 0; x < 8; ++x) {
				row.append("\t" + squares[x][y]);
			}

			logger.debug("{}", row);
		}

		row = new StringBuilder();
		row.append("\ta\tb\tc\td\te\tf\tg\th");
		logger.debug("{}", row);
	}

	public String compare(Chessboard previousChessboard) {
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 8; ++x) {
				if (previousChessboard.squares[x][y] == squares[x][y]) {

				}
			}
		}

		return null;
	}

	private String convertCoordinatesToAlgebraic(boolean whitePiecesBottom, int coordinateX, int coordinateY) {
		String retValue = null;

		if (whitePiecesBottom) {
			retValue = algebraicAnnotationForCols[coordinateX] + algebraicAnnotationForRows[coordinateY];
		}

		return retValue;
	}

}
