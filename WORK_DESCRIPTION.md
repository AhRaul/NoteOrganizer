# NoteOrganizer
Краткое описание проделанной работы:
## entity 
чисто конфигурации (БД и SharedPreferences)
#### data_base
  - DataBase.java
  Основной класс конфигурации БД Room. Просто предоставляет доступ к интерфейсам той или иной базы.
  - NoteDAO.java
  Основной интерфейс работы с Note DB. CRUD система.
  - PersistenceManager.java
  Класс предоставления доступа к NoteDAO::obj. Пока не юзается, но хочется через него сделать предоставление доступа к БД и SP.
  
#### shared_prefs
- SharedPreferencesManager.java
  Основной класс для сохранения локальных настроек приложения. Так же предоставляет доступ к этим сохраненным настройкам.
- AppConfig.java
  Конфигурационный класс, который создает доступы к БД и SP при запуске приложения.
## notes 
#### model
- Note.java
#### presenter
- BasePresenter.java
  Описывает интерфейс базового презентера для списка заметок. 1 метод - notifyDatasetChanged()
- INotesPresenter.java
  Описывает именно интерфейс INotesPresenter - биндинг, кликанье на item и тд.
- NotesPresenter.java
  Реализация презентера для нашего фрагмента NotesFragment.java. 
- SingleNotePresenter.java
  Реализация презентера для нашего активити SingleNotePresenter.java. 
#### view
- MyAdapter.java
  Адаптер для RecyclerView, который задается в NotesFragment.java
- NotesBottomDialogFragment.java
- SingleNoteActivity.java
  Два класса, потом решим, что лучше. Один реализует через активити, другой - через фрагмент. Смысл один - просмотр записи, редактирование или создание новой записи.
- NotesFragment.java
  Вьюшка для отображения списка заметок.
## settings
------------
## todos 
------------
- MainAcrivity.java
Главная активити. Туда можно НЕ лезть, просто сделана навигация по приложению.
- NoteDaoImpl.java
Главный класс по ассинхронной работе с Note DB. Лучше всего вызывать наследовать презентер от этого класса и оверайдить методы нужные. Потом сделаем его генериком.
- BaseActivity.java
Описывает базовую активити с 2 функциями - установка темы приложения и сокрытие клавиатуры, если есть текстовое поле
