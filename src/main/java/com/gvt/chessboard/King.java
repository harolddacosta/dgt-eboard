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
