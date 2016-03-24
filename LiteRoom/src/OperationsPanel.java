import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OperationsPanel extends JPanel{
	private LiteRoom _app;
	
	private JButton reload;
	private JButton equalize;
	private JButton filter;
	//add this + equalize button to own panel
	private JPanel histogramEq;
	private JSlider histogramScalar;
	
	public OperationsPanel(LiteRoom app){
		this.setLayout(new GridLayout(8,1));
		
		//Reload button doesn't need its own class because it is added
		reload = new JButton("RELOAD");
		reload.addActionListener(new ReloadImageButtonHandler(app));
		
		histogramEq = new JPanel();
		histogramEq.setLayout(new GridLayout(2,1));
		
		histogramScalar = new JSlider();
		histogramScalar.addChangeListener(new HistogramEqualizeSliderHandler(app,histogramScalar));
		histogramScalar.setMaximum(100);
		histogramScalar.setMinimum(0);
		
		equalize = new JButton("EQUALIZE");
		equalize.addActionListener(new EqualizeHistogramButtonHandler(app,histogramScalar));
		histogramEq.add(equalize);
		histogramEq.add(histogramScalar);
		this.add(reload);
		filter = new JButton("FILTER");
		filter.addActionListener(new ApplyFilterButtonHandler(app));
	}
	
	//this is sloppy, refactor, probably put in its own class
	public JPanel getHgPanel(){
		return histogramEq;
	}
	public void addFilter(){
		this.add(filter);
	}
	
	private class HistogramEqualizeSliderHandler implements ChangeListener{
		private LiteRoom _app;
		private JSlider _slider;
		public HistogramEqualizeSliderHandler(LiteRoom app, JSlider slider){
			_app = app;
			_slider = slider;
		}
		public void stateChanged(ChangeEvent arg0) {
			_app.reloadImage();
			_app.histogramEqualize((float)_slider.getValue()/100f);
		}
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
		public ApplyFilterButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.applyFilter();
		}
	}
}
