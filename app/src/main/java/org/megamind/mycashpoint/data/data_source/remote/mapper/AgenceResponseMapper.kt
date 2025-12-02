package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.agence.AgenceResponse
import org.megamind.mycashpoint.domain.model.Agence


fun AgenceResponse.toAgence(): Agence {

    return Agence(

        codeAgence = code,
        designation = name

    )
}