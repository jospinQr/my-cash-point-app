package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeRequestDto
import org.megamind.mycashpoint.domain.model.Solde

fun Solde.toSoldeRequestDto(): SoldeRequestDto {
    return SoldeRequestDto(
        operateurId = idOperateur.toLong(),
        soldeType = soldeType.name,
        montant = montant.toDouble(),
        deviseCode = devise.name,
        seuilAlerte = seuilAlerte,
        misAJourPar = misAJourPar,
        dernierMiseAJour = dernierMiseAJour,
        codeAgence = codeAgence
    )
}
