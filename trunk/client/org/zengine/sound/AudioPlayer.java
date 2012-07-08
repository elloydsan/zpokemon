package org.zengine.sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * 
 * @author Troy
 *
 */
public class AudioPlayer {
	
	/**
	 * To play a audio clip specify the file.
	 * 
	 * Supports WAV|MIDI files only.
	 * 
	 * @param clipFile
	 */
	public void playClip(File clipFile) {
		try{
			class AudioListener implements LineListener {
				private boolean done = false;
					
				@Override 
				public synchronized void update(LineEvent event) {
					Type eventType = event.getType();
					if(eventType == Type.STOP || eventType == Type.CLOSE) {
						done = true;
						notifyAll();
					}
				}
					
				public synchronized void waitUntilDone() throws InterruptedException {
					while(!done){ 
						wait(); 
					}
				}
			}
				
			AudioListener listener = new AudioListener();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
			  
	
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			clip.start();
			listener.waitUntilDone();
			clip.close();
			audioInputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}