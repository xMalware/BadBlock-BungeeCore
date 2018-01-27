package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;

@Getter
public class McJson {
    private McJsonComponent[] components;

    public McJson(McJsonComponent... components) {
        this.components = components;
    }

    @Override
    public String toString() {
        StringBuilder mcjson = new StringBuilder();
        mcjson.append("[");
        for (int i = 0; i < components.length; i++) {
            if (i > 0) mcjson.append(",");
            mcjson.append(components[i]);
        }
        mcjson.append("]");
        return mcjson.toString();
    }
}
