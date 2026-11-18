# Інформаційна система керування навчальними курсами та зарахуванням (Варіант № 26)

Клієнт–серверна інформаційна система для обліку навчальних курсів, слухачів, контрольних завдань та автоматизованого оцінювання успішності навчання.

## Використані технології
* **Мова та збірка:** Java 21, Apache Maven
* **Серверна частина:** Servlet API 4.0.1 javax.servlet, Apache Tomcat 9.x, Jackson Databind
* **База даних:** MySQL (XAMPP), JDBC PreparedStatement
* **Клієнтська частина:** JavaFX 21 HTTP/JSON клієнт java.net.http.HttpClient
* **Тестування:** JUnit 5

## Структура проєкту
* server/ — серверний web-модуль ( DAO, Service, Servlet, модульні тести JUnit 5).
* client/ — графічний клієнт JavaFX із підтримкою фонового CSV-імпорту (JavaFX Task + ProgressBar).
* sample-data/ — вхідні CSV-файли для імпорту студентів (students.csv).
* database.sql — SQL-скрипт розгортання структури БД course_management_db та демонстраційних даних.

## Підготовка бази даних MySQL
1. Запустіть модулі **Apache** та **MySQL** у панелі керування XAMPP.
2. Відкрийте http://localhost/phpmyadmin у браузері.
3. Перейдіть на вкладку **SQL**, вставте вміст файлу database.sql та натисніть **Go**.

## Команди складання, тестування та запуску сервера
Відкрийте термінал у директорії server/:
1. **Запуск модульних тестів JUnit 5 (працюють автономно без БД і Tomcat):**
   ```bash
   mvn clean test
   ```
2. **Складання WAR-архіву:**
   ```bash
   mvn clean package
   ```
3. Скопіюйте створений архів server/target/server.war у каталог webapps/ сервера **Apache Tomcat 9.x** та запустіть сервер (startup.bat).

## Запуск JavaFX-клієнта
Після запуску MySQL та Apache Tomcat відкрийте модуль client в IntelliJ IDEA та запустіть головний клас **com.kursova.Launcher**.

## Формат та використання CSV-файлу (sample-data/students.csv)
Імпорт слухачів виконується у фоновому потоці за відносним шляхом sample-data/students.csv у кодуванні UTF-8 (роздільник — кома):
* **Заголовок:** 
* first_name,last_name,email,registration_date
* **Приклад рядка:** 
* Андрій,Бабай,babay@example.com,2026-11-01
