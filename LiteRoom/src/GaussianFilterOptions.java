import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class GaussianFilterOptions extends FilterOptionsPanel {
	private JSpinner kernelSizeSpinner;
	
	public GaussianFilterOptions(){
		super();
		kernelSizeSpinner = new JSpinner(new SpinnerNumberModel(3,3,99,2));
		this.add(kernelSizeSpinner);
	}
	
	public int getKernelSize(){
		return (int)kernelSizeSpinner.getValue();
	}
}
