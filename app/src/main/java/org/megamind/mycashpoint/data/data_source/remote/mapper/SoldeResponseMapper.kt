package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeResponse
import org.megamind.mycashpoint.domain.model.Solde


fun SoldeResponse.toSolde(): Solde {

    return Solde(
        idOperateur = this.operateurId,
        montant = this.montant,
        soldeType = this.soldeType,
        devise = this.devise,
        dernierMiseAJour = this.dernierMiseAJour,
        seuilAlerte = this.seuilAlerte,
        misAJourPar = this.misAJourPar,
        codeAgence = this.codeAgence

    )
}