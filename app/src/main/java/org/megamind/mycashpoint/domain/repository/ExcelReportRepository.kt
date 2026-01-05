package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.utils.Result

/**
 * Repository interface for fetching Excel reports from the server
 */
interface ExcelReportRepository {

    /**
     * Récupère le rapport Excel Grand Livre pour une agence
     * @param codeAgence Le code de l'agence
     * @param startDate Date de début (optionnel, en millisecondes)
     * @param endDate Date de fin (optionnel, en millisecondes)
     */
    fun getGrandLivreExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>>

    /**
     * Récupère le rapport Excel Journal des Transactions pour une agence
     * @param codeAgence Le code de l'agence
     * @param startDate Date de début (optionnel, en millisecondes)
     * @param endDate Date de fin (optionnel, en millisecondes)
     */
    fun getJournalTransactionExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>>

    /**
     * Récupère le rapport Excel Journal des Opérations Internes pour une agence
     * @param codeAgence Le code de l'agence
     * @param startDate Date de début (optionnel, en millisecondes)
     * @param endDate Date de fin (optionnel, en millisecondes)
     */
    fun getJournalOperationInterneExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>>
}
