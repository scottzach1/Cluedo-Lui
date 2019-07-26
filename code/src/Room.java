package src;

import java.util.HashSet;
import java.util.Set;

public class Room extends Card {

	/**
	 * An Enum defining the different Rooms in the Game.
	 */
	public enum RoomAlias {
		 KITCHEN,
		 BALLROOM, 
		 CONSERVATORY, 
		 DINING_ROOM,
		 BILLARD_ROOM,
		 LIBRARY,
		 LOUNGE,
		 HALL,
		 STUDY
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Room Attributes
	private Set<Cell> cells;
	private Set<User> inThisRoom;
	private Set<Cell> doors;
	private Weapon weapon;
	private RoomAlias roomAlias;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	/**
	 * Room: The constructor for a Room.
	 * @param roomAlias The RoomAlias of the Room to create.
	 */
	public Room(RoomAlias roomAlias) {
		super(roomAlias.toString());
		this.roomAlias = roomAlias;
		cells = new HashSet<>();
		doors = new HashSet<>();
		inThisRoom = new HashSet<>();
	}

	/**
	 * calculateDoors: Updates the doors of the room.
	 *
	 * If any cell in room has a neighbour that is not in this room.
	 * Then add that neighbour to the doors. (no door should be in the room).
	 */
	public void calculateDoors() {
		doors = new HashSet<>();

		for (Cell cell : cells) {
			for (Cell neigh : cell.getNeighbors().values()) {
				if (neigh.getRoom() == this) continue;
				doors.add(neigh);
			}
		}
	}

	/**
	 * getDoors: Returns a set of all the doors of the room.
	 * @return
	 */
	public Set<Cell> getDoors() {
		return doors;
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	/**
	 * getCells: Returns a set of all the cells stored in a Room.
	 * @return Set containing all cells.
	 */
	public Set<Cell> getCells() { return cells; }

	/**
	 * addCell: Adds a Cell to a room.
	 * Also sets the cells Room to this.
	 * @param cell Cell to add to the room.
	 */
	public void addCell(Cell cell) {
		if (cell == null) return;
		this.cells.add(cell);
		cell.setRoom(this);
	}

	/**
	 * getInThisRoom: Returns a Set of all the Users in the current Room.
	 * @return Set of users.
	 */
	public Set<User> getInThisRoom() { return inThisRoom; }

	/**
	 * addToRoom: Add a current user to the room.
	 * @param user User to add to the room.
	 */
	public void addToRoom(User user) { this.inThisRoom.add(user); }

	/**
	 * removeFromRoom: Remove a provided User from the room.
	 * @param user User to remove from the room.
	 */
	public void removeFromRoom(User user) { this.inThisRoom.remove(user); }

	/**
	 * getWeapon: Gets the Weapon stored in the Room.
	 * @return Weapon that is stored in the current Room.
	 */
	public Weapon getWeapon() { return weapon; }

	/**
	 * setWeapon: Sets the Weapon currently stored in the Room to a new one.
	 * @param weapon The new Weapon to replace the old Weapon.
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		if (weapon == null) return;
		weapon.setRoom(this);
	}


	/**
	 * getRoomAlias: Gets the RoomAlias of the current Room.
	 * @return RoomAlias of the current Room.
	 */
	public RoomAlias getRoomAlias() { return roomAlias; }

	/**
	 * Given a char, find the matching RoomAlias according to our MapBase.txt file.
	 * @param c The char corresponding to a Room on the MapFile.
	 * @return The corresponding RoomAlias.
	 */
	public static RoomAlias parseAliasFromOrdinalChar(char c) {
		switch (c) {
			case 'K': return RoomAlias.KITCHEN;
			case 'B': return RoomAlias.BALLROOM;
			case 'C': return RoomAlias.CONSERVATORY;
			case 'A': return RoomAlias.BILLARD_ROOM;
			case 'D': return RoomAlias.DINING_ROOM;
			case 'L': return RoomAlias.LIBRARY;
			case 'E': return RoomAlias.LOUNGE;
			case 'H': return RoomAlias.HALL;
			case 'S': return RoomAlias.STUDY;
			default: throw new IllegalStateException("Unexpected Character For Room: " + c);
		}
	}

	/**
	 * Given an int, find the matching RoomAlias according to its ordinal position in the Enum.
	 * @param i The int corresponding to a RoomAlias' enum position.
	 * @return The RoomAlias declared at that enum ordinal.
	 */
	public static RoomAlias parseAliasFromOrdinalInt(int i) {
		switch (i) {
			case 0: return RoomAlias.KITCHEN;
			case 1: return RoomAlias.BALLROOM;
			case 2: return RoomAlias.CONSERVATORY;
			case 3: return RoomAlias.BILLARD_ROOM;
			case 4: return RoomAlias.DINING_ROOM;
			case 5: return RoomAlias.LIBRARY;
			case 6: return RoomAlias.LOUNGE;
			case 7: return RoomAlias.HALL;
			case 8: return RoomAlias.STUDY;
			default: throw new IllegalStateException("Unexpected Character For Room: " + i);
		}
	}
	
	public String toString() {
		StringBuilder peopleStr = new StringBuilder();
		StringBuilder weaponStr = new StringBuilder();
		
		// Append users in the room 
		for (User user : inThisRoom) { peopleStr.append(user.getCharacter().getName() + ", ");}
		if (inThisRoom.isEmpty()) peopleStr.append("no one");
		
		// Append weapons in this room 
		if (weapon != null) weaponStr.append("the " + weapon.getName());
		else weaponStr.append("no weapon");
		
		
		return getName() + ":\n\t has " + peopleStr.toString() + "in it"
				+ "\n\t along with " + weaponStr.toString();
	}
}