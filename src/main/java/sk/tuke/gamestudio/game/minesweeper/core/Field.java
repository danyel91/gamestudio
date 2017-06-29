package sk.tuke.gamestudio.game.minesweeper.core;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio.game.minesweeper.core.Tile.State;


/**
 * Field represents playing field and game logic.
 */
public class Field {
	/**
	 * Playing field tiles.
	 */
	private final Tile[][] tiles;

	/**
	 * Field row count. Rows are indexed from 0 to (rowCount - 1).
	 */
	private final int rowCount;

	/**
	 * Column count. Columns are indexed from 0 to (columnCount - 1).
	 */
	private final int columnCount;

	/**
	 * Mine count.
	 */
	private final int mineCount;

	/**
	 * Game state.
	 */
	private GameState state = GameState.PLAYING;

//	@Autowired
//	private ScoreService scoreService;
	
	
	/**
	 * Constructor.
	 *
	 * @param rowCount
	 *            row count
	 * @param columnCount
	 *            column count
	 * @param mineCount
	 *            mine count
	 */
	public Field(int rowCount, int columnCount, int mineCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
		tiles = new Tile[rowCount][columnCount];

		// generate the field content
		generate();
		
	}

	/**
	 * Opens tile at specified indeces.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void openTile(int row, int column) {
		
		

		Tile tile = tiles[row][column];
		if (tile.getState() == Tile.State.CLOSED) {
			tile.setState(Tile.State.OPEN);
			if (tile instanceof Mine) {
				state = GameState.FAILED;
				return;
			}

			if (((Clue) tiles[row][column]).getValue() == 0) {
				openAdjacentTiles(row, column);
			}

			if (isSolved()) {
				state = GameState.SOLVED;
				return;
			}
		}
	}

	// vyberiem dlazdicu
	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getMineCount() {
		return mineCount;
	}

	public GameState getState() {
		return state;
	}

	/**
	 * Marks tile at specified indeces.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void markTile(int row, int column) {
		{
			Tile tile = tiles[row][column];
			if (tile.getState() == Tile.State.CLOSED) {
				tile.setState(Tile.State.MARKED);

			} else if (tile.getState() == Tile.State.MARKED) {
				tile.setState(Tile.State.CLOSED);
			}
		}
	}

	/**
	 * Generates playing field.
	 */
	private void generate() {
		Random r = new Random();
		int i = 0;
		while (i < mineCount) {
			int a = r.nextInt(rowCount);
			int b = r.nextInt(columnCount);
			if (tiles[a][b] == null) {
				tiles[a][b] = new Mine();
				i++;
			}
		}
		for (int k = 0; k < rowCount; k++) {
			for (int j = 0; j < columnCount; j++) {
				if (tiles[k][j] == null) {
					tiles[k][j] = new Clue(countAdjacentMines(k, j));
				}
			}
		}
	}

	/**
	 * Returns true if game is solved, false otherwise.
	 *
	 * @return true if game is solved, false otherwise
	 */
	private boolean isSolved() {
		return ((getRowCount() * getColumnCount()) - getNumberOf(State.OPEN) == getMineCount());
	}

	private int getNumberOf(Tile.State state) {
		int number = 0;
		for (int j = 0; j < getRowCount(); j++) {

			for (int k = 0; k < getColumnCount(); k++) {
				if (tiles[j][k].getState() == state) {
					number++;
				}
			}

		}
		return number;
	}

	/**
	 * Returns number of adjacent mines for a tile at specified position in the
	 * field.
	 *
	 * @param row
	 *            row number.
	 * @param column
	 *            column number.
	 * @return number of adjacent mines.
	 */
	private int countAdjacentMines(int row, int column) {
		int count = 0;
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						if (tiles[actRow][actColumn] instanceof Mine) {
							count++;
						}
					}
				}
			}
		}

		return count;
	}

	private void openAdjacentTiles(int row, int column) {
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						openTile(actRow, actColumn);
					}
				}
			}
		}

	}

	public int getRemainingMineCount() {
		return getMineCount() - getNumberOf(Tile.State.MARKED);
	}

	@Override
	public String toString() {
		Formatter field = new Formatter();
		field.format("   ");

		for (int c = 1; c <= getColumnCount(); c++) {
			field.format("%3s", c);
		}
		field.format("\n");

		for (int r = 0; r < getRowCount(); r++) {
			field.format("%3s", (char) ('A' + r));
			for (int c = 0; c < getColumnCount(); c++) {
				field.format("%3s", getTile(r, c));

			}
			field.format("\n");

		}
		return field.toString();
	}

}
