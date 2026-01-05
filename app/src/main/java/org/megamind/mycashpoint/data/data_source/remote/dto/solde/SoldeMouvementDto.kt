package org.megamind.mycashpoint.data.data_source.remote.dto.solde

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.SoldeMouvement
import java.math.BigDecimal

@Serializable
data class SoldeMouvementDto(
    val id: Long,
    val soldeId: Long,
    val soldeType: String, // PHYSIQUE | VIRTUEL
    val devise: String,    // USD | CDF
    @Serializable(with = BigDecimalSerializer::class)
    val montantAvant: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val montantApres: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val montantChange: BigDecimal,
    val typeMouvement: String, // CREATION | TRANSACTION
    val referenceContext: String? = null,
    val dateMouvement: Long,
    val auteurId: Long,
    val auteurName: String,
    val motif: String
)

fun SoldeMouvementDto.toSoldeMouvement(): SoldeMouvement {
    return SoldeMouvement(
        id = id,
        soldeId = soldeId,
        soldeType = soldeType,
        devise = devise,
        montantAvant = montantAvant,
        montantApres = montantApres,
        montantChange = montantChange,
        typeMouvement = typeMouvement,
        referenceContext = referenceContext,
        dateMouvement = dateMouvement,
        auteurId = auteurId,
        auteurName = auteurName,
        motif = motif

    )
}


@Serializable
data class PageDto<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

typealias PaginateSoldeMouvementResponse = PageDto<SoldeMouvementDto>
