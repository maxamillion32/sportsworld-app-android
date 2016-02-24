package mx.com.sportsworld.sw.loader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.io.IOException;
import java.util.List;

import mx.com.sportsworld.sw.model.FinancialInfo;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.FinancialInfoResource;
import mx.com.sportsworld.sw.parser.FinancialInfoParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class FinancialInfoListLoader.
 */
public class FinancialInfoListLoader extends CursorLoader {

    /**
     * Instantiates a new financial info list loader.
     *
     * @param context the context
     * @param projection the projection
     * @param sortOrder the sort order
     */
    public FinancialInfoListLoader(Context context, String[] projection, String sortOrder) {
        super(context, SportsWorldContract.FinancialInfo.CONTENT_URI, projection,
                null/* selection */, null/* selectionArgs */, sortOrder);
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.CursorLoader#loadInBackground()
     */
    @Override
    public Cursor loadInBackground() {
        final RequestResult<FinancialInfo> result;
        try {
            
            result = FinancialInfoResource.getSportsWorldFinancialInfo();

            if (!result.isSuccesful()) {
                return null;
            }

            final List<FinancialInfo> financialInfo = result.getData();

            if (financialInfo == null || financialInfo.size() == 0) {
                return null;
            }

            final ContentResolver cr = getContext().getContentResolver();
            final BatchOperation batchOperation = new BatchOperation(
                    SportsWorldContract.CONTENT_AUTHORITY, cr);

            final ContentProviderOperation deleteOperation = ContentProviderOperation.newDelete(
                    SportsWorldContract.FinancialInfo.CONTENT_URI).build();
            batchOperation.add(deleteOperation);

            final ContentValues values = new ContentValues();
            final FinancialInfoParser parser = new FinancialInfoParser();
            final int financialInfoCount = financialInfo.size();
            for (int i = 0; i < financialInfoCount; i++) {
                final FinancialInfo item = financialInfo.get(i);
                parser.parse(item, values /* contentValue */);
                final ContentProviderOperation insertOperation = ContentProviderOperation
                        .newInsert(SportsWorldContract.FinancialInfo.CONTENT_URI)
                        .withYieldAllowed(true).withValues(values).build();
                batchOperation.add(insertOperation);
                values.clear();
            }

            batchOperation.execute();

        } catch (IOException e) {
            return null;
        }

        return super.loadInBackground();
    }

}
