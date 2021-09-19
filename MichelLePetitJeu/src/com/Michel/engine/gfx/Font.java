package com.Michel.engine.gfx;

public class Font {
	
	public static final Font STANDARD=new Font("/font/Standard.png");
	
	private Image fontImage;
	private int [] widths;
	private int [] offsets;
	
	public Font(String path) {
		fontImage= new Image(path);
		offsets=new int [59];
		widths=new int[59];
		
		int unicode=0;
		for(int i=0;i<fontImage.getW();i++) {
			if(fontImage.getP()[i]==0xff0000ff) {
				offsets[unicode]=i;
				
			}
			if(fontImage.getP()[i]==0xffffff00) {
				widths[unicode]=i-offsets[unicode];
				unicode++;
				
			}
		}
	}

	public Image getFontImage() {
		return fontImage;
	}

	public int[] getWidths() {
		return widths;
	}

	public int[] getOffsets() {
		return offsets;
	}

}
