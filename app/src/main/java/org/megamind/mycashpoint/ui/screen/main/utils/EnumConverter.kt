package org.megamind.mycashpoint.ui.screen.main.utils

import androidx.room.TypeConverter
import org.megamind.mycashpoint.domain.model.SoldeType

import org.megamind.mycashpoint.domain.model.TransactionType
import java.math.BigDecimal

class EnumConverters {

    // ---- TypeFlux ----

    @TypeConverter
    fun fromTypSolde(value: SoldeType): String = value.name

    @TypeConverter
    fun toTypSolde(value: String): SoldeType = SoldeType.valueOf(value)

    @TypeConverter
    fun fromTypeFlux(value: TransactionType): String = value.name

    @TypeConverter
    fun toTypeFlux(value: String): TransactionType = TransactionType.valueOf(value)



    @TypeConverter
    fun toDevise(value: String): Constants.Devise = Constants.Devise.valueOf(value)


    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}