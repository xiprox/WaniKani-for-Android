package tr.xip.wanikani;

import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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
 * A workaround class to fix CPU leaks when doing long review sessions.
 * Description of the problem: apparently some releases of Android are affected
 * by a bug in the WebView class. Each HTML5 audio tag creates a timer class
 * which is never released, even if the user navigates away from the page, or
 * the tag is removed from the HTML page. This is true, I believe, for each
 * release since introduction of support for HTML5 audio.
 * This is somewhat tolerable, as long as those timers contain no tasks (yes, we
 * have a number of sleeping threads -- one for each audio tag ever encountered).
 * Older Android versions, however, have an additional bug: if the audio tag
 * is never "played", those threads have a recurring task which is scheduled
 * four times per second. This is especially problematic for WK review sessions:
 * each page containing media (i.e. vocab readings, when available) contains
 * two audio tags. One is played if the user touches the button, or if autoplay
 * is enabled; the other is not used. As a result, if autoplay is disabled, each
 * vocab item introduces two threads, and the app quickly becomes unusable.
 * This class looks for all those timers, and kills them: it has no effect on
 * newer releases, since only timers containing a task are killed.
 */
public class TimerThreadsReaper {

    /**
     * This class wraps an audio tag. We need this since the Android class is not
     * public. The class implements a strict ordering for threads: newer threads
     * are "greater" than older ones.
     */
    private static class AudioTagWrapper implements Comparable<AudioTagWrapper>  {

        /// The wrapped object
        Object tag;

        /// The timer thread number
        int tcount;

        /**
         * Constructor
         * @param tcount the timer thread number
         * @param tag the wrapped object
         */
        public AudioTagWrapper (int tcount, Object tag)
        {
            this.tcount = tcount;
            this.tag = tag;
        }

        /**
         * Attempts to stop the timer, by calling {@link Timer#cancel()}.
         * @return <tt>true</tt> if successful
         */
        private boolean stopTimer ()
        {
            Timer timer;

            try {
                timer = (Timer) TimerThreadsReaper.getField (tag, "mTimer", Timer.class);
                if (timer == null)
                    return false;

                timer.cancel ();

                return true;
            } catch (Throwable t) {
                return false;
            }
        }

        /**
         * Attempts to stop the timer, by calling the audio tag class <tt>resetMediaPlayer</tt> method.
         * @return <tt>true</tt> if successful
         */
        private boolean resetMediaPlayer ()
        {
            Method method;

            try {
                method = tag.getClass().getDeclaredMethod ("resetMediaPlayer", new Class [0]);
                method.setAccessible (true);
                method.invoke (tag, new Object [0]);
            } catch (Throwable t) {
                return false;
            }

            return true;
        }

        /**
         * Tries to stop the timer
         * @return <tt>true</tt> if successful
         */
        public boolean release ()
        {
            return stopTimer () || resetMediaPlayer ();
        }


        @Override
        public boolean equals (Object w)
        {
            return tag == ((AudioTagWrapper) w).tag;
        }

        @Override
        public int hashCode ()
        {
            return tag.hashCode();
        }

        @Override
        public int compareTo (AudioTagWrapper w)
        {
            return tcount - w.tcount;
        }
    }

    /**
     * An optional interface that may be implemented by classes wishing to know how many
     * threads have been reaped.
     */
    public interface ReaperTaskListener {

        /**
         * Called to update the stats
         * @param count number of reaped timers at this instance
         * @param total number of reaped timers so far
         */
        public void reaped (int count, int total);

    }

    /**
     * A periodic task that takes care of reaping all the timers.
     * A configurable number of the latest timers are spared, to make sure that
     * we don't accidentally destroy a tag being currently played.
     */
    public class ReaperTask implements Runnable {

        /// The Android OS handler
        Handler handler;

        /// Number of timers to spare
        int grace;

        /// Number of milliseconds between two runs
        long period;

        /// True if active
        boolean active;

        /// Optional listener
        ReaperTaskListener listener;

        /// Total number of timers reaped so far
        int total;

        /**
         * Constrcutor
         * @param handler OS handler
         * @param grace number of timers to spare
         * @param period interval between two runs
         */
        private ReaperTask (Handler handler, int grace, long period)
        {
            this.handler = handler;
            this.grace = grace;
            this.period = period;
        }

        /**
         * Sets an optional listener
         * @param listener the listener
         */
        public void setListener (ReaperTaskListener listener)
        {
            this.listener = listener;
        }

        /**
         * Called when the task should be started
         */
        public void resume ()
        {
            active = true;
            run ();
        }

        /**
         * Called when the task should be suspended
         */
        public void pause ()
        {
            active = false;
        }

