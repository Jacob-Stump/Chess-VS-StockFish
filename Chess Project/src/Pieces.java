import java.util.LinkedList;

public class Pieces {
	
		int col; //location on board
		int row; //location on board
		boolean hasMoved; //used for castling and en passant privileges 
		boolean inCheck; //used for moving privileges via checks
		PieceColor color; //color of piece 
		PieceType type; //type of piece 
		String name;
		public static LinkedList<Pieces> pieceBox = new LinkedList<Pieces>();
		
		public Pieces(String name, PieceColor color, PieceType type) {
			this.color = color;
			this.type = type; 
			this.name = name;
			
			
			if(type == PieceType.KING) {
				this.hasMoved = false;
				this.inCheck = false;
			}
			
			if(type == PieceType.PAWN) {
				this.hasMoved = false;
			} 
			
			if(type == PieceType.ROOK) {
				this.hasMoved = false;
			}
		    
			if(this.type != PieceType.NONE) {
				pieceBox.add(this);
			}
		}
		
		public String getPieceName() {
		    return name;
		}
		
	}
