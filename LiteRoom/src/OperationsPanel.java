import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OperationsPanel extends JPanel{
	private LiteRoom _app;
	
	//history changing operations
	private JButton reload;
	private JButton undo;
	private JPanel historyOps;
	
	private JButton equalize;
	private JButton filter;
	//add this + equalize button to own panel
	private JPanel histogramEq;
	private JSlider histogramScalar;
	
	private JPanel filterPanel;
	private JComboBox filterSelect;
	private String[] filters = { "Sobel" };
	
	public OperationsPanel(LiteRoom app){
		this.setLayout(new GridLayout(8,1));
		
		//Should create new class to encapsulate this panel?
		reload = new JButton("RELOAD");
		reload.addActionListener(new ReloadImageButtonHandler(app));
		undo = new JButton("UNDO");
		undo.addActionListener(new UndoOperationButtonHandler(app));
		
		historyOps = new JPanel();
		historyOps.setLayout(new GridLayout(2,1));
		historyOps.add(reload);
		historyOps.add(undo);
		
		//should do that for this one too
		histogramEq = new JPanel();
		histogramEq.setLayout(new GridLayout(2,1));
		
		histogramScalar = new JSlider();
		histogramScalar.addChangeListener(new HistogramEqualizeSliderHandler());
		histogramScalar.setMaximum(100);
		histogramScalar.setMinimum(0);
		
		equalize = new JButton("EQUALIZE");
		equalize.addActionListener(new EqualizeHistogramButtonHandler(app,histogramScalar));
		histogramEq.add(equalize);
		histogramEq.add(histogramScalar);
		
		
		this.add(historyOps);
		
		//and this one...
		filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(2,1));
		
		filterSelect = new JComboBox(filters);
		filterSelect.setSelectedIndex(0);
		filter = new JButton("FILTER");
		filter.addActionListener(new ApplyFilterButtonHandler(app,filterSelect));
		
		filterPanel.add(filter);
		filterPanel.add(filterSelect);

	}
	
	//this is sloppy, refactor, probably put in its own class
	public JPanel getHgPanel(){
		return histogramEq;
	}
	public void addFilter(){
		this.add(filterPanel);
	}
	
	private class HistogramEqualizeSliderHandler implements ChangeListener{
		public HistogramEqualizeSliderHandler(){
		}
		public void stateChanged(ChangeEvent arg0) {}
	}
	
	
	private class ReloadImageButtonHandler implements ActionListener{
		private LiteRoom _app;
		public ReloadImageButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.reloadImage();
		}
	}
	
	private class UndoOperationButtonHandler implements ActionListener{
		private LiteRoom _app;
		public UndoOperationButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.undoOperation();
		}
	}
	
	private class EqualizeHistogramButtonHandler implements ActionListener{
		private LiteRoom _app;
		private JSlider _slider;
		public EqualizeHistogramButtonHandler(LiteRoom app,JSlider slider){
			_app = app;		
			_slider = slider;
		}
		public void actionPerformed(ActionEvent e){
			_app.histogramEqualize((float)_slider.getValue()/100f);
		}
	}
	
	private class ApplyFilterButtonHandler implements ActionListener{
		private LiteRoom _app;
		private JComboBox _selection;
		
		public ApplyFilterButtonHandler(LiteRoom app, JComboBox cbox){
			_app = app;
			_selection = cbox;
		}
		public void actionPerformed(ActionEvent e){
			Filter f = new Filter();
			switch((String)_selection.getSelectedItem()){
				case "Sobel": f = new SobelFilter(); 
			}
			_app.applyFilter(f);
		}
	}
}
