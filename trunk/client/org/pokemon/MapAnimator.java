package org.pokemon;


/**
 * 
 * @author Troy
 *
 */
public class MapAnimator {
	private Thread animator;
	private final int UPDATE_RATE = 2;
	private final long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
	
	private boolean running = false;
	
	/**
	 * Construct a new animator.
	 */
	public MapAnimator(){
		animator = new Thread() {
			@Override
			public void run(){
				running = true;
				loop();
			}
		};
		animator.start();
	}
	
	/**
	 * Animate the map.
	 */
	public void loop() {
		while(running){
			beginTime = System.currentTimeMillis();

			for(int a = 0; a < GameConstants.getTilemap().getTileRows(); a++){
				for(int b = 0; b < GameConstants.getTilemap().getTileCols(); b++){
					if(GameConstants.getTilemap().getLayer1()[b][a].getImage() == 164)
						GameConstants.getTilemap().getLayer1()[b][a].setImage((short)194);
					else if(GameConstants.getTilemap().getLayer1()[b][a].getImage() == 194)
						GameConstants.getTilemap().getLayer1()[b][a].setImage((short)224);
					else if(GameConstants.getTilemap().getLayer1()[b][a].getImage() == 224)
						GameConstants.getTilemap().getLayer1()[b][a].setImage((short)164);
				}
			}
				
			timeTaken = System.currentTimeMillis() - beginTime;
			timeLeft = (UPDATE_PERIOD - timeTaken);
	
			if(timeLeft < 10) 
				timeLeft = 10;
			
			try {
				Thread.sleep(timeLeft);
			}catch(InterruptedException ex){
				break;
			}
		}
	}
	
	/**
	 * Stop the animator.
	 */
	public void stop(){
		running = false;
		
		while(!animator.isInterrupted())
			animator.interrupt();
	}

}