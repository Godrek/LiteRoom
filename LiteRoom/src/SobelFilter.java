import java.awt.image.BufferedImage;

public class SobelFilter extends Filter {
	private float[][] Sx = {{-1,-2,-1},{0,0,0},{1,2,1}};
	private float[][] Sy = {{-1,0,1},{-2,0,2},{-1,0,1}};
	private int width,height;
	private float[][] xPass,yPass;
	private int x,y;
	private float accumulator;
	private Pixel p;

	
	public SobelFilter(){}
	
	@Override
	public BufferedImage applyFilter(BufferedImage img) {
		width = img.getWidth();
		height = img.getHeight();
		p = new Pixel();
		xPass = new float[width][height];
		yPass = new float[width][height];
		if(img != null){
			//xpass
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					accumulator = 0;
					for(int i = -1;i<2;i++){
						for(int j=-1;j<2;j++){	
							if(x + i >= 0 && y + j >=0 && x + i < width && y + j < height)
								p.setPixel(img.getRGB(x + i, y + j));
							else
								p.setPixel(0);
							accumulator += p.getIntensity() * Sx[i+1][j+1];
						}
					}
					xPass[x][y] = accumulator/9f;
				}
			}

			//ypass
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					accumulator = 0;
					for(int i = -1;i<2;i++){
						for(int j=-1;j<2;j++){	
							if(x + i >= 0 && y + j >=0 && x + i < width && y + j < height)
								p.setPixel(img.getRGB(x + i, y + j));
							else
								p.setPixel(0);
							accumulator += p.getIntensity() * Sy[i+1][j+1];
						}
					}
					yPass[x][y] = accumulator/9f;
				}
			}
			//combination
			float xsq,ysq;
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					xsq = (float) Math.pow(xPass[x][y], 2);
					ysq = (float) Math.pow(yPass[x][y], 2);
					p.setGrayscale((int)Math.sqrt(xsq+ysq));
					if(Math.min(Math.pow(p.getRed(),3),255) >= 50)
						img.setRGB(x, y, p.getPixel());
					else
						img.setRGB(x, y, 0);
				}
			} 
		}
		return img;
	}

}
