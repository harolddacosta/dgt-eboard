package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class King implements Piece {

	private static Logger logger = LoggerFactory.getLogger(King.class);

	private char fenLetter;

	public King(Color color) {
		fenLetter = color == Color.BLACK ? 'k' : 'K';
	}

	public char getFenLetter() {
		return fenLetter;
	}

	@Override
	public String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare) {
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
