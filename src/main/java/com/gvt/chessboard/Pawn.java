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

		logger.debug("finalSquare.getAlgebraicCoordinate().charAt(0):{}", finalSquare.getAlgebraicCoordinate());
		logger.debug("startingSquare.getAlgebraicCoordinate().charAt(0):{}", startingSquare.getAlgebraicCoordinate());
		logger.debug("previousStateInfinalSquare.getAlgebraicCoordinate():{}", previousStateInfinalSquare.getAlgebraicCoordinate());

		logger.debug("finalSquare.getPiece():{}", finalSquare.getPiece().getFenLetter());
		logger.debug("startingSquare.getPiece():{}", startingSquare.getPiece().getFenLetter());
//		logger.debug("previousStateInfinalSquare.getPiece():{}", previousStateInfinalSquare.getPiece().getFenLetter());

		if (!previousStateInfinalSquare.isEmpty()
				|| (finalSquare.getAlgebraicCoordinate().charAt(0) != startingSquare.getAlgebraicCoordinate().charAt(0))) {
			logger.debug("There was a capture");

			thereWasCapture = true;
		}

		if ((finalSquare.getPiece().getFenLetter() == 'P' && startingSquare.getPiece().getFenLetter() == 'p')) {
			logger.debug("There was a capture en passant to left");

			return retValue = Character.valueOf((char) ((int) (startingSquare.getAlgebraicCoordinate().charAt(0)) + 1)) + "x"
					+ finalSquare.getAlgebraicCoordinate();
		}

		if ((finalSquare.getPiece().getFenLetter() == 'p' && startingSquare.getPiece().getFenLetter() == 'P')) {
			logger.debug("There was a capture en passant to left");

			return retValue = Character.valueOf((char) ((int) (startingSquare.getAlgebraicCoordinate().charAt(0)) - 1)) + "x"
					+ finalSquare.getAlgebraicCoordinate();
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
