package com.android.sofla.drj;

import com.android.sofla.drj.Pothole;

import android.graphics.Canvas;

class Droid {

	float x;
	float y;
	float vy;
	boolean jumping;
	boolean falling;
	
	final float w = 40.0f;
	final float h = 45.0f;
	
	final float startX = 380.0f;
	final float startY = 352.5f;
	final float initialVelocity = 15.0f;
	
	float yAdjust;
	
	Game game;

	public Droid(Game game) {		
		this.game = game;
		reset();		
	}

	public void reset() {
		
		jumping = false;
		falling = false;
		
		x = startX;
		y = startY;
		
		// since droid is floating a little bit above the ground need
		// to take this into account for collision purposes
		yAdjust = game.groundY - y - h; 
	}

	public void update() {

		//
		// first: handle collision detection with potholes
		//
		if (!jumping) {
			doCollisionDetection();
		}

		//
		// handle falling
		//
		if (falling) {
			doPlayerFall();
		}

		//
		// handle jumping
		//
		if (jumping) {
			doPlayerJump();
		}

		//
		// does player want to jump?
		//
		if (game.playerTap && !jumping && !falling) {
			startPlayerJump();
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(x, y, x + w, y + h, game.greenPaint);
	}
	
	private void doCollisionDetection() {

		float ey = y + h + yAdjust;

		for (Pothole p : game.potholes) {
			if (!p.alive) {
				continue;
			}

			float lx = x;
			float rx = x + w;

			if (
					// am I over the pothole?
					(p.x < lx) 
					
					// am I still inside the pothole?
					&& ((p.x + p.w) > rx) 
					
					// have I fallen into the pothole?
					&& (p.y <= ey)
				
				) {
				
				game.initGameOver();
				
			}
		}		
	}
	
	private void doPlayerFall() {
		vy += 1.0f;
		y += vy;
		float tmpY = y + h;
		if (tmpY > game.groundY) {
			y = startY;
			falling = false;
		}		
	}
	
	private void doPlayerJump() {
		y -= vy;
		vy -= 1.0f;
		if (vy <= 0.0f) {
			jumping = false;
			falling = true;
		}		
	}
	
	private void startPlayerJump() {
		jumping = true;
		game.playerTap = false;
		vy = initialVelocity;		
	}

}