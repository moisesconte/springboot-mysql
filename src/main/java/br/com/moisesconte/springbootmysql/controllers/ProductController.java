package br.com.moisesconte.springbootmysql.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import br.com.moisesconte.springbootmysql.repositories.IProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private IProductRepository productRepository;

  @GetMapping("/all")
  public ResponseEntity<?> products() {
    var products = this.productRepository.findAll();

    return ResponseEntity.ok().body(products);
  }

  // itextpdf - melhor opção...
  @GetMapping("/pdf")
  public ResponseEntity<?> gerarPDF2() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos)); // new PdfDocument(new PdfWriter(baos));
    PdfPage page = pdfDocument.addNewPage();
    Document document = new Document(pdfDocument);

    Paragraph paragraph = new Paragraph("Hello, PDF World!");
    document.add(paragraph);

    // Crie uma tabela com 3 colunas
    Table table = new Table(3);

    Paragraph col1Text = new Paragraph("Coluna 1");
    Paragraph col2Text = new Paragraph("Coluna 2");
    Paragraph col3Text = new Paragraph("Coluna 3");
    
    // Adicione células à tabela
    Cell cell1 = new Cell().add(col1Text);
    Cell cell2 = new Cell().add(col2Text);
    Cell cell3 = new Cell().add(col3Text);

    table.addCell(cell1);
    table.addCell(cell2);
    table.addCell(cell3);

    // Adicione a tabela ao documento
    document.add(table);

    document.close();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("sample.pdf", "sample.pdf");

    return ResponseEntity
        .ok()
        .headers(headers)
        .contentLength(baos.size())
        .body(baos.toByteArray());
  }

  // pdfbox
  @GetMapping("/pdf2")
  public ResponseEntity<?> gerarPDF() throws IOException {

    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
      contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
      contentStream.beginText();
      contentStream.newLineAtOffset(100, 700);
      contentStream.showText("Hello, PDF World!");
      contentStream.endText();
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    document.save(baos);
    document.close();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("sample.pdf", "sample.pdf");
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return ResponseEntity
        .ok()
        .headers(headers)
        .contentLength(baos.size())
        .body(baos.toByteArray());

    // Document document = new Document();
    // ByteArrayOutputStream out = new ByteArrayOutputStream();

    // try {
    // PdfWriter.getInstance(document, out);
    // document.open();

    // document.add(new Paragraph("Gerando PDF"));
    // document.close();

    // return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
    // .body(new ByteArrayInputStream(out.toByteArray()));

    // } catch (Exception e) {
    // // TODO: handle exception
    // System.out.println("Error => " + e.getMessage());
    // }

    // return ResponseEntity.ok().body("");

  }
}
