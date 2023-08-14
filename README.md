# WebFlux project + JWT + Spring Security + Swagger

С Spring Security 6 возникли проблемы при прикручивании ее к WebFlux, решил откатиться на 5 версию, так как на ней гораздо
больше материалов для изучения и решение на 6 версии могло сильно затянуться по времени. WebFlux реализовал через контроллеры,
хотя изначально пытался делать через роутер и хендлеры, но возникли сложности с секьюрити, также подход с контроллерами
оказалось проще отлаживать. Тесты реализовывал с использованием WebTestClient. 

Скриншот тест кейсов:

![test_cases .png](src/main/resources/tests.png)

Сваггер (http://localhost:8080/api/swagger-ui.html):

![swagger.png](src/main/resources/swagger.png)