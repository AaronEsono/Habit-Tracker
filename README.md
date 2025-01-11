💻 Habit Tracker

- Habit Tracker es una app para mantener controlados tus hábitos y poder hacer un seguimiento de ellos día a día.

📘 Apartados

- Herramientas utilizadas
- Otros apartados
- Cómo probar la app
- Cómo usar
- Uso de la app

🛠️ Herramientas utilizadas

- Jetpack Compose
- Navegación de tipado seguro
_ Arquitectura MVVM
- Corrutinas
- Room
- DataStore
- Inyección de dependencias (Dagger-Hilt)
- AlarmManager
- Firebase Auth
- Firebase Crashlytics
- Firebase Analytics
- Firebase Firestore
- Credential Manager

🛠️ Otros apartados

- Varios temas (4 temas)
- Traducción de la aplicación al inglés y al español con stringResources

✨ Cómo probar la app
- Dentro de app/app_apk teneis la apk de la aplicación.
- También teneis un QR por si lo quereis descargar de esa forma.

✨ Cómo usar

- Clona el proyecto con: https://github.com/AaronEsono/Habit-Tracker.git
- Actualiza la versión de Gradle
- Con esto, el proyecto no funcionará, ya que faltaría implementar vuestro fichero de Firebase, llamado google-services.json.
- Para crear vuestro fichero de Firebase, cread un nuevo proyecto aquí: https://console.firebase.google.com/u/0/
- Dentro de este, os pedirá el nombre del paquete, el cual es este: aeb.proyecto.habittracker
- Pegad el fichero donde se indica.

🚀 Uso de la app

- Pulsa el botón de arriba en la pantalla de hábitos para crear uno.
- Rellena el nombre del hábito y las veces que lo quieres hacer al día.
- Una vez creado, pulsando el botón +, sumas 1 al contador de veces hecho.
- En el apartado de opciones, puedes cambiar el tema y registrarte en la app para guardar tu progreso en la nube y transferir los datos a otro teléfono si lo necesitas.
- Todavía en la versión lanzada no se puede autenticar con Google porque me falta la clave. Sin embargo, si clonáis el proyecto, podéis autenticaros de esta manera.
- Si no has podido rellenar hábitos de días anteriores, pulsa en el hábito y, en el botón del calendario, podrás marcar los días anteriores.
