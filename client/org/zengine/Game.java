package org.zengine;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.zengine.graphics.Paintable;


/**
 * 
 * @author Troy
 *
 */
public abstract class Game implements Paintable, MouseListener, MouseMotionListener, KeyListener{
	public abstract void onStart();
	public abstract void start();
	public abstract void onEnd();
}
