package org.megamind.mycashpoint.domain.usecase.excel

import org.megamind.mycashpoint.domain.repository.ExcelReportRepository

class GetJournalTransactionExcelUseCase(private val repository: ExcelReportRepository) {

    operator fun invoke(codeAgence: String, startDate: Long?, endDate: Long?) =
        repository.getJournalTransactionExcel(codeAgence, startDate, endDate)
}
