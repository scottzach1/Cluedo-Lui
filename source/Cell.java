import java.util.Map;
import java.util.regex.Pattern;

public class Cell {

	public static enum Direction { NORTH, SOUTH, EAST, WEST; }
	public static enum Type { ROOM, WALL, BLANK, START_PAD, WEAPON; }

	public static Type getType(char c) {
		switch (c) {
			case '#': return Type.WALL;
			case '_': return Type.BLANK;
			case 'W': return Type.WEAPON;
		}

		if (Pattern.matches("[0-5]", c + "")) {
			return Type.START_PAD;
		}

		try {
			Room.getEnum(c);
			return Type.ROOM;
		} catch (Exception e) {
			return Type.BLANK;
		}
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Cell Attributes
	private Map<Direction, Cell> neighbors;
	private Character character;
	private Weapon weapon;
	private Room room;
	private int col;
	private int row;

	private Type type;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Cell(int aCol, int aRow, Cell.Type aType) {
		col = aCol;
		row = aRow;
		type = aType;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public Type getType() { return type; }

	public Map<Direction, Cell> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Map<Direction, Cell> neighbors) {
		this.neighbors = neighbors;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public void setWeapon(Weapon weapon) { this.weapon = weapon; }

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getCol() { return col; }

	public int getRow() { return row; }
	
	public String toString() {
		return "Yet to implement";
	}
}