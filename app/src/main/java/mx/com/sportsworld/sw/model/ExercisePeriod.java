package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import mx.com.sportsworld.sw.parser.ExerciseDay;

/**
 * The Class ExercisePeriod.
 */
public class ExercisePeriod extends ArrayList<ExerciseWeek> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Gets the active week position.
	 * 
	 * @return the active week position
	 */
    public int getActiveWeekPosition() {
        final int count = size();
        for (int i = 0; i < count; i++) {
            if (get(i).isActive()) {
                return i;
            }
        }
        return -1;
    }
    
    /**
	 * Gets the active day position.
	 * 
	 * @return the active day position
	 */
    public int getActiveDayPosition() {
        final int activeWeekPos = getActiveWeekPosition();
        if (activeWeekPos == -1) {
            return -1;
        }
        final List<ExerciseDay> exerciseDays = get(activeWeekPos).getDayIds();
        final int count = exerciseDays.size();
        for (int i = 0; i < count; i++) {
            if(exerciseDays.get(i).isActive()) {
                return i;
            }
        }
        return -1;
    }

}
