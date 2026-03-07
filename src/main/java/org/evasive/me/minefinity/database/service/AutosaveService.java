package org.evasive.me.minefinity.database.service;

import org.evasive.me.minefinity.Minefinity;
import org.evasive.me.minefinity.database.ServerDataHandler;

import java.sql.SQLException;

public class AutosaveService {

    final int AUTOSAVE_TIMER = 5 ; //Minutes

    final ServerDataHandler serverDataHandler;

    long lastSave;
    long nextSave;

    public AutosaveService(ServerDataHandler serverDataHandler) {
        this.serverDataHandler = serverDataHandler;
        this.lastSave = System.currentTimeMillis();
        calculateNextSave();//Conversion to milliseconds
    }

    public void attemptSave() throws SQLException {
        if(System.currentTimeMillis() >= nextSave)
            save();
    }

    public void save() throws SQLException {
        if(!serverDataHandler.haveDirty()) return;
        Minefinity.getCore().databaseConnect();
        lastSave = System.currentTimeMillis();
        calculateNextSave();
        serverDataHandler.saveDirty();
        Minefinity.getDatabaseManager().closePool();
    }

    private void calculateNextSave(){
        this.nextSave = lastSave + (AUTOSAVE_TIMER * 60000);
    }
}
