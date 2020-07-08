package com.gvt.chessboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rook implements Piece {

	private static Logger logger = LoggerFactory.getLogger(Rook.class);

	private Color color;
	private char fenLetter;

	public Rook(Color color) {
		this.color = color;
		fenLetter = color == Color.BLACK ? 'r' : 'R';
	}

	public Color getColor() {
		return color;
	}

	@Override
	public char getFenLetter() {
		return fenLetter;
	}

	@Override
	public String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare) {
		String retValue = null;
		boolean thereWasCapture = false;

		if (finalSquare.getPiece().getFenLetter() == 'K' && finalSquare.getAlgebraicCoordinate().equals("c1")) {
			logger.info("Move:{}", "O-O-O");

			return "O-O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'K' && finalSquare.getAlgebraicCoordinate().equals("g1")) {
			logger.info("Move:{}", "O-O");

			return "O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'k' && finalSquare.getAlgebraicCoordinate().equals("g8")) {
			logger.info("Move:{}", "O-O");

			return "O-O";
		}

		if (finalSquare.getPiece().getFenLetter() == 'k' && finalSquare.getAlgebraicCoordinate().equals("c8")) {
			logger.info("Move:{}", "O-O-O");

			return "O-O-O";
		}

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
