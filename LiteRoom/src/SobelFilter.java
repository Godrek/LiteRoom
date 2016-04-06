import java.awt.image.BufferedImage;

public class SobelFilter extends Filter {
	private float[][] Sx = {{-1,-2,-1},{0,0,0},{1,2,1}};
	private float[][] Sy = {{-1,0,1},{-2,0,2},{-1,0,1}};
	private int width,height;
	private float[][] xPass,yPass;
	private int x,y;
	private SobelFilterOptions _options;
	private Pixel p;
	private int tail,head;
	private float[][] window = new float[3][3];
	
	public SobelFilter(SobelFilterOptions options){_options = options;}
	
	@Override
	public BufferedImage applyFilter(BufferedImage img) {
		width = img.getWidth();
		height = img.getHeight();
		p = new Pixel();
		xPass = new float[width][height];
		yPass = new float[width][height];
		
		if(img != null){
			
			//kernel passes
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					
					//prime moving window
					if(y==0){
						for(int i=0;i<3;i++){
							window[i][0] = 0; //pad top of window w/ zeros
							for(int j=1;j<3;j++){
								try{
									window[i][j] = p.setPixel(img.getRGB(x + i - 1, y + j - 1)).getIntensity();
								}
								catch(ArrayIndexOutOfBoundsException e){
									window[i][j] = 0; //more zero padding if edge
								}
							}
						}
						for(int i=0;i<3;i++){
							xPass[x][y] += window[0][i] * Sx[0][i];
							xPass[x][y] += window[2][i] * Sx[2][i];
							
							yPass[x][y] += window[i][0] * Sy[i][0];
							yPass[x][y] += window[i][2] * Sy[i][2];
						}
						tail = 0;
						head = 1;
					}
					else{
						//read in new row
						for(int i=0;i<3;i++){
							try{
								window[i][tail] = p.setPixel(img.getRGB(x + i - 1, y + 1)).getIntensity();
							}
							catch(ArrayIndexOutOfBoundsException e){
								window[i][tail] = 0;
							}
						}
						for(int i=0;i<3;i++){
							xPass[x][y] += window[0][(head+i)%3] * Sx[0][i];
							xPass[x][y] += window[2][(head+i)%3] * Sx[2][i];
							
							yPass[x][y] += window[i][head] * Sy[i][0];
							yPass[x][y] += window[i][tail] * Sy[i][2];
						}
						tail++;head++;tail%=3;head%=3;
					}
					//
					xPass[x][y] /= 8f;
					yPass[x][y] /= 8f;
					
				}
			}

			//combination
			float xsq,ysq;
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					xsq = (float) Math.pow(xPass[x][y], 2);
					ysq = (float) Math.pow(yPass[x][y], 2);
					p.setGrayscale((int)Math.sqrt(xsq+ysq));
					//Math.min(Math.pow(p.getRed(),3),255) >= 50
					if(p.getRed() > _options.getThreshold()){
						if(_options.isBlack())
							img.setRGB(x, y, 0);
						else
							img.setRGB(x, y, p.getPixel() * 10);
					}
					else{
						if(_options.isBlack())
							img.setRGB(x, y, p.getPixel()* 10);
						else
							img.setRGB(x, y, 0);	
					}
				}
			}  
		}
		return img;
	}

}
