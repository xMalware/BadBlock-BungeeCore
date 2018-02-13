package fr.badblock.bungee.players.layer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignore a field when settings is (de)serialised in/from json
 *
 * @author RedSpri
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface SettingIgnore {
}
