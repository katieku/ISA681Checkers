
public class Piece {
	String location = "";
	final int pieceID;
	private static int numOfPieces = 0;
	String color = "";
	public enum Color {Red, Black};
	Color side;
	public enum PieceType {Pawn, King};
	PieceType type;
	String temp = "";
	
	public Piece(String start, String col){
		//If Location isn't defined, make sure that movement holds a valid value.
		if (location == "" && (start.charAt(0) == 'A' || start.charAt(0) == 'B' || start.charAt(0) == 'C' || start.charAt(0) == 'F' || start.charAt(0) == 'G' || start.charAt(0) == 'H') && (start.charAt(1) == '1' || start.charAt(0) == '2' || start.charAt(0) == '3' || start.charAt(0) == '4' || start.charAt(0) == '5' || start.charAt(0) == '6' || start.charAt(0) == '7' || start.charAt(0) == '8') && start.length() == 2)
			location = start;
		//Moving if location is already set. Does not have information on attacking a piece.
		//throw assertion error if movement or col don't fall into proper mold. 
		type = PieceType.Pawn;
		numOfPieces++;
		pieceID = numOfPieces;
		if (col == "Red")
			side = Color.Red;
		else if (col == "Black")
			side = Color.Black;
	}
	
	public Object clone(){
		 Piece result;
		try {
			result = (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
		 return result;
	}
	
	protected void moveMe(String movement){
		if (location != ""){
			if (location.charAt(0) == 'A'){
				if (movement.charAt(0) == 'B')
					temp = "B";}
			else if (location.charAt(0) == 'B'){
				if (movement.charAt(0) == 'C')
					temp = "C";}
			else if (location.charAt(0) == 'C'){
				if (movement.charAt(0) == 'D')
					temp = "D";}
			else if (location.charAt(0) == 'D'){
				if (movement.charAt(0) == 'E')
					temp = "E";}
			else if (location.charAt(0) == 'E'){
				if (movement.charAt(0) == 'F')
					temp = "F";}
			else if (location.charAt(0) == 'F'){
				if (movement.charAt(0) == 'G')
					temp = "G";}
			else if (location.charAt(0) == 'G'){
				if (movement.charAt(0) == 'H')
					temp = "H";}
			if (location.charAt(1) == '1'){
				if (movement.charAt(0) == '2')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '2'){
				if (movement.charAt(0) == '3' || movement.charAt(0) == '1')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '3'){
				if (movement.charAt(0) == '2' || movement.charAt(0) == '4')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '4'){
				if (movement.charAt(0) == '3' || movement.charAt(0) == '5')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '5'){
				if (movement.charAt(0) == '4' || movement.charAt(0) == '6')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '6'){
				if (movement.charAt(0) == '5' || movement.charAt(0) == '7')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '7'){
				if (movement.charAt(0) == '6' || movement.charAt(0) == '8')
					temp = temp + location.charAt(1);}
			else if (location.charAt(1) == '8'){
				if (movement.charAt(0) == '7')
					temp = temp + location.charAt(1);}
		}
	}
	
	protected void kingMe(){
		if (location.charAt(0) == 'A' || location.charAt(0) == 'H')
			type = PieceType.King;
	}
	
	protected void CaptureMe(){
		location = "CC";
	}
	
	public int HashCode(){
		int num = pieceID;
		return num;
	}
}
