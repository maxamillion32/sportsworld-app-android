package mx.com.sportsworld.sw.model;

/**
 * The Class FinancialInfo.
 * 
 * @author Jose Torres Fuentes Ironbit
 */
public class FinancialInfo {
   
    /** The m company name. */
    final String mCompanyName;
    
    /** The m last trade. */
    final double mLastTrade;
    
    /** The m change. */
    final double mChange;
    
    /** The m result. */
    final double mResult;
    
    /**
	 * Instantiates a new financial info.
	 * 
	 * @param companyName
	 *            the company name
	 * @param lastTrade
	 *            the last trade
	 * @param change
	 *            the change
	 */
    public FinancialInfo(String companyName, double lastTrade, double change) {
        mCompanyName = companyName;
        mLastTrade = lastTrade;
        mChange = change;
        mResult = (change / (lastTrade + change)) * 100; 
    }

    /**
	 * Gets the company name.
	 * 
	 * @return the company name
	 */
    public String getCompanyName() {
        return mCompanyName;
    }
    
    /**
	 * Gets the last trade.
	 * 
	 * @return the last trade
	 */
    public double getLastTrade() {
        return mLastTrade;
    }

    /**
	 * Gets the change.
	 * 
	 * @return the change
	 */
    public double getChange() {
        return mChange;
    }

    /**
	 * Gets the result.
	 * 
	 * @return the result
	 */
    public double getResult() {
        return mResult;
    }
    
}
