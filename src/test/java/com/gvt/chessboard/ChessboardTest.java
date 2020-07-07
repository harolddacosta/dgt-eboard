package com.gvt.chessboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChessboardTest {

	@Test
	public void init_chessboard_white_pieces_down() {
		Chessboard chesboard = new Chessboard(true);
		chesboard.print(true);

		assertEquals("coordinate test", "a1", chesboard.getSquare("a1").getAlgebraicCoordinate());
		assertEquals("coordinate test", "f6", chesboard.getSquare("f6").getAlgebraicCoordinate());
		assertEquals("coordinate test", "g3", chesboard.getSquare("g3").getAlgebraicCoordinate());
	}

	@Test
	public void init_chessboard_white_pieces_up() {
		Chessboard chesboard = new Chessboard(false);
		chesboard.print(true);

		assertEquals("coordinate test", "a1", chesboard.getSquare("a1").getAlgebraicCoordinate());
		assertEquals("coordinate test", "f6", chesboard.getSquare("f6").getAlgebraicCoordinate());
		assertEquals("coordinate test", "g3", chesboard.getSquare("g3").getAlgebraicCoordinate());
	}

	@Test
	public void init_chessboard_white_pieces_up_all_pieces() {
		Chessboard chesboard = new Chessboard(true);

		chesboard.setPiece("r", 0, 0);
		chesboard.setPiece("r", 7, 0);

		chesboard.print(false);

		assertEquals("coordinate test with piece", "r", chesboard.getSquare("a8"));
	}
}
