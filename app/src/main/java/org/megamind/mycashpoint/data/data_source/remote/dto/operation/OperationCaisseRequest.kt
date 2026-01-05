package org.megamind.mycashpoint.data.data_source.remote.dto.operation

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.OperationCaisseType
import java.math.BigDecimal

@Serializable
data class OperationCaisseRequest(
    val operateurId: Long,
    val agenceCode: String,
    val type: OperationCaisseType,
    val soldeType: String = "ESPECES", // Default as per JSON example, though could be enum if rigid
    @Serializable(with = BigDecimalSerializer::class)
    val montant: BigDecimal,
    val devise: String,
    val motif: String,
    val userId: Long,
    val horodatage: Long
)
