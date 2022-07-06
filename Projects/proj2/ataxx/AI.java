/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.Random;
import java.util.ArrayList;

import static ataxx.PieceColor.*;

/** A Player that computes its own moves.
 *  @author Eduard Mirzoyan
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        Move best = null;
        int bestScore = 0;
        if (depth == 0) {
            return staticScore(board, INFTY);
        }
        ArrayList<Move> possibleMoves = getPossibleMoves(board);
        if (board.legalMove(Move.PASS)) {
            possibleMoves.add(Move.pass());
        }
        int scoreA = alpha;
        int scoreB = beta;
        for (int i = 0; i < possibleMoves.size(); i += 1) {
            Move currentMove = possibleMoves.get(i);
            if (!board.legalMove(currentMove)) {
                continue;
            }
            if (sense == 1) {
                board.makeMove(currentMove);
                bestScore = minMax(board, depth - 1, false, -1, scoreA, scoreB);
                if (bestScore > scoreA) {
                    scoreA = bestScore;
                    if (saveMove) {
                        _lastFoundMove = currentMove;
                    }
                }
                board.undo();
            } else {
                board.makeMove(currentMove);
                bestScore = minMax(board, depth - 1, false, 1, scoreA, scoreB);
                if (bestScore < scoreB) {
                    scoreB = bestScore;
                    if (saveMove) {
                        _lastFoundMove = currentMove;
                    }
                }
                board.undo();
            }
        }
        return staticScore(board, INFTY);
    }

    private ArrayList<Move> getPossibleMoves(Board board) {
        int extendedSide = Move.EXTENDED_SIDE;
        ArrayList<Move> possibleMoves = new ArrayList<Move>();
        for (int a = 0; a < extendedSide * extendedSide; a++) {
            if (board.get(a) == board.whoseMove()) {
                for (int i = -2; i < 3; i++) {
                    for (int j = -2; j < 3; j++) {
                        int toIndex = board.neighbor(a, i, j);
                        PieceColor pieceColor = board.get(toIndex);
                        if (pieceColor == EMPTY) {
                            char c0 = (char) ((a - (extendedSide * 2 + 2))
                                    % extendedSide + 'a');
                            char c1 = (char) ((toIndex - (extendedSide * 2 + 2))
                                    % extendedSide + 'a');
                            char r0 = (char) ((a - (extendedSide * 2 + 2))
                                    / extendedSide + '1');
                            char r1 = (char) ((toIndex - (extendedSide * 2 + 2))
                                    / extendedSide + '1');
                            possibleMoves.add(Move.move(c0, r0, c1, r1));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }

        return board.redPieces() - board.bluePieces();
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
