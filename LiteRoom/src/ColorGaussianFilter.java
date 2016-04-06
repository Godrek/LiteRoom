import java.awt.image.BufferedImage;

public class ColorGaussianFilter extends Filter {
	GaussianFilterOptions _options;
	private int width,height;
	private Pixel p;
	double mu,accumulator;
	private double[] kernel;
	private double[] window;
	private double[][] xResult;
	private int kernelSize;
	private Pixel[][] cResult;
	
	public ColorGaussianFilter(GaussianFilterOptions options){
		_options = options;
		kernelSize = _options.getKernelSize();
		mu = ((double)_options.getKernelSize() - 1d)/2d;
		double sigma = mu/2d;
		kernel = new double[_options.getKernelSize()];
		window = new double[kernelSize];
		double sum = 0;
		
		for(int x=0;x<2*mu+1;x++){
			kernel[x] = gaussian((double)x,mu,sigma);
			sum += kernel[x];
		}

		for(int x = 0;x<kernelSize;x++){
				kernel[x] /= sum;			
		}
	}
	
	public BufferedImage applyFilter(BufferedImage img){
		width = img.getWidth();
		height = img.getHeight();
		p = new Pixel();
		int x,y;
		cResult = new Pixel[width][height];
		xResult = new double[width][height];
		final long startTime = System.currentTimeMillis();
		
		if(img != null){
		//xpass red
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
						//read in new row
						for(int i=0;i<kernelSize;i++){
							try{
								window[i] = p.setPixel(img.getRGB(x + i - (int)mu , y)).getRed();
							}
							catch(ArrayIndexOutOfBoundsException e){
								window[i] = 0;
							}
							xResult[x][y] += window[i] * kernel[i];
						}
				}
			}
			//ypass red
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					accumulator = 0;
					for(int i=0;i<kernelSize;i++){
						try{
							window[i] = xResult[x][y+i-(int)mu];
						}
						catch(ArrayIndexOutOfBoundsException e){
							window[i] = 0;
						}
						accumulator += window[i] * kernel[i];
					}
					cResult[x][y] = new Pixel();
					cResult[x][y].setRed((int)accumulator);
					cResult[x][y].setAlpha(255);
				}
			}
			
			//reset array
			xResult = new double[width][height];
			//xpass green
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
						//read in new row
						for(int i=0;i<kernelSize;i++){
							try{
								window[i] = p.setPixel(img.getRGB(x + i - (int)mu , y)).getGreen();
							}
							catch(ArrayIndexOutOfBoundsException e){
								window[i] = 0;
							}
							xResult[x][y] += window[i] * kernel[i];
						}
				}
			}
			//ypass
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					accumulator = 0;
					p.setPixel(0);
					for(int i=0;i<kernelSize;i++){
						try{
							window[i] = xResult[x][y+i-(int)mu];
						}
						catch(ArrayIndexOutOfBoundsException e){
							window[i] = 0;
						}
						accumulator += window[i] * kernel[i];
					}
					cResult[x][y].setGreen((int)accumulator);
				}
			}
			//reset array
			xResult = new double[width][height];
			//xpass blue
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
						//read in new row
						for(int i=0;i<kernelSize;i++){
							try{
								window[i] = p.setPixel(img.getRGB(x + i - (int)mu , y)).getBlue();
							}
							catch(ArrayIndexOutOfBoundsException e){
								window[i] = 0;
							}
							xResult[x][y] += window[i] * kernel[i];
						}
				}
			}
			//ypass
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					accumulator = 0;
					p.setPixel(0);
					for(int i=0;i<kernelSize;i++){
						try{
							window[i] = xResult[x][y+i-(int)mu];
						}
						catch(ArrayIndexOutOfBoundsException e){
							window[i] = 0;
						}
						accumulator += window[i] * kernel[i];
					}
					cResult[x][y].setBlue((int)accumulator);
					img.setRGB(x, y, cResult[x][y].getPixel());
				}
			}
		}
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );
		return img;
	}
	
	//gaussian function for calculating spread with a given kernel radius (mu) and standard deviation (sigma)
	public double gaussian(double x, double mu, double sigma){
		return Math.exp( -(((x-mu)/(sigma))*((x-mu)/(sigma)))/2d );
	}
}
