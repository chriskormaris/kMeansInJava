package plot;

import java.util.Comparator;

import de.erichseifert.gral.data.DataSeries;

public class DataSeriesComparator implements Comparator<DataSeries> {
	
    @Override
    public int compare(DataSeries t1, DataSeries t2) {
    	return t1.getName().compareTo(t2.getName());
    }
    
}
