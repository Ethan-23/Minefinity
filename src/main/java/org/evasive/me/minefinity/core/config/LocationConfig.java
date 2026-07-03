package org.evasive.me.minefinity.core.config;

import org.evasive.me.minefinity.Minefinity;

public class LocationConfig extends BaseConfig{

    public LocationConfig() {
        super(Minefinity.getCore(), "locations.yml");
        load();
    }

}
