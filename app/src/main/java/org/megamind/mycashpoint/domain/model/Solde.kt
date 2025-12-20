package org.megamind.mycashpoint.domain.model

import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

data class Solde(

    val idOperateur: Long = 0,
    val montant: BigDecimal = BigDecimal.ZERO,
    val soldeType: SoldeType = SoldeType.PHYSIQUE,
    val devise: Constants.Devise = Constants.Devise.USD,
    val dernierMiseAJour: Long = System.currentTimeMillis(),
    val seuilAlerte: Double? = null,
    val misAJourPar: Long = 0,
    val agenceCode: String = ""

)


enum class SoldeType {
    PHYSIQUE, VIRTUEL
}
