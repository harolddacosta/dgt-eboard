package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Chessboard.PlayMode;

public class Pawn implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Pawn.class);

	private Color color;
	private char fenLetter;

	public Pawn(Color color) {
		this.color = color;
		fenLetter = color == Color.BLACK ? 'p' : 'P';
	}

	public Color getColor() {
		return color;
	}

	@Override
	public char getFenLetter() {
		return fenLetter;
	}

	@Override
	public String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare, PlayMode playMode) {
		String retValue = null;
		boolean thereWasCapture = false;

		if (!previousStateInfinalSquare.isEmpty()) {
			logger.trace("There was a capture");

			thereWasCapture = true;
		}

		if (!thereWasCapture) {
			retValue = finalSquare.getAlgebraicCoordinate();
		} else {
			retValue = startingSquare.getAlgebraicCoordinate().charAt(0) + "x" + finalSquare.getAlgebraicCoordinate();
		}

		logger.info("Move:{}", retValue);

		return retValue;
	}

}
