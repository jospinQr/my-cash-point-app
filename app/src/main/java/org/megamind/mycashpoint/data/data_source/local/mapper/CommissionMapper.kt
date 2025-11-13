                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.dao.CommissionStats as DaoCommissionStats
import org.megamind.mycashpoint.data.data_source.local.entity.CommissionEntity
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.model.CommissionStats

fun CommissionEntity.toCommission(): Commission {
    return Commission(
        id = id,
        idOperateur = idOperateur,
        type = type,
        devise = devise,
        taux = taux,
        montantFixe = montantFixe
    )
}

fun Commission.toCommissionEntity(): CommissionEntity {
    return CommissionEntity(
        id = id,
        idOperateur = idOperateur,
        type = type,
        devise = devise,
        taux = taux,
        montantFixe = montantFixe
    )
}

fun DaoCommissionStats.toDomain(): CommissionStats {
    return CommissionStats(
        idOperateur = idOperateur,
        devise = devise,
        nombreTaux = nombreTaux,
        tauxMoyen = tauxMoyen
    )
}







