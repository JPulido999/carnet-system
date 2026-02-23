package com.zkteco.apizk.util;

import java.util.Map;

public final class EventDictionary {

    private static final Map<String, String> MAP = Map.ofEntries(

        // ✅ EVENTOS PERMITIDOS A TRADUCIR
        Map.entry("acc_newEventNo_0",  "Apertura con verificación normal"),
        Map.entry("acc_newEventNo_1",  "Verificación con tarjeta durante apertura programada"),
        Map.entry("acc_newEventNo_21", "Apertura de puerta durante puerta inactiva"),
        Map.entry("acc_newEventNo_23", "Acceso denegado"),
        Map.entry("acc_newEventNo_27", "Usuario no registrado"),
        Map.entry("acc_newEventNo_29", "Usuario expirado"),
        Map.entry("acc_eventNo_39",    "Acceso desactivado")
    );

    private EventDictionary() {}

    public static String translate(String eventCode) {
        if (eventCode == null) return null;
        return MAP.getOrDefault(eventCode, eventCode); // 👈 si no existe, devuelve el mismo código
    }
}
