package com.gvt.chessboard;

public class Rook implements Piece {

	private char fenLetter;

	public Rook(Color color) {
		fenLetter = color == Color.BLACK ? 'r' : 'R';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
