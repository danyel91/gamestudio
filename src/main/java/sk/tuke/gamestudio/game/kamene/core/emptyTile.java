package sk.tuke.gamestudio.game.kamene.core;

import java.io.Serializable;

public class EmptyTile extends Tile implements Serializable {

	private int k;
	private int j;
	private int value = 0;

	private final String mark = "-";

	public EmptyTile(int row, int column) {
		k = row;
		j = column;

	}

	public void setK(int k) {
		this.k = k;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getK() {
		return k;
	}

	public int getJ() {
		return j;
	}

	public String toString() {
		return mark;

	}

	@Override
	public int getValue() {

		return this.value;
	}

}
