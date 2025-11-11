package org.megamind.mycashpoint.data.data_source.local.mapper

import org.megamind.mycashpoint.data.data_source.local.entity.AgenceEntity
import org.megamind.mycashpoint.domain.model.Agence


fun Agence.toAgenceEntity(): AgenceEntity {
    return AgenceEntity(
        codeAgence = codeAgence,
        designation = designation
    )
}


fun AgenceEntity.toAgence(): Agence {
    return Agence(
        codeAgence = codeAgence,
        designation = designation
    )
}