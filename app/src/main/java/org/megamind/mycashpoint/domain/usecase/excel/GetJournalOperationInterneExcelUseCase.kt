package org.megamind.mycashpoint.domain.usecase.excel

import org.megamind.mycashpoint.domain.repository.ExcelReportRepository

class GetJournalOperationInterneExcelUseCase(private val repository: ExcelReportRepository) {

    operator fun invoke(codeAgence: String, startDate: Long?, endDate: Long?) =
        repository.getJournalOperationInterneExcel(codeAgence, startDate, endDate)
}
