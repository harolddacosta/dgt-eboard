package com.gvt.chessboard;

public interface Piece {

	enum Color {
		WHITE, BLACK;
	}

	char getFenLetter();
}
