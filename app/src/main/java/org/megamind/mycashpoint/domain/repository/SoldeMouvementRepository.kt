package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.SoldeMouvement
import org.megamind.mycashpoint.utils.Result

interface SoldeMouvementRepository {


    fun getSoldeMouvementsByAgence(
        agenceCode: String,
        page: Int,
        size: Int
    ): Flow<Result<List<SoldeMouvement>>>

    /**
     * Récupère le rapport Excel Grand Livre pour une agence
     * @param codeAgence Le code de l'agence
     * @param startDate Date de début (optionnel, en millisecondes)
     * @param endDate Date de fin (optionnel, en millisecondes)
     */
    fun getGrandLivreExcelReport(
        codeAgence: String,
        startDate: Long? = null,
        endDate: Long? = null
    ): Flow<Result<ByteArray>>
}