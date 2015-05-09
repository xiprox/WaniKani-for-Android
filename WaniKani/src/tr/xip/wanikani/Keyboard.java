package tr.xip.wanikani;

/*
 *  Copyright (c) 2013 Alberto Cuda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Implementation of this class correspond to entries of the "Reviews/Lessons kbd" menu entry.
 * Each one provides a different way to insert readings/meaning into the reviews and lesson quizzes.
 * Note that instances of this class are instantiated by {@link tr.xip.wanikani.app.activity.WebReviewActivity} only once, and
 * they are switched by calling {@link #hide()} and {@link #show(boolean)}.
 */
public interface Keyboard {

    /**
     * Called when a new URL is reloaded. This is the proper place to hide the keyboard when
     * exiting a review/lesson sessions
     */
    public void reset ();

    /**
     * Show the keyboard
     * @param hasEnter set if the enter key should be shown. May not make sense
     * on all the implementations
     */
    public void show (boolean hasEnter);

    /**
     * Iconizes the keyboard, leaving the possibility to maximize it again.
     * @param hasEnter set if the enter key should be shown. May not make sense
     * on all the implementations
     */
    public void iconize (boolean hasEnter);

    /**
     * Hides the keyboard
     */
    public void hide ();

    /**
     * Called when the user presses the ignore answer button.
     */
    public void ignore ();

    /**
     * Tells if the ignore answer button should be shown. Usually this can be
     * done only if the answer is wrong and the keyboard supports it.
     * @return true if it can be shown
     */
    public boolean canIgnore ();

    /**
     * Tells if fonts can be overridden
     * @return true if they can
     */
    public boolean canOverrideFonts ();

    /**
     * Toggle font overriding
     */
    public void overrideFonts ();

    /**
     * Get font overriding state
     * @return true if fonts are overridden
     */
    public boolean getOverrideFonts ();

    /**
     * Mute/unmute
     * @param m true if should be muted
     */
    public void setMute (boolean m);
}