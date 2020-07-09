package com.gvt.chessboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.chessboard.Chessboard.PlayMode;
import com.gvt.chessboard.Piece.Color;

public class ChessboardUCITest {

	private static Logger logger = LoggerFactory.getLogger(ChessboardUCITest.class);

	@Test
	public void init_chessboard_white_pieces_down() {
		Chessboard chesboard = new Chessboard(true, PlayMode.UCI);
		chesboard.print(true);

		assertEquals("coordinate test", "a1", chesboard.getSquare("a1").getAlgebraicCoordinate());
		assertEquals("coordinate test", "f6", chesboard.getSquare("f6").getAlgebraicCoordinate());
		assertEquals("coordinate test", "g3", chesboard.getSquare("g3").getAlgebraicCoordinate());
	}

	@Test
	public void init_chessboard_white_pieces_up() {
		Chessboard chesboard = new Chessboard(false, PlayMode.UCI);
		chesboard.print(true);

		assertEquals("coordinate test", "a1", chesboard.getSquare("a1").getAlgebraicCoordinate());
		assertEquals("coordinate test", "f6", chesboard.getSquare("f6").getAlgebraicCoordinate());
		assertEquals("coordinate test", "g3", chesboard.getSquare("g3").getAlgebraicCoordinate());
	}

