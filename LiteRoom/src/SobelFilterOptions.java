import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class SobelFilterOptions extends FilterOptionsPanel {
	private JCheckBox color;
	private JSpinner thresholdSpinner;
	private JSpinner multiplierSpinner;
	
	
	public SobelFilterOptions(){
		super();
		color = new JCheckBox("Black");
		this.add(color);
		thresholdSpinner = new JSpinner(new SpinnerNumberModel(0,0,255,1));
		multiplierSpinner = new JSpinner(new SpinnerNumberModel(0,0,255,1));
		this.add(thresholdSpinner);
		this.add(multiplierSpinner);
	}
	
	public boolean isBlack(){
		return color.isSelected();
	}
	
	public int getThreshold(){
		return (int)thresholdSpinner.getValue();
	}
	
	public int getMultiplier(){
		return (int)multiplierSpinner.getValue();
	}
}
