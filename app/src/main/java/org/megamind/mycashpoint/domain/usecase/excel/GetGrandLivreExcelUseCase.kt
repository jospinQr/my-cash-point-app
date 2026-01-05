package org.megamind.mycashpoint.domain.usecase.excel

import org.megamind.mycashpoint.domain.repository.ExcelReportRepository

class GetGrandLivreExcelUseCase(private val repository: ExcelReportRepository) {

    operator fun invoke(codeAgence: String, startDate: Long?, endDate: Long?) =
        repository.getGrandLivreExcel(codeAgence, startDate, endDate)
}
