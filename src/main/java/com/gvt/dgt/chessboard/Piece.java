package com.gvt.dgt.chessboard;

public interface Piece {

	enum Color {
		WHITE, BLACK;
	}

	char getFenLetter();
}
