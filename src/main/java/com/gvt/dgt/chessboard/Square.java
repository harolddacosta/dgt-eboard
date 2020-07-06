package com.gvt.dgt.chessboard;

public class Square {

	private String algebraicCoordinate;
	private Piece piece = null;

	public Square(String square, Piece piece) {
		algebraicCoordinate = square;
		this.piece = piece;
	}

	public Square(String square) {
		algebraicCoordinate = square;
	}

	public boolean isEmpty() {
		return piece == null ? Boolean.TRUE : Boolean.FALSE;
	}

	public Piece getPiece() {
		return piece;
	}

	@Override
	public String toString() {
		return piece == null ? "X" : String.valueOf(piece.getFenLetter());
	}

}
