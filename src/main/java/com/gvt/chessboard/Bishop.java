package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bishop implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Bishop.class);

	private char fenLetter;

	public Bishop(Color color) {
		fenLetter = color == Color.BLACK ? 'b' : 'B';
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
			retValue = "B" + finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = "B" + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}

}
