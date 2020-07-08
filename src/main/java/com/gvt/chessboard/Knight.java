package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Knight implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Knight.class);

	private Color color;
	private char fenLetter;

	public Knight(Color color) {
		this.color = color;
		fenLetter = color == Color.BLACK ? 'n' : 'N';
	}

	public Color getColor() {
		return color;
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
			retValue = "N" + finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = "N" + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}

}
