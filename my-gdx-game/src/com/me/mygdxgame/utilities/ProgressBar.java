package com.me.mygdxgame.utilities;

/**
 * Interface for things such as health bars.
 */
public interface ProgressBar extends Updatable {
    
    /**
     * Sets the value to be displayed by this progress bar, as a percentage.
     * Value should be in the range [0, 1]. No level of precision is guaranteed.
     * A segmented bar, for example, may not be able to depict values with the
     * same granularity as a smooth one.
     * 
     * @param value
     *            A value in the range [0, 1] to display.
     */
    public void setValue(float value);
    
    /**
     * Retrieves the value currently being displayed by this bar.
     * 
     * @return a float in the range [0, 1] indicating the current percentage
     *         displayed.
     */
    public float getValue();
}
