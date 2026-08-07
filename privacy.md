# Política de Privacidad de Habit Tracker

**Fecha de actualización:** 06/08/2026

Esta Política de Privacidad ha sido elaborada de conformidad con el Reglamento (UE) 2016/679 del Parlamento Europeo y del Consejo (Reglamento General de Protección de Datos o RGPD), la Ley Orgánica 3/2018 (LOPDGDD) y teniendo en consideración otras normativas internacionales de protección de datos que puedan resultar aplicables.

---

## 1. Información que recopilamos y cómo se almacena

La aplicación almacena los datos que el usuario introduce voluntariamente para gestionar sus hábitos: nombre, descripción, estado, registro de realización y notificaciones programadas.

A su vez, el usuario es responsable del contenido que introduce en la aplicación. Se recomienda no introducir información especialmente sensible (datos de salud, financieros, etc.) que no sea estrictamente necesaria para el seguimiento de sus hábitos.

Por defecto, los datos se almacenan de forma exclusivamente local en tu dispositivo (a través de Room y DataStore). Si el usuario no lo requiere, estos datos no se subirán a ningún servidor y el desarrollador no tiene acceso a dichos datos.

Si el usuario crea una cuenta y activa la sincronización, los datos introducidos en la aplicación se almacenarán en **Google Firebase Firestore**, un servicio de Google Firebase utilizado como proveedor de almacenamiento y sincronización de datos.

Puede obtener más información sobre cómo Google trata los datos personales consultando su Política de Privacidad:
* [Política de Privacidad de Google](https://policies.google.com/privacy)
* [Privacidad y Seguridad en Firebase](https://firebase.google.com/support/privacy)

---

## 2. Servicios de terceros y Analítica

La aplicación utiliza determinados servicios proporcionados por Google Firebase con el fin de ofrecer una mejor experiencia de usuario, garantizar el correcto funcionamiento de la aplicación y mejorar su rendimiento.

En particular, la aplicación utiliza los siguientes servicios:

* **Firebase Authentication:** para la autenticación y gestión de las cuentas de usuario.
* **Cloud Firestore:** para almacenar y sincronizar los datos del usuario cuando este decide crear una cuenta y habilitar la sincronización en la nube.
* **Firebase Analytics:** para recopilar información estadística y de uso de la aplicación, permitiendo analizar su funcionamiento y mejorar la experiencia de los usuarios.
* **Firebase Crashlytics:** para detectar, registrar y analizar errores técnicos o fallos inesperados de la aplicación con el objetivo de corregir incidencias y mejorar su estabilidad.

La información recopilada por estos servicios puede incluir datos técnicos del dispositivo, información sobre el sistema operativo, la versión de la aplicación, identificadores de instalación, eventos de uso y registros de errores. Estos datos se utilizan exclusivamente para fines de funcionamiento, mantenimiento, análisis y mejora de la aplicación.

---

## 3. Base Legítima y Transferencia Internacional de Datos

* **Base Jurídica:** El tratamiento de los datos sincronizados en la nube se basa en la ejecución del servicio solicitado por el usuario. Cuando la normativa aplicable lo exija, determinadas funcionalidades podrán requerir el consentimiento previo del usuario.
* **Transferencia Internacional:** La sincronización en la nube mediante Firebase puede implicar el procesamiento de datos en servidores de Google ubicados fuera del Espacio Económico Europeo (EEE), contando con las garantías y acuerdos de transferencia internacional de Google conforme a la normativa vigente.

---

## 4. Permisos solicitados

Para ofrecer todas sus funcionalidades, la aplicación solicitará los siguientes permisos en tu dispositivo:

* **Notificaciones (`POST_NOTIFICATIONS`):** Necesario para enviarte recordatorios de tus hábitos a los días y horas que el usuario haya establecido.
* **Mostrar sobre otras aplicaciones (`SYSTEM_ALERT_WINDOW`):** Requerido única y exclusivamente si utilizas la función de temporizador en modo flotante (*overlay*) mientras usas otras aplicaciones.

Puedes revocar estos permisos en cualquier momento desde los ajustes de tu sistema operativo.

---

## 5. Conservación de los datos

* **Datos almacenados localmente:** Permanecerán en el dispositivo del usuario hasta que este los elimine manualmente o desinstale la aplicación.
* **Datos sincronizados en la nube:** Permanecerán almacenados mientras la cuenta del usuario permanezca activa o hasta que el usuario solicite su eliminación o elimine su cuenta.

Una vez eliminados los datos o la cuenta, podrán mantenerse temporalmente copias de seguridad durante el tiempo estrictamente necesario por motivos técnicos o legales, tras lo cual serán eliminadas de forma definitiva.

---

## 6. Control, eliminación de datos y Derechos del Usuario

### A. Gestión de tus datos:
* **Datos locales:** Puedes borrar todos los datos locales desinstalando la aplicación o limpiando el almacenamiento interno desde la configuración de tu teléfono.
* **Datos en la nube:** Si has iniciado sesión con Google, puedes solicitar la eliminación completa de tus datos o la eliminación de tu cuenta directamente desde la aplicación, o contactando con el desarrollador.

### B. Derechos de protección de datos:
Como usuario, tienes derecho a:
* Acceder a tus datos personales.
* Rectificar datos inexactos.
* Solicitar la supresión de tus datos (Derecho al olvido).
* Solicitar la limitación del tratamiento de tus datos en los casos previstos por la normativa aplicable.
* Oponerte al tratamiento de tus datos personales cuando proceda.
* Solicitar la portabilidad de tus datos personales, cuando sea técnicamente posible y legalmente aplicable.
* Revocar el consentimiento otorgado en cualquier momento, sin que ello afecte a la licitud del tratamiento realizado con anterioridad a dicha revocación.

Para ejercer cualquiera de estos derechos, puedes enviar un correo al desarrollador a la dirección indicada al final de este documento.

---

## 7. Privacidad de Menores

La aplicación no está dirigida a menores de la edad mínima exigida por la legislación aplicable para prestar válidamente el consentimiento al tratamiento de datos personales. No recopilamos a sabiendas información personal de menores. Si se detecta que se han almacenado datos en la nube de un menor sin consentimiento tutelar, se procederá a su eliminación inmediata.

---

## 8. Seguridad de la información

Se adoptan medidas técnicas y organizativas razonables para proteger los datos personales frente al acceso no autorizado, la pérdida, la modificación o la divulgación. No obstante, ningún método de transmisión o almacenamiento electrónico puede garantizar una seguridad absoluta.

---

## 9. Cambios en esta política

Nos reservamos el derecho de actualizar esta Política de Privacidad si añadimos nuevas funciones. Te recomendamos revisar esta página periódicamente para estar al tanto de cualquier cambio.

---

## 10. Contacto

Si tienes alguna pregunta o duda sobre esta política de privacidad, puedes contactar con el desarrollador con la siguiente información:
* **Responsable del tratamiento:** Aaron Esono
* **Correo electrónico:** x@gmail.com
* **País:** España

La aplicación ofrece una función para reportar problemas mediante el cliente de correo electrónico del usuario. En ese caso, el borrador del mensaje podrá incluir información técnica como la versión de la aplicación, el modelo del dispositivo, la zona horaria o la fecha y hora, con el único fin de facilitar la resolución de incidencias. El usuario podrá revisar, modificar o eliminar dicha información antes de enviar el correo.