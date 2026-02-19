package com.lx862.jcm.mod.scripting.jcm.pids;

import com.lx862.jcm.mod.block.entity.PIDSBlockEntity;
import com.lx862.jcm.mod.util.JCMUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import cn.zbx1425.sowcer.math.Vector3f;
import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.data.ScheduleEntry;
import mtr.data.Station;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Set;

public class PIDSWrapper {
    private final PIDSBlockEntity be;
    private final ArrivalsWrapper arrivalsWrapper;
    public final String type;
    public final int rows;
    public final int width;
    public final int height;

    public PIDSWrapper(PIDSBlockEntity be, List<ScheduleEntry> arrivalsResponse, int width, int height) {
        this.be = be;
        this.type = be.getPIDSType();
        this.width = width;
        this.height = height;
        this.rows = be.getRowAmount();
        this.arrivalsWrapper = new ArrivalsWrapper(arrivalsResponse);
    }

    public Vector3f pos() {
        return JCMUtil.blockPosToVector3f(be.getBlockPos());
    }

    public BlockPos blockPos() {
        return be.getBlockPos();
    }

    public boolean isRowHidden(int i) {
        boolean[] rowHidden = be.getRowHidden();
        if(i >= rowHidden.length) {
            return false;
        } else {
            return rowHidden[i];
        }
    }

    public String getCustomMessage(int i) {
        String[] customMessages = be.getCustomMessages();
        return i >= customMessages.length ? null : customMessages[i];
    }

    public boolean isPlatformNumberHidden() {
        return be.platformNumberHidden();
    }

    public Station station() {
        return RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, be.getBlockPos());
    }

    public ArrivalsWrapper arrivals() {
        return arrivalsWrapper;
    }
}
