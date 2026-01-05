package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.remote.service.ExcelService
import org.megamind.mycashpoint.domain.repository.ExcelReportRepository
import org.megamind.mycashpoint.utils.Result

class ExcelReportRepositoryImpl(
    private val excelService: ExcelService
) : ExcelReportRepository {

    private val TAG = "ExcelReportRepository"

    override fun getGrandLivreExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>> = flow {
        emit(Result.Loading)
        Log.i(TAG, "Chargement du rapport Grand Livre Excel pour agence: $codeAgence")
        try {
            val effectiveStartDate = startDate ?: 0L
            val effectiveEndDate = endDate ?: System.currentTimeMillis()
            
            when (val result = excelService.generateGrandLivre(codeAgence, effectiveStartDate, effectiveEndDate)) {
                is Result.Success -> {
                    result.data?.let { bytes ->
                        emit(Result.Success(bytes))
                        Log.i(TAG, "Rapport Grand Livre Excel téléchargé: ${bytes.size} bytes")
                    } ?: emit(Result.Error(Exception("Aucune donnée reçue")))
                }
                is Result.Error<*> -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
                    Log.e(TAG, "Erreur téléchargement Grand Livre: ${result.e}")
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(TAG, "Exception téléchargement Grand Livre: ${e.message}")
        }
    }

    override fun getJournalTransactionExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>> = flow {
        emit(Result.Loading)
        Log.i(TAG, "Chargement du rapport Journal Transaction Excel pour agence: $codeAgence")
        try {
            val effectiveStartDate = startDate ?: 0L
            val effectiveEndDate = endDate ?: System.currentTimeMillis()
            
            when (val result = excelService.generateJournalTransaction(codeAgence, effectiveStartDate, effectiveEndDate)) {
                is Result.Success -> {
                    result.data?.let { bytes ->
                        emit(Result.Success(bytes))
                        Log.i(TAG, "Rapport Journal Transaction Excel téléchargé: ${bytes.size} bytes")
                    } ?: emit(Result.Error(Exception("Aucune donnée reçue")))
                }
                is Result.Error<*> -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
                    Log.e(TAG, "Erreur téléchargement Journal Transaction: ${result.e}")
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(TAG, "Exception téléchargement Journal Transaction: ${e.message}")
        }
    }

    override fun getJournalOperationInterneExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>> = flow {
        emit(Result.Loading)
        Log.i(TAG, "Chargement du rapport Journal Opération Interne Excel pour agence: $codeAgence")
        try {
            val effectiveStartDate = startDate ?: 0L
            val effectiveEndDate = endDate ?: System.currentTimeMillis()
            
            when (val result = excelService.generateJournalOperationInterne(codeAgence, effectiveStartDate, effectiveEndDate)) {
                is Result.Success -> {
                    result.data?.let { bytes ->
                        emit(Result.Success(bytes))
                        Log.i(TAG, "Rapport Journal Opération Interne Excel téléchargé: ${bytes.size} bytes")
                    } ?: emit(Result.Error(Exception("Aucune donnée reçue")))
                }
                is Result.Error<*> -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
                    Log.e(TAG, "Erreur téléchargement Journal Opération Interne: ${result.e}")
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(TAG, "Exception téléchargement Journal Opération Interne: ${e.message}")
        }
    }
}
