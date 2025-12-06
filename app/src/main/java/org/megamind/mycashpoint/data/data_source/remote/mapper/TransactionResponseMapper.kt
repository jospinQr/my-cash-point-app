package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.PaginationDTO
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.PaginateTransactionsResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionResponse
import org.megamind.mycashpoint.domain.model.PaginatedTransaction
import org.megamind.mycashpoint.domain.model.Pagination
import org.megamind.mycashpoint.domain.model.Transaction

fun TransactionResponse.toTransaction(): Transaction {
    return Transaction(
        id = id,
        transactionCode = transactionCode,
        idOperateur = operateurId,
        type = type,
        montant = montant,
        commission = commission,
        nomClient = nomClient,
        numClient = numClient,
        nomBeneficaire = nomBeneficaire,
        numBeneficaire = numBeneficaire,
        soldeAvant = soldeAvant,
        soldeApres = soldeApres,
        devise = devise,
        reference = reference,
        note = note,
        horodatage = horodatage,
        creePar = creePar,
        codeAgence = codeAgence,
    )
}

fun PaginationDTO.toPagination(): Pagination {
    return Pagination(
        page = page,
        size = size,
        totalPages = totalPages,
        totalElements = totalElements
    )
}

fun PaginateTransactionsResponse.toPaginatedTransaction(): PaginatedTransaction {
    return PaginatedTransaction(
        items = items.map { it.toTransaction() },
        pagination = pagination.toPagination(),
        totalMontant = totalMontant
    )
}
