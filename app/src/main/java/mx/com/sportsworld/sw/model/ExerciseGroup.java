package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import mx.com.sportsworld.sw.model.Exercise.OnDoneChangedListener;

/**
 * The Class ExerciseGroup.
 */
public class ExerciseGroup implements OnDoneChangedListener {

    /** The m name. */
    final String mName;
    
    /** The m exercises. */
    final List<Exercise> mExercises;
    
    /** The m done. */
    boolean mDone;

    /**
	 * Instantiates a new exercise group.
	 * 
	 * @param exercises
	 *            the exercises
	 */
    public ExerciseGroup(List<Exercise> exercises) {

        mName = exercises.get(0).getMuscleWorked();
        mExercises = exercises;

        checkIfDone();

        final int count = mExercises.size();
        for (int i = 0; i < count; i++) {
            mExercises.get(i).setOnDoneChangedlistener(this);
        }

    }

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return mName;
    }

    /**
	 * Gets the exercises.
	 * 
	 * @return the exercises
	 */
    public List<Exercise> getExercises() {
        return mExercises;
    }

    /**
	 * Checks if is done.
	 * 
	 * @return true, if is done
	 */
    public boolean isDone() {
        return mDone;
    }

    /**
	 * Check if done.
	 * 
	 * @return true, if successful
	 */
    private boolean checkIfDone() {
        boolean done = true;
        final int count = mExercises.size();
        for (int i = 0; i < count; i++) {
            if (mExercises.get(i).isDone() == false) {
                done = false;
                break;
            }
        }
        mDone = done;
        return done;
    }

    /**
	 * Sets the done.
	 * 
	 * @param done
	 *            the new done
	 */
    public void setDone(boolean done) {
        final int count = mExercises.size();
        for (int i = 0; i < count; i++) {
            mExercises.get(i).setDone(done);
        }
    }

    /**
	 * Checks if is circuit.
	 * 
	 * @return true, if is circuit
	 */
    public boolean isCircuit() {
        return (mExercises.size() > 0 && (mExercises.get(0).getCircuitName() != null));
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.model.Exercise.OnDoneChangedListener#onDoneChanged(com.sportsworld.android.model.Exercise, boolean)
     */
    @Override
    public void onDoneChanged(Exercise exercise, boolean newValue) {
        checkIfDone();
    }

}
