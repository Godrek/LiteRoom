import java.awt.image.BufferedImage;

public class SobelFilter extends Filter {
	private double[][] Sx = {{-1,-2,-1},{0,0,0},{1,2,1}};
	private double[][] Sy = {{-1,0,1},{-2,0,2},{-1,0,1}};
	
	private static final double pi = Math.PI;
	
	private static final char HORIZONTAL = 0;
	private static final char NEGATIVEDIAGONAL = 1;
	private static final char VERTICAL = 2;
	private static final char POSITIVEDIAGONAL = 3;
	
	private static final double TWENTYTWOPOINTFIVE = pi/8d;
	private static final double SIXTYSEVENPOINTFIVE = 3d*TWENTYTWOPOINTFIVE;
	private static final double ONEHUNDREDTWELVEPOINTFIVE = 5d*TWENTYTWOPOINTFIVE;
	private static final double ONEFIFTYSEVENPOINTFIVE = pi - TWENTYTWOPOINTFIVE;

	
	//Therefore, any edge direction falling within the yellow range (0 to 22.5 & 157.5 to 180 degrees) 
	//is set to 0 degrees. Any edge direction falling in the green range (22.5 to 67.5 degrees) is set to 45 degrees. 
	//Any edge direction falling in the blue range (67.5 to 112.5 degrees) is set to 90 degrees. 
	//And finally, any edge direction falling within the red range (112.5 to 157.5 degrees) is set to 135 degrees. 
	
	
	private int width,height;
	private double[][] xPass,yPass;
	private int x,y;
	private SobelFilterOptions _options;
	private Pixel p;
	private int tail,head;
	private double[][] window = new double[3][3];
	private double[][] magnitude;
	private char[][] orientation;
	
	public SobelFilter(SobelFilterOptions options){_options = options;}
	
	@Override
	public BufferedImage applyFilter(BufferedImage img) {
		width = img.getWidth();
		height = img.getHeight();
		double theta = 0;
		p = new Pixel();
		xPass = new double[width][height];
		yPass = new double[width][height];
		magnitude = new double[width][height];
		orientation = new char[width][height];
		
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
					//xPass[x][y] /= 8f;
					//yPass[x][y] /= 8f;
					
					//calculate magnitude
					magnitude[x][y] = Math.sqrt(Math.pow(xPass[x][y], 2) + Math.pow(yPass[x][y], 2));
					//calculate angle orientation
					theta = Math.atan2(yPass[x][y], xPass[x][y]);
					if(theta < 0)
						theta += pi; //we only care about upper quadrants
					
					if(theta < TWENTYTWOPOINTFIVE || theta > ONEFIFTYSEVENPOINTFIVE){
						orientation[x][y] = HORIZONTAL; //mark as horizontal
					}
					else if(theta >= TWENTYTWOPOINTFIVE && theta < SIXTYSEVENPOINTFIVE){
						orientation[x][y] = NEGATIVEDIAGONAL;
					}
					else if(theta >= SIXTYSEVENPOINTFIVE && theta <= ONEHUNDREDTWELVEPOINTFIVE){
						orientation[x][y] = VERTICAL;
					}
					else if(theta > ONEHUNDREDTWELVEPOINTFIVE && theta <= ONEFIFTYSEVENPOINTFIVE){
						orientation[x][y] = POSITIVEDIAGONAL;
					}
				}
			}

			//combination
			//float xsq,ysq;
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
				//	xsq = (float) Math.pow(xPass[x][y], 2);
					//ysq = (float) Math.pow(yPass[x][y], 2);
					p.setGrayscale((int)magnitude[x][y]);
					//Math.min(Math.pow(p.getRed(),3),255) >= 50
					if(p.getRed() >= _options.getThreshold()){
						if(_options.isBlack())
							img.setRGB(x, y, 0);
						else
							img.setRGB(x, y,p.getPixel() * _options.getMultiplier());
					}
					else{
						if(_options.isBlack())
							img.setRGB(x, y, p.getPixel() * _options.getMultiplier());
						else
							img.setRGB(x, y, 0);	
					}
				}
			}  
		}
		return img;
	}

}
