package com.Michel.animation;

import java.util.ArrayList;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;

public abstract class BodyPart {
	
	protected int cX,cY;
	protected int angle=0;
	protected Image image;
	protected int zDepth;
	protected int offX,offY;
	protected BodyPart fixation;
	protected boolean reverse=false;
	
	protected ArrayList<BodyPart> bodyParts=new ArrayList<BodyPart>();
	


	public abstract void render(GameContainer gc, Renderer r) ;

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getcX() {
		return cX;
	}

	public int getcY() {
		return cY;
	}

	public void setcX(int cX) {
		this.cX = cX;
	}

	public void setcY(int cY) {
		this.cY = cY;
	}
	
	public void addBodyPart(BodyPart bodyPart) {
		bodyParts.add(bodyPart);
		bodyPart.setFixation(this);
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}

	public int getOffX() {
		return offX;
	}

	public void setOffX(int offX) {
		this.offX = offX;
	}

	public int getOffY() {
		return offY;
	}

	public void setOffY(int offY) {
		this.offY = offY;
	}

	public BodyPart getFixation() {
		return fixation;
	}

	public void setFixation(BodyPart fixation) {
		this.fixation = fixation;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		if(reverse^this.reverse) {
		image.setReverse(reverse);
		this.reverse = reverse;
		for(BodyPart bodyPart : bodyParts) {
			bodyPart.setReverse(reverse);
		}}
	}

}
