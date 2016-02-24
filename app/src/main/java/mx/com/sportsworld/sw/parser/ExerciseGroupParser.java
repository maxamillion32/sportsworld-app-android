package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.model.ExerciseGroup;

/**
 * The Class ExerciseGroupParser.
 */
public class ExerciseGroupParser implements CursorParser<ExerciseGroup> {

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.CursorParser#parse(android.database.Cursor)
	 */
	@Override
	public List<ExerciseGroup> parse(Cursor cursor) {

		final List<Exercise> exercises = new ExerciseParser().parse(cursor);
		
		
		final int exerciseCount = exercises.size();

		final String[] allMuscles = new String[exerciseCount];
		final List<String> muscles = new ArrayList<String>();
		for (int i = 0; i < exerciseCount; i++) {

			allMuscles[i] = exercises.get(i).getMuscleWorked();
			boolean isadded = false;
			int b = 0;
			for (int a = 0; a < exerciseCount; a++) {
				if (allMuscles[i].equals(exercises.get(a).getMuscleWorked())) {
					isadded = true;
					b++;
				}
				else
					isadded=false;
				if (isadded && b==1){
							if(!muscles.contains(allMuscles[i]))
					muscles.add(allMuscles[i]);
				}
			}
		}
		
		final List<ExerciseGroup> exerciseGroups = new ArrayList<ExerciseGroup>();
		final int muscleCount = muscles.size();

		for (int i = 0; i < muscleCount; i++) {

			final String muscle = muscles.get(i);
			final List<Exercise> muscleExercises = new ArrayList<Exercise>();

			for (int j = 0; j < exerciseCount; j++) {

				final Exercise exercise = exercises.get(j);
				final String exerciseMuscle = exercise.getMuscleWorked();

				if (exerciseMuscle.equals(muscle)) {
					muscleExercises.add(exercise);
				}

			}

			exerciseGroups.add(new ExerciseGroup(muscleExercises));

		}

		return exerciseGroups;

	}

}
