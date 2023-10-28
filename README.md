# Инструкция по запуску приложения

1. В файле application.properties надо указать информацию о PostgreSQL
   (пустая бд с указанным в этом файле названием должна быть создана вручную).
   Путь к файлу: /src/main/resources
2. Находясь в cmd в основной папке проекта(simbirGo) ввести gradlew build
3. cd build/libs
4. java -jar simbirGo-0.0.1-SNAPSHOT.jar

## URL: http://localhost:8080/swagger-ui/index.html

1. В проекте не реализован POST /api/Account/SignOut. Его функции выполняет swagger,
   где можно выйти через кнопку Authorize.
2. Названия параметров lat и long изменены из-за совпадения с используемым Java ключевым словом
3. Ссылка на github - https://github.com/SaYxGal/VolgaITSimbir