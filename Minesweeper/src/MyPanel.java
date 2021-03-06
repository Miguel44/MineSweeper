import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9; 
	
	
	Random randomBomb = new Random ();
	private final int TOTAL_BOMBS = 15;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public int numBombs = 0;
	public int [][] adjacentBombs = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public  int [][] bombArray = new int [TOTAL_COLUMNS+1][TOTAL_ROWS+1];
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;	
			}
		}
		
		while(numBombs<TOTAL_BOMBS){// Populates cells with bombs
			
			int c = randomBomb.nextInt(TOTAL_COLUMNS);
			int r = randomBomb.nextInt(TOTAL_ROWS);
			int Bomb = randomBomb.nextInt(2);
			if( Bomb == 1 && bombArray[c][r] !=1){
				numBombs ++;
				bombArray [c][r]=Bomb;
			}
		}

		// Counts the bomb adjacent to a cell
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   
			for (int y = 0; y < TOTAL_ROWS; y++) {
				int numCount = 0;
				if((y==0 || x==0 )){
					if(bombArray[x][y+1]==1){
						numCount++;
					}
					if(bombArray[x+1][y]==1){
						numCount++;
					}
					if(bombArray[x+1][y+1]==1){
						numCount++;
					}
			 }else{
				if(bombArray[x+1][y-1]==1){
					numCount++;
				}
				if(bombArray[x-1][y+1]==1){
					numCount++;
				} 
				if(bombArray[x-1][y-1]==1){
					numCount++;
				}
				if(bombArray[x][y-1]==1){
					numCount++;
				}
				if(bombArray[x-1][y]==1){
					numCount++;
				}
				if(bombArray[x][y+1]==1){
					numCount++;
				}
				if(bombArray[x+1][y]==1){
					numCount++;
				}
				if(bombArray[x+1][y+1]==1){
					numCount++;
				}
			 }
				adjacentBombs[x][y] = numCount;
			}
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS ; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				
					Color c = colorArray[x][y];
					g.setColor(c);					
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
						if(bombArray[x][y]!=1 && adjacentBombs(x,  y)>0 && colorArray[x][y]==Color.GRAY){
									g.setColor(Color.BLUE);
									g.drawString(adjacentBombs(x,y) + "",x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 12, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 20);
									
					}
				
			}
		}
	}
	
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		
		return y;
	}
	
	public boolean checkForBombs ( int x, int y){// Returns true if a bomb is present
		return bombArray[x][y]==1;
	}
	
	public int adjacentBombs(int x, int y){// Checks adjacent cells for bombs
		return adjacentBombs[x][y];
	}
	
	public void clickedBomb(){// Checks if player touches bomb
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if(bombArray[x][y]==1){
					colorArray[x][y]=Color.BLACK;
				}
			}
		}
		this.repaint();
		JOptionPane.showMessageDialog(null, "GAME OVER!");
		System.exit(0);
		return;
	}
	
	public boolean playerWon(){ // Check if player has clicked on every white cell that does not include bomb
		int cellsClicked = 0;
		for (int i = 0; i < TOTAL_ROWS; i++){
			for (int j = 0; j < TOTAL_COLUMNS; j++){
				if (colorArray[i][j] == Color.GRAY){
					cellsClicked++;
				}
			}
		}
		if (cellsClicked >= TOTAL_ROWS * TOTAL_COLUMNS - TOTAL_BOMBS){
			this.repaint();
			JOptionPane.showMessageDialog(null, "YOU WON!");
			return true;
		}
		return false;
	}

	
}