package com.Michel.animation;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;

public class BodyPartOut extends BodyPart{
	
	private int ScX,ScY;
	private Image link;
	private int LcX,LcY;
	
	public BodyPartOut(String path,String linkPath, int cX,int cY,int zDepth,int ScX,int ScY,int LcX,int LcY){
		this.image =new Image(path);
		this.link=new Image(linkPath);
		this.cX=cX;
		this.cY=cY;
		this.ScX=ScX;
		this.ScY=ScY;
		this.LcX=LcX;
		this.LcY=LcY;
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
		r.drawImageRotate(link, offX, offY,LcX,LcY, fixation.getAngle());
		r.drawImageRotate(image, offX,offY, cX, cY, angle);
		r.setzDepth(0);
	}
	
	public void setReverse(boolean reverse) {
		if(reverse^this.reverse) {
		image.setReverse(reverse);
		link.setReverse(reverse);
		this.reverse = reverse;
		ScX=fixation.getImage().getW()-ScX;
		for(BodyPart bodyPart : bodyParts) {
			bodyPart.setReverse(reverse);
		}}
	}
}
