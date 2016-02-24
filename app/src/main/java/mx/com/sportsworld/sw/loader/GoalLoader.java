package mx.com.sportsworld.sw.loader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

// TODO: Auto-generated Javadoc

/**
 * The Class GoalLoader.
 */
public class GoalLoader extends AsyncTaskLoader<String[]> {

    /**
     * Instantiates a new goal loader.
     *
     * @param context the context
     */
    public GoalLoader(Context context) {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
     */
    @Override
    public String[] loadInBackground() {
        // TODO Auto-generated method stub
        return null;
    }

}
