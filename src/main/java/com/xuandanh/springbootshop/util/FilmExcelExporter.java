package com.xuandanh.springbootshop.util;

import com.xuandanh.springbootshop.domain.Film;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FilmExcelExporter {
    static String SHEET = "films";
    private final Workbook workbook = new XSSFWorkbook();
    private final Sheet sheet = workbook.createSheet(SHEET);
    private final List<Film>filmList;

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

    public FilmExcelExporter(List<Film>filmList){
        this.filmList = filmList;
    }

    public void writeHeaderLine(){
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        createCell(row, 0, "filmId", style);
        createCell(row, 1, "filmName", style);
        createCell(row, 2, "title", style);
        createCell(row, 3, "rentalDuration", style);
        createCell(row, 4, "rentalRate", style);
        createCell(row, 5, "replacementCost", style);
        createCell(row, 6, "rating", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void writeDataLines(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFont(font);
        for (Film film: filmList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,film.getFilmId(), style);
            createCell(row, columnCount++, film.getFilmName(), style);
            createCell(row, columnCount++, film.getRentalDuration(), style);
            createCell(row, columnCount++, film.getTitle(), style);
            createCell(row, columnCount++, film.getRentalRate(), style);
            createCell(row, columnCount++, film.getReplacementCost(), style);
            createCell(row, columnCount++, film.getRating(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
