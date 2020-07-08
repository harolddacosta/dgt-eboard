package com.gvt.chessboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessboardTest {

	private static Logger logger = LoggerFactory.getLogger(ChessboardTest.class);

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
	public void init_chessboard_white_pieces_down_few_pieces() {
		Chessboard chesboard = new Chessboard(true);

		chesboard.setPiece("r", 0, 0);
		chesboard.setPiece("r", 7, 0);
		chesboard.setPiece("p", 4, 2);

		chesboard.print(false);

		assertEquals("coordinate test with piece", 'r', chesboard.getSquare("a8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', chesboard.getSquare("h8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'p', chesboard.getSquare("e6").getPiece().getFenLetter());
	}

	@Test
	public void init_chessboard_black_pieces_down_few_pieces() {
		Chessboard chesboard = new Chessboard(false);

		chesboard.setPiece("r", 0, 0);
		chesboard.setPiece("r", 7, 0);
		chesboard.setPiece("p", 4, 2);

		chesboard.print(false);

		assertEquals("coordinate test with piece", 'r', chesboard.getSquare("a1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', chesboard.getSquare("h1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'p', chesboard.getSquare("d3").getPiece().getFenLetter());
	}

/////////////// *************** Pawn

	@Test
	public void movement_pawn_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("P", 0, 6);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'P', previousPosition.getSquare("a2").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("P", 0, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'P', currentPosition.getSquare("a4").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "a4", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_pawn_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("p", 0, 6);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'p', previousPosition.getSquare("h7").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("p", 0, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'p', currentPosition.getSquare("h5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "h5", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_pawn_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("P", 0, 6);
		previousPosition.setPiece("n", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'P', previousPosition.getSquare("a2").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("b3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("P", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("a2").isEmpty());
		assertEquals("coordinate test with piece", 'P', currentPosition.getSquare("b3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "axb3", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_pawn_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("p", 0, 6);
		previousPosition.setPiece("N", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'p', previousPosition.getSquare("h7").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("g6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("p", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("h7").isEmpty());
		assertEquals("coordinate test with piece", 'p', currentPosition.getSquare("g6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "hxg6", currentPosition.compare(previousPosition));
	}

	/////////////// *************** Rook

	@Test
	public void movement_rook_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("R", 1, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("b1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("R", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("b3").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "Rb3", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_rook_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("r", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("r", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("e6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Re6", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_rook_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("R", 6, 6);
		previousPosition.setPiece("n", 6, 1);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("g2").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("g7").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("R", 6, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("g2").isEmpty());
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("g7").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Rxg7", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_rook_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("r", 1, 6);
		previousPosition.setPiece("N", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("g7").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("g6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("r", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("g7").isEmpty());
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("g6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Rxg6", currentPosition.compare(previousPosition));
	}

/////////////// *************** Knight

	@Test
	public void movement_knight_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("N", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("N", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'N', currentPosition.getSquare("c6").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "Nc6", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_knight_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("n", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("n", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'n', currentPosition.getSquare("f3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Nf3", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_knight_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("N", 3, 4);
		previousPosition.setPiece("n", 2, 2);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("c6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("N", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'N', currentPosition.getSquare("c6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Nxc6", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_knight_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("n", 3, 4);
		previousPosition.setPiece("N", 2, 2);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("f3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("n", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'n', currentPosition.getSquare("f3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Nxf3", currentPosition.compare(previousPosition));
	}

/////////////// *************** Bishop

	@Test
	public void movement_bishop_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("B", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'B', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("B", 0, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'B', currentPosition.getSquare("a7").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "Ba7", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_bishop_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("b", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'b', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("b", 0, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'b', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Bh8", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_bishop_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("B", 3, 4);
		previousPosition.setPiece("n", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'B', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("B", 7, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'B', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Bxh8", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_bishop_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("b", 3, 4);
		previousPosition.setPiece("N", 4, 3);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'b', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("b", 4, 3);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'b', currentPosition.getSquare("d4").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Bxd4", currentPosition.compare(previousPosition));
	}

/////////////// *************** Queen

	@Test
	public void movement_queen_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("Q", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("Q", 0, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', currentPosition.getSquare("a7").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "Qa7", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_queen_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("q", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'q', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("q", 4, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'q', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Qd5", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_queen_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("Q", 3, 4);
		previousPosition.setPiece("n", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("Q", 7, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'Q', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Qxh8", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_queen_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("q", 3, 4);
		previousPosition.setPiece("N", 2, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'q', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("f5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("q", 2, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'q', currentPosition.getSquare("f5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Qxf5", currentPosition.compare(previousPosition));
	}

/////////////// *************** King

	@Test
	public void movement_king_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("K", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("K", 3, 3);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "Kd5", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_king_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("k", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("k", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("e6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Ke6", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_king_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true);
		previousPosition.setPiece("K", 3, 4);
		previousPosition.setPiece("n", 3, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("d3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true);
		currentPosition.setPiece("K", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("d3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Kxd3", currentPosition.compare(previousPosition));
	}

	@Test
	public void movement_king_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPiece("k", 3, 4);
		previousPosition.setPiece("N", 4, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false);
		currentPosition.setPiece("k", 4, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "Kxd5", currentPosition.compare(previousPosition));
	}

}
