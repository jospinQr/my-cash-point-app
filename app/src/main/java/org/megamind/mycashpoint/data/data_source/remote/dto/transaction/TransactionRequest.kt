package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.TransactionType
import java.math.BigDecimal

@Serializable
data class TransactionRequest(

    val operateurId: Long,
    val type: TransactionType,
    @Serializable(with = BigDecimalSerializer::class)
    val montant: BigDecimal,
    val transactionCode: String? = null,
    val commission: Float? = null,
    val nomClient: String? = null,
    val numClient: String? = null,
    val nomBeneficaire: String? = null,
    val numBeneficaire: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val soldeAvant: BigDecimal? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val soldeApres: BigDecimal? = null,
    val deviseCode: String, // USD | CDF (mapped to enum)
    val reference: String? = null,
    val note: String? = null,
    val codeAgence: String,
    val creePar: Long,
)

