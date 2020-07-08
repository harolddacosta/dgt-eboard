package com.gvt.chessboard;

public interface Piece {

	enum Color {
		WHITE, BLACK;
	}

	Color getColor();

	char getFenLetter();

	String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare);
}