	@Test
	public void init_chessboard_white_pieces_down_few_pieces() {
		Chessboard chesboard = new Chessboard(true, PlayMode.UCI);

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
		Chessboard chesboard = new Chessboard(false, PlayMode.UCI);

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

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("P", 0, 6);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'P', previousPosition.getSquare("a2").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("P", 0, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'P', currentPosition.getSquare("a4").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "a4", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_pawn_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("p", 0, 6);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'p', previousPosition.getSquare("h7").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("p", 0, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'p', currentPosition.getSquare("h5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "h5", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_pawn_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("P", 0, 6);
		previousPosition.setPiece("n", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'P', previousPosition.getSquare("a2").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("b3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("P", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("a2").isEmpty());
		assertEquals("coordinate test with piece", 'P', currentPosition.getSquare("b3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "axb3", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_pawn_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("p", 0, 6);
		previousPosition.setPiece("N", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'p', previousPosition.getSquare("h7").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("g6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("p", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("h7").isEmpty());
		assertEquals("coordinate test with piece", 'p', currentPosition.getSquare("g6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "hxg6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	/////////////// *************** Rook

	@Test
	public void movement_rook_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("R", 1, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("b1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("R", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("b3").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "b1b3", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_rook_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("r", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("r", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("e6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5e6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_rook_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("R", 6, 6);
		previousPosition.setPiece("n", 6, 1);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("g2").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("g7").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("R", 6, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("g2").isEmpty());
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("g7").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "g2g7", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_rook_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false);
		previousPosition.setPlayMode(PlayMode.UCI);
		previousPosition.setPiece("r", 1, 6);
		previousPosition.setPiece("N", 1, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("g7").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("g6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("r", 1, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("g7").isEmpty());
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("g6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "g7g6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

/////////////// *************** Knight

	@Test
	public void movement_knight_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("N", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("N", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'N', currentPosition.getSquare("c6").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "d4c6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_knight_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("n", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("n", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'n', currentPosition.getSquare("f3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5f3", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_knight_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("N", 3, 4);
		previousPosition.setPiece("n", 2, 2);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("c6").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("N", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'N', currentPosition.getSquare("c6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "d4c6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_knight_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("n", 3, 4);
		previousPosition.setPiece("N", 2, 2);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("f3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("n", 2, 2);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'n', currentPosition.getSquare("f3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5f3", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

/////////////// *************** Bishop

	@Test
	public void movement_bishop_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("B", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'B', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("B", 0, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'B', currentPosition.getSquare("a7").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "d4a7", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_bishop_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("b", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'b', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("b", 0, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'b', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5h8", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_bishop_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("B", 3, 4);
		previousPosition.setPiece("n", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'B', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("B", 7, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'B', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "d4h8", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_bishop_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("b", 3, 4);
		previousPosition.setPiece("N", 4, 3);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'b', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("b", 4, 3);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'b', currentPosition.getSquare("d4").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5d4", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

/////////////// *************** Queen

	@Test
	public void movement_queen_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("Q", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("Q", 0, 1);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', currentPosition.getSquare("a7").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "d4a7", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_queen_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("q", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'q', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("q", 4, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'q', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5d5", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_queen_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("Q", 3, 4);
		previousPosition.setPiece("n", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'Q', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("Q", 7, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'Q', currentPosition.getSquare("h8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "d4h8", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_queen_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("q", 3, 4);
		previousPosition.setPiece("N", 2, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'q', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("f5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("q", 2, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'q', currentPosition.getSquare("f5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5f5", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

/////////////// *************** King

	@Test
	public void movement_king_piece_from_initial_position_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("K", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("d4").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("K", 3, 3);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Rook movement from starting point", "d4d5", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_from_initial_position_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("k", 3, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("k", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("e6").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5e6", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_capture_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("K", 3, 4);
		previousPosition.setPiece("n", 3, 5);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("d4").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'n', previousPosition.getSquare("d3").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("K", 3, 5);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("d4").isEmpty());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("d3").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "d4d3", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_capture_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("k", 3, 4);
		previousPosition.setPiece("N", 4, 4);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e5").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'N', previousPosition.getSquare("d5").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("k", 4, 4);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("should be empty", true, currentPosition.getSquare("e5").isEmpty());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("d5").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "e5d5", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_castle_kingside_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("K", 4, 7);
		previousPosition.setPiece("R", 7, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("e1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("h1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("K", 6, 7);
		currentPosition.setPiece("R", 5, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("f1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("g1").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_castle_queenside_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("K", 4, 7);
		previousPosition.setPiece("R", 0, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("e1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("a1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("K", 2, 7);
		currentPosition.setPiece("R", 3, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("d1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("c1").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_castle_kingside_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("k", 3, 7);
		previousPosition.setPiece("r", 0, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("k", 1, 7);
		currentPosition.setPiece("r", 2, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("f8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("g8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_castle_queenside_black_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("k", 3, 7);
		previousPosition.setPiece("r", 7, 7);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("a8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("k", 5, 7);
		currentPosition.setPiece("r", 4, 7);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("d8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("c8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_black_castle_kingside_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("k", 4, 0);
		previousPosition.setPiece("r", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("h8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("k", 6, 0);
		currentPosition.setPiece("r", 5, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("f8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("g8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_black_castle_queenside_white_down() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(true, PlayMode.UCI);
		previousPosition.setPiece("k", 4, 0);
		previousPosition.setPiece("r", 0, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'k', previousPosition.getSquare("e8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'r', previousPosition.getSquare("a8").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(true, PlayMode.UCI);
		currentPosition.setPiece("k", 2, 0);
		currentPosition.setPiece("r", 3, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'r', currentPosition.getSquare("d8").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'k', currentPosition.getSquare("c8").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.BLACK, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_white_castle_kingside_white_up() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("K", 3, 0);
		previousPosition.setPiece("R", 0, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("e1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("h1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("K", 1, 0);
		currentPosition.setPiece("R", 2, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("f1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("g1").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

	@Test
	public void movement_king_piece_white_castle_queenside_white_up() {
		logger.trace("**************** Movements test ****************");

		Chessboard previousPosition = new Chessboard(false, PlayMode.UCI);
		previousPosition.setPiece("K", 3, 0);
		previousPosition.setPiece("R", 7, 0);
		logger.trace("**************** Previous chesboard ****************");
		previousPosition.print(false);
		assertEquals("coordinate test with piece", 'K', previousPosition.getSquare("e1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'R', previousPosition.getSquare("a1").getPiece().getFenLetter());

		Chessboard currentPosition = new Chessboard(false, PlayMode.UCI);
		currentPosition.setPiece("K", 5, 0);
		currentPosition.setPiece("R", 4, 0);
		logger.trace("**************** current chesboard ****************");
		currentPosition.print(false);
		assertEquals("coordinate test with piece", 'R', currentPosition.getSquare("d1").getPiece().getFenLetter());
		assertEquals("coordinate test with piece", 'K', currentPosition.getSquare("c1").getPiece().getFenLetter());

		assertEquals("Pawn movement from starting point", "O-O-O", currentPosition.compare(previousPosition));
		assertEquals("Pawn movement from starting point", Color.WHITE, currentPosition.getPlayForColor());
	}

}
