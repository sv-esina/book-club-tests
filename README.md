# Проект по автоматизации тестовых сценариев для сайта [Book-Club](https://book-club.qa.guru/)

---
## :clipboard: Содержание:

- [Технологии и инструменты](#технологии-и-инструменты)
- [Сборка в Jenkins](#сборка-в-jenkins)
- [Allure-отчет](#allure-отчет)
  - [Overview](#overview)
  - [Детализаци отчета](#детализация-отчета)
- [Видео выполнения автотеста](#видео-выполнения-автотеста)
- [Интеграция с Allure TestOps](#интеграция-с-allure-testops)
  - [Дашборд](#дашборд)
  - [Тест-кейсы](#тест-кейсы)
- [Интеграция с Jira](#интеграция-с-jira)
- [Уведомление в Telegram](#уведомление-в-telegram)

---

## **Технологии и инструменты:**

<p align="center">
    <a href="https://www.jetbrains.com/ru-ru/idea/">
        <img width="5%" title="IntelliJ IDEA" src="media/icons/Intelij_IDEA.svg"></a>
    <a href="https://gradle.org/">
        <img width="5%" title="Gradle" src="media/icons/Gradle.svg"></a>
    <a href="https://www.java.com/ru/">
        <img width="5%" title="Java" src="media/icons/Java.svg"></a>
    <a href="https://ru.selenide.org/">
        <img width="5%" title="Selenide" src="media/icons/Selenide.svg"></a>
    <a href="https://junit.org/">
        <img width="5%" title="JUnit5" src="media/icons/JUnit5.svg"></a>
    <a href="https://rest-assured.io/"> 
        <img width="5%" src="images/logo/rest_assured.png" title="REST-assured"/> </a>
    <a href="https://rest-assured.io/">
        <img width="5%" title="GitHub" src="media/icons/GitHub.svg"></a>
    <a href="https://aerokube.com/selenoid/">
        <img width="5%" title="Selenoid" src="media/icons/Selenoid.svg"></a>
    <a href="https://allurereport.org/">
        <img width="5%" title="Allure Report" src="media/icons/Allure_Report.svg"></a>
    <a href="https://qameta.io/">
        <img width="4.5%" title="Allure TestOps" src="media/icons/AllureTestOps.svg"></a>
    <a href="https://www.jenkins.io/">
        <img width="5%" title="Jenkins" src="media/icons/Jenkins.svg"></a>
    <a href="https://telegram.org/">
        <img width="5%" title="Telegram" src="media/icons/Telegram.svg"></a>
    <a href="https://www.atlassian.com/software/jira">
        <img width="4.5%" title="Jira" src="media/icons/Jira.svg"></a>
</p>

- В данном проекте представлены автоматизированные тесты разработанные на языке <code>Java</code> с использованием фреймворка <code>Selenide</code>.
- В качестве сборщика использован <code>Gradle</code>.  
- В качестве фреймворка модульного тестирования использован <code>JUnit 5</code>.
- Для прогона тестов в браузере используется [Selenoid](https://aerokube.com/selenoid/).
- Для удаленного запуска тестов реализована джоба в [Jenkins](https://www.jenkins.io/).
- Реализовано формирование [Allure-отчета](https://jenkins.autotests.cloud/view/java_students/job/SvetlanaV_Esina-Jenkins_first-project/26/allure/) с отправкой результатов прогона тестов в <code>Telegram</code> при помощи бота.
- В проекте так же задействована интеграция с [Allure TestOps](https://qameta.io/) и [Jira](https://www.atlassian.com/software/jira).

---

## **Сборка в Jenkins:**
>Запуск сборки осуществляется через раздел `Build with Parameters` путём нажатия кнопки `Build` 

![Jenkins_build_params.png](media/screenshots/Jenkins_build_params.png)

>
![Jenkins.png](media/screenshots/Jenkins.png)

---

## **Allure-отчет:**
### Overview

> Главная страница отчета, которая содержит общую информацию о прохождении тестов:
- <code>ALLURE REPORT</code>: Содержит дату и время прохождения тестов, общее количество кейсов и диаграмму с распределением успешных (passed), упавших (failed) и сломавшихся (broken) тестов.
- <code>TREND</code>: Показывает историю прохождения тестов от сборки к сборке.
- <code>SUITES</code>: Распределение результатов тестов по тестовым наборам (пакетам).
- <code>ENVIRONMENT</code>: Информация о тестовом окружении, на котором запускались тесты (версия браузера, ОС, URL стенда и т.д.).

![Allure_report.png](media/screenshots/Allure_report.png)

### Детализация отчета
> Для детального анализа прохождения тестов используется раздел Suites, где тесты отображаются в виде иерархического дерева, что помогает лучше ориентироваться в результатах.

![Allure_report_steps.png](media/screenshots/Allure_report_steps.png)

---

## **Видео выполнения автотеста в Selenoid:**
> К каждому web-тесту в отчете прилагается видео:

https://github.com/user-attachments/assets/cc4d25b8-a932-41a3-aa17-5328233a6082

---

## **Интеграция с Allure TestOps:**

> Реализована интеграция сборки Jenkins с Allure TestOps.  Запуск сборки можно осуществить из "Allure TestOps".

### Дашборд

> Результат и статистика выполнения автотестов отображается в разделе Dashboard.

![TestOpsDashboard.png](media/screenshots/TestOpsDashboard.png)

### Тест-кейсы
> В разеделе Тест-кейсы представлен список автоматизированных и ручных тестов, реализованных в рамках проекта
![TestOps.png](media/screenshots/TestOps.png)

---

## **Интеграция с Jira:**

> Реализована интеграция Allure TestOps с Jira. В задаче отображен список связанных тестов и результаты их прогонов.

![Jira.png](media/screenshots/Jira.png)

---

## **Уведомление в Telegram с использованием чат-бота:**
> По завершению сборки в чат Telegram автоматически направляется уведомление с результатом прогона тестов. 
> Из уведомления возможен переход в Allure Report по указанной ссылке.

<img width="50%" title="TelegramBot" src="media/screenshots/TelegramBot.png">
