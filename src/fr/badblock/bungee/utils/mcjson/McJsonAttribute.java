package fr.badblock.bungee.utils.mcjson;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum McJsonAttribute {
    TEXT("text", "text", 0),
    WEBSITE("site", "open_url", 1),
    SUGGEST("insert", "suggest_command", 1),
    EXECUTE("do", "run_command", 1),
    HOVER("hover", "show_text", 2);

    private String decodetag, mcjsontag;
    private int type;

    public McJsonAttribute get(String decodetag) {
        for (McJsonAttribute attribute : values())
            if (attribute.getDecodetag().equalsIgnoreCase(decodetag)) return attribute;
        return null;
    }

}
