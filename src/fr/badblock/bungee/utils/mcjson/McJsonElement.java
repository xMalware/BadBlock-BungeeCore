package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;
import lombok.Setter;

@Getter
public class McJsonElement {
    private McJsonAttribute attribute;
    @Setter
    private String value;

    public McJsonElement(McJsonAttribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public McJsonElement(McJsonAttribute attribute) {
        this(attribute, "");
    }


    @Override
    public String toString() {
        StringBuilder mcjson = new StringBuilder();
        mcjson.append("\"");
        if (attribute.getType() == 0) mcjson.append(attribute.getMcjsontag());
        else if (attribute.getType() == 1) mcjson.append("clickEvent");
        else if (attribute.getType() == 2) mcjson.append("hoverEvent");
        mcjson.append("\":");
        if (attribute.getType() > 0) mcjson.append("{\"action\":\"" + attribute.getMcjsontag() + "\",\"value\":");
        mcjson.append("\"" + getValue().replace("\"", "\\\"") + "\"");
        if (attribute.getType() > 0) mcjson.append("}");
        return mcjson.toString();
    }
}
