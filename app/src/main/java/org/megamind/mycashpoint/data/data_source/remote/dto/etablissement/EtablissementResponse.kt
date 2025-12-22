package org.megamind.mycashpoint.data.data_source.remote.dto.etablissement

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.domain.model.Etablissement

@Serializable
data class EtablissementResponse(
    val id: Long, val name: String, val adress: String, val contact: String, val rccm: String
)


fun EtablissementResponse.toEtablissement(): Etablissement {

    return Etablissement(
        id = id, name = name, addrees = adress, contat = contact, rccm = rccm

    )
}