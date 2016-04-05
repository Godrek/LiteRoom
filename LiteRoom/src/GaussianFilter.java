import java.awt.image.BufferedImage;

public class GaussianFilter extends Filter {
	GaussianFilterOptions _options;
	private int width,height;
	private Pixel p;
	private double[] hKernel;
	private double[] vKernel;
	private double[][] hvKernel;
	private double[][] window;
	private double[][] result;
	private int kernelSize,tail,head;
	
	public GaussianFilter(GaussianFilterOptions options){
		_options = options;
		kernelSize = _options.getKernelSize();
		double mu = ((double)_options.getKernelSize() - 1d)/2d;
		double sigma = mu/2d;
		hKernel = new double[_options.getKernelSize()];
		vKernel = new double[_options.getKernelSize()];
		hvKernel = new double[kernelSize][kernelSize];
		window = new double[kernelSize][kernelSize];
		
		for(int x=0;x<2*mu+1;x++){
			hKernel[x] = gaussian((double)x,mu,sigma);
		}
		for(int x=0;x<2*mu+1;x++){
			vKernel[x] = hKernel[x];
		}
		double sum = 0;
		for(int x = 0;x<kernelSize;x++){
			for(int y = 0; y<kernelSize;y++){
				hvKernel[x][y] = hKernel[x] * vKernel[y];
				sum += hvKernel[x][y];
			}
		}
		for(int x = 0;x<kernelSize;x++){
			for(int y = 0; y<kernelSize;y++){
				hvKernel[x][y] /= sum;			}
		}
	}
	
	public BufferedImage applyFilter(BufferedImage img){
		width = img.getWidth();
		height = img.getHeight();
		p = new Pixel();
		int x,y;
		result = new double[width][height];
		
		if(img != null){
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					
					//prime moving window
					if(y==0){
						for(int i=0;i<kernelSize;i++){
							window[i][0] = 0; //pad top of window w/ zeros
							for(int j=1;j<kernelSize;j++){
								try{
									window[i][j] = p.setPixel(img.getRGB(x + i - 1, y + j - 1)).getIntensity();
								}
								catch(ArrayIndexOutOfBoundsException e){
									window[i][j] = 0; //more zero padding if edge
								}
							}
						}
						for(int i=0;i<kernelSize;i++){
							for(int j=0;j<kernelSize;j++){
								result[x][y] += window[i][(j+head)%kernelSize] * hvKernel[i][j];
							}
						}
						tail = 0;
						head = 1;
					}
					else{
						//read in new row
						for(int i=0;i<kernelSize;i++){
							try{
								window[i][tail] = p.setPixel(img.getRGB(x + i - 1, y + 1)).getIntensity();
							}
							catch(ArrayIndexOutOfBoundsException e){
								window[i][tail] = 0;
							}
						}
						for(int i=0;i<kernelSize;i++){
							for(int j=0;j<kernelSize;j++){
								result[x][y] += window[i][(j+head)%kernelSize] * hvKernel[i][j];
							}

						}
						tail++;head++;tail%=kernelSize;head%=kernelSize;
					}
					//
				}
			}
		}
		for(x=0;x<width;x++){
			for(y=0;y<height;y++){
				img.setRGB(x, y, p.setGrayscale((int)result[x][y]));
			}
		}
		return img;
	}
	
	public double gaussian(double x, double mu, double sigma){
		return Math.exp( -(((x-mu)/(sigma))*((x-mu)/(sigma)))/2d );
	}
}
