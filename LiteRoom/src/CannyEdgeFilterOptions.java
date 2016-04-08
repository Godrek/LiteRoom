import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class CannyEdgeFilterOptions extends FilterOptionsPanel {
	private JSpinner lowThresholdSpinner;
	private JSpinner highThresholdSpinner;
	
	
	public CannyEdgeFilterOptions(){
		super();
		lowThresholdSpinner = new JSpinner(new SpinnerNumberModel(1,0,1020,1));
		highThresholdSpinner = new JSpinner(new SpinnerNumberModel(127,0,1020,1));
		this.add(lowThresholdSpinner);
		this.add(highThresholdSpinner);
	}

	public int getLowThreshold(){
		return (int)lowThresholdSpinner.getValue();
	}
	
	public int getHighThreshold(){
		return (int)highThresholdSpinner.getValue();
	}
}
