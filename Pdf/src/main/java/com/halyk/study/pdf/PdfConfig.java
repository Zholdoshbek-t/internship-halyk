package com.halyk.study.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class PdfConfig {

    private final Environment environment;


    @Bean
    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix(environment.getProperty("pdf.templates.path"));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
        templateResolver.setCheckExistence(true);

        return templateResolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        templateEngine.setTemplateResolver(classLoaderTemplateResolver());

        return templateEngine;
    }

    @Bean(name = "local")
    public DateTimeFormatter localDateFormat() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Bean(name = "full")
    public DateTimeFormatter fullDateTimeFormat() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    }

    @Bean(name = "save")
    public DateTimeFormatter savePdfDateFormat() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Bean
    public ITextRenderer iTextRenderer() throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();

        renderer.getFontResolver()
                .addFont(environment.getProperty("pdf.font.family"), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        return renderer;
    }

}
