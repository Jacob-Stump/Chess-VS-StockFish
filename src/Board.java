import java.util.Arrays;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;


public class Board extends JFrame implements MouseListener {
                       
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String lastMove;
	public static int [] enPassantCheck;
	public int[] stockFishPieceSelect = new int [2];
	public int[]stockFishDestinationSelect = new int [2];
	public String FEN;
	private Pieces[][] chessBoard = new Pieces [8][8]; //physical game board
	private boolean[][] sightBoard = new boolean[8][8];
	private boolean[][] blackSightBoard = new boolean[8][8];
    private JPanel panel;
    private JFrame frame = new JFrame();
    private int[] position = new int[2]; //returns a pieces location as an array [row, col]
    public  LinkedList <Pieces> pieceBox = Pieces.pieceBox; //holds all current pieces on board (null pieces excluded)
    public boolean whiteTurn = true;
    private Pieces selectedPiece;
    private int[] selectedPieceLocation;
    private Pieces destinationPiece;
    private int[] destinationPieceLocation;
    public boolean firstmovemade = false;
    private int moveNumber = 1; //keeps track of current move number for stockfish FEN string
    private Stockfish sf = new Stockfish();
    public int SFlevel;
      
    private Image[] pieceImages = new Image[12];
    Turn Turn = new Turn(PieceColor.WHITE);
    
    Pieces nul = new Pieces(" -- ", PieceColor.NONE, PieceType.NONE);
    Pieces wR  = new Pieces(" wR ", PieceColor.WHITE, PieceType.ROOK);
	Pieces wN  = new Pieces(" wN ", PieceColor.WHITE, PieceType.KNIGHT);
	Pieces wB  = new Pieces(" wB ", PieceColor.WHITE, PieceType.BISHOP);
	Pieces wQ  = new Pieces(" wQ ", PieceColor.WHITE, PieceType.QUEEN);
	Pieces wK  = new Pieces(" wK ", PieceColor.WHITE, PieceType.KING);
	Pieces wR2 = new Pieces(" wR ", PieceColor.WHITE, PieceType.ROOK);
	Pieces wN2 = new Pieces(" wN ", PieceColor.WHITE, PieceType.KNIGHT);
	Pieces wB2 = new Pieces(" wB ", PieceColor.WHITE, PieceType.BISHOP);
	
