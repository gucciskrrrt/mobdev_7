# MyMessenger - MVVM Architecture

## Реализованный функционал

### Лабораторная работа 2: MVVM и управление состоянием

 **Архитектура MVVM**
- Разделение UI и бизнес-логики
- ViewModel для Profile и Settings
- LiveData для реактивного обновления

 **ProfileViewModel**
- Хранение имени пользователя
- Хранение статуса пользователя
- Методы обновления данных

 **SettingsViewModel**
- Хранение состояния темы
- Переключение светлой/темной темы

 **Сохранение состояния**
- Данные не теряются при поворотах экрана
- ViewModel переживает configuration changes
- LiveData автоматически обновляет UI

## Структура

```
app/src/main/java/com/example/mymessenger/
├── MainActivity.kt
├── FeedFragment.kt
├── ProfileFragment.kt
├── ProfileViewModel.kt      
├── SettingsFragment.kt
└── SettingsViewModel.kt    
```

## Сборка

```bash
./gradlew assembleDebug
```

## Установка

```bash
./gradlew installDebug
```

