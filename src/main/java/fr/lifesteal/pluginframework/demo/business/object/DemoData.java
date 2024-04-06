package fr.lifesteal.pluginframework.demo.business.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("DemoData")
public class DemoData implements Cloneable, ConfigurationSerializable {
    private final int id;
    private final String name;

    public DemoData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<>(){{
            put("id", id);
            put("name", name);
        }};
    }

    public static DemoData deserialize(Map<String, Object> args) {
        return new DemoData(Integer.parseInt(args.get("id").toString()), args.get("name").toString());
    }
}
