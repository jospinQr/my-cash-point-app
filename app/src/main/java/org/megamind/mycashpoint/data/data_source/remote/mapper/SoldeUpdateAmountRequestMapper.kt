package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeUpdateAmountRequestDto
import org.megamind.mycashpoint.domain.model.Solde

fun Solde.toSoldeUpdateAmountRequestDto(): SoldeUpdateAmountRequestDto {
    return SoldeUpdateAmountRequestDto(
        codeAgence = agenceCode,
        soldeType = soldeType.name,
        deviseCode = devise.name,
        montant = montant.toDouble(),
        misAJourPar = misAJourPar,
        dernierMiseAJour = dernierMiseAJour
    )
}