        /**
         * Kills the timers.
         */
        public void run ()
        {
            int count;

            if (!active)
                return;

            count = killDelta (grace);
            total += count;
            if (listener != null)
                listener.reaped (count, total);

            handler.postDelayed (this, period);
        }
    }

    /// A list of all the reaped tags. We keep it to avoid killing them twice
    List<AudioTagWrapper> reaped;

    /**
     * Constrcutor.
     */
    public TimerThreadsReaper ()
    {
        reaped = new Vector<AudioTagWrapper> ();
    }

    /**
     * Creates a periodic task that reapes the timers. This step is optional
     * @param handler the OS handler
     * @param grace the number of timers to spare
     * @param period the interval between two runs
     * @return the task being created. It is initially stopped
     */
    public ReaperTask createTask (Handler handler, int grace, long period)
    {
        return new ReaperTask (handler, grace, period);
    }

    /**
     * Kills all the timers it can.
     * @return number of reaped timers
     */
    public int stopAll ()
    {
        return killDelta (0);
    }

    /**
     * Kills all the timers minus a configurable number of the most recent ones.
     * @param grace the number of timers to spare
     * @return the number of reaped timers
     */
    public int killDelta (int grace)
    {
        List<AudioTagWrapper> current;
        AudioTagWrapper w;
        int count, i;

        current = snapshot ();

        count = 0;
        for (i = 0; i < current.size () - grace; i++) {
            w = current.get (i);
            if (!reaped.contains (w) && current.get (i).release ())
                count++;
            reaped.add (w);
        }

        return count;
    }

    /**
     * Returns a list of all the active tags (having an active timer).
     * @return a list of tag wrappers
     */
    private List<AudioTagWrapper> snapshot ()
    {
        List<AudioTagWrapper> ans;
        ThreadGroup group;
        Thread thread [];
        int i, len;

        ans = new Vector<AudioTagWrapper> ();

        group = Thread.currentThread ().getThreadGroup ();
        thread = new Thread [group.activeCount()];
        while (group.enumerate (thread, true) == thread.length)
            thread = new Thread [thread.length * 2];
        len = group.enumerate (thread, true);
        for (i = 0; i < len; i++) {
            addWrapper (ans, thread [i]);
        }

        Collections.sort (ans);

        return ans;
    }

    /**
     * Given a thread, it checks to see if this is a timer thread created by an audio tag.
     * If so, it wraps it, and adds it to the list of wrappers.
     * @param wrappers the list of wrappers
     * @param thread a thread
     */
    private void addWrapper (List<AudioTagWrapper> wrappers, Thread thread)
    {
        TimerTask tasks [];
        Object obj, task;
        Runnable r;
        String tname;
        int i, size, tcount;

        tname = thread.getName ();
        if (!tname.startsWith ("Timer-"))
            return;

        try {
            tcount = Integer.parseInt (tname.substring (6));
        } catch (NumberFormatException e) {
            return;
        }

        r = (Runnable) getField (thread, "target", Runnable.class);
        if (r == null)
            r = thread;

        obj = getField (r, "tasks", Object.class);
        if (obj == null)
            return;

        tasks = (TimerTask []) getField (obj, "timers", TimerTask [].class);
        if (tasks == null)
            return;

        obj = getField (obj, "size", Integer.class);
        if (obj == null)
            return;

        size = (Integer) obj;

        for (i = 0; i < size; i++) {
            try {
				/* We must handle tasks with care since it can change while we are running */
                task = tasks [i];
                if (task == null)
                    continue;
            } catch (Throwable t) {
                continue;
            }

            if (task.getClass ().getCanonicalName ()
                    .equals ("android.webkit.HTML5Audio.TimeupdateTask")) {
                obj = getField (task, "this$0", Object.class);
                if (obj != null)
                    wrappers.add (new AudioTagWrapper (tcount, obj));
            }
        }
    }

    /**
     * Convenience method that returns a (possible private) field of an object.
     * @param parent the object
     * @param name the field name
     * @param clazz the expected class name
     * @return the field, or <tt>null</tt> if the fields does not exist or does
     * not match the expected class
     */
    private static Object getField (Object parent, String name, Class clazz)
    {
        Class pclass;
        Field field;
        Object obj;

        pclass = parent.getClass();
        try {
            field = pclass.getDeclaredField (name);
            if (field == null)
                return null;

            field.setAccessible (true);

            obj = field.get (parent);
            if (obj == null)
                return null;

            if (!clazz.isInstance (obj))
                return null;

            return obj;

        } catch (Throwable t) {
            return null;
        }
    }

}