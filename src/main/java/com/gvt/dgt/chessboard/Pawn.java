package com.gvt.dgt.chessboard;

public class Pawn implements Piece {

	private char fenLetter;

	public Pawn(Color color) {
		fenLetter = color == Color.BLACK ? 'p' : 'P';
	}

	public char getFenLetter() {
		return fenLetter;
	}

}
