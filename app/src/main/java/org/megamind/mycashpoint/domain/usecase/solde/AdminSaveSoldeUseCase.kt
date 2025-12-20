package org.megamind.mycashpoint.domain.usecase.solde

import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeResponse
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.repository.SoldeRepository

class AdminSaveSoldeUseCase(private val response: SoldeRepository) {

    operator fun invoke(solde: Solde) = response.saveSoldeToServer(solde)
}