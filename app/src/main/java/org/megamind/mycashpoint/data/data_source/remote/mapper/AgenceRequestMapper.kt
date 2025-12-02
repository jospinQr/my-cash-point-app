package org.megamind.mycashpoint.data.data_source.remote.mapper

import org.megamind.mycashpoint.data.data_source.remote.dto.agence.AgenceRequest
import org.megamind.mycashpoint.domain.model.Agence


fun Agence.toAgenceRequest(): AgenceRequest {
    return AgenceRequest(
        code = codeAgence,
        name = designation


    )
}