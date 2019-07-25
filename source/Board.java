import java.io.File;
import java.util.*;

public class Board {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Board Attributes
	private Map<Room.RoomAlias, Room> rooms;
	private Map<Character.CharacterAlias, Character> characters;
	private Map<Weapon.WeaponAlais, Weapon> weapons;

	private Cell[][] cells;
	private int rows, cols;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------
	Board() {

		// Generate room cards
		rooms = new HashMap<>();
		for (Room.RoomAlias alias : Room.RoomAlias.values()) {
			rooms.put(alias, new Room(alias.toString()));
		}

		// Generate character cards
		characters = new HashMap<>();
		for (Character.CharacterAlias alias : Character.CharacterAlias.values()) {
			characters.put(alias, new Character(alias.toString()));
		}

		// Generate Weapon cards
		weapons = new HashMap<>();
		for (Weapon.WeaponAlais alias : Weapon.WeaponAlais.values()) {
			weapons.put(alias, new Weapon(alias.toString()));
		}

		// Load Map Based off file layout
		try {
			Scanner sc = new Scanner(getMapBase());
			if (!sc.next().equals("MAP"))
				throw new Exception("Invalid File Type");
			
			// First two indexes = Row Col of map
			rows = sc.nextInt();
			cols = sc.nextInt();
			
			cells = new Cell[rows][cols];

			sc.next(); // skip '\r'
			sc.nextLine(); // skip _ _ _ _ _ ...

			for (int row = 0; row != rows; ++row) {
				String line = sc.nextLine();

				int lineIndex = 3; // skip to cell 01 -> # <- |# ...

				for (int col = 0; col != cols; ++col, lineIndex += 2) {
					char c = line.charAt(lineIndex);

					Cell.Type type = Cell.getType(c);
					Cell cell = new Cell(row, col, type);
					cells[row][col] = cell;

					if (type == Cell.Type.START_PAD) {
						try {
							Character currentCharacter = characters.get(Character.parseAliasFromOrdinalChar(c));
							currentCharacter.setPosition(cell);
							cell.setCharacter(currentCharacter);
						} catch (Exception e) {
							System.out.println("Not a number cell!");
						}
					}

					else if (type == Cell.Type.ROOM) {
						Room room = rooms.get(Room.parseAliasFromOrdinalChar(c));
						room.addCell(cell);
						cell.setRoom(room);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("File Exception: " + e);
		}
		
		
		// Set all the cells neighbors
		for (int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				Cell cell = cells[row][col];
				
				// Neighbor can not be a wall, or out of bounds
				// Walls have no neighbors
				
				if (cell.getType() == Cell.Type.WALL) continue;
				
				if (row > 0) {
					Cell other = cells[row - 1][col];
					if (other.getType() != Cell.Type.WALL) {
						cell.setNeighbor(Cell.Direction.NORTH, other);
					}
				}	
				
				if (row < (rows - 1)) {
					Cell other = cells[row + 1][col];
					if (other.getType() != Cell.Type.WALL) {
						cell.setNeighbor(Cell.Direction.SOUTH, other);
					}
				}	
				
				if (col > 0) {
					Cell other = cells[row][col - 1];
					if (other.getType() != Cell.Type.WALL) {
						cell.setNeighbor(Cell.Direction.WEST, other);
					}
				}	
				
				if (col < (cols - 1)) {
					Cell other = cells[row][col + 1];
					if (other.getType() != Cell.Type.WALL) {
						cell.setNeighbor(Cell.Direction.EAST, other);
					}
				}
			}
		}
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public Map<Character.CharacterAlias, Character> getCharacters() {
		return characters;
	}

	public Map<Room.RoomAlias, Room> getRooms() {
		return rooms;
	}

	public Map<Weapon.WeaponAlais, Weapon> getWeapons() {
		return weapons;
	}

	public Stack<Cell> getPath(Cell start, Cell end, int numSteps) {
		Stack<Cell> path = new Stack<>();
		path.push(start);
		return getPathHelper(path, end, numSteps);
	}

	private Stack<Cell> getPathHelper(Stack<Cell> path, Cell end, int numStepsLeft) {
		// TODO: Implement path finding.
		return null;
	}

	public Cell getCell(int row, int col) {
		if (row < 0 || row >= rows - 1)
			return null;
		if (col < 0 || col >= cols - 1)
			return null;

		return cells[row][col];
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void printBoardState() {
		System.out.println("\t\t   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
		for (int row = 0; row < rows; row++) {
			System.out.print("\t\t");
			for (int col = 0; col < cols; col++) {
				// Print the row number at the start of each line
				if (col == 0) {
					String factoredRowNum = row + "";
					while (factoredRowNum.length() < 2)
						factoredRowNum = "0" + factoredRowNum;
					System.out.print(factoredRowNum + "|");
				}

				System.out.print(cells[row][col].toString());
				System.out.print("|");
			}
			// New line for every row
			System.out.print("\n");
		}
	}
	
	
	public String getMapBase() {
		return "MAP 25 24\r\n" + 
				"   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\r\n" + 
				"01|#|#|#|#|#|#|#|0|#|#|#|#|#|#|#|#|1|#|#|#|#|#|#|#|\r\n" + 
				"02|#|K|K|K|K|#|_|_|#|#|#|B|B|#|#|#|_|_|#|C|C|C|C|#|\r\n" + 
				"03|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|C|C|C|#|\r\n" + 
				"04|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|C|C|C|#|\r\n" + 
				"05|#|K|K|K|K|#|_|_|#|B|B|B|B|B|B|#|_|_|#|C|#|C|C|#|\r\n" + 
				"06|#|#|#|#|K|#|_|_|B|B|B|B|B|B|B|B|_|_|_|_|#|#|#|#|\r\n" + 
				"07|#|_|_|_|_|_|_|_|#|#|B|#|#|B|#|#|_|_|_|_|_|_|_|2|\r\n" + 
				"08|#|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|\r\n" + 
				"09|#|#|#|#|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|#|#|#|#|#|\r\n" + 
				"10|#|D|D|#|#|#|#|_|_|#|#|#|#|#|#|_|_|_|#|A|A|A|A|#|\r\n" + 
				"11|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|A|A|A|A|A|#|\r\n" + 
				"12|#|D|D|D|D|D|D|_|_|#|#|#|#|#|#|_|_|_|#|A|A|A|A|#|\r\n" + 
				"13|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|#|#|#|#|A|#|\r\n" + 
				"14|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|_|_|_|_|_|#|\r\n" + 
				"15|#|D|D|D|D|D|#|_|_|#|#|#|#|#|#|_|_|_|#|#|L|#|#|#|\r\n" + 
				"16|#|#|#|#|D|#|#|_|_|#|#|#|#|#|#|_|_|#|#|L|L|L|L|#|\r\n" + 
				"17|#|_|_|_|_|_|_|_|_|#|#|#|#|#|#|_|_|L|L|L|L|L|L|#|\r\n" + 
				"18|5|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|#|#|L|L|L|L|#|\r\n" + 
				"19|#|_|_|_|_|_|_|_|#|#|H|H|H|#|#|_|_|_|#|#|#|#|#|#|\r\n" + 
				"20|#|#|#|#|E|#|_|_|#|H|H|H|H|H|#|_|_|_|_|_|_|_|_|3|\r\n" + 
				"21|#|E|E|E|E|#|_|_|#|H|H|H|H|H|H|_|_|_|_|_|_|_|_|#|\r\n" + 
				"22|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|#|#|#|#|#|\r\n" + 
				"23|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|S|S|S|S|#|\r\n" + 
				"24|#|E|E|E|E|#|_|_|#|H|H|H|H|H|#|_|_|#|S|S|S|S|S|#|\r\n" + 
				"25|#|#|#|#|#|#|4|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|#|\r\n" + 
				"   A B C D E F G H I J K L M N O P Q R S T U V W X";
	}
	
	public static void main(String[] args) {
		Board b = new Board();
		b.printBoardState();
	}
	
	
}