### Запуск программы

- сервер: `./gradlew :server:run --args="username=<username> password=<password>"`
- клиент: `./gradlew :client:run`
### опциональные аргументы для сервера
- host - хост сервера базы данных, по умолчанию localhost
- port - порт для подключеня к БД, по умолчанию 5432
- database - имя базы данных, по умолчанию studs