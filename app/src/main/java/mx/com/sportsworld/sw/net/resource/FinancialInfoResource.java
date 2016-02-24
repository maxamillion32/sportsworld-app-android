package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.com.sportsworld.sw.model.FinancialInfo;
import mx.com.sportsworld.sw.net.RequestMaker;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.parser.FinancialInfoParser;

/**
 * The Class FinancialInfoResource.
 */
public class FinancialInfoResource {
    
    /** The Constant UL_FINANCIAL_INFO. */
    public static final String UL_FINANCIAL_INFO = "http://download.finance.yahoo.com/d/quotes.csv?s=SPORTS.MX&f=sl1d1t1c1ohgv&e=.csv";

    /**
	 * Gets the sports world financial info.
	 * 
	 * @return the sports world financial info
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public static RequestResult<FinancialInfo> getSportsWorldFinancialInfo() throws IOException {

        List<FinancialInfo> data = null;

        final RequestMaker requestMaker = new RequestMaker();
        final Response response = requestMaker
                .get(UL_FINANCIAL_INFO, null /* header */, null /* keyValues */);
        final boolean success = (response.getResponseCode() / 100 == 2);

        if (success) {
            final String csvData = response.getBodyAsString();
            final FinancialInfoParser parser = new FinancialInfoParser();
            final FinancialInfo info = parser.parse(csvData);
            data = new ArrayList<FinancialInfo>(1);
            data.add(info);
        }

        final long currentTimeMillis = System.currentTimeMillis();
        final RequestResult<FinancialInfo> result = new RequestResult<FinancialInfo>(
                response.getResponseCode(), success, null /* message */,
                currentTimeMillis /* expires */, currentTimeMillis /* lastModified */, data);

        return result;

    }
}
