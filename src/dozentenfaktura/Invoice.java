/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura;

import dozentenfaktura.datenbank.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amederake
 */
public class Invoice
{

    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font UNDERLINE = new Font(Font.FontFamily.HELVETICA, 10, Font.UNDERLINE);
    private static final Font BOLD11 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font BOLDITALIC11 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLDITALIC);

    private final Rechnung rechnung;
    private final Kunde kunde;
    private final Auftrag auftrag;
    private final Dozent dozent;

    public Invoice(Rechnung rechnung)
    {
        this.rechnung = rechnung;
        this.auftrag = DozentenFaktura.getMainHandle().getAuftraege().getAuftragByNr(rechnung.getAuftragNr());
        this.kunde = DozentenFaktura.getMainHandle().getKunden().getKundeByNr(auftrag.getKdNr());
        this.dozent = DozentenFaktura.getMainHandle().getDozenten().getDozentById(auftrag.getDozent());
        LocalDate datum = rechnung.getDatum();
        //String file = "Rechnungen/Rechnung_" + String.format("%02d", datum.getMonthValue()) + "-" + String.valueOf(datum.getYear()) + ".pdf";
        //createDocument(file);
    }

    public boolean createDocument(String file)
    {
        Document document;
        PdfWriter writer;
        try
        {
            // create bas document, writer and rectangle for positioning
            document = new Document(PageSize.A4, 60, 50, 50, 30);
            Rectangle p = document.getPageSize();
            writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            Rectangle rect = new Rectangle(p.getLeft() + 60, p.getBottom() + 30, p.getRight() - 50, p.getTop() - 50);
            writer.setBoxSize("art", rect);

            // create and set page event for header and footer
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);

            document.open();
            addMetaData(document);
            addAddressHead(writer);
            addBetreff(writer);
            addEinleitung(writer);
            addRechnung(writer);
            addAusleitung(writer);
            document.close();
        } catch (IOException | DocumentException ex)
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * add meta data to document
     *
     * @param document
     */
    private void addMetaData(Document document)
    {
        LocalDate datum = rechnung.getDatum();
        String title = "Rechnung_" + String.format("%02d", datum.getMonthValue()) + "-" + String.valueOf(datum.getYear());

        document.addTitle(title);
        document.addAuthor(dozent.getVorname() + " " + dozent.getNachname());
        document.addCreator(dozent.getVorname() + " " + dozent.getNachname());
        document.addCreationDate();
    }

    /**
     * address header with layers
     *
     * @param writer
     */
    private void addAddressHead(PdfWriter writer)
    {
        PdfLayer page;
        PdfLayer left_ctnt;
        PdfLayer right_ctnt;
        PdfContentByte cb;
        Rectangle rect = writer.getBoxSize("art");
        try
        {
            // create layer
            page = new PdfLayer("Page", writer);
            left_ctnt = new PdfLayer("Left", writer);
            right_ctnt = new PdfLayer("Right", writer);

            // get canvas
            cb = writer.getDirectContent();

            // add left and right layer to page layer
            page.addChild(left_ctnt);
            page.addChild(right_ctnt);

            // fill page with content
            cb.beginLayer(page);
            cb.saveState();
            cb.setColorStroke(BaseColor.BLACK);
            cb.setLineWidth(2.0);
            cb.moveTo((rect.getRight() / 3) * 2, rect.getTop() - 90);
            cb.lineTo((rect.getRight() / 3) * 2, rect.getTop() - 220);
            cb.closePath();
            cb.stroke();
            cb.restoreState();
            cb.endLayer();

            // fill left layer with content
            cb.beginLayer(left_ctnt);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(kunde.getName(), NORMAL), rect.getLeft(), rect.getTop() - 115, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(kunde.getStrasse(), NORMAL), rect.getLeft(), rect.getTop() - 130, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(kunde.getPlz() + " " + kunde.getOrt(), BOLD), rect.getLeft(), rect.getTop() - 145, 0);
            cb.endLayer();

            float left = (rect.getRight() / 3) * 2 + 10;
            String d_name = dozent.getVorname() + " " + dozent.getNachname();
            String d_PlzOrt = dozent.getPlz() + " " + dozent.getOrt();
            LocalDate datum = rechnung.getDatum();
            String re_datum = String.format("%02d.%02d.%4d", datum.getDayOfMonth(), datum.getMonthValue(), datum.getYear());
            String re_Nr = String.format("%2d / %4d", rechnung.getRechnungNr(), datum.getYear());

            cb.beginLayer(right_ctnt);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(d_name, BOLD), left, rect.getTop() - 100, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(dozent.getStrasse(), BOLD), left, rect.getTop() - 115, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(d_PlzOrt, BOLD), left, rect.getTop() - 145, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("Steuer-Nr.     : " + dozent.getSteuerId(), BOLD), left, rect.getTop() - 175, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("Rechnungsdatum : " + re_datum, BOLD), left, rect.getTop() - 190, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("Rechnungsnummer: " + re_Nr, BOLD), left, rect.getTop() - 205, 0);
            cb.endLayer();

        } catch (IOException ex)
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * add betreff line to document
     *
     * @param writer PdfWriter
     */
    private void addBetreff(PdfWriter writer)
    {
        try
        {
            Rectangle rect = writer.getBoxSize("art");
            PdfContentByte cb = writer.getDirectContent();

            ColumnText ct = new ColumnText(cb);
            ct.setSimpleColumn(rect.getLeft(), rect.getTop() - 280, rect.getRight(), rect.getTop() - 230);
            Paragraph para = new Paragraph("Abrechnung Unterrichtsstunden freie Mitarbeit auf Grundlage der gültigen Honorarvereinbarung für die Veranstaltungen ", BOLD11);
            para.add(new Phrase(auftrag.getThema(), BOLDITALIC11));
            ct.addElement(para);
            ct.go();

        } catch (DocumentException ex)
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * add einleitung text to document
     *
     * @param writer PdfWriter object
     */
    private void addEinleitung(PdfWriter writer)
    {
        try
        {
            Rectangle rect = writer.getBoxSize("art");
            PdfContentByte cb = writer.getDirectContent();
            LocalDate sDate = rechnung.getVon_Datum();
            LocalDate eDate = rechnung.getBis_Datum();
            String start = String.format("%02d.%2d.%4d", sDate.getDayOfMonth(), sDate.getMonthValue(), sDate.getYear());
            String ende = String.format("%02d.%2d.%4d", eDate.getDayOfMonth(), eDate.getMonthValue(), eDate.getYear());

            ColumnText ct = new ColumnText(cb);
            ct.setSimpleColumn(rect.getLeft(), rect.getTop() - 340, rect.getRight(), rect.getTop() - 280);
            Paragraph para = new Paragraph("Sehr geehrte Damen und Herren,", NORMAL);
            para.add(Chunk.NEWLINE);
            para.add(Chunk.NEWLINE);
            para.add("hiermit übersende ich Ihnen die Rechnung zur Dozententätigkeit für den Zeitraum "
                    + start + " bis "
                    + ende + " für die o.g. Veranstaltung.");
            ct.addElement(para);
            ct.go();
        } catch (DocumentException ex)
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * add rechnung table to document
     *
     * @param writer PdfWriter object
     */
    private void addRechnung(PdfWriter writer)
    {
        LocalDate sDate = rechnung.getVon_Datum();
        LocalDate eDate = rechnung.getBis_Datum();
        
        String ue = String.valueOf(countWorkDays(sDate, eDate) * 9);
        String uh = auftrag.getHonorar();
        String sum = rechnung.getSumme();
        
        Rectangle rect = writer.getBoxSize("art");

        float[] spalten =
        {
            30f, 100f, 100f, 100f, 80f
        };
        PdfPTable tbl = new PdfPTable(spalten);
        tbl.setTotalWidth(rect.getWidth());
        PdfPCell cell;

        // table header
        cell = new PdfPCell(new Phrase("Nr.", BOLD));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2f);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Bezeichnung", BOLD));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2f);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Unterrichtseinheiten", BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2f);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Stundenlohn/€", BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidth(2f);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Gesamt/€", BOLD));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setFixedHeight(25);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(2f);
        tbl.addCell(cell);

        // table body
        cell = new PdfPCell(new Phrase("1", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(50);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Unterrichtsseminar Modul siehe Vertragsnummer", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(ue, NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setFixedHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(uh, NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setFixedHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(sum, NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setFixedHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tbl.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.TOP);
        cell.setColspan(5);
        cell.setFixedHeight(15);
        tbl.addCell(cell);

        // Table footer
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Summe netto:", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(sum, NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tbl.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Rechnungsbetrag:", BOLD));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(sum, BOLD));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tbl.addCell(cell);

        tbl.setHeaderRows(1);
        tbl.writeSelectedRows(0, -1, rect.getLeft(), rect.getTop() - 350, writer.getDirectContent());
    }

    private void addAusleitung(PdfWriter writer)
    {
        PdfPTable tbl = new PdfPTable(new float[]
        {
            50f, 50f
        });
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Kontoinhaber:", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(dozent.getKInhaber(), NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("Bank:", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(dozent.getKBank(), NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("IBAN:", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(dozent.getKIban(), NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase("BIC:", NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);
        cell = new PdfPCell(new Phrase(dozent.getKBIC(), NORMAL));
        cell.setBorder(Rectangle.NO_BORDER);
        tbl.addCell(cell);

        try
        {
            Rectangle rect = writer.getBoxSize("art");
            PdfContentByte cb = writer.getDirectContent();

            ColumnText ct = new ColumnText(cb);
            ct.setSimpleColumn(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop() - 470);
            Paragraph txt1 = new Paragraph("Umsatzsteuerfrei nach § 4 Nr. 21 Buchstabe a) bb) i.V.m. § 34 AFG bzw. SGB III", BOLD);
            txt1.add(Chunk.NEWLINE);
            txt1.add(Chunk.NEWLINE);
            ct.addElement(txt1);
            Paragraph txt2 = new Paragraph("Der Rechnungsbetrag ist entsprechend der geltenden Vertragsvereinbarung ohne Abzug zu zahlen bis zum 21.ten Tag nach Seminarende auf das unten angegebene Konto.", NORMAL);
            txt2.add(Chunk.NEWLINE);
            txt2.add(Chunk.NEWLINE);
            ct.addElement(txt2);
            Paragraph txt3 = new Paragraph("Bankdaten:", UNDERLINE);
            txt3.add(Chunk.NEWLINE);
            ct.addElement(txt3);
            ct.addElement(tbl);
            Paragraph txt4 = new Paragraph(20, "Bei Überweisungen bitte immer die Rechnungsnummer angeben!", NORMAL);
            txt4.add(Chunk.NEWLINE);
            txt4.add("Ich danke Ihnen für Ihren Auftrag.");
            txt4.add(Chunk.NEWLINE);
            txt4.add(Chunk.NEWLINE);
            txt4.add(Chunk.NEWLINE);
            txt4.add(new Phrase("Unterschrift", BOLDITALIC11));
            ct.addElement(txt4);
            ct.go();

            cb.saveState();
            cb.setColorStroke(BaseColor.BLACK);
            cb.setLineWidth(1.0);
            cb.moveTo(rect.getLeft(), rect.getBottom() + 40);
            cb.lineTo(rect.getLeft() + 200, rect.getBottom() + 40);
            cb.closePath();
            cb.stroke();
            cb.restoreState();

        } catch (DocumentException ex)
        {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * event helper for header and footer
     */
    public static class HeaderFooterPageEvent extends PdfPageEventHelper
    {

        @Override
        public void onStartPage(PdfWriter writer, Document document)
        {
            Rectangle rect = writer.getBoxSize("art");
            try
            {
                String img = "tjlogo.png";
                Image image;

                image = Image.getInstance(getClass().getResource(img));
                image.scalePercent(50); // scale to 50%
                image.setAbsolutePosition(rect.getLeft(), rect.getTop() - 80);
                writer.getDirectContent().addImage(image, true);
            } catch (IOException | DocumentException ex)
            {
                Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // footer with page centered number
        @Override
        public void onEndPage(PdfWriter writer, Document document)
        {
            Rectangle rect = writer.getBoxSize("art");
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Seite " + document.getPageNumber()), (rect.getRight() / 2), rect.getBottom(), 0);
        }
    }

    /**
     * return working day count
     *
     * @param start start date
     * @param end end date
     * @return vcount of working days
     */
    private int countWorkDays(LocalDate start, LocalDate end)
    {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(Date.valueOf(start));
        to.setTime(Date.valueOf(end));
        int wd = 0;
        while (!from.after(to))
        {
            int day = from.get(Calendar.DAY_OF_WEEK);
            if (day != Calendar.SATURDAY || day != Calendar.SUNDAY)
            {
                wd++;
            }
            from.add(Calendar.DAY_OF_MONTH, 1);
        }
        return wd;
    }

}
