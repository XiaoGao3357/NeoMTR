package com.lx862.mtrscripting.util;

import cn.zbx1425.mtrsteamloco.render.RenderUtil;
import com.lx862.mtrscripting.core.ScriptInstance;

/* From https://github.com/zbx1425/mtr-nte/blob/master/common/src/main/java/cn/zbx1425/mtrsteamloco/render/scripting/util/TimingUtil.java */

@SuppressWarnings("unused")
public class TimingUtil {
    private double timeElapsedForScript = 0;
    private double frameDeltaForScript = 0;

    public void prepareForScript(ScriptInstance<?> scriptContext) {
        timeElapsedForScript = RenderUtil.runningSeconds;
        frameDeltaForScript = timeElapsedForScript - scriptContext.lastExecuteTime;
        scriptContext.lastExecuteTime = timeElapsedForScript;
    }

    public static double globalElapsed() {
        return RenderUtil.runningSeconds;
    }

    public double elapsed() {
        return timeElapsedForScript;
    }

    public double delta() {
        return frameDeltaForScript;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long nanoTime() {
        return System.nanoTime();
    }
}
