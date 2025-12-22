package org.megamind.mycashpoint.ui.navigation

enum class Destination(route: String) {


    LOGIN("login"),
    REGISTER("register"),
    MAIN("main"),
    CHASPOINT("cashpoint"),
    SPLASH("splash"),

    OPERATEUR("operateur"),

    TRANSACTION("transaction"),
    CAISSE("caisse"),
    RAPPORT("rapport"),

    AGENCE("agences"),

    DASHBOARD("dashboard"),
    ADMIN_REPPORT("admin_repport"),
    SETTINGS("settings"),

    SYNCTRSACT("sync_transact"),
    ETABLISSEMENT("etablissement")
}