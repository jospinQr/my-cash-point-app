package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.toSoldeMouvement
import org.megamind.mycashpoint.data.data_source.remote.service.SoldeService
import org.megamind.mycashpoint.domain.model.SoldeMouvement
import org.megamind.mycashpoint.domain.repository.SoldeMouvementRepository
import org.megamind.mycashpoint.utils.Result

class SoldeMouvementRepositoryImpl(private val service: SoldeService) : SoldeMouvementRepository {


    val TAG = "Solde mouvement"
    override fun getSoldeMouvementsByAgence(
        agenceCode: String,
        page: Int,
        size: Int
    ): Flow<Result<List<SoldeMouvement>>> = flow {

        emit(Result.Loading)
        Log.i(TAG, "Chargement des mouvement de solde")
        try {
            when (val result = service.getSoldeMouvements(agenceCode = agenceCode, page, size)) {


                is Result.Success -> {
                    emit(Result.Success(result.data?.content?.map { it.toSoldeMouvement() }))
                    Log.i(TAG, result.data.toString())

                }

                is Result.Error<*> -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnu")))
                    Log.e(TAG, result.e.toString())

                }

                else -> {}
            }
        } catch (e: Exception) {

            emit(Result.Error(e))
        }
    }

    override fun getGrandLivreExcelReport(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>> = flow {
        emit(Result.Loading)
        Log.i(TAG, "Chargement du rapport Grand Livre Excel pour agence: $codeAgence")
        try {
            when (val result = service.getGrandLivreExcelReport(codeAgence, startDate, endDate)) {
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
}