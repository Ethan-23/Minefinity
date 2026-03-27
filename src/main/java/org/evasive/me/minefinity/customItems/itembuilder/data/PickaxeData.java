package org.evasive.me.minefinity.customItems.itembuilder.data;

import org.evasive.me.minefinity.customItems.itembuilder.data.base.BasePickaxeComponent;

import java.util.ArrayList;
import java.util.List;

public class PickaxeData {

    private final BasePickaxeComponent head;
    private final BasePickaxeComponent core;
    private final BasePickaxeComponent handle;

    public PickaxeData(BasePickaxeComponent head, BasePickaxeComponent core, BasePickaxeComponent handle) {
        if (head != null && head.getCustomItemType() != CustomItemType.PICKAXE_HEAD) {
            throw new IllegalStateException("Invalid head component: " + head.getID());
        }
        this.head = head;
        if (core != null && core.getCustomItemType() != CustomItemType.PICKAXE_CORE) {
            throw new IllegalStateException("Invalid core component: " + core.getID());
        }
        this.core = core;
        if (handle != null && handle.getCustomItemType() != CustomItemType.PICKAXE_HANDLE) {
            throw new IllegalStateException("Invalid handle component: " + handle.getID());
        }
        this.handle = handle;
    }

    public BasePickaxeComponent getHead() { return head; }
    public BasePickaxeComponent getCore() { return core; }
    public BasePickaxeComponent getHandle() { return handle; }

    public BasePickaxeComponent[] getPickaxeParts() {

        BasePickaxeComponent[] pickaxeParts = new BasePickaxeComponent[3];
        if(head != null) pickaxeParts[0] = head;
        if(core != null) pickaxeParts[1] = core;
        if(handle != null) pickaxeParts[2] = handle;

        return pickaxeParts;
    }
}
