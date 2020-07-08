package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rook implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Rook.class);

	private char fenLetter;

	public Rook(Color color) {
		fenLetter = color == Color.BLACK ? 'r' : 'R';
	}

	@Override
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
			retValue = "R" + finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = "R" + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}

}
