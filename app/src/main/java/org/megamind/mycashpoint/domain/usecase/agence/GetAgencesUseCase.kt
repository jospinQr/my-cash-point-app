package org.megamind.mycashpoint.domain.usecase.agence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.utils.Result

class GetAgencesUseCase(
    private val repository: AgenceRepository
) {
    operator fun invoke(): Flow<Result<List<Agence>>> = repository.getAll()



}












