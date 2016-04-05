import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel {
	private LiteRoom _app;
	
	private JButton filter;
	private JComboBox<String> filterSelect;
	private JPanel filterSubPanel;
	private FilterOptionsPanel options;
	private String[] filters = { "Sobel" , "Grayscale Gaussian"};
	
	public FilterPanel(LiteRoom app){
		this.setLayout(new GridLayout(2,1));
		_app = app;
		
		filterSubPanel = new JPanel();
		filterSubPanel.setLayout(new GridLayout(2,1));
		
		filter = new JButton("FILTER");
		filter.addActionListener(new ApplyFilterButtonHandler(_app));
		
		filterSelect = new JComboBox<String>(filters);
		filterSelect.addItemListener(new FilterSelectChangeListener());
		filterSelect.setSelectedIndex(1);
		setFilterOptions(getSelection());
		
		filterSubPanel.add(filter);
		filterSubPanel.add(filterSelect);
		this.add(filterSubPanel);
		this.add(options);
	}
	
	public void setFilterOptions(String choice){
		if(options != null)
			this.remove(options);
		switch(choice){
			case "Grayscale Gaussian": options = new GaussianFilterOptions();break;
			case "Sobel": options = new SobelFilterOptions();break;
		}
		this.add(options);
		this.revalidate();
		_app.repaint();
	}
	
	public String getSelection(){
		return (String)filterSelect.getSelectedItem();
	}
	
	private class ApplyFilterButtonHandler implements ActionListener{
		private LiteRoom _app;
		
		public ApplyFilterButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			Filter f = new Filter();
			String choice = getSelection();
			switch(choice){
				case "Sobel": f = new SobelFilter((SobelFilterOptions)options);break;
				case "Grayscale Gaussian": f = new GaussianFilter((GaussianFilterOptions)options);break;
			}
			_app.applyFilter(f);
		}
	}
	

	private class FilterSelectChangeListener implements ItemListener{
		
	    public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          setFilterOptions((String)item);
	       }
	    }       
	}
	
}
