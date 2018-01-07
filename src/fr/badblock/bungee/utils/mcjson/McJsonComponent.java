package fr.badblock.bungee.utils.mcjson;

import lombok.Getter;

@Getter
public class McJsonComponent {
    private McJsonElement[] elements;

    public McJsonComponent(McJsonElement... elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder mcjson = new StringBuilder();
        mcjson.append("{");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) mcjson.append(",");
            mcjson.append(elements[i]);
        }
        mcjson.append("}");
        return mcjson.toString();
    }
}
