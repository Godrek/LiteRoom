import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class SobelFilterOptions extends FilterOptionsPanel {
	private LiteRoom _app;
	private JCheckBox color;
	
	public SobelFilterOptions(LiteRoom app){
		_app = app;
		color = new JCheckBox("Black");
		this.add(color);
	}
	
	public boolean isBlack(){
		return color.isSelected();
	}
}
