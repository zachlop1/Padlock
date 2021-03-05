package Lock;

/**
 * Padlockthat represents a model of a typical combination lock with a rotating
 * dial, found in schools and locker roomseverywhere
 */
public class Padlock {

	/**
	 * the width of the tooth on each disc, expressed in degrees of rotation
	 */
	public static final int TOOTH = 2;

	// lock mechanism consists of three discs mounted on the same axis
	private int disc1 = 4; // rear disk
	private int disc2 = 2; // middle disc
	private int disc3 = 0; // front disc
	
	//combination n1, n2, n3
	private int n1;
	private int n2;
	private int n3;
	

	// opened or closed
	private boolean opened = true;

	/**
	 * constructor
	 * 
	 * The initial position of the discs is always 0
	 * degrees for disc 3, 
	 * 2 degrees for disc 2, 
	 * and 4 degrees for disc 1.
	 * (This is true regardless of the constructor arguments.). 
	 * @param n1 
	 * @param n2 
	 * @param n3 
	 */
	public Padlock(int n1, int n2, int n3) {
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
	}

	/**
	 * sets the disc positions to randomly generated, valid value
	 */
	public void randomizePositions(java.util.Random rand) {

		setPositions(rand.nextInt(360),
				rand.nextInt(360),
				rand.nextInt(360));
	}

	public void setPositions(int n1, int n2, int n3) {
		disc1 = n1;
		disc2 = n2;
		disc3 = n3;
		
		//normalize disc 1
		while (disc1 < 0) {
			disc1 += 360;
		}
		disc1 = disc1 % 360;
		
		//normalize disc 2
		while (disc2 < 0) {
			disc2 += 360;
		}
		disc2 = disc2 % 360;
		
		//normalize disc 3
		while (disc3 < 0) {
			disc3 += 360;
		}
		disc3 = disc3 % 360;
	}

	/**
	 * turns the dial counterclockwise until the given number is at the top
	 */
	public void turnLeftTo(int number) {
		
		while(true) {
		
			turn(1);
			
			if (disc3 == number) {
				break;
			}
		}
	}

	/**
	 * turns the dial clockwise until the given number is at the top
	 */
	public void turnRightTo(int number) {
		while(true) {
			
			turn(-1);
			
			if (disc3 == number) {
				break;
			}
		}
	}

	/**
	 * turns the dial the given number of degrees, where a positive number indicates
	 * a counterclockwise rotation and a negative number indicates a clockwise
	 * rotation
	 */
	public void turn(int degrees) {
		
		//position of disc3 before moving it
		int previousDisc3 = disc3;
		
		//position of disc2 before moving it
		int previousDisc2 = disc2;
		
		int moves = 0;
		
		if (degrees > 0) { //counterclockwise rotation
			
		    // Next, how does disc 3 affect disc 2? 
		    // Difference when moving ccw is 90 - 30 = 60 degrees,
		    // less the tooth width, leaving 58 degrees.
		    // We're rotating 70 degrees ccw, so disc 2 is 
		    // 12 degrees ccw to angle 102.
		    //p.setPositions(0, 90, 30); 
		    //p.turn(70);
		    //printPositions(p);  // expected 0 102 100
			
			disc3 = (disc3 + degrees) % 360;			
			
			//now check disc 2
			int diff = disc2 - previousDisc3 - TOOTH;
			while (diff < 0) {
				diff += 360;
			}
			disc2 += (degrees - diff);
			
			moves = diff;			
			
			
		}else {//clockwise rotation
			disc3 = disc3 + degrees;
			//normalize disc 3
			while (disc3 < 0) {
				disc3 += 360;
			}
			
			//now check disc 2
			int diff = 360 - (disc2 - previousDisc3) - TOOTH;
			while (diff < 0) {
				diff += 360;
			}
			moves = -degrees - diff;
			disc2 = disc2 - moves;
			disc2 = (360 + (disc2 % 360)) % 360;
			
			
		
		}
		
		//check if disc 1 affected?
		int distance = 0;
		if (disc1 - previousDisc2 > 0) {
			distance = 360 - (disc1 - previousDisc2) - TOOTH;
		}else {
			distance =  -(disc1 - previousDisc2) - TOOTH;
		}
		distance = (360 + distance % 360) % 360;
		if (moves < distance) {
			return;
		}
		
	    // What about disc 1?
	    // Same example as above, rotating clockwise, but 
	    // disc 1 starts at 10 degrees.
	    // Clockwise difference between disc 1 and disc 2 is 
	    // 40 - 10 = 30, less the tooth width is 28.
	    // From previous example, disc 2 is rotating 52 degrees cw, 
	    // so it will push disc 1 clockwise 24 degrees.
	    // New disc 1 position is 10 - 24 = -44 or 346.
		
		//Clockwise difference between disc 1 and disc 2 is 
	    // 40 - 10 = 30
		int difference = previousDisc2 - n1 - TOOTH; //e.g. 28
		
	    // From previous example, disc 2 is rotating 52 degrees cw, 
	    // so it will push disc 1 clockwise 24 degrees.
		//note: moves = 52
		int value = moves - difference;//e.g. 24			
		
	    // New disc 1 position is 10 - 24 = -44 or 346.
		disc1 = n1 - value;
		disc1 = (360 + (disc1 % 360)) % 360;
		
	}

	/**
	 * get current position of the given disc (1, 2,or 3)
	 */
	public int getDiscPosition(int which) {

		if (which == 1) {
			return disc1;
		} else if (which == 2) {
			return disc2;
		} else {
			return disc3;
		}
	}

	/**
	 * returns true if the three discs are aligned in position for the lock toopen
	 */
	public boolean isAligned() {

		return n3 == disc3 && n2 == disc2 - TOOTH &&
				n1 == disc1 +  2 * TOOTH;
	}

	/**
	 * opens the lock, if possible
	 */
	public void open() {
		if (isAligned()) {
			opened = true;
		}
	}

	/**
	 * closes the lock, regardless of whether the discs are aligned
	 */
	public void close() {
		opened = false;
	}

	/**
	 * returns true if the lock is currently open
	 */
	public boolean isOpen() {
		return opened;
	}
}
