package org.zpokemon.server;

/**
 * 
 * @author Troy
 *
 */
public class TimeManager {
	private Thread TimeThread;
	
	private final int UPDATE_RATE = 30;
	private final long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft;
	
	public TimeManager(){
		 //Start a new listener.
		TimeThread = new Thread(){
        	@Override
			public void run(){
				loop();
			}
        };
        TimeThread.start();
	}
	
	public void loop(){
		while(true){
			beginTime = System.currentTimeMillis();
			
			/**
	    	 * Manager the server time.
	    	 */
	    	if(!Constants.isNight() && Constants.getFulldayTimer().isUp()){
				if(Constants.getTransitionTimer().isNotUp()){
					if(Constants.getChangeTransitionTimer().isUp() && Constants.getTransition() < 0.6f){
						Constants.setTransition(Constants.getTransition() + 0.0033f);
						Constants.setSunmoonY(Constants.getSunmoonY() + (double)0.1485);
						Constants.getChangeTransitionTimer().reset();
					}
				}else{
					Constants.getFullnightTimer().reset();
					Constants.getTransitionTimer().reset();
					Constants.setNight(true);
				}
			}else if(!Constants.isNight() && Constants.getFulldayTimer().isNotUp()){
				Constants.getTransitionTimer().reset();
			}
			
			/**
			 * Change back to day.
			 */
			if(Constants.isNight() && Constants.getFulldayTimer().isUp()){
				if(Constants.getTransitionTimer().isNotUp()){
					if(Constants.getChangeTransitionTimer().isUp() && Constants.getTransition() > 0.0033f){
						Constants.setTransition(Constants.getTransition() - 0.0033f);
						Constants.setSunmoonY(Constants.getSunmoonY() - (double)0.1485);
						Constants.getChangeTransitionTimer().reset();
					}
				}else{
					Constants.getFulldayTimer().reset();
					Constants.getTransitionTimer().reset();
					Constants.setNight(false);
				}
			}else if(Constants.isNight() && Constants.getFullnightTimer().isNotUp()){
				Constants.getTransitionTimer().reset();
			}	
				
			timeTaken = System.currentTimeMillis() - beginTime;
			timeLeft = (UPDATE_PERIOD - timeTaken);
	
			if(timeLeft < 10) timeLeft = 10;
			
			try{
				Thread.sleep(timeLeft);
			}catch(InterruptedException ex){break;}
		}
	}

}