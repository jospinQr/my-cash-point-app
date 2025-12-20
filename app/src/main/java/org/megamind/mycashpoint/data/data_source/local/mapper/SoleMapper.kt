package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.domain.model.Solde


fun SoldeEntity.toSolde(): Solde {
    return Solde(
        idOperateur = idOperateur,
        montant = montant,
        soldeType = soldeType,
        devise = devise,
        dernierMiseAJour = dernierMiseAJour,
        seuilAlerte = seuilAlerte,
        misAJourPar = misAJourPar,
        agenceCode = codeAgence
    )
}


fun Solde.toSoldeEntity(isSynced: Boolean): SoldeEntity {
    return SoldeEntity(
        idOperateur = idOperateur,
        montant = montant,
        soldeType = soldeType,
        devise = devise,
        dernierMiseAJour = dernierMiseAJour,
        seuilAlerte = seuilAlerte,
        misAJourPar = misAJourPar,
        codeAgence = agenceCode,
        isSynced = isSynced
    )
}