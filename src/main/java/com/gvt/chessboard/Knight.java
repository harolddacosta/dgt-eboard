package com.gvt.chessboard;

public class Knight implements Piece {

	private char fenLetter;

	public Knight(Color color) {
		fenLetter = color == Color.BLACK ? 'n' : 'N';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