	Pieces wP1 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP2 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP3 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP4 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP5 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP6 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP7 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);
	Pieces wP8 = new Pieces(" wP ", PieceColor.WHITE, PieceType.PAWN);

	Pieces bR  = new Pieces(" bR ", PieceColor.BLACK, PieceType.ROOK);
	Pieces bN  = new Pieces(" bN ", PieceColor.BLACK, PieceType.KNIGHT);
	Pieces bB  = new Pieces(" bB ", PieceColor.BLACK, PieceType.BISHOP);
	Pieces bQ  = new Pieces(" bQ ", PieceColor.BLACK, PieceType.QUEEN);
	Pieces bK  = new Pieces(" bK ", PieceColor.BLACK, PieceType.KING);
	Pieces bR2 = new Pieces(" bR ", PieceColor.BLACK, PieceType.ROOK);
	Pieces bN2 = new Pieces(" bN ", PieceColor.BLACK, PieceType.KNIGHT);
	Pieces bB2 = new Pieces(" bB ", PieceColor.BLACK, PieceType.BISHOP);
	
	Pieces bP1 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP2 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP3 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP4 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP5 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP6 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP7 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
	Pieces bP8 = new Pieces(" bP ", PieceColor.BLACK, PieceType.PAWN);
    
 
	public void initializeGUI(Board board) throws IOException {
		
		getPieceImages();
		frame.setBounds(10, 10, 512, 512);
    	frame.setUndecorated(true);
    	panel = new JPanel() {
    		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
    		public void paint (Graphics g) {
				Color lightBrown = new Color(184,134,11);
	   	
				boolean white = true;
	    			for(int row = 0; row < 8; row++) {
	    				for(int col = 0; col < 8; col++) {
	    					if(white) {
	    						g.setColor(Color.white);
	    					}
	    					else {
	    						g.setColor(lightBrown);
	    					}
	    					
	    					g.fillRect(row*64, col*64, 64, 64);
	    					white=!white;
	    				}
	    				white=!white;
	    			}
	    			
	    			for(Pieces p: pieceBox) {
	    				int i = 0;
	    				if(p.name.equals(" wK ")) {
	    					i = 0;
	    				}
	    				if(p.name.equals(" wQ " )) {
	    					i = 1;
	    				}
	    				if(p.name.equals(" wB ")) {
	    					i = 2;
	    				}
	    				if(p.name.equals(" wN ")) {
	    					i = 3;
	    				}
	    				if(p.name.equals(" wR ")) {
	    					i = 4;
	    				}
	    				if(p.name.equals(" wP ")) {
	    					i = 5;
	    				}
	    				if(p.name.equals(" bK ")) {
	    					i = 6;
	    				}
	    				if(p.name.equals(" bQ ")) {
	    					i = 7;
	    				}
	    				if(p.name.equals(" bB ")) {
	    					i = 8;
	    				}
	    				if(p.name.equals(" bN ")) {
	    					i = 9;
	    				}
	    				if(p.name.equals(" bR ")) {
	    					i = 10;
	    				}
	    				if(p.name.equals(" bP ")) {
	    					i = 11;
	    				}
	    				getPosition(p);
	    				g.drawImage(pieceImages[i], position[1]*64, position[0]*64, this);
	    						
	    			}
				}
    	};

    	
    	frame.add(panel);
    	frame.addMouseListener(board);
    	frame.setDefaultCloseOperation(3);
    	frame.setVisible(true);
	}
	
	public void initializeBoard(Board board) {
		
		board.chessBoard[0][0] = board.bR;
		board.chessBoard[0][1] = board.bN;
		board.chessBoard[0][2] = board.bB;
		board.chessBoard[0][3] = board.bQ;
		board.chessBoard[0][4] = board.bK;
		board.chessBoard[0][5] = board.bB2;
		board.chessBoard[0][6] = board.bN2;
		board.chessBoard[0][7] = board.bR2;
				
		board.chessBoard[1][0] = board.bP1;
		board.chessBoard[1][1] = board.bP2;
		board.chessBoard[1][2] = board.bP3;
		board.chessBoard[1][3] = board.bP4;
		board.chessBoard[1][4] = board.bP5;
		board.chessBoard[1][5] = board.bP6;
		board.chessBoard[1][6] = board.bP7;
		board.chessBoard[1][7] = board.bP8;
				
		board.chessBoard[7][0] = board.wR;
		board.chessBoard[7][1] = board.wN;
		board.chessBoard[7][2] = board.wB;
		board.chessBoard[7][3] = board.wQ;
		board.chessBoard[7][4] = board.wK;
		board.chessBoard[7][5] = board.wB2;
		board.chessBoard[7][6] = board.wN2;
		board.chessBoard[7][7] = board.wR2;
				
		board.chessBoard[6][0] = board.wP1;
		board.chessBoard[6][1] = board.wP2;
		board.chessBoard[6][2] = board.wP3;
		board.chessBoard[6][3] = board.wP4;
		board.chessBoard[6][4] = board.wP5;
		board.chessBoard[6][5] = board.wP6;
		board.chessBoard[6][6] = board.wP7;
		board.chessBoard[6][7] = board.wP8;
		
		for(int row = 0; row < board.chessBoard.length; row++) {
			for(int col = 0; col < board.chessBoard[row].length; col++)
			{
				if (board.chessBoard[row][col] == null) {
					board.chessBoard[row][col] = board.nul;
				}
			}
		}
		
	}
	
	public void arrayToMove() { //converts array position to PGN position format  (ie. [0,0] to A8)
		 lastMove = "";
		int rowSelected = selectedPieceLocation[0];
		int colSelected = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		
		if(colSelected == 0) {
			lastMove += "a";
		}
		else if (colSelected == 1) {
			lastMove += "b";
		}
		else if (colSelected == 2) {
			lastMove += "c";
		}
		else if (colSelected == 3) {
			lastMove += "d";
		}
		else if (colSelected == 4) {
			lastMove += "e";
		}
		else if (colSelected == 5) {
			lastMove += "f";
		}
		else if (colSelected == 6) {
			lastMove += "g";
		}
		else if (colSelected == 7) {
			lastMove += "h";
		}
		if(rowSelected == 0) {
			lastMove += "8";
		}
		else if (rowSelected == 1) {
			lastMove += "7";
		}
		else if (rowSelected == 2) {
			lastMove += "6";
		}
		else if (rowSelected == 3) {
			lastMove += "5";
		}
		else if (rowSelected == 4) {
			lastMove += "4";
		}
		else if (rowSelected == 5) {
			lastMove += "3";
		}
		else if (rowSelected == 6) {
			lastMove += "2";
		}
		else if (rowSelected == 7) {
			lastMove += "1";
		}
		
		if(colDest == 0) {
			lastMove += "a";
		}
		else if (colDest == 1) {
			lastMove += "b";
		}
		else if (colDest == 2) {
			lastMove += "c";
		}
		else if (colDest == 3) {
			lastMove += "d";
		}
		else if (colDest == 4) {
			lastMove += "e";
		}
		else if (colDest == 5) {
			lastMove += "f";
		}
		else if (colDest == 6) {
			lastMove += "g";
		}
		else if (colDest == 7) {
			lastMove += "h";
		}
		
		if(rowDest == 0) {
			lastMove += "8";
		}
		else if (rowDest == 1) {
			lastMove += "7";
		}
		else if (rowDest == 2) {
			lastMove += "6";
		}
		else if (rowDest == 3) {
			lastMove += "5";
		}
		else if (rowDest == 4) {
			lastMove += "4";
		}
		else if (rowDest == 5) {
			lastMove += "3";
		}
		else if (rowDest == 6) {
			lastMove += "2";
		}
		else if (rowDest == 7) {
			lastMove += "1";
		}
		
		

	}

	public void moveToArray(String move) { //converts PGN position to array position (ie.[A8 to [0,0])
		for(int i = 0; i < move.length(); i++) {
			if (i == 0) {
				if(move.charAt(i) == 'a') {
					stockFishPieceSelect[1] = 0;
				}
				else if(move.charAt(i) == 'b') {
					stockFishPieceSelect[1] = 1;
				}
				else if(move.charAt(i) == 'c') {
					stockFishPieceSelect[1] = 2;
				}
				else if(move.charAt(i) == 'd') {
					stockFishPieceSelect[1] = 3;
				}
				else if(move.charAt(i) == 'e') {
					stockFishPieceSelect[1] = 4;
				}
				else if(move.charAt(i) == 'f') {
					stockFishPieceSelect[1] = 5;
				}
				else if(move.charAt(i) == 'g') {
					stockFishPieceSelect[1] = 6;
				}
				else if(move.charAt(i) == 'h') {
					stockFishPieceSelect[1] = 7;
				}
			}
			else if(i == 1) {
				if(move.charAt(i) == '1') {
					stockFishPieceSelect[0] = 7;
				}
				else if(move.charAt(i) == '2') {
					stockFishPieceSelect[0] = 6;
				}
				else if(move.charAt(i) == '3') {
					stockFishPieceSelect[0] = 5;
				}
				else if(move.charAt(i) == '4') {
					stockFishPieceSelect[0] = 4;
				}
				else if(move.charAt(i) == '5') {
					stockFishPieceSelect[0] = 3;
				}
				else if(move.charAt(i) == '6') {
					stockFishPieceSelect[0] = 2;
				}
				else if(move.charAt(i) == '7') {
					stockFishPieceSelect[0] = 1;
				}
				else if(move.charAt(i) == '8') {
					stockFishPieceSelect[0] = 0;
				}
			}
			else if(i == 2) {
				if(move.charAt(i) == 'a') {
					stockFishDestinationSelect[1] = 0;
				}
				else if(move.charAt(i) == 'b') {
					stockFishDestinationSelect[1] = 1;
				}
				else if(move.charAt(i) == 'c') {
					stockFishDestinationSelect[1] = 2;
				}
				else if(move.charAt(i) == 'd') {
					stockFishDestinationSelect[1] = 3;
				}
				else if(move.charAt(i) == 'e') {
					stockFishDestinationSelect[1] = 4;
				}
				else if(move.charAt(i) == 'f') {
					stockFishDestinationSelect[1] = 5;
				}
				else if(move.charAt(i) == 'g') {
					stockFishDestinationSelect[1] = 6;
				}
				else if(move.charAt(i) == 'h') {
					stockFishDestinationSelect[1] = 7;
				}
			}
			else if(i == 3) {
				if(move.charAt(i) == '1') {
					stockFishDestinationSelect[0] = 7;
				}
				else if(move.charAt(i) == '2') {
					stockFishDestinationSelect[0] = 6;
				}
				else if(move.charAt(i) == '3') {
					stockFishDestinationSelect[0] = 5;
				}
				else if(move.charAt(i) == '4') {
					stockFishDestinationSelect[0] = 4;
				}
				else if(move.charAt(i) == '5') {
					stockFishDestinationSelect[0] = 3;
				}
				else if(move.charAt(i) == '6') {
					stockFishDestinationSelect[0] = 2;
				}
				else if(move.charAt(i) == '7') {
					stockFishDestinationSelect[0] = 1;
				}
				else if(move.charAt(i) == '8') {
					stockFishDestinationSelect[0] = 0;
				}
			}
		}
		
	}
	
	public void getPieceImages() throws IOException { //obtains piece images by parsing multiple images from one image
    	BufferedImage bf = ImageIO.read(new File("C:\\Users\\Stump\\Desktop\\chess.png"));
    	int i = 0;
    	for(int row = 0; row < 400; row+= 200) {
    		for(int col = 0; col < 1200; col+= 200) {
    			pieceImages[i] = bf.getSubimage(col, row, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
    			i++;
    		}
    	}
    }
   
	public void stockFishMove() { //obtains and returns the move calculated by stockfish
		String output = sf.output;
		String concOutput = output.replace("bestmove ", "");
		System.out.println(concOutput);
		moveToArray(concOutput);
		int sfSelectRow = stockFishPieceSelect[0];
		int sfSelectCol = stockFishPieceSelect[1];
		int sfDestRow = stockFishDestinationSelect[0];
		int sfDestCol = stockFishDestinationSelect[1];
		Pieces sfSelect = getPiece(sfSelectRow, sfSelectCol);
		Pieces sfDest = getPiece(sfDestRow, sfDestCol);
		Pieces queenSideRook = getPiece(0,0);
		Pieces kingSideRook = getPiece(0,7);
		
		if(chessBoard[sfSelectRow][sfSelectCol].type == PieceType.PAWN && chessBoard[sfSelectRow][sfSelectCol].hasMoved == false) {
			if(sfSelectCol - 2 == sfDestCol) {
				enPassantCheck[0] = sfDestRow + 1;
				enPassantCheck[1] = sfDestCol;
			}
		}
			
		
		if(chessBoard[sfSelectRow][sfSelectCol].type == PieceType.KING) {
			if(sfSelectCol - 2 == sfDestCol) {
				chessBoard[sfSelectRow][sfSelectCol] = nul;
				chessBoard[sfDestRow][sfDestCol] = sfSelect;
				chessBoard[0][3] = queenSideRook;
				chessBoard[0][0] = nul;
			}
			if(sfSelectCol + 2 == sfDestCol ) {
				chessBoard[sfSelectRow][sfSelectCol] = nul;
				chessBoard[sfDestRow][sfDestCol] = sfSelect;
				chessBoard[0][5] = kingSideRook;
				chessBoard[0][7] = nul;
			}
			
		}
		
		if(chessBoard[sfDestRow][sfDestCol].type == PieceType.NONE) { //no piece in dest square
			chessBoard[sfSelectRow][sfSelectCol] = nul;
			chessBoard[sfDestRow][sfDestCol] = sfSelect;
		}
		
		else { //piece in dest square
			chessBoard[sfSelectRow][sfSelectCol] = nul;
			chessBoard[sfDestRow][sfDestCol] = sfSelect;
			int index = pieceBox.indexOf(sfDest);
			pieceBox.remove(index);
		}
	}

	public void getFEN() { //returns FEN for current position in game
    	FEN = ("");
    	String rowFEN = ("");
    	int consecutiveBlanks = 0;
    	
    	 for(int row = 0; row < 8; row++) {
    		for(int col = 0; col < 8; col++) {
    			if(chessBoard[row][col].type == PieceType.NONE) {
    				consecutiveBlanks ++;
    			}
    			else if(consecutiveBlanks > 0 && col < 8) {
    				rowFEN += consecutiveBlanks;
    				consecutiveBlanks = 0;
    			}
    			if(chessBoard[row][col].type == PieceType.PAWN) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "P";
    				}
    				else if(chessBoard[row][col].color == PieceColor.BLACK) {
    					rowFEN += "p";	
    				}
    			}
    			
    			if(chessBoard[row][col].type == PieceType.KNIGHT) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "N";
    				}
    				else {
    					rowFEN += "n";	
    				}
    			}
    			
    			if(chessBoard[row][col].type == PieceType.BISHOP) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "B";
    				}
    				else {
    					rowFEN += "b";	
    				}
    			}
    			
    			if(chessBoard[row][col].type == PieceType.ROOK) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "R";
    				}
    				else {
    					rowFEN += "r";	
    				}
    			}
    			
    			if(chessBoard[row][col].type == PieceType.QUEEN) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "Q";
    				}
    				else {
    					rowFEN += "q";	
    				}
    			}
    			
    			if(chessBoard[row][col].type == PieceType.KING) {
    				if(chessBoard[row][col].color == PieceColor.WHITE) {
    					rowFEN += "K";
    				}
    				else {
    					rowFEN += "k";	
    				}
    			}
    			
    			if(col == 7) {
    				if(consecutiveBlanks != 0) {
    				rowFEN += consecutiveBlanks;
    				}
    				
    				consecutiveBlanks = 0;
    				FEN += rowFEN;
    				rowFEN = ("");
    				if(row != 7) {
    					FEN += "/";
    				}
    			}
    		}
    	}
    	FEN += " b ";
    	if((bK.hasMoved == true || (bR.hasMoved == true && bR2.hasMoved == true)) && (wK.hasMoved == true || (wR.hasMoved == true && wR2.hasMoved == true))) { //if neither side can castle
    		FEN += "- - ";
    	}
    	if(wK.hasMoved == false && wR2.hasMoved == false) { //if white can king side castle
    		FEN += "K";
    	}
    	if(wK.hasMoved == false && wR.hasMoved == false) { //if white can queen side castle
    		FEN += "Q";
    	}
    	if(bK.hasMoved == false && bR2.hasMoved == false) { //if white can king side castle
    		FEN += "k";
    	}
    	
    	if(bK.hasMoved == false && bR.hasMoved == false) { //if white can queen side castle
    		FEN += "q";
    	}
    	
    	FEN += (" 0 " + moveNumber);
    	FEN += " move e2e4";
    }
	
	public void opposingPieceSight() { //algorithm to display all squares that are being "looked at" by the opposing color
    	boardFlip();
    	for(int row = 0; row < 8; row++) { //must reset every time to avoid keeping previous sights
    		for(int col = 0; col < 8; col++) {
    		blackSightBoard[row][col] = false;	
    		}
    	}
    	
    	for(int row = 0; row < 8; row++) {
    		for(int col = 0; col < 8; col++) {
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK) {
    				blackSightBoard[row][col] = false;
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.ROOK ) { //checks for the squares each white rook is looking at for four possible directions
    				for(int rookDown = row + 1; rookDown < 8; rookDown++) {
    					if(chessBoard[rookDown][col].type == PieceType.NONE) {
    						blackSightBoard[rookDown][col] = true;
    					}
    					else {
    						blackSightBoard[rookDown][col] = true;
    						break;				
    					}
    				}
    				
    				for(int rookRight = col + 1; rookRight < 8; rookRight++) {
    					if(chessBoard[row][rookRight].type == PieceType.NONE) {
    						blackSightBoard[row][rookRight] = true;
    					}
    					else {	
    						
    						blackSightBoard[row][rookRight] = true;
    						break;
    					}
    				}	
    				
    				for(int rookLeft = col - 1; rookLeft >= 0; rookLeft--) {
    					if(chessBoard[row][rookLeft].type == PieceType.NONE) {
    						blackSightBoard[row][rookLeft] = true;
    					}
    					else {
    						blackSightBoard[row][rookLeft] = true;
    						break;				
    					}
    				}
    				
    				for(int rookUp = row - 1; rookUp >= 0; rookUp--) {
    					if(chessBoard[rookUp][col].type == PieceType.NONE) {
    						blackSightBoard[rookUp][col] = true;
    					}
    					else {
    						blackSightBoard[rookUp][col] = true;
    						break;				
    					}
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.PAWN ) { //if the current piece in the loop is a white pawn
    				if(row == 0) { //backlogged for when working on queening a pawn
    					continue;
    				}
    				if(col == 0) {
    					blackSightBoard[row - 1][col + 1] = true;
    					
    				}
    				else if(col == 7) {
    					blackSightBoard[row - 1][col - 1] = true;
    					
    				}
    				
    				else {
    					blackSightBoard[row - 1][col - 1] = true;
    					blackSightBoard[row - 1][col + 1] = true;	
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.KNIGHT) {
    				if(col - 1 >= 0 && row - 2 >= 0) {
    					blackSightBoard[row -2][col - 1] = true;
    				}
    				if(col + 1 < 8 && row - 2 >= 0) {
    					blackSightBoard[row -2][col + 1] = true;
    				}
    				if(col + 2 < 8 && row - 1 >= 0) {
    					blackSightBoard[row - 1][col + 2] = true;
    				}
    				if(col - 2 >= 0 && row - 1 >= 0) {
    					blackSightBoard[row - 1][col - 2] = true;
    				}
    				if(col - 2 >= 0 && row + 1 < 8 ) {
    					blackSightBoard[row + 1][col - 2] = true;
    				}
    				if(col - 1 >= 0 && row + 2 < 8) {
    					blackSightBoard[row + 2][col - 1] = true;
    				}   				
    				if(col + 1 < 8 && row + 2 < 8) {
    					blackSightBoard[row + 2][col + 1] = true;
    				}
    				if(col + 2 < 8 && row + 1 < 8 ) {
    					blackSightBoard[row + 1][col + 2] = true;
    				}	
    			}
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.BISHOP) {
    				int bishopRowUp = row - 1;
    				int bishopColLeft = col - 1;
    				int bishopRowDown = row + 1;
    				int bishopColRight = col + 1;

    	
    				while(bishopRowUp >= 0 && bishopColLeft >= 0) { //checking upper left diagonal
    					if(chessBoard[bishopRowUp][bishopColLeft].type == PieceType.NONE) {
    						blackSightBoard[bishopRowUp][bishopColLeft] = true;
    							bishopRowUp--;
    							bishopColLeft--;
    							//System.out.println("Upper Left Checked");
    					}
    					else {
    						blackSightBoard[bishopRowUp][bishopColLeft] = true;
    						break;
    					}
    				}
    				bishopColLeft = col -1;
    				bishopRowUp = row - 1;
    				
    				while(bishopRowUp >= 0 && bishopColRight < 8) { //checking upper right diagonal
    					if(chessBoard[bishopRowUp][bishopColRight].type == PieceType.NONE) {
    						blackSightBoard[bishopRowUp][bishopColRight] = true;
    						bishopRowUp--;
    						bishopColRight++;
    					}
    					else {
    						blackSightBoard[bishopRowUp][bishopColRight] = true;
    						break;
    					}
    				}
    				bishopRowUp = row - 1;
    				bishopColRight = col + 1;
    				
    				while(bishopRowDown < 8 && bishopColLeft >= 0) { //checking lower left diagonal
    					if(chessBoard[bishopRowDown][bishopColLeft].type == PieceType.NONE) {
    						blackSightBoard[bishopRowDown][bishopColLeft] = true;
    							bishopRowDown++;
    							bishopColLeft--;
    							//System.out.println("Lower Left Checked");
    					}
    					else {
    						blackSightBoard[bishopRowDown][bishopColLeft] = true;
    						break;
    					}
    				}
    				bishopRowDown = row + 1;
    				bishopColLeft = col - 1;
    				
    				while(bishopRowDown < 8 && bishopColRight < 8) { //checking lower right
    					if(chessBoard[bishopRowDown][bishopColRight].type == PieceType.NONE) {
    						//System.out.println("Lower Right Checked");
    						blackSightBoard[bishopRowDown][bishopColRight] = true;
    						bishopRowDown++;
    						bishopColRight++;
    					}
    					else {
    						blackSightBoard[bishopRowDown][bishopColRight] = true;
    						break;
    					}
    				}
    				bishopRowDown = row + 1;
    				bishopColRight = col + 1;
    				
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.QUEEN) { //super easy, reusing bishop and rook code
    				int queenRowUp = row - 1;
    				int queenColLeft = col - 1;
    				int queenRowDown = row + 1;
    				int queenColRight = col + 1;
    				
    				while(queenRowUp >= 0 && queenColLeft >= 0) { //checking upper left diagonal
    					if(chessBoard[queenRowUp][queenColLeft].type == PieceType.NONE) {
    						blackSightBoard[queenRowUp][queenColLeft] = true;
    							queenRowUp--;
    							queenColLeft--;
    					}
    					else {
    						blackSightBoard[queenRowUp][queenColLeft] = true;
    						break;
    					}
    				}
    				queenRowUp = row - 1;
    				queenColLeft = col - 1;
 
    				while(queenRowUp >= 0 && queenColRight < 8) { //checking upper right diagonal
    					if(chessBoard[queenRowUp][queenColRight].type == PieceType.NONE) {
    						blackSightBoard[queenRowUp][queenColRight] = true;
    						queenRowUp--;
    						queenColRight++;
    					}
    					else {
    						blackSightBoard[queenRowUp][queenColRight] = true;
    						break;
    					}
    				}
    				queenRowUp = row - 1;
    				queenColRight = col + 1;
    				
    				
    				while(queenRowDown < 8 && queenColLeft >= 0) { //checking lower left diagonal
    					if(chessBoard[queenRowDown][queenColLeft].type == PieceType.NONE) {
    						blackSightBoard[queenRowDown][queenColLeft] = true;
    							queenRowDown++;
    							queenColLeft--;
    					}
    					else {
    						blackSightBoard[queenRowDown][queenColLeft] = true;
    						break;
    					}
    				}
    				queenRowDown = row + 1;
    				queenColLeft = col - 1;
    				
    				while(queenRowDown < 8 && queenColRight < 8) { //checking lower right
    					if(chessBoard[queenRowDown][queenColRight].type == PieceType.NONE) {
    						blackSightBoard[queenRowDown][queenColRight] = true;
    						queenRowDown++;
    						queenColRight++;
    					}
    					else {
    						blackSightBoard[queenRowDown][queenColRight] = true;
    						break;
    					}
    				}
    				queenRowDown = row + 1;
    				queenColRight = col + 1;
    				
    				for(int queenDown = row + 1; queenDown < 8; queenDown++) {
    					if(chessBoard[queenDown][col].type == PieceType.NONE) {
    						blackSightBoard[queenDown][col] = true;
    					}
    					else {
    						blackSightBoard[queenDown][col] = true;
    						break;				
    					}
    				}
    				
    				for(int queenRight = col + 1; queenRight < 8; queenRight++) {
    					if(chessBoard[row][queenRight].type == PieceType.NONE) {
    						blackSightBoard[row][queenRight] = true;
    					}
    					else {
    						blackSightBoard[row][queenRight] = true;
    						break;				
    					}
    				}	
    				
    				for(int queenLeft = col - 1; queenLeft >= 0; queenLeft--) {
    					if(chessBoard[row][queenLeft].type == PieceType.NONE) {
    						blackSightBoard[row][queenLeft] = true;
    					}
    					else {
    						blackSightBoard[row][queenLeft] = true;
    						break;				
    					}	
    				}
    				
    				for(int queenUp = row - 1; queenUp >= 0; queenUp--) {
    					if(chessBoard[queenUp][col].type == PieceType.NONE) {
    						blackSightBoard[queenUp][col] = true;
    					}
    					else {
    						blackSightBoard[queenUp][col] = true;
    						break;				
    					}
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.BLACK && chessBoard[row][col].type == PieceType.KING) {
    				int kingUp = row - 1;
    				int kingRight = col + 1;
    				int kingDown = row + 1;
    				int kingLeft = col - 1;
    				
    				if(kingUp >= 0) { //checking space above king, if possible
    					blackSightBoard[kingUp][col] = true;
    				}
    					
    				if(kingRight < 8) { //checking space to right of king, if possible
    					blackSightBoard[row][kingRight] = true;
    				}
    					
    				if(kingLeft >= 0) { //checking space to left of king, if possible
    					blackSightBoard[row][kingLeft] = true;
    				}
    				
    				if(kingDown < 8) { //checking space below king, if possible
    					blackSightBoard[kingDown][col] = true;
    				}
    				
    				if(kingUp >=0 && kingRight < 8) { //checking upper right square, if possible
    					blackSightBoard[kingUp][kingRight] = true;		
    				}
    					
    				if(kingUp >=0 && kingLeft >= 0) { //checking upper left square, if possible
    					blackSightBoard[kingUp][kingLeft] = true;
    				}	
    				
    				if(kingDown < 8 && kingRight < 8) { //checking lower right square, if possible
    					blackSightBoard[kingDown][kingRight] = true;	
    				}
    				
    				if(kingDown < 8 && kingLeft >= 0) { //checking lower right square, if possible
    					blackSightBoard[kingDown][kingLeft] = true;
    				}
    			}
    		}
    	}
    	boardFlip();
    	opposingSightBoardFlip();
    	blackSightState();
    	
    }
    
	public void pieceSight() { //algorithm to display all squares that are being "looked at" by the players color"
    	
    	for(int row = 0; row < 8; row++) { //must reset every time to avoid keeping previous sights
    		for(int col = 0; col < 8; col++) {
    		sightBoard[row][col] = false;	
    		}
    	}
    	
    	for(int row = 0; row < 8; row++) {
    		for(int col = 0; col < 8; col++) {
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE) {
    				sightBoard[row][col] = true;
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.ROOK ) { //checks for the squares each white rook is looking at for four possible directions
    				for(int rookDown = row + 1; rookDown < 8; rookDown++) {
    					if(chessBoard[rookDown][col].type == PieceType.NONE) {
    						sightBoard[rookDown][col] = true;
    					}
    					else {
    						sightBoard[rookDown][col] = true;
    						break;				
    					}
    				}
    				
    				for(int rookRight = col + 1; rookRight < 8; rookRight++) {
    					if(chessBoard[row][rookRight].type == PieceType.NONE) {
    						sightBoard[row][rookRight] = true;
    					}
    					else {	
    						
    						sightBoard[row][rookRight] = true;
    						break;
    					}
    				}	
    				
    				for(int rookLeft = col - 1; rookLeft >= 0; rookLeft--) {
    					if(chessBoard[row][rookLeft].type == PieceType.NONE) {
    						sightBoard[row][rookLeft] = true;
    					}
    					else {
    						sightBoard[row][rookLeft] = true;
    						break;				
    					}
    				}
    				
    				for(int rookUp = row - 1; rookUp >= 0; rookUp--) {
    					if(chessBoard[rookUp][col].type == PieceType.NONE) {
    						sightBoard[rookUp][col] = true;
    					}
    					else {
    						sightBoard[rookUp][col] = true;
    						break;				
    					}
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.PAWN ) { //if the current piece in the loop is a white pawn
    				if(row == 0) { //backlogged for when working on queening a pawn
    					continue;
    				}
    				if(col == 0) {
    					sightBoard[row - 1][col + 1] = true;
    					
    				}
    				else if(col == 7) {
    					sightBoard[row - 1][col - 1] = true;
    					
    				}
    				
    				else {
    					sightBoard[row - 1][col - 1] = true;
    					sightBoard[row - 1][col + 1] = true;	
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.KNIGHT) {
    				if(col - 1 >= 0 && row - 2 >= 0) {
    					sightBoard[row -2][col - 1] = true;
    				}
    				if(col + 1 < 8 && row - 2 >= 0) {
    					sightBoard[row -2][col + 1] = true;
    				}
    				if(col + 2 < 8 && row - 1 >= 0) {
    					sightBoard[row - 1][col + 2] = true;
    				}
    				if(col - 2 >= 0 && row - 1 >= 0) {
    					sightBoard[row - 1][col - 2] = true;
    				}
    				if(col - 2 >= 0 && row + 1 < 8 ) {
    					sightBoard[row + 1][col - 2] = true;
    				}
    				if(col - 1 >= 0 && row + 2 < 8) {
    					sightBoard[row + 2][col - 1] = true;
    				}   				
    				if(col + 1 < 8 && row + 2 < 8) {
    					sightBoard[row + 2][col + 1] = true;
    				}
    				if(col + 2 < 8 && row + 1 < 8 ) {
    					sightBoard[row + 1][col + 2] = true;
    				}	
    			}
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.BISHOP) {
    				int bishopRowUp = row - 1;
    				int bishopColLeft = col - 1;
    				int bishopRowDown = row + 1;
    				int bishopColRight = col + 1;

    	
    				while(bishopRowUp >= 0 && bishopColLeft >= 0) { //checking upper left diagonal
    					if(chessBoard[bishopRowUp][bishopColLeft].type == PieceType.NONE) {
    							sightBoard[bishopRowUp][bishopColLeft] = true;
    							bishopRowUp--;
    							bishopColLeft--;
    							//System.out.println("Upper Left Checked");
    					}
    					else {
    						sightBoard[bishopRowUp][bishopColLeft] = true;
    						break;
    					}
    				}
    				bishopColLeft = col -1;
    				bishopRowUp = row - 1;
    				
    				while(bishopRowUp >= 0 && bishopColRight < 8) { //checking upper right diagonal
    					if(chessBoard[bishopRowUp][bishopColRight].type == PieceType.NONE) {
    						sightBoard[bishopRowUp][bishopColRight] = true;
    						bishopRowUp--;
    						bishopColRight++;
    					}
    					else {
    						sightBoard[bishopRowUp][bishopColRight] = true;
    						break;
    					}
    				}
    				bishopRowUp = row - 1;
    				bishopColRight = col + 1;
    				
    				while(bishopRowDown < 8 && bishopColLeft >= 0) { //checking lower left diagonal
    					if(chessBoard[bishopRowDown][bishopColLeft].type == PieceType.NONE) {
    							sightBoard[bishopRowDown][bishopColLeft] = true;
    							bishopRowDown++;
    							bishopColLeft--;
    							//System.out.println("Lower Left Checked");
    					}
    					else {
    						sightBoard[bishopRowDown][bishopColLeft] = true;
    						break;
    					}
    				}
    				bishopRowDown = row + 1;
    				bishopColLeft = col - 1;
    				
    				while(bishopRowDown < 8 && bishopColRight < 8) { //checking lower right
    					if(chessBoard[bishopRowDown][bishopColRight].type == PieceType.NONE) {
    						//System.out.println("Lower Right Checked");
    						sightBoard[bishopRowDown][bishopColRight] = true;
    						bishopRowDown++;
    						bishopColRight++;
    					}
    					else {
    						sightBoard[bishopRowDown][bishopColRight] = true;
    						break;
    					}
    				}
    				bishopRowDown = row + 1;
    				bishopColRight = col + 1;
    				
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.QUEEN) { //super easy, reusing bishop and rook code
    				int queenRowUp = row - 1;
    				int queenColLeft = col - 1;
    				int queenRowDown = row + 1;
    				int queenColRight = col + 1;
    				
    				while(queenRowUp >= 0 && queenColLeft >= 0) { //checking upper left diagonal
    					if(chessBoard[queenRowUp][queenColLeft].type == PieceType.NONE) {
    							sightBoard[queenRowUp][queenColLeft] = true;
    							queenRowUp--;
    							queenColLeft--;
    					}
    					else {
    						sightBoard[queenRowUp][queenColLeft] = true;
    						break;
    					}
    				}
    				queenRowUp = row - 1;
    				queenColLeft = col - 1;
 
    				while(queenRowUp >= 0 && queenColRight < 8) { //checking upper right diagonal
    					if(chessBoard[queenRowUp][queenColRight].type == PieceType.NONE) {
    						sightBoard[queenRowUp][queenColRight] = true;
    						queenRowUp--;
    						queenColRight++;
    					}
    					else {
    						sightBoard[queenRowUp][queenColRight] = true;
    						break;
    					}
    				}
    				queenRowUp = row - 1;
    				queenColRight = col + 1;
    				
    				
    				while(queenRowDown < 8 && queenColLeft >= 0) { //checking lower left diagonal
    					if(chessBoard[queenRowDown][queenColLeft].type == PieceType.NONE) {
    							sightBoard[queenRowDown][queenColLeft] = true;
    							queenRowDown++;
    							queenColLeft--;
    					}
    					else {
    						sightBoard[queenRowDown][queenColLeft] = true;
    						break;
    					}
    				}
    				queenRowDown = row + 1;
    				queenColLeft = col - 1;
    				
    				while(queenRowDown < 8 && queenColRight < 8) { //checking lower right
    					if(chessBoard[queenRowDown][queenColRight].type == PieceType.NONE) {
    						sightBoard[queenRowDown][queenColRight] = true;
    						queenRowDown++;
    						queenColRight++;
    					}
    					else {
    						sightBoard[queenRowDown][queenColRight] = true;
    						break;
    					}
    				}
    				queenRowDown = row + 1;
    				queenColRight = col + 1;
    				
    				for(int queenDown = row + 1; queenDown < 8; queenDown++) {
    					if(chessBoard[queenDown][col].type == PieceType.NONE) {
    						sightBoard[queenDown][col] = true;
    					}
    					else {
    						sightBoard[queenDown][col] = true;
    						break;				
    					}
    				}
    				
    				for(int queenRight = col + 1; queenRight < 8; queenRight++) {
    					if(chessBoard[row][queenRight].type == PieceType.NONE) {
    						sightBoard[row][queenRight] = true;
    					}
    					else {
    						sightBoard[row][queenRight] = true;
    						break;				
    					}
    				}	
    				
    				for(int queenLeft = col - 1; queenLeft >= 0; queenLeft--) {
    					if(chessBoard[row][queenLeft].type == PieceType.NONE) {
    						sightBoard[row][queenLeft] = true;
    					}
    					else {
    						sightBoard[row][queenLeft] = true;
    						break;				
    					}	
    				}
    				
    				for(int queenUp = row - 1; queenUp >= 0; queenUp--) {
    					if(chessBoard[queenUp][col].type == PieceType.NONE) {
    						sightBoard[queenUp][col] = true;
    					}
    					else {
    						sightBoard[queenUp][col] = true;
    						break;				
    					}
    				}
    			}
    			
    			if(chessBoard[row][col].color == PieceColor.WHITE && chessBoard[row][col].type == PieceType.KING) {
    				int kingUp = row - 1;
    				int kingRight = col + 1;
    				int kingDown = row + 1;
    				int kingLeft = col - 1;
    					
    				
    				if(kingUp >= 0) { //checking space above king, if possible
    					sightBoard[kingUp][col] = true;
    				}
    					
    				if(kingRight < 8) { //checking space to right of king, if possible
    					sightBoard[row][kingRight] = true;
    				}
    					
    				if(kingLeft >= 0) { //checking space to left of king, if possible
    					sightBoard[row][kingLeft] = true;
    				}
    				
    				if(kingDown < 8) { //checking space below king, if possible
    					sightBoard[kingDown][col] = true;
    				}
    				
    				if(kingUp >=0 && kingRight < 8) { //checking upper right square, if possible
    					sightBoard[kingUp][kingRight] = true;		
    				}
    					
    				if(kingUp >=0 && kingLeft >= 0) { //checking upper left square, if possible
    					sightBoard[kingUp][kingLeft] = true;
    				}	
    				
    				if(kingDown < 8 && kingRight < 8) { //checking lower right square, if possible
    					sightBoard[kingDown][kingRight] = true;	
    				}
    				
    				if(kingDown < 8 && kingLeft >= 0) { //checking lower right square, if possible
    					sightBoard[kingDown][kingLeft] = true;
    				}
    			}
    		}
    	}
    	sightState();
    	
    }
	
    public int[] getPosition(Pieces p) { //obtains the current position of the Pieces object being queried in the form of a two index array
    	for(int row = 0; row < 8; row++) {
			for(int col = 0; col < 8; col++) {
				if(chessBoard[row][col] == p) {
					position[0] = row;
					position[1] = col;
				}
			}
    	}
    	return position;
    }
    
    public Pieces getPiece (int rows, int cols) { //returns the Pieces object at a particular position on the board
    	for(int row = 0; row < 8; row++) {
			for(int col = 0; col < 8; col++) {
				if(row == rows && col == cols ) {
					return chessBoard[row][col];
				}
			}
		}
    	return null;
    }
    
    public void opposingSightBoardFlip() { //flips the boolean board for opposingPieceSight() so it is correctly displayed
		for(int i = 0; i < (blackSightBoard.length / 2); i++) {
	        boolean[] temp = blackSightBoard[i];
	        blackSightBoard[i] = blackSightBoard[blackSightBoard.length - i - 1];
	        blackSightBoard[blackSightBoard.length - i - 1] = temp;
		}
	}
  
    public void boardFlip() { //flips the main game board along the X-axis
		for(int i = 0; i < (chessBoard.length / 2); i++) {
	        Pieces[] temp = chessBoard[i];
	        chessBoard[i] = chessBoard[chessBoard.length - i - 1];
	        chessBoard[chessBoard.length - i - 1] = temp;
		}
	}
    
    public void opponentTurn() { //initiates functions for stockfish's turn
    	frame.repaint();
    	arrayToMove();
    	getFEN();
    	sf.position(FEN);
    	sf.go();
    	stockFishMove();
    	moveNumber++;
    }
    
    public void knightMove() { //checks players move for correct/allowed move logic for the knight
    	int row = selectedPieceLocation[0];
		int col = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		
		if(destinationPiece.color != selectedPiece.color) { // if the knight you're moving to does not have a white piece
			if(destinationPiece.type == PieceType.NONE && destinationPiece.type != PieceType.KING) {
				if(rowDest == row - 2 && (colDest == col + 1 || colDest == col -1)) {
					chessBoard[row][col] = destinationPiece; //places the empty destination square where the knight is
					chessBoard[rowDest][colDest] = selectedPiece;//places the pawn where the destination square is 
					boardState();
					opponentTurn();
				}
				else if(rowDest == row + 2 && (colDest == col + 1 || colDest == col -1)) {
					chessBoard[row][col] = destinationPiece; 
					chessBoard[rowDest][colDest] = selectedPiece; 
					boardState();
					opponentTurn();
				}
				else if(colDest == col + 2 && (rowDest == row + 1 || rowDest == row - 1)) {
					chessBoard[row][col] = destinationPiece; 
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					opponentTurn();
				}
				else if(colDest == col - 2 && (rowDest == row + 1 || rowDest == row - 1)) {
					chessBoard[row][col] = destinationPiece; 
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					opponentTurn();
				}
			}
			
			if(destinationPiece.type != PieceType.NONE && destinationPiece.type != PieceType.KING ) {
				if(rowDest == row - 2 && (colDest == col + 1 || colDest == col -1)) {
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					int index = pieceBox.indexOf(destinationPiece);
					pieceBox.remove(index);
					opponentTurn();
				}
				else if(rowDest == row + 2 && (colDest == col + 1 || colDest == col -1)) {
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					int index = pieceBox.indexOf(destinationPiece);
					pieceBox.remove(index);
					opponentTurn();
				}
				else if(colDest == col + 2 && (rowDest == row + 1 || rowDest == row - 1)) {
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					int index = pieceBox.indexOf(destinationPiece);
					pieceBox.remove(index);
					opponentTurn();
				}
				else if(colDest == col - 2 && (rowDest == row + 1 || rowDest == row - 1)) {
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					boardState();
					int index = pieceBox.indexOf(destinationPiece);
					pieceBox.remove(index);
					opponentTurn();
				}
			}
			//checking for checks to the opposing king after succesful move is made by using the location of the opposing king and the possible moves of the piece destination coords
			int[] enemyKing;
			
			if(whiteTurn == true) {
				enemyKing = getPosition(bK);
				System.out.println("wT");
			}
			else {
				enemyKing = getPosition(wK);
				System.out.println("Bt");
			}
			
			int kingRow = enemyKing[0];
			int kingCol = enemyKing[1];
			
			if(kingRow + 2 == rowDest && (kingCol + 1 == colDest || kingCol - 1 == colDest)) {
				bK.inCheck = true;
				System.out.println("incheck");
			}
			if(kingRow - 2 == rowDest && (kingCol + 1 == colDest || kingCol - 1 == colDest)) {
				bK.inCheck = true;
				System.out.println("incheck");
			}
			if(kingCol + 2 == colDest && (kingRow + 1 == rowDest || kingRow - 1 == rowDest)) {
				bK.inCheck = true;
				System.out.println("incheck");
			}
			if(kingCol - 2 == colDest && (kingRow + 1 == rowDest || kingRow - 1 == rowDest)) {
				bK.inCheck = true;
				System.out.println("incheck");
			}
		}
		pieceSight();
	}
	
	public void bishopMove() { //checks players move for correct/allowed move logic for the bishop
		int row = selectedPieceLocation[0];
		int col = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		
		int bishopColRight = col + 1; 
		int bishopColLeft = col - 1;
		int bishopRowDown = row + 1;
		int bishopRowUp = row - 1;
		
		if(rowDest < row && colDest < col ) { //if the move is upper left
			if(destinationPiece.type != PieceType.KING) {
				while(bishopRowUp >= 0 && bishopColLeft >= 0) { 
					if(bishopRowUp == rowDest && bishopColLeft == colDest) {
						if(chessBoard[bishopRowUp][bishopColLeft].color == PieceColor.BLACK) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowUp][bishopColLeft] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[bishopRowUp][bishopColLeft].color == PieceColor.NONE) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowUp][bishopColLeft] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[bishopRowUp][bishopColLeft].color == PieceColor.WHITE || chessBoard[bishopRowUp][bishopColLeft].color == PieceColor.BLACK) {
						break;
					}
					bishopRowUp--;
					bishopColLeft--;
				}
			}
		}
			
		if(rowDest < row && colDest > col) { //if the move is upper right
			if(destinationPiece.type != PieceType.KING) {
				while(bishopRowUp >= 0 && bishopColRight < 8) { 
					if(bishopRowUp == rowDest && bishopColRight == colDest) {
						if(chessBoard[bishopRowUp][bishopColRight].color == PieceColor.BLACK) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowUp][bishopColRight] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[bishopRowUp][bishopColRight].color == PieceColor.NONE) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowUp][bishopColRight] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[bishopRowUp][bishopColRight].color == PieceColor.WHITE || chessBoard[bishopRowUp][bishopColRight].color == PieceColor.BLACK) {
						break;
					}
					bishopRowUp--;
					bishopColRight++;
				}
			}
		}
			
		if(rowDest > row && colDest > col) { //if the move is lower right
			if(destinationPiece.type != PieceType.KING) {
				while(bishopRowDown < 8 && bishopColRight < 8) { 
					if(bishopRowDown == rowDest && bishopColRight == colDest) {
						if(chessBoard[bishopRowDown][bishopColRight].color == PieceColor.BLACK) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowDown][bishopColRight] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[bishopRowDown][bishopColRight].color == PieceColor.NONE) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowDown][bishopColRight] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[bishopRowDown][bishopColRight].color == PieceColor.WHITE || chessBoard[bishopRowDown][bishopColRight].color == PieceColor.BLACK) {
						break;
					}
					bishopRowDown++;
					bishopColRight++;
				}
			}
		}
		
		if(rowDest > row && colDest < col) { //if the move is lower left
			if(destinationPiece.type != PieceType.KING) {
				while(bishopRowDown < 8 && bishopColLeft >= 0) { 
					if(bishopRowDown == rowDest && bishopColLeft == colDest) {
						if(chessBoard[bishopRowDown][bishopColLeft].color == PieceColor.BLACK) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowDown][bishopColLeft] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[bishopRowDown][bishopColLeft].color == PieceColor.NONE) {
							chessBoard[row][col] = nul;
							chessBoard[bishopRowDown][bishopColLeft] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[bishopRowDown][bishopColLeft].color == PieceColor.WHITE || chessBoard[bishopRowDown][bishopColLeft].color == PieceColor.BLACK) {
						break;
					}
					bishopRowDown++;
					bishopColLeft--;
				}
			}
		}
		boardState();
		pieceSight();
	}
	
	public void rookMove() { //checks players move for correct/allowed move logic for the rook
		int row = selectedPieceLocation[0];
		int col = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		
		if(destinationPiece.type != PieceType.KING) {
			if(rowDest > row && colDest == col) { // down movement 
				for(int rookDown = row + 1; rookDown < 8; rookDown++) {
					if(rookDown == rowDest && col == colDest) {
						if(chessBoard[rookDown][col].color == PieceColor.BLACK) { //if taking at dest square
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;	
							chessBoard[rookDown][colDest] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[rookDown][col].color == PieceColor.WHITE) {
							break;
						}
						else { // if not taking at dest square
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rookDown][colDest] = selectedPiece;
							opponentTurn();
						}
					}
					else if(chessBoard[rookDown][col].color == PieceColor.WHITE || chessBoard[rookDown][col].color == PieceColor.BLACK) { 
						break;
					}
				}
			}
			if(rowDest < row && colDest == col) { // up movement 
				for(int rookUp = row - 1; rookUp >= 0; rookUp--) {
					if(rookUp == rowDest && col == colDest) {
						if(chessBoard[rookUp][col].color == PieceColor.BLACK) {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rookUp][colDest] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
							}
						else if(chessBoard[rookUp][col].color == PieceColor.WHITE) {
							break;
						}
						else {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rookUp][colDest] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[rookUp][col].color == PieceColor.WHITE || chessBoard[rookUp][col].color == PieceColor.BLACK) {
						break;
					}
				}
			}
			if(colDest > col && rowDest == row) {// right movement 
				for(int rookRight = col + 1; rookRight < 8; rookRight++) {
					if(row == rowDest && rookRight == colDest) {
						if(chessBoard[row][rookRight].color == PieceColor.BLACK) {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][rookRight] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
						else if(chessBoard[row][rookRight].color == PieceColor.WHITE) {
							break;
						}
						else {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][rookRight] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[row][rookRight].color == PieceColor.WHITE || chessBoard[row][rookRight].color == PieceColor.BLACK) {
						break;
					}
				}
			}
			
			if(colDest < col && rowDest == row) {// left movement 	
				for(int rookLeft = col - 1; rookLeft >= 0; rookLeft--) {
					if(row == rowDest && rookLeft == colDest) {
						if(chessBoard[row][rookLeft].color == PieceColor.BLACK) {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][rookLeft] = selectedPiece;
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
							}
						else if(chessBoard[row][rookLeft].color == PieceColor.WHITE) {
							break;
						}
						else {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][rookLeft] = selectedPiece;
							opponentTurn();
						}
						break;
					}
					else if(chessBoard[row][rookLeft].color == PieceColor.WHITE || chessBoard[row][rookLeft].color == PieceColor.BLACK) {
						break;
					}
				}
			}
		}
		boardState();
		pieceSight();
	}
	
	public void pawnMove() { //checks players move for correct/allowed move logic for the pawn
		int row = selectedPieceLocation[0];
		int col = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		
		if(destinationPiece.color != selectedPiece.color) { // if the pawn you're moving to does not have a white piece
			if(destinationPiece.type == PieceType.NONE) {
				if(col == 0) {
					if(rowDest == row - 1 && colDest == col + 1) {
						if(row == enPassantCheck[0] && col == enPassantCheck[1] - 1) {
							chessBoard[row][col] = nul;
							chessBoard[rowDest][colDest] = selectedPiece;
							//boardState();
							int index = pieceBox.indexOf(destinationPiece); //gets index of the taken piece from the piecebox
							pieceBox.remove(index); //removes taken piece from the piecebox
							opponentTurn();
						}
					}
				}
			}
			
			
			
			if(destinationPiece.type != PieceType.NONE && destinationPiece.type != PieceType.KING) { // if the square you're moving to is not empty and is not a king (AKA taking an opposite colored piece)
				if(col == 0) { //segregation based on whether or not the pawn is on the edge of the players board for out of bounds checking for checks to the king
					if(rowDest == row - 1 && colDest == col + 1) {
						selectedPiece.hasMoved = true;
						chessBoard[row][col] = nul;
						chessBoard[rowDest][colDest] = selectedPiece;
						//boardState();
						int index = pieceBox.indexOf(destinationPiece); //gets index of the taken piece from the piecebox
						pieceBox.remove(index); //removes taken piece from the piecebox
						opponentTurn();
						}
					}
					if(col == 7) { //segregation based on whether or not the pawn is on the edge of the players board for out of bounds checking for checks to the king
						if(rowDest == row - 1 && colDest == col - 1) {
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][colDest] = selectedPiece;
							//boardState();
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
					}
					if(col != 0 && col != 7) {
						if((rowDest == row - 1 && colDest == col + 1) || (rowDest == row - 1 && colDest == col - 1)) { // take the juicer and replace the space moved from with an empty piece, also ensure the pawn hasMoved = true for en passant checks
							selectedPiece.hasMoved = true;
							chessBoard[row][col] = nul;
							chessBoard[rowDest][colDest] = selectedPiece;
							//boardState();
							int index = pieceBox.indexOf(destinationPiece);
							pieceBox.remove(index);
							opponentTurn();
						}
					}
			}
			
			if(chessBoard[row - 1][col].type == PieceType.NONE) { // if there is no piece in front of the pawn
				if(rowDest == row - 1 && colDest == col) { //move that juicer if the user selects the square one row ahead of the pawn
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul; //places the empty destination square where the pawn is
					chessBoard[rowDest][colDest] = selectedPiece;//places the pawn where the destination square is 
					opponentTurn();
				}	
				if(rowDest == row - 2 && colDest == col && selectedPiece.hasMoved == false) {
					if(chessBoard[row - 1][col].type == PieceType.NONE) {
						selectedPiece.hasMoved = true;
						chessBoard[row][col] = nul;
						chessBoard[rowDest][colDest] = selectedPiece;
						opponentTurn();
					}
				}
			}
			
			if(rowDest != 0) {
				if(col == 0 || colDest == 0) {
					if(chessBoard[rowDest - 1][colDest + 1].name.equals(" bK " )) {
						chessBoard[rowDest][colDest].inCheck = true;
					}
				}
				
				if(col == 7|| colDest == 7) {
					if (chessBoard[rowDest - 1][colDest - 1].name.equals(" bK " )) {
						chessBoard[rowDest][colDest].inCheck = true;
						System.out.println("here");
					}
				}
				
				if ((col != 0 && colDest != 0) && (col != 7 && colDest !=7)) {
					if((chessBoard[rowDest - 1][colDest - 1].name.equals(" bK " )) || (chessBoard[rowDest - 1][colDest + 1].name.equals(" bK " ))) { //checks to see if the pawn move creates a state of check via the piece
						chessBoard[rowDest][colDest].inCheck = true;
						System.out.println("check");
					}
				}
			}
		}
		pieceSight();
		return;
	}
	
	public void kingMove() { //checks players move for correct/allowed move logic for the king
		int row = selectedPieceLocation[0];
		int col = selectedPieceLocation[1];
		int rowDest = destinationPieceLocation[0];
		int colDest = destinationPieceLocation[1];
		opposingPieceSight();
		pieceSight();
		
		if(blackSightBoard[rowDest][colDest] == false && destinationPiece.color != PieceColor.WHITE && destinationPiece.type != PieceType.KING) {
			if(row >= 0 && rowDest == row - 1 && colDest == col) { //king move up
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { 
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(row < 8 && rowDest == row + 1 && colDest == col) { //king move down
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(col < 8 && rowDest == row && colDest == col + 1) { //king move right
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index);
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(col >= 0 && rowDest == row && colDest == col - 1) { //king move left
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(row >= 0 && col < 8 && rowDest == row - 1 && colDest == col + 1) { //king move upper right
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(row >= 0 && col >= 0 && rowDest == row - 1 && colDest == col - 1) { //king move upper left
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(row < 8 && col < 8 && rowDest == row + 1 && colDest == col + 1) { //king move lower right
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			if(row < 8 && col >= 0 && rowDest == row + 1 && colDest == col - 1) { //king move lower left
				if(chessBoard[rowDest][colDest].color == PieceColor.BLACK) { //for taking
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					int index = pieceBox.indexOf(destinationPiece); 
					pieceBox.remove(index); 
					opponentTurn();
				}
				else if(chessBoard[rowDest][colDest].color == PieceColor.NONE) { //for moving
					selectedPiece.hasMoved = true;
					chessBoard[row][col] = nul;
					chessBoard[rowDest][colDest] = selectedPiece;
					opponentTurn();
				}
			}
			
		}
		if(destinationPiece.type == PieceType.NONE && rowDest == 7 && colDest == 6 && wR2.hasMoved == false && wK.hasMoved == false) {
			if(chessBoard[row][col + 1].type == PieceType.NONE && blackSightBoard[row][col+1] == false) {
				selectedPiece.hasMoved = true;
				wR2.hasMoved = true;
				chessBoard[row][col] = nul;
				chessBoard[rowDest][colDest] = selectedPiece;
				chessBoard[7][7] = nul;
				chessBoard[7][5] = wR2;
				opponentTurn();
			}
		}
		if(destinationPiece.type == PieceType.NONE && rowDest == 7 && colDest == 2 && wR.hasMoved == false && wK.hasMoved == false) {
			if(chessBoard[row][col - 1].type == PieceType.NONE && blackSightBoard[row][col - 1] == false && chessBoard[row][col - 2].type == PieceType.NONE && blackSightBoard[row][col - 2] == false) {
				selectedPiece.hasMoved = true;
				wR2.hasMoved = true;
				chessBoard[row][col] = nul;
				chessBoard[rowDest][colDest] = selectedPiece;
				chessBoard[7][0] = nul;
				chessBoard[7][3] = wR;
				opponentTurn();
			}
		}
		boardState();
	}
	
	public void sightState() { //prints current state of the players piece sights, mostly used for debugging
		for(int row = 0; row < sightBoard.length; row++) {
		    System.out.println();
		    for(int col = 0; col < sightBoard[row].length; col++) {
		    	if(sightBoard[row][col] == true) {
		    		System.out.print(" T ");
		    	}
		    	else {
		    		System.out.print(" F ");
		    	}
		    }
		}
	}
	
	public void blackSightState() { //prints current state of stockfish's piece sight, mostly used for debugging 
		for(int row = 0; row < blackSightBoard.length; row++) {
		    System.out.println();
		    for(int col = 0; col < blackSightBoard[row].length; col++) {
		    	if(blackSightBoard[row][col] == true) {
		    		System.out.print(" T ");
		    	}
		    	else {
		    		System.out.print(" F ");
		    	}
		    }
		}
	}
	
	public void boardState() { //prints out the current state of the board
		
		for(int row = 0; row < chessBoard.length; row++) {
		    System.out.println();
		    for(int col = 0; col < chessBoard[row].length; col++) {
		    	Pieces tempPiece = chessBoard[row][col];
		    	System.out.print(tempPiece.getPieceName());  	
		    }
		}
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		Board board = new Board();
		board.initializeGUI(board); //initializing GUI
		board.initializeBoard(board); //initializing the game logic state
		//board.boardState();
		
		String input = JOptionPane.showInputDialog("Choose Stockfish Difficulty (0-20)");
		int Level = Integer.parseInt(input);
		
		board.sf.startEngine(); //initializing the stockfish engine
		board.sf.uci();
		board.sf.setLevel(Level);
		board.sf.isReady();
    }
	
	
	
	
	
	
	
	
	@Override
	public void mousePressed(MouseEvent e) { //grabbing piece and piece position from square that mouse is clicked
	
		selectedPiece = getPiece(e.getY()/64, e.getX()/64);
			
		if(selectedPiece.type == PieceType.NONE) {
			return;
		}
		
		selectedPieceLocation = getPosition(selectedPiece);
		System.out.println(Arrays.toString(selectedPieceLocation));
		System.out.println(selectedPiece);
	}

	@Override
	public void mouseReleased(MouseEvent e) { //grabbing piece and piece position from square that mouse is clicked
		
		if(!(e.getX() <frame.getContentPane().getWidth() && e.getX() > 0)) //prevents piece placement outside of the JFrame bounds
        {
            return;
        }

        if(!(e.getY() <frame.getContentPane().getHeight() && e.getY() > 0))
        {
            return;
        }
		
		int[] dest = {e.getY()/64, e.getX()/64}; //grabbing square coords from release 
		System.out.println("dest" + Arrays.toString(dest));
		
		destinationPiece = (getPiece(e.getY()/64, e.getX()/64)); //getting the piece at the release coords
		destinationPieceLocation = dest; //
		System.out.println(Arrays.toString(destinationPieceLocation));
		System.out.println(destinationPiece);
		
		if(selectedPiece.type == PieceType.PAWN) {
			pawnMove();
		}
		
		else if(selectedPiece.type == PieceType.KNIGHT) {
			knightMove();
		}
		
		else if(selectedPiece.type == PieceType.BISHOP) {
			bishopMove();
		}
		
		else if(selectedPiece.type == PieceType.ROOK) {
			rookMove();
		}
		
		else if(selectedPiece.type == PieceType.QUEEN) {
			rookMove();
			bishopMove();
		}
		
		else if(selectedPiece.type == PieceType.KING) {
			kingMove();
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) { //prevents placing pieces off the board
		if(!(e.getX() <frame.getContentPane().getWidth() && e.getX() > 0))
        {
            return;
        }

        if(!(e.getY() <frame.getContentPane().getHeight() && e.getY() > 0))
        {
            return;
        }
    }
}