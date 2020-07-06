package com.gvt.dgt.chessboard;

public class King implements Piece {

	private char fenLetter;

	public King(Color color) {
		fenLetter = color == Color.BLACK ? 'k' : 'K';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
