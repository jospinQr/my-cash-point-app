package org.megamind.mycashpoint.utils

import androidx.room.TypeConverter
import org.megamind.mycashpoint.data.data_source.local.entity.StatutSync
import org.megamind.mycashpoint.data.data_source.local.entity.TypTransct

class EnumConverters {

    // ---- TypeFlux ----
    @TypeConverter
    fun fromTypeFlux(value: TypTransct): String = value.name

    @TypeConverter
    fun toTypeFlux(value: String): TypTransct = TypTransct.valueOf(value)

    // ---- StatutSync ----
    @TypeConverter
    fun fromStatutSync(value: StatutSync): String = value.name

    @TypeConverter
    fun toStatutSync(value: String): StatutSync = StatutSync.valueOf(value)

    @TypeConverter
    fun toDevise(value: String): Constants.Devise = Constants.Devise.valueOf(value)
}