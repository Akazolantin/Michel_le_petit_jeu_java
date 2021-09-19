package com.Michel.animation;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;

public class Body extends BodyPart {
	
	public Body(String path, int cX,int cY,int zDepth,int offX,int offY){
		this.image =new Image(path);
		this.cX=cX;
		this.cY=cY;
		this.zDepth=zDepth;
		this.offX=offX;
		this.offY=offY;
	}

	@Override
	public void render(GameContainer gc, Renderer r){
		for(BodyPart bodyPart : bodyParts) {
			bodyPart.render(gc, r);
		}
		r.setzDepth(zDepth);
		r.drawImageRotate(image, offX,offY, cX, cY, angle);
		r.setzDepth(0);
	}
}
