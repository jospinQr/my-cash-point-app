package org.megamind.mycashpoint.data.data_source.remote.dto.etablissement

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.domain.model.Etablissement

@Serializable
data class EtablissementRequest(
    val name: String,
    val adress: String,
    val contact: String?,
    val rccm: String?
)


fun Etablissement.toEtablissementRequest(): EtablissementRequest = EtablissementRequest(

    name = name,
    adress = addrees,
    contact = contat,
    rccm = rccm
)