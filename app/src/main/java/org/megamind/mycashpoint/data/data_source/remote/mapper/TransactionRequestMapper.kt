package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionRequest
import org.megamind.mycashpoint.domain.model.Transaction


fun Transaction.toTransactionRequest(): TransactionRequest {


    return TransactionRequest(
        operateurId = idOperateur,
        type = type,
        montant = montant,
        transactionCode = transactionCode,
        commission = commission,
        nomClient = nomClient,
        numClient = numClient,
        nomBeneficaire = nomBeneficaire,
        numBeneficaire = numBeneficaire,
        soldeAvant = soldeAvant,
        soldeApres = soldeApres,
        deviseCode = device.name,
        reference = reference,
        note = note,
        codeAgence = codeAgence,
        creePar = creePar

    )
}