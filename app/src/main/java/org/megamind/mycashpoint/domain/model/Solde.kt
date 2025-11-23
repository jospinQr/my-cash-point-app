package org.megamind.mycashpoint.domain.model

import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import java.math.BigDecimal

data class Solde(

    val idOperateur: Int = 0,
    val montant: BigDecimal = BigDecimal.ZERO,
    val soldeType: SoldeType = SoldeType.PHYSIQUE,
    val devise: Constants.Devise = Constants.Devise.USD,
    val dernierMiseAJour: Long = System.currentTimeMillis(),
    val seuilAlerte: Double? = null,
    val misAJourPar: Long = 0,
    val codeAgence: String = ""

)


enum class SoldeType {
    PHYSIQUE, VIRTUEL
}
