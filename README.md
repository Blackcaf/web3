# Лабораторная работа №3

Веб-приложение для проверки попадания точки в заданную область с интеграцией Apache Kafka для сбора статистики.

## Технологический стек

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 17 | Язык программирования |
| Jakarta EE | 10 | CDI, JSF 4.0, JPA 3.1 |
| PrimeFaces | 14.0.0 | UI компоненты |
| EclipseLink | 4.0.2 | Реализация JPA |
| PostgreSQL | 42.7.1 | База данных |
| Apache Kafka | 3.6.1 | Брокер сообщений |
| Jackson | 2.16.1 | JSON сериализация |
| Docker | - | Контейнеризация |
| Gradle | - | Сборка проекта |

---

## Kafka: Мониторинг и управление

### Просмотр топиков
```bash
# Войти в контейнер Kafka
docker exec -it kafka bash

# Список всех топиков
/opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092

# Информация о топике results-topic
/opt/kafka/bin/kafka-topics.sh --describe --topic results-topic --bootstrap-server localhost:9092
```

### Просмотр сообщений в реальном времени
```bash
# Читать все новые сообщения
/opt/kafka/bin/kafka-console-consumer.sh --topic results-topic --bootstrap-server localhost:9092

# Читать с начала (все сообщения)
/opt/kafka/bin/kafka-console-consumer.sh --topic results-topic --bootstrap-server localhost:9092 --from-beginning

# Читать последние 10 сообщений
/opt/kafka/bin/kafka-console-consumer.sh --topic results-topic --bootstrap-server localhost:9092 --from-beginning --max-messages 10
```

### Просмотр consumer groups
```bash
# Список групп потребителей
/opt/kafka/bin/kafka-consumer-groups.sh --list --bootstrap-server localhost:9092

# Детальная информация о группе
/opt/kafka/bin/kafka-consumer-groups.sh --describe --group statistics-group --bootstrap-server localhost:9092
```
---

## Структура проекта

```
src/main/java/org/example/
├── beans/                      # JSF Managed Beans
│   ├── AreaCheckBean.java      # @ViewScoped - обработка формы проверки
│   ├── ResultsBean.java        # @ApplicationScoped - список результатов + WebSocket
│   ├── StartPageBean.java      # @RequestScoped - данные стартовой страницы
│   └── StatisticsBean.java     # @ApplicationScoped - отображение статистики
│
├── config/                     # Конфигурация
│   ├── AppStartupListener.java # Инициализация при старте приложения
│   └── Config.java             # Константы (допустимые X, Y, R)
│
├── dto/                        # Data Transfer Objects
│   ├── ResultDTO.java          # DTO результата проверки
│   └── StatisticsDTO.java      # DTO статистики
│
├── entities/                   # JPA сущности
│   └── ResultEntity.java       # Сущность результата в БД
│
├── event/                      # CDI события
│   └── ResultAddedEvent.java   # Событие добавления результата
│
├── kafka/                      # Kafka интеграция
│   ├── KafkaProducerConfig.java    # Конфигурация Producer
│   ├── ResultKafkaProducer.java    # Отправка в Kafka
│   └── ResultKafkaConsumer.java    # Получение из Kafka
│
├── repository/                 # Слой данных
│   └── ResultRepository.java   # JPA репозиторий
│
├── service/                    # Бизнес-логика
│   ├── ResultEventPublisher.java   # Интерфейс публикации событий
│   ├── ResultService.java          # Сервис результатов
│   └── StatisticsService.java      # Сервис статистики
│
├── util/                       # Утилиты
│   └── AreaCheckUtil.java      # Проверка попадания точки
│
└── validators/                 # JSF валидаторы
    ├── XValidator.java         # Валидация X
    ├── YValidator.java         # Валидация Y
    └── RValidator.java         # Валидация R
```

### Веб-ресурсы

```
src/main/webapp/
├── start.xhtml                 # Стартовая страница с часами
├── main.xhtml                  # Основная страница (форма + график)
├── statistics.xhtml            # Страница статистики
│
├── resources/styles/
│   ├── reset.css               # CSS сброс
│   ├── main.css                # Основные стили
│   └── clock.css               # Стили часов
│
├── scripts/
│   ├── area.js                 # Интерактивный график
│   ├── clock.js                # Анимированные часы
│   └── main.js                 # Вспомогательные функции
│
└── WEB-INF/
    ├── web.xml                 # Конфигурация сервлетов
    ├── beans.xml               # CDI конфигурация
    └── faces-config.xml        # JSF навигация
```

### Конфигурационные файлы

```
├── build.gradle                # Gradle конфигурация и зависимости
├── docker-compose.yml          # Docker Compose для Kafka
├── settings.gradle             # Настройки Gradle проекта
└── src/main/resources/
    └── META-INF/
        └── persistence.xml     # JPA конфигурация (PostgreSQL)
```

---

## Статистика (Statistics Service)

Метрики, вычисляемые в реальном времени:

| Метрика | Описание |
|---------|----------|
| `totalRequests` | Общее количество проверок |
| `hitCount` | Количество попаданий |
| `missCount` | Количество промахов |
| `hitRate` | Процент попаданий (%) |
| `averageX` | Среднее значение X |
| `averageY` | Среднее значение Y |
| `averageR` | Среднее значение R |
| `mostFrequentX` | Наиболее частый X |
| `mostFrequentR` | Наиболее частый R |
| `averageExecutionTimeMs` | Среднее время проверки (мс) |

---

## Область проверки

Область состоит из трёх фигур:
- **Прямоугольник**: x ∈ [0, R], y ∈ [0, R]
- **Треугольник**: x ∈ [-R/2, 0], y ∈ [0, R], ограничен линией y = 2x + R
- **Четверть круга**: x ∈ [0, R], y ∈ [-R, 0], x² + y² ≤ R²

---

## Автор

**Мантуш Даниил Валерьевич**  
Группа: P3219  
ИТМО, 2025


