package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;

import mx.com.sportsworld.sw.model.FinancialInfo;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class FinancialInfoParser.
 */
public class FinancialInfoParser implements CsvParser<FinancialInfo>,
        ContentValuesParser<FinancialInfo> {

    /** The Constant SEPARATOR. */
    private static final String SEPARATOR = ",";
    
    /** The Constant COMPANY_NAME. */
    private static final int COMPANY_NAME = 0;
    
    /** The Constant LAST_TRADE. */
    private static final int LAST_TRADE = 1;
    
    /** The Constant CHANGE. */
    private static final int CHANGE = 4;

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.CsvParser#parse(java.lang.String)
     */
    @Override
    public FinancialInfo parse(String csv) {
        final String[] data = csv.split(SEPARATOR);
        final String companyName = data[COMPANY_NAME];
        final double lastTrade = Double.parseDouble(data[LAST_TRADE]);
        final double change = Double.parseDouble(data[CHANGE]);
        return new FinancialInfo(companyName, lastTrade, change);
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
     */
    @Override
    public ContentValues parse(FinancialInfo object, ContentValues values) {

        if (values == null) {
            values = new ContentValues();
        }

        values.put(SportsWorldContract.FinancialInfo.COMPANY_NAME, object.getCompanyName());
        values.put(SportsWorldContract.FinancialInfo.LAST_TRADE, object.getLastTrade());
        values.put(SportsWorldContract.FinancialInfo.CHANGE, object.getChange());
        values.put(SportsWorldContract.FinancialInfo.RESULT, object.getResult());

        return values;
        
    }

}
