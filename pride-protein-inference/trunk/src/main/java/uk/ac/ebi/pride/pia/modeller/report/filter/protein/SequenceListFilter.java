package uk.ac.ebi.pride.pia.modeller.report.filter.protein;

import uk.ac.ebi.pride.pia.modeller.peptide.ReportPeptide;
import uk.ac.ebi.pride.pia.modeller.protein.ReportProtein;
import uk.ac.ebi.pride.pia.modeller.report.filter.AbstractFilter;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterComparator;
import uk.ac.ebi.pride.pia.modeller.report.filter.FilterType;
import uk.ac.ebi.pride.pia.modeller.report.filter.RegisteredFilters;

import java.util.ArrayList;
import java.util.List;

public class SequenceListFilter extends AbstractFilter {
	
	protected static final String shortName = RegisteredFilters.SEQUENCE_LIST_FILTER.getShortName();
	
	protected static final String name = "Sequence List Filter";
	
	protected static final String filteringName = "Sequence List";
	
	public static final FilterType filterType = FilterType.literal_list;
	
	protected static final Class<String> valueInstanceClass = String.class;
	
	private String value;
	
	
	
	public SequenceListFilter(FilterComparator arg, String value, boolean negate) {
		this.comparator = arg;
		this.value = value;
		this.negate = negate;
	}
	
	
	@Override
	public String getShortName() {
		return shortName;
	}
	
	
	public static String shortName() {
		return shortName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	
	public static String name() {
		return name;
	}
	
	@Override
	public String getFilteringName() {
		return filteringName;
	}
	
	
	public static String filteringName() {
		return filteringName;
	}
	
	@Override
	public Object getFilterValue() {
		return value;
	}
	
	@Override
	public FilterType getFilterType() {
		return filterType;
	}
	
	
	public static boolean isCorrectValueInstance(Object value) {
		return valueInstanceClass.isInstance(value);
	}
	
	@Override
	public Object getObjectsValue(Object o) {
		if (o instanceof ReportProtein) {
			return ((ReportProtein) o).getPeptides();
		} else {
			// nothing supported
			return null;
		}
	}
	
	@Override
	public boolean valueNeedsFileRefinement() {
		return true;
	}
	
	@Override
	public Object doFileRefinement(Long fileID, Object o) {
		List<String> strList = new ArrayList<String>();
		
		if (o instanceof List<?>) {
			for (Object obj : (List<?>)o) {
				if (obj instanceof ReportPeptide) {
					strList.add(((ReportPeptide) obj).getSequence());
				} else if (obj instanceof String) {
					strList.add((String)obj);
				}
			}
		}
		
		return strList;
	}
	
	@Override
	public boolean supportsClass(Object c) {
		if (c instanceof ReportProtein) {
			return true;
		} else {
			return false;
		}
	}
}