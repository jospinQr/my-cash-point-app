package org.megamind.mycashpoint.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.Etablissement


import java.io.FileOutputStream
import java.io.IOException
import androidx.core.graphics.scale

class MyPrintDocumentAdapter(
    val context: Context,
    val title: String,
    val data: Map<String, Any>,
    val etablissement: Etablissement? = null
) : PrintDocumentAdapter() {


    private var pdfDocument: PrintedPdfDocument? = null
    private val MARGIN_MM = 5f
    private val CELL_HEIGHT_MM = 5f
    private val CELL_HEIGHT = CELL_HEIGHT_MM * 2.83465

    // pour styler les texte
    val titlePaint = Paint().apply {
        color = Color.BLACK
        textSize = 20f
        isFakeBoldText = true
    }

    val bodyPainter = Paint().apply {
        color = Color.BLACK
        textSize = 20f
        isFakeBoldText = true
    }

    val linePainter = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isFakeBoldText = true
    }


    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        pdfDocument = PrintedPdfDocument(context, newAttributes!!)
        if (cancellationSignal?.isCanceled == true) {
            callback?.onLayoutCancelled()
            return
        }
        val pages = computePageCount(newAttributes)

        if (pages > 0) {
            PrintDocumentInfo.Builder("facture.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(pages)
                .build()
                .also {
                    callback?.onLayoutFinished(it, true)
                }
        } else {
            callback?.onLayoutFailed("Erreur de calcul du nombre de pages")
        }
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        try {
            pdfDocument?.let { document ->
                for (i in 0 until data.size) {
                    if (containsPage(pages!!, i)) {
                        document.startPage(i).also { page ->
                            try {
                                if (cancellationSignal?.isCanceled == true) {
                                    callback?.onWriteCancelled()
                                    return
                                }
                                drawPage(page, title)
                            } finally {
                                document.finishPage(page)
                            }
                        }
                    }
                }

                document.writeTo(FileOutputStream(destination!!.fileDescriptor))
                callback?.onWriteFinished(pages)
            } ?: callback?.onWriteFailed("Document PDF non initialisé.")
        } catch (e: IOException) {
            callback?.onWriteFailed(e.toString())
        } finally {
            pdfDocument?.close()
            pdfDocument = null
        }
    }

    private fun drawPage(page: PdfDocument.Page, title: String) {
        page.canvas.apply {
            var titleBaseLine = 72f
            val leftMargin = 20f
            val pageWidth = page.info.pageWidth


            // Dessiner une image
            val bitmap: Bitmap = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.logo
            )
            val scaledBitmap = bitmap.scale(100, 100)
            drawBitmap(scaledBitmap, 72f, 4f, null) // Dessiner l'image sur le canvas

            titleBaseLine = drawTextWithSpacing(
                this,
                "Republique democratique \n du congo".uppercase(),
                leftMargin,
                titleBaseLine,
                titlePaint,
                pageWidth
            )
            titleBaseLine = drawTextWithSpacing(
                this,
                (etablissement?.name ?: "MY CASH POINT").uppercase(),
                leftMargin,
                titleBaseLine,
                titlePaint,
                pageWidth
            )

            etablissement?.rccm?.let { rccm ->
                titleBaseLine = drawTextWithSpacing(
                    this,
                    "RCCM : $rccm",
                    leftMargin,
                    titleBaseLine,
                    bodyPainter,
                    pageWidth
                )
            }

            titleBaseLine = drawTextWithSpacing(
                this,
                "Contact : ${etablissement?.contat ?: "+243 000 000 000"}",
                leftMargin,
                titleBaseLine,
                bodyPainter,
                pageWidth
            )

            titleBaseLine = drawTextWithSpacing(
                this,
                "Adresse : ${etablissement?.addrees ?: "Butembo, Rue Kinshasa"}",
                leftMargin,
                titleBaseLine,
                bodyPainter,
                pageWidth
            )
            drawLine(
                leftMargin,
                titleBaseLine + 20,
                pageWidth.toFloat() - leftMargin,
                titleBaseLine + 20,
                linePainter
            )
            titleBaseLine = drawTextWithSpacing(
                this,
                "Reçu No.: ${data.get("numero")}",
                leftMargin,
                titleBaseLine + 60,
                titlePaint,
                pageWidth
            )


            titleBaseLine = drawTextWithSpacing(
                this, "${data.get("date")} ",
                60F,
                titleBaseLine + 10,
                bodyPainter,
                pageWidth
            )

            titleBaseLine = drawTextWithSpacing(
                this,
                "-  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ",
                60F,
                titleBaseLine + 10,
                linePainter,
                pageWidth
            )

            titleBaseLine = drawTextWithSpacing(
                this, "${data.get("motif")} par / ${data.get("nom")}",
                60F,
                titleBaseLine,
                bodyPainter,
                pageWidth
            )

            titleBaseLine = drawTextWithSpacing(
                this,
                "-  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ",
                60F,
                titleBaseLine + 10,
                linePainter,
                pageWidth
            )
            titleBaseLine = drawTextWithSpacing(
                this, " Montant  : ${data.get("montant")} ${data.get("devise")}",
                60F,
                titleBaseLine + 10,
                titlePaint,
                pageWidth
            )
            titleBaseLine = drawTextWithSpacing(
                this,
                "-  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ",
                60F,
                titleBaseLine + 10,
                linePainter,
                pageWidth
            )

            titleBaseLine = drawTextWithSpacing(
                this, "Agent  : ${data.get("agent")}",
                60F,
                titleBaseLine + 10,
                titlePaint,
                pageWidth
            )

        }
    }

    // Fonction pour dessiner du texte centré
    private fun drawTextWithSpacing(
        canvas: Canvas,
        text: String,
        leftMargin: Float,
        titleBaseLine: Float,
        paint: Paint,
        pageWidth: Int
    ): Float {
        val textWidth = paint.measureText(text)
        val newLeftMargin = (pageWidth - textWidth) / 2
        canvas.drawText(text, newLeftMargin, titleBaseLine, paint)
        return titleBaseLine + paint.textSize + 10
    }

    private fun containsPage(pageRange: Array<out PageRange>, pageIndex: Int): Boolean {
        return pageRange.any { pageIndex in it.start..it.end }
    }

    private fun computePageCount(printAttributes: PrintAttributes): Int {
        val pageSize = printAttributes.mediaSize
        val availableHeight = (pageSize?.heightMils ?: 0) / 1000f - 2 * MARGIN_MM
        val itemsPerPage = (availableHeight / CELL_HEIGHT).toInt()


        return if (itemsPerPage > 0 && data.isNotEmpty()) {
            val pageCount = (data.size + itemsPerPage - 1) / itemsPerPage
            println("Calculated Page Count: $pageCount")
            pageCount
        } else {
            println("Defaulting to 1 Page")
            1
        }
    }
}




