package com.wtz.pdf;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * pdf
 *
 * @author wangtianzeng
 */
@RestController
public class PdfController {

    @PostMapping("pdf/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        try {
            String filename = "test.pdf";
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                filename = java.net.URLEncoder.encode(filename, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");

            out = new BufferedOutputStream(response.getOutputStream());
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            // 水印
            writer.setPageEvent(new Watermark("HELLO ITEXTPDF"));
            // 页眉/页脚
//            writer.setPageEvent(new MyHeaderFooter());

            document.open();
            document.addTitle("Title@PDF-Java");// 标题
            document.addAuthor("Author@umiz");// 作者
            document.addSubject("Subject@iText pdf sample");// 主题
            document.addKeywords("Keywords@iTextpdf");// 关键字
            document.addCreator("Creator@umiz`s");// 创建者

            // 4.向文档中添加内容
            this.generatePDF(document);

            // 5.关闭文档
            document.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    // 定义全局的字体静态变量
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;
    // 最大宽度
    private static int maxWidth = 520;
    // 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfChinese, 16, Font.BOLD);
            headfont = new Font(bfChinese, 14, Font.BOLD);
            keyfont = new Font(bfChinese, 10, Font.BOLD);
            textfont = new Font(bfChinese, 10, Font.NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 生成PDF文件
    public void generatePDF(Document document) throws Exception {
        // 添加图片
        Image image = Image.getInstance("https://img-blog.csdn.net/20180801174617455?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzg0ODcxMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70");
        image.setAlignment(Image.ALIGN_CENTER);
        image.scalePercent(40); //依照比例缩放

        // 段落
//        Paragraph paragraph = new Paragraph("路径还原", titlefont);
//        paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
//        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
//        paragraph.setLeading(20f); //行间距
//        paragraph.setSpacingBefore(5f); //设置段落上空白
//        paragraph.setSpacingAfter(10f); //设置段落下空白
//        paragraph.set(0, image);

        // 直线
//        Paragraph p1 = new Paragraph();
//        p1.add(new Chunk(new LineSeparator()));

        // 点线
//        Paragraph p2 = new Paragraph();
//        p2.add(new Chunk(new DottedLineSeparator()));

        // 超链接
//        Anchor anchor = new Anchor("baidu");
//        anchor.setReference("www.baidu.com");

        // 定位
//        Anchor gotoP = new Anchor("goto");
//        gotoP.setReference("#top");

        // 表格
//        PdfPTable table = createTable(new float[]{40, 120, 120, 120, 80, 80});
        PdfPTable table = createTable(new float[]{120, 120, 120});
//        table.addCell(createCell("美好的一天", headfont, Element.ALIGN_LEFT, 6, false));
//        table.addCell(createCell("早上9:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("中午11:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("中午13:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("下午15:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("下午17:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("晚上19:00", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("牌识路径", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("交易路径", keyfont, Element.ALIGN_CENTER));
//        table.addCell(createCell("还原路径", keyfont, Element.ALIGN_CENTER));
        table.addCell(createCell(image, Element.ALIGN_CENTER));
        table.addCell(createCell(image, Element.ALIGN_CENTER));
        table.addCell(createCell(image, Element.ALIGN_CENTER));
        Integer totalQuantity = 0;
        for (int i = 0; i < 50; i++) {
//            table.addCell(createCell("起床", textfont));
//            table.addCell(createCell("吃午饭", textfont));
//            table.addCell(createCell("午休", textfont));
//            table.addCell(createCell("下午茶", textfont));
//            table.addCell(createCell("回家", textfont));
//            table.addCell(createCell("吃晚饭", textfont));
            for (int j = 0; j < 2; j++){
                table.addCell(createCell("|", textfont));
                table.addCell(createCell("|", textfont));
                table.addCell(createCell("|", textfont));
            }
            table.addCell(createCell("o", textfont));
            table.addCell(createCell("o", textfont));
            table.addCell(createCell("o", textfont));
            totalQuantity++;
        }
//        table.addCell(createCell("总计", keyfont));
//        table.addCell(createCell("", textfont));
//        table.addCell(createCell("", textfont));
//        table.addCell(createCell("", textfont));
//        table.addCell(createCell(String.valueOf(totalQuantity) + "件事", textfont));
//        table.addCell(createCell("", textfont));

//        document.add(paragraph);
//        document.add(anchor);
//        document.add(p2);
//        document.add(gotoP);
//        document.add(p1);
        document.add(table);
//        document.add(image);
    }

    /**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align) throws BadElementException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorder(0);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定图片、水平..）
     * @param image
     * @param align
     * @return
     */
    public PdfPCell createCell(Image image, int align) throws BadElementException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBorder(0);
        cell.setImage(image);
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorderWidthLeft(borderWidth[0]);
        cell.setBorderWidthRight(borderWidth[1]);
        cell.setBorderWidthTop(borderWidth[2]);
        cell.setBorderWidthBottom(borderWidth[3]);
        cell.setPaddingTop(paddingSize[0]);
        cell.setPaddingBottom(paddingSize[1]);
        if (flag) {
            cell.setColspan(2);
        }
        return cell;
    }
/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
    public PdfPTable createTable(int colNumber, int align) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(align);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 创建指定列宽、列数的表格
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 创建空白的表格
     * @return
     */
    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", keyfont));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }
/**--------------------------创建表格的方法end------------------- ---------*/

}