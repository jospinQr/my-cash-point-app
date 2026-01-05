package org.megamind.mycashpoint.domain.usecase.operation

import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.megamind.mycashpoint.data.data_source.remote.dto.operation.OperationCaisseRequest
import org.megamind.mycashpoint.domain.model.OperationCaisseType
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.repository.OperationCaisseRepository
import org.megamind.mycashpoint.ui.screen.admin.operation.OperationCaisseScreen
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload
import java.math.BigDecimal

class SaveOperationCaisseUseCase(
    private val repository: OperationCaisseRepository,
    private val dataStorageManager: DataStorageManager
) {

    suspend operator fun invoke(
        type: OperationCaisseType,
        montant: String,
        motif: String,
        devise: Constants.Devise,
        operateurId: Long,
        soldeType: SoldeType,
        codAgence: String
    ): Flow<Result<Unit>> {


        val userId =
            dataStorageManager.getToken()?.let { decodeJwtPayload(it) }?.optString("sub")

        val request = OperationCaisseRequest(
            operateurId = operateurId,
            agenceCode = codAgence,
            type = type,
            montant = BigDecimal(montant),
            devise = devise.name,
            soldeType = soldeType.name,
            motif = motif,
            userId = userId?.toLong() ?: 0L,
            horodatage = System.currentTimeMillis(),
        )

        return repository.saveOperation(request)

    }

}
