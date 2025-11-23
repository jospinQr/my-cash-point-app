package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import java.math.BigDecimal


@Serializable
data class TransactionResponse(
    val id: Long,
    val transactionCode: String,
    val operateurId: Int,
    val type: TransactionType,
    @Serializable(with = BigDecimalSerializer::class)
    val montant: BigDecimal,
    val commission: Float?,
    val nomClient: String?,
    val numClient: String?,
    val nomBeneficaire: String?,
    val numBeneficaire: String?,
    @Serializable(with = BigDecimalSerializer::class)
    val soldeAvant: BigDecimal?,
    @Serializable(with = BigDecimalSerializer::class)
    val soldeApres: BigDecimal?,
    val devise: Constants.Devise,
    val reference: String?,
    val note: String?,
    val horodatage: Long,
    val creePar: Long,
    val codeAgence: String,

    )


fun Transaction.toResponse(): TransactionResponse =
    TransactionResponse(
        id = id,
        transactionCode = transactionCode,
        operateurId = idOperateur,
        type = type,
        montant = montant,
        commission = commission,
        nomClient = nomClient,
        numClient = numClient,
        nomBeneficaire = nomBeneficaire,
        numBeneficaire = numBeneficaire,
        soldeAvant = soldeAvant,
        soldeApres = soldeApres,
        devise = device,
        reference = reference,
        note = note,
        horodatage = horodatage,
        creePar = creePar,
        codeAgence = codeAgence,
    )


