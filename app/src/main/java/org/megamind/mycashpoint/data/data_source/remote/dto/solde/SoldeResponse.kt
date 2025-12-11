package org.megamind.mycashpoint.data.data_source.remote.dto.solde


import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

@Serializable()
data class SoldeResponse(
    val id: Long,
    val operateurId: Long,
    val operateurName: String,
    val soldeType: SoldeType,
    @Serializable(with = BigDecimalSerializer::class)
    val montant: BigDecimal,
    val devise: Constants.Devise,
    val dernierMiseAJour: Long,
    val seuilAlerte: Double?,
    val misAJourPar: Long,
    val codeAgence: String,
)


