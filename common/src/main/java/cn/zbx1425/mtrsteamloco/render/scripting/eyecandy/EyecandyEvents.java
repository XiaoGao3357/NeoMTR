package cn.zbx1425.mtrsteamloco.render.scripting.eyecandy;

import com.lx862.mtrscripting.util.StateTracker;

public class EyecandyEvents {
    public final StateTracker onBlockUse = new StateTracker();

    public void triggerOnBlockUse() {
        synchronized (onBlockUse) {
            onBlockUse.setState(true);
        }
    }

    public void accept() {
        synchronized (onBlockUse) {
            onBlockUse.setState(false);
        }
    }
}
