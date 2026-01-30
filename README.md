# Информационно-справочная система волонтёрских мероприятий

Веб-приложение для публикации, поиска и участия в волонтёрских мероприятиях с разграничением прав доступа.

**Как выложить проект на GitHub:** пошаговая инструкция в файле [GITHUB_SETUP.md](GITHUB_SETUP.md).

## Технологии

- **Бэкенд:** Java 17+, Spring Boot 3.x, Spring MVC, Spring Data JPA (Hibernate), Spring Security, Thymeleaf
- **БД:** PostgreSQL
- **Фронтенд:** HTML5, CSS3 (Bootstrap 5), Thymeleaf

## Настройка БД (pgAdmin / PostgreSQL)

1. Создайте базу данных в pgAdmin:
   - Правый клик по «Databases» → Create → Database
   - Имя: `volunteer_db`
   - Owner: `postgres` (или ваш пользователь)

2. В `src/main/resources/application.properties` укажите свои данные подключения:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/volunteer_db
   spring.datasource.username=postgres
   spring.datasource.password=ваш_пароль
   ```
   Таблицы создаются автоматически при первом запуске (`spring.jpa.hibernate.ddl-auto=update`).

## Запуск

**Подробная пошаговая инструкция по сборке и запуску через Maven:** см. файл **[BUILD_AND_RUN.md](BUILD_AND_RUN.md)**.

Кратко:
```bash
mvn spring-boot:run
```

Или соберите JAR и запустите:
```bash
mvn clean package -DskipTests
java -jar target/volunteer-base-1.0.0.jar
```

Приложение будет доступно по адресу: http://localhost:8080

## Первый вход

После первого запуска создаётся учётная запись администратора:
- **Email:** admin@volunteer.ru  
- **Пароль:** admin  

Рекомендуется сменить пароль после первого входа.

## Роли

- **Administrator:** управление пользователями (роли, блокировка), утверждение мероприятий, просмотр мероприятий и волонтёров.
- **Coordinator:** создание мероприятий, обработка откликов (утверждение/отклонение/резерв), выставление статуса и часов волонтёрам, просмотр волонтёров.
- **Volunteer:** регистрация на мероприятия, просмотр мероприятий, просмотр своих участий и начисленных часов.

## Об авторе

- **ФИО:** Махойдина Мария Дмитриевна  
- **Группа:** ИД23-2  
- **Контакты:** 233527@edu.fa.ru  
- **Технологии:** Java, Spring Boot, PostgreSQL, Bootstrap  
