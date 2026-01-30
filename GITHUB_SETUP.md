# Как выложить проект на GitHub

## Шаг 1. Установить Git (если ещё не установлен)

1. Скачайте Git для Windows: https://git-scm.com/download/win  
2. Установите с настройками по умолчанию.  
3. Проверьте в командной строке или PowerShell:
   ```bash
   git --version
   ```

---

## Шаг 2. Создать репозиторий на GitHub

1. Войдите на https://github.com (или зарегистрируйтесь).  
2. Нажмите **«+»** в правом верхнем углу → **«New repository»**.  
3. Заполните:
   - **Repository name:** например `volunteer-base` или `volunteer-base-id23-2`
   - **Description:** по желанию (например: «Информационно-справочная система волонтёрских мероприятий»)
   - **Public** или **Private** — на ваш выбор
   - **НЕ** ставьте галочку «Add a README file» (у вас уже есть свой проект)
4. Нажмите **«Create repository»**.  
5. Скопируйте URL репозитория — он понадобится дальше, например:
   - `https://github.com/ВАШ_ЛОГИН/volunteer-base.git`
   - или `git@github.com:ВАШ_ЛОГИН/volunteer-base.git` (если настроен SSH)

---

## Шаг 3. Открыть папку проекта в терминале

В **PowerShell** или **cmd** перейдите в каталог проекта (где лежит `pom.xml`):

```bash
cd "c:\Users\User\OneDrive\Volunteer base"
```

---

## Шаг 4. Инициализировать Git в проекте

Если в этой папке ещё **нет** папки `.git`, выполните:

```bash
git init
```

Появится сообщение: `Initialized empty Git repository in ...`

---

## Шаг 5. Добавить файлы и сделать первый коммит

1. Добавить все файлы в индекс (папка `target/` и служебные файлы IDE в индекс не попадут благодаря `.gitignore`):

   ```bash
   git add .
   ```

2. Проверить, что будет закоммичено (по желанию):

   ```bash
   git status
   ```

3. Создать первый коммит:

   ```bash
   git commit -m "Initial commit: volunteer base project"
   ```

---

## Шаг 6. Привязать удалённый репозиторий и отправить код

1. Подставить вместо `ВАШ_ЛОГИН` и `volunteer-base` свои значения (URL из шага 2):

   ```bash
   git remote add origin https://github.com/ВАШ_ЛОГИН/volunteer-base.git
   ```

   Пример: если ваш логин `mahoidina`, а репозиторий `volunteer-base`:
   ```bash
   git remote add origin https://github.com/mahoidina/volunteer-base.git
   ```

2. Указать основную ветку (обычно `main`):

   ```bash
   git branch -M main
   ```

3. Отправить коммиты на GitHub:

   ```bash
   git push -u origin main
   ```

4. При первом `git push` браузер или Git может запросить вход в GitHub — войдите под своим аккаунтом.  
   Если используете **HTTPS** и просят пароль — используйте **Personal Access Token** (GitHub больше не принимает обычный пароль для push).  
   Создать токен: GitHub → **Settings** → **Developer settings** → **Personal access tokens** → **Generate new token**.

---

## Шаг 7. Проверка

Откройте в браузере страницу вашего репозитория на GitHub.  
Должны отображаться все файлы проекта (без папки `target/` и без локальных настроек из `.gitignore`).

---

## Важно: не выкладывать пароли

В репозитории **не должно быть** паролей от БД. В проекте пароль задаётся в `application.properties`.  

**Рекомендация:**  
- Либо не коммитить свой реальный пароль: в `application.properties` оставить плейсхолдер (`your_password`) и в инструкции (README) написать «подставьте свой пароль».  
- Либо вынести локальные настройки в отдельный файл, например `application-local.properties`, добавить его в `.gitignore` и в README описать, что нужно создать этот файл со своими данными.

Файл `.gitignore` в проекте уже добавлен; в нём указано игнорировать `application-local.properties`, если вы решите использовать такой подход.

---

## Краткая шпаргалка команд

| Действие | Команда |
|----------|--------|
| Инициализация репозитория | `git init` |
| Добавить все файлы | `git add .` |
| Сделать коммит | `git commit -m "Сообщение"` |
| Привязать GitHub | `git remote add origin https://github.com/ЛОГИН/РЕПОЗИТОРИЙ.git` |
| Отправить на GitHub | `git push -u origin main` |
| Дальнейшие изменения | `git add .` → `git commit -m "Описание"` → `git push` |
