package com.kursova.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvParserUtil {

    private static final Path DEFAULT_CSV_PATH = Path.of("sample-data", "students.csv");

    public static List<String[]> parseStudentsCsv() throws Exception {
        List<String[]> validRecords = new ArrayList<>();

        if (!Files.exists(DEFAULT_CSV_PATH)) {
            throw new Exception("CSV-файл не знайдено за шляхом: " + DEFAULT_CSV_PATH.toAbsolutePath());
        }

        List<String> lines = Files.readAllLines(DEFAULT_CSV_PATH);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");

            if (parts.length != 4) {
                System.err.println("Рядок " + (i + 1) + " пропущено (невірна кількість полів): " + line);
                continue;
            }

            try {
                String firstName = parts[0].trim();
                String lastName = parts[1].trim();
                String email = parts[2].trim();
                LocalDate.parse(parts[3].trim());

                if (firstName.isEmpty() || lastName.isEmpty() || !email.contains("@")) {
                    throw new IllegalArgumentException("Некоректний текст або email");
                }

                validRecords.add(new String[]{firstName, lastName, email, parts[3].trim()});
            } catch (Exception e) {
                System.err.println("Рядок " + (i + 1) + " містить помилку даних: " + e.getMessage());
            }
        }

        return validRecords;
    }

    public static void main(String[] args) {
        try {
            List<String[]> students = parseStudentsCsv();
            System.out.println("Успішно розібрано коректних записів із CSV: " + students.size());
            for (String[] s : students) {
                System.out.println("Студент: " + s[0] + " " + s[1] + " | Email: " + s[2] + " | Дата: " + s[3]);
            }
        } catch (Exception e) {
            System.err.println("Помилка читання CSV: " + e.getMessage());
        }
    }
}
