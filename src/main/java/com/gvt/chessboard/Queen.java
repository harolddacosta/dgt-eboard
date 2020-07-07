package com.gvt.chessboard;

public class Queen implements Piece {

	private char fenLetter;

	public Queen(Color color) {
		fenLetter = color == Color.BLACK ? 'q' : 'Q';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
