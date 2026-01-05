package org.megamind.mycashpoint.domain.usecase.solde

import org.megamind.mycashpoint.domain.repository.SoldeMouvementRepository

class GetSoldeMouvementUseCase(private val repository: SoldeMouvementRepository) {


    operator fun invoke(codeAgence: String, page: Int, size: Int) =
        repository.getSoldeMouvementsByAgence(codeAgence, page, size)


}