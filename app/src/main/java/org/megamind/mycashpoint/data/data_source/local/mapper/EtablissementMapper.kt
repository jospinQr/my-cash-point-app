package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.entity.EtablissementEntity
import org.megamind.mycashpoint.domain.model.Etablissement


fun Etablissement.toEntity(): EtablissementEntity {
    return EtablissementEntity(
        id = id,
        name = name,
        addrees = addrees,
        contat = contat,
        rccm = rccm

    )


}


fun EtablissementEntity.toEtablissement(): Etablissement {
    return Etablissement(
        id = id,
        name = name,
        addrees = addrees,
        contat = contat,
        rccm = rccm
    )


}