package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Etablissement
import org.megamind.mycashpoint.utils.Result
interface EtablissementRepository {

    fun getEtablissementFromServer(): Flow<Result<Etablissement>>

    fun editEtablissement(etablissement: Etablissement): Flow<Result<Unit>>


    fun getEtablissementFromLocal(): Flow<Result<Etablissement>>
    fun getFromServerAndInsertLocaly(): Flow<Result<Unit>>


}