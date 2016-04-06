import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class HistogramPanel extends JPanel {
	private Histogram _histogram;
	
	private JButton equalize;
	private JSlider histogramScalar;
	private JPanel histogramEq;

	public HistogramPanel(LiteRoom app, Histogram histogram){
		_histogram = histogram;
		this.setLayout(new GridLayout(2,1));
		this.setFocusable(false);
		
		histogramEq = new JPanel();
		histogramEq.setLayout(new GridLayout(2,1));
		
		histogramScalar = new JSlider();
		histogramScalar.setMaximum(100);
		histogramScalar.setMinimum(0);
		
		equalize = new JButton("EQUALIZE");
		equalize.addActionListener(new EqualizeHistogramButtonHandler(app,histogramScalar));
		
		histogramEq.add(equalize);
		histogramEq.add(histogramScalar);
		this.add(_histogram);
		this.add(histogramEq);
	}
	
	public int[] calculateHistogram(int choice){
		return _histogram.calculateHistogram(choice);
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
}
