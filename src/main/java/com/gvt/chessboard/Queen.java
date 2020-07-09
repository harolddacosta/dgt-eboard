package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Chessboard.PlayMode;

public class Queen implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Queen.class);

	private Color color;
	private char fenLetter;

	public Queen(Color color) {
		this.color = color;
		fenLetter = color == Color.BLACK ? 'q' : 'Q';
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

		if (playMode == PlayMode.UCI) {
			return startingSquare.getAlgebraicCoordinate() + finalSquare.getAlgebraicCoordinate();
		}

		if (!previousStateInfinalSquare.isEmpty()) {
			logger.trace("There was a capture");

			thereWasCapture = true;
		}

		if (!thereWasCapture) {
			retValue = "Q" + finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = "Q" + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}

}
