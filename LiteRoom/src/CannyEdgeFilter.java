import java.awt.image.BufferedImage;

public class CannyEdgeFilter extends Filter {
	private double[][] Sx = {{-1,-2,-1},{0,0,0},{1,2,1}};
	private double[][] Sy = {{-1,0,1},{-2,0,2},{-1,0,1}};
	private char[][] result;
	
	private BufferedImage _img;
	
	private static final double pi = Math.PI;
	
	private static final int HORIZONTAL = 0;
	private static final int NEGATIVEDIAGONAL = 1;
	private static final int VERTICAL = 2;
	private static final int POSITIVEDIAGONAL = 3;
	
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
	private int x,y,tx,ty;
	private CannyEdgeFilterOptions _options;
	private Pixel p;
	private int tail,head;
	private double[][] window = new double[3][3];
	private double[][] magnitude;
	private int[][] orientation;
	private int lowThreshold,highThreshold;
	
	public CannyEdgeFilter(CannyEdgeFilterOptions options){_options = options;}
	
	@Override
	public BufferedImage applyFilter(BufferedImage img) {
		final long startTime = System.currentTimeMillis();
		_img = img;
		width = img.getWidth();
		height = img.getHeight();
		lowThreshold = _options.getLowThreshold();
		highThreshold = _options.getHighThreshold();
		double theta = 0;
		p = new Pixel();
		xPass = new double[width][height];
		yPass = new double[width][height];
		magnitude = new double[width][height];
		orientation = new int[width][height];
		result = new char[width][height];
		
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
					//xPass[x][y] /= 4f;
					//yPass[x][y] /= 4f;
					
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
						orientation[x][y] = POSITIVEDIAGONAL;
					}
					else if(theta >= SIXTYSEVENPOINTFIVE && theta <= ONEHUNDREDTWELVEPOINTFIVE){
						orientation[x][y] = VERTICAL;
					}
					else if(theta > ONEHUNDREDTWELVEPOINTFIVE && theta <= ONEFIFTYSEVENPOINTFIVE){
						orientation[x][y] = NEGATIVEDIAGONAL;
					}
				}
			}

			//nonmax suppression
			p.setPixel(0).setAlpha(255);
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					if(orientation[x][y] == HORIZONTAL){
						try{
							if(magnitude[x][y] >= magnitude[x-1][y] && magnitude[x][y] >= magnitude[x+1][y]){
								magnitude[x-1][y] = magnitude[x+1][y] = 0;
							}else{
								magnitude[x][y] = 0;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
					else if(orientation[x][y] == NEGATIVEDIAGONAL){
						try{
							if(magnitude[x][y] >= magnitude[x-1][y-1] && magnitude[x][y] >= magnitude[x+1][y+1]){
								magnitude[x-1][y-1] = magnitude[x+1][y+1] = 0;
							}else{
								magnitude[x][y] = 0;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
					else if(orientation[x][y] == VERTICAL){
						try{
							if(magnitude[x][y] >= magnitude[x][y-1] && magnitude[x][y] >= magnitude[x][y+1]){
								magnitude[x][y-1] = magnitude[x][y+1] = 0;
							}else{
								magnitude[x][y] = 0;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
					if(orientation[x][y] == POSITIVEDIAGONAL){
						try{
							if(magnitude[x][y] >= magnitude[x-1][y+1] && magnitude[x][y] >= magnitude[x+1][y-1]){
								magnitude[x-1][y+1] = magnitude[x+1][y-1] = 0;
							}else{
								magnitude[x][y] = 0;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
					//p.setGrayscale((int)magnitude[x][y]);
					//img.setRGB(x, y, p.setGrayscale((int)magnitude[x][y]));
					
					//
					if(magnitude[x][y] < lowThreshold){
						result[x][y] = 0; // no edge
						//img.setRGB(x, y, 0xFF000000);
					}
					else if(magnitude[x][y] >= highThreshold){
						result[x][y] = 2; //strong edge
					}
					else if(magnitude[x][y] >= lowThreshold){
						result[x][y] = 1; //weak edge
						//img.setRGB(x, y, 0xFF000000);
					}
				}
			}  
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					if(result[x][y] == 0){
						img.setRGB(x, y, 0xFF000000); //nonedge
					}
					else if(result[x][y] == 2){
						img.setRGB(x, y,0xFFFFFFFF);
					}
					else if(result[x][y] == 1){
					//	System.out.println("weak checking for strong");
						if(hasStrongConnection(x,y)){
							img.setRGB(x, y, 0xFFFFFFFF);
							result[x][y] = 2; //promote to strong edge
						}
						else{
							//System.out.println("weak -> non-edge");
							img.setRGB(x, y, 0xFF808080); //show as weak edge
						}
					}
				}
			}
		}
		final long endTime = System.currentTimeMillis();


		System.out.println("Total execution time: " + (endTime - startTime) );
		return img;
	}
	
	public boolean hasStrongConnection(int x,int y){
		//forward travel
		tx = x; ty = y;
		try{
			//System.out.println("checking weak edge @ [" + tx + "][" + ty + "]" + "with magnitude" + magnitude[tx][ty]);
			while(result[tx][ty] >= 1){
				if(orientation[tx][ty] == HORIZONTAL){
					tx++;
				}
				else if(orientation[tx][ty] == NEGATIVEDIAGONAL){
					tx++;ty++;
				}
				else if(orientation[tx][ty] == VERTICAL){
					ty++;
				}
				else if(orientation[tx][ty] == POSITIVEDIAGONAL){
					tx++;ty--;
				}
				//System.out.println("checking neighbor @ [" + tx + "][" + ty + "]" + "with magnitude" + magnitude[tx][ty]);
				if(result[tx][ty] == 2){
				//	System.out.println("weak -> strong!!!!!!!!!!!");
					return true;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){}//end of picture
		tx = x; ty = y;
		//backwards
		try{
			while(result[tx][ty] >= 1){
			//	System.out.println("checking weak edge @ [" + tx + "][" + ty + "]" + "with magnitude" + magnitude[tx][ty]);
				if(orientation[tx][ty] == HORIZONTAL){
					tx--;
				}
				else if(orientation[tx][ty] == NEGATIVEDIAGONAL){
					tx--;ty--;
				}
				else if(orientation[tx][ty] == VERTICAL){
					ty--;
				}
				else if(orientation[tx][ty] == POSITIVEDIAGONAL){
					tx--;ty++;
				}
				//System.out.println("checking neighbor @ [" + tx + "][" + ty + "]" + "with magnitude" + magnitude[tx][ty]);
				if(result[tx][ty] == 2){
					//System.out.println("weak -> strong!!!!!!!!!!!!!");
					return true;
				}
			}
		} catch(ArrayIndexOutOfBoundsException e){}//end
		return false;
	}


}
