package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import mx.com.sportsworld.sw.model.ExercisePeriod;
import mx.com.sportsworld.sw.model.ExerciseWeek;
import mx.com.sportsworld.sw.model.WeekDayRelationship;

/**
 * The Class ExercisePeriodTranslator.
 */
public class ExercisePeriodTranslator {

    /**
	 * Translate.
	 * 
	 * @param array
	 *            the array
	 * @return the exercise period
	 */
    public ExercisePeriod translate(List<WeekDayRelationship> array) {

        final ExercisePeriod exerciseWeeks = new ExercisePeriod();
        final int relationshipCount = array.size();
        long lastWeekId = 0;
        for (int i = 0; i < relationshipCount; i++) {
            final WeekDayRelationship current = array.get(i);
            final long weekId = current.getWeekId();
            if (lastWeekId != weekId) {
                lastWeekId = weekId;
                exerciseWeeks.add(new ExerciseWeek(weekId, new ArrayList<ExerciseDay>()));
            }
            final ExerciseWeek currentExercise = exerciseWeeks.get(exerciseWeeks.size() - 1);
            if (!currentExercise.isActive()) {
                currentExercise.setActive(current.isActive());
            }
            currentExercise.getDayIds()
                    .add(new ExerciseDay(current.getDayId(), current.isActive()));
        }

        return exerciseWeeks;
        
    }

}
