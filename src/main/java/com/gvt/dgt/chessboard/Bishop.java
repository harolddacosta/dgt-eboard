package com.gvt.dgt.chessboard;

public class Bishop implements Piece {

	private char fenLetter;

	public Bishop(Color color) {
		fenLetter = color == Color.BLACK ? 'b' : 'B';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
