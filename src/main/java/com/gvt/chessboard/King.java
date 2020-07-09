package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Chessboard.PlayMode;
import com.gvt.chessboard.Piece.Color;

public class King implements Piece {

	private static Logger logger = LoggerFactory.getLogger(King.class);

	private Color color;
	private char fenLetter;

	public King(Color color) {
		this.color = color;
		fenLetter = color == Color.BLACK ? 'k' : 'K';
	}

	public Color getColor() {
		return color;
	}

	public char getFenLetter() {
		return fenLetter;
	}

	@Override
	public String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare, PlayMode playMode) {
		String retValue = null;
		boolean thereWasCapture = false;

		logger.trace("Pieza en {} {}", finalSquare.getAlgebraicCoordinate(), finalSquare.getPiece().getFenLetter());

		if (finalSquare.getPiece().getFenLetter() == 'R' && finalSquare.getAlgebraicCoordinate().equals("f1")) {
			logger.info("Move:{}", "O-O");

			return "O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'R' && finalSquare.getAlgebraicCoordinate().equals("d1")) {
			logger.info("Move:{}", "O-O-O");

			return "O-O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'r' && finalSquare.getAlgebraicCoordinate().equals("f8")) {
			logger.info("Move:{}", "O-O");

			return "O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'r' && finalSquare.getAlgebraicCoordinate().equals("d8")) {
			logger.info("Move:{}", "O-O-O");

			return "O-O-O";
		}

		if (playMode == PlayMode.UCI) {
			return startingSquare.getAlgebraicCoordinate() + finalSquare.getAlgebraicCoordinate();
		}

		if (!previousStateInfinalSquare.isEmpty()) {
			logger.trace("There was a capture");

			thereWasCapture = true;
		}

		if (!thereWasCapture) {

			retValue = "K" + finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = "K" + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}
}
