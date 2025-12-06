package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Transaction


fun Transaction.toTransactionEntity(isSynced: Boolean): TransactionEntity {


    return TransactionEntity(

        id = id,
        transactionCode = transactionCode,
        idOperateur = idOperateur,
        type = type,
        montant = montant,
        commission=commission,
        nomClient = nomClient,
        numClient = numClient,
        nomBeneficaire = nomBeneficaire,
        numBeneficaire = numBeneficaire,
        soldeAvant = soldeAvant,
        soldeApres = soldeApres,
        device = devise,
        reference = reference,
        note = note,
        horodatage = horodatage,
        creePar = creePar,
        codeAgence = codeAgence,
        isSynced = isSynced
    )

}


fun TransactionEntity.toTransaction(): Transaction {

    return Transaction(
        id = id,
        transactionCode = transactionCode,
        idOperateur = idOperateur,
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
        codeAgence = codeAgence
    )
}