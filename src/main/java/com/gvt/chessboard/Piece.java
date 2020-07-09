package com.gvt.chessboard;

import com.gvt.chessboard.Chessboard.PlayMode;

public interface Piece {

	enum Color {
		WHITE, BLACK;
	}

	Color getColor();

	char getFenLetter();

	String getMovement(Square startingSquare, Square previousStateInfinalSquare, Square finalSquare, PlayMode playMode);
}
