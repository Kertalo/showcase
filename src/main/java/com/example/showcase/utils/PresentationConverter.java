package com.example.showcase.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PresentationConverter {

    /***
     * Получение картинок в виде списка Resources из потока pptx-файла презентации
     * (ВОЗМОЖНЫ АРТЕФАКТЫ КОНВЕРТАЦИИ)
     * @param pptxInputStream
     * @return
     * @throws IOException
     */
    public static List<Resource> getPngFromPptxAll(InputStream pptxInputStream) throws IOException {
        List<Resource> pngResources = new ArrayList<>();
        XMLSlideShow ppt = new XMLSlideShow(pptxInputStream);
        Dimension pgsize = ppt.getPageSize();

        for (Slide<?, ?> slide : ppt.getSlides()) {
            BufferedImage img = new BufferedImage(
                    pgsize.width, pgsize.height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(
                    0, 0, pgsize.width, pgsize.height));
            slide.draw(graphics);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(img, "png", bout);
            pngResources.add(new ByteArrayResource(bout.toByteArray()));
            bout.close();
            graphics.dispose();
        }
        return pngResources;
    }

    /***
     * Получение картинки титульного слайда из потока pptx-файла презентации
     * (ВОЗМОЖНЫ АРТЕФАКТЫ КОНВЕРТАЦИИ)
     * @param pptxInputStream
     * @return
     * @throws IOException
     */
    public static Resource getPngFromPptxTitlePage(InputStream pptxInputStream) throws IOException {
        return getPngFromPptxAll(pptxInputStream).get(0);
    }

    /***
     * Получение картинок в виде списка Resources из потока pdf-файла презентации
     * @param pdfInputStream
     * @return
     * @throws IOException
     */
    public static List<Resource> getPngFromPdfAll(InputStream pdfInputStream) throws IOException {
        List<Resource> pngResources = new ArrayList<>();
        PDDocument document = PDDocument.load(pdfInputStream);
        PDFRenderer renderer = new PDFRenderer(document);

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage img = renderer.renderImageWithDPI(i, 300);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(img, "png", bout);
            pngResources.add(new ByteArrayResource(bout.toByteArray()));
            bout.close();
        }
        document.close();

        return pngResources;
    }

    /***
     * Получение картинки титульного слайда из потока pdf-файла презентации
     * @param pdfInputStream
     * @return
     * @throws IOException
     */
    public static Resource getPngFromPdfTitlePage(InputStream pdfInputStream) throws IOException {
        return getPngFromPdfAll(pdfInputStream).get(0);
    }
}

