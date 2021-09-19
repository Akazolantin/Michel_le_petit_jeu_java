package com.Michel.animation;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;

public class BodyPartIn extends BodyPart{
	
	private int ScX,ScY;
	
	public BodyPartIn(String path, int cX,int cY,int zDepth,int ScX,int ScY){
		this.image =new Image(path);
		this.cX=cX;
		this.cY=cY;
		this.ScX=ScX;
		this.ScY=ScY;
		this.zDepth=zDepth;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		double radians =Math.toRadians(fixation.getAngle());
		int FcX=fixation.getcX();
		int FcY=fixation.getcY();
		if(fixation.isReverse()) {radians=-radians;FcX=fixation.getImage().getW()-FcX;}
		double cosinus=Math.cos(radians);
		double sinus=Math.sin(radians);
		this.offX=fixation.getOffX()+(int)((ScX-FcX)*cosinus+(ScY-FcY)*sinus);
		this.offY=fixation.getOffY()+(int)(cosinus*(ScY-FcY)-(ScX-FcX)*sinus);
		for(BodyPart bodyPart : bodyParts) {
			bodyPart.render(gc, r);
		}
		r.setzDepth(zDepth);
		r.drawImageRotate(image, offX,offY, cX, cY, angle);
		r.setzDepth(0);
	}
	
	public void setReverse(boolean reverse) {
		if(reverse^this.reverse) {
		image.setReverse(reverse);
		this.reverse = reverse;
		ScX=fixation.getImage().getW()-ScX;
		for(BodyPart bodyPart : bodyParts) {
			bodyPart.setReverse(reverse);
		}}
	}
}

