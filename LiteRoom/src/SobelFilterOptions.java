import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class SobelFilterOptions extends FilterOptionsPanel {
	private JCheckBox color;
	private JSpinner thresholdSpinner;
	
	
	public SobelFilterOptions(){
		color = new JCheckBox("Black");
		this.add(color);
		thresholdSpinner = new JSpinner(new SpinnerNumberModel(127,0,255,1));
		this.add(thresholdSpinner);
	}
	
	public boolean isBlack(){
		return color.isSelected();
	}
	
	public int getThreshold(){
		return (int)thresholdSpinner.getValue();
	}
}
