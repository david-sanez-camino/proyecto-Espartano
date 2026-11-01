package com.espartano.app.domain.usecase

import android.content.Context
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.Perfil
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExportarDatosUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun exportarCsv(mediciones: List<Medicion>, perfil: Perfil?): File {
        val file = File(context.getExternalFilesDir(null), "espartano_historial.csv")
        file.bufferedWriter().use { w ->
            w.write("Fecha,Peso(kg),Grasa(%),Musculo(%),IMC\n")
            mediciones.forEach { m ->
                val imc = perfil?.let { m.imc(it.alturaCm) }?.let { "%.2f".format(it) } ?: ""
                w.write("${m.fecha.format(fmt)},${m.pesoKg},${m.grasaPct},${m.musculoPct},$imc\n")
            }
        }
        return file
    }

    fun exportarPdf(mediciones: List<Medicion>, perfil: Perfil?): File {
        val file = File(context.getExternalFilesDir(null), "espartano_historial.pdf")
        val document = Document()
        PdfWriter.getInstance(document, file.outputStream())
        document.open()

        val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
        val headerFont = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)
        val bodyFont = Font(Font.FontFamily.HELVETICA, 10f)

        document.add(Paragraph("ESPARTANO — Historial de mediciones", titleFont).apply {
            alignment = Element.ALIGN_CENTER
            spacingAfter = 16f
        })

        perfil?.let {
            document.add(Paragraph("Altura: ${it.alturaCm} cm  |  Edad: ${it.edad} años", bodyFont).apply {
                spacingAfter = 12f
            })
        }

        val table = PdfPTable(5).apply { setWidthPercentage(100f) }
        val headers = listOf("Fecha", "Peso (kg)", "Grasa (%)", "Músculo (%)", "IMC")
        headers.forEach { h ->
            table.addCell(PdfPCell(Phrase(h, headerFont)).apply {
                backgroundColor = BaseColor(30, 30, 30)
                horizontalAlignment = Element.ALIGN_CENTER
                paddingTop = 6f
                paddingBottom = 6f
            })
        }

        mediciones.forEach { m ->
            val imc = perfil?.let { "%.1f".format(m.imc(it.alturaCm)) } ?: "-"
            listOf(m.fecha.format(fmt), "${m.pesoKg}", "${m.grasaPct}", "${m.musculoPct}", imc).forEach { v ->
                table.addCell(PdfPCell(Phrase(v, bodyFont)).apply {
                    horizontalAlignment = Element.ALIGN_CENTER
                    paddingTop = 4f
                    paddingBottom = 4f
                })
            }
        }

        document.add(table)
        document.close()
        return file
    }
}
