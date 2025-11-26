package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class SyncSoldesUseCase(private val soldeRepository: SoldeRepository) {
    operator fun invoke(): Flow<Result<Unit>> {
        return soldeRepository.syncSoldes()
    }
}
