import java.awt.image.BufferedImage;

public class GaussianFilter extends Filter {
	GaussianFilterOptions _options;
	private int width,height;
	private Pixel p;
	double mu,accumulator;
	private double[] kernel;
	private double[] window;
	private double[][] xResult;
	private int kernelSize;
	
	public GaussianFilter(GaussianFilterOptions options){
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
		xResult = new double[width][height];
		final long startTime = System.currentTimeMillis();
		
		if(img != null){
		//xpass
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
						//read in new row
						for(int i=0;i<kernelSize;i++){
							try{
								window[i] = p.setPixel(img.getRGB(x + i - (int)mu , y)).getIntensity();
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
					for(int i=0;i<kernelSize;i++){
						try{
							window[i] = xResult[x][y+i-(int)mu];
						}
						catch(ArrayIndexOutOfBoundsException e){
							window[i] = 0;
						}
						accumulator += window[i] * kernel[i];
					}
					img.setRGB(x, y, p.setGrayscale((int)accumulator));
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
