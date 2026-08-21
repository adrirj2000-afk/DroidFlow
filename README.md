# DroidFlow 🤖 ⚡

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![License](https://img.shields.io/badge/License-Copyright_ZortVibes-blue?style=for-the-badge)

**DroidFlow** es una potente aplicación de automatización nativa para Android, desarrollada con Kotlin y Jetpack Compose. Permite a los usuarios crear rutinas automáticas (Flujos) vinculando eventos del sistema (Cuándo) con acciones del dispositivo (Entonces), sin necesidad de root ni de configuraciones complejas.

<p align="center">
  <i>Desarrollada por ZortVibes.</i>
</p>

---

## 🚀 Características (Features)

DroidFlow está diseñado con un motor en segundo plano extremadamente ligero y respetuoso con la batería, impulsado por Coroutines y BroadcastReceivers.

### ⚡ Disparadores (Triggers)
Eventos del sistema que inician un flujo:
- 📩 **Mensajería y Llamadas:** Interceptación de Notificaciones (ej. WhatsApp), llamadas entrantes y SMS recibidos en tiempo real.
- 🕒 **Hora exacta:** Alarmas programadas.
- 🔋 **Batería y Carga:** Batería baja, carga completada (100%), cargador conectado/desconectado.
- 🎧 **Hardware:** Conexión de auriculares.
- 📱 **Pantalla:** Encendido y apagado de pantalla (Detección en tiempo real).
- 📡 **Conectividad:** Bluetooth, WiFi conectado/desconectado, Modo Avión.
- 📱 **Software:** Apertura de aplicaciones específicas.

### 🛠 Acciones (Actions)
Tareas que el motor ejecutará automáticamente:
- **Comunicación Silenciosa:** Rechazar llamadas silenciosamente y enviar SMS invisibles en segundo plano, abrir chats de WhatsApp.
- **Botones Virtuales:** Simulación de pulsación del botón 'Inicio', 'Atrás' o 'Recientes' (Útil para Filtro Anti-Mirones).
- **Dispositivo:** Control de brillo, volumen, linterna, modo no molestar (DND), ahorro de batería.
- **Conectividad:** Control de Bluetooth y Wi-Fi.
- **Multimedia:** Reproducir/Pausar música, pasar de canción, reproducir sonidos de alerta, Texto-A-Voz (TTS).
- **Sistema:** Abrir aplicaciones, cambiar fondo de pantalla, bloquear/apagar pantalla.
- **Avanzado:** Notificaciones personalizadas, vibraciones hápticas, y Peticiones HTTP para integraciones con domótica.

---

## 🏗 Arquitectura y Tecnologías
El proyecto está estructurado bajo principios limpios para ser escalable y testeable:
- **UI:** Jetpack Compose (Material Design 3) con soporte completo para Modo Oscuro/Claro.
- **Data Layer:** Room Database para la persistencia de flujos y el historial de ejecuciones.
- **Dependency Injection:** Dagger Hilt para acoplamiento débil.
- **Background Execution:** Foreground Services y APIs de Accessibility Service (para acciones que requieren permisos elevados como el bloqueo de pantalla).
- **Testing:** JUnit 4 y Mockito aislando la capa de lógica del motor de automatización.

---

## 🛡 Privacidad y Permisos (Just-In-Time)
DroidFlow cree en la transparencia. La aplicación no solicita ningún permiso extraño al iniciar. Utiliza un sistema de **Permisos Dinámicos (Just-In-Time)**: la aplicación solo te pedirá permiso para acceder al SMS, al Bluetooth o a la Accesibilidad en el momento exacto en el que intentes guardar un flujo que requiere dichos permisos. Si no lo usas, no te lo pide.

---

## 💻 Compilación y Desarrollo
Para compilar este proyecto por tu cuenta:
1. Clona el repositorio.
2. Ábrelo en **Android Studio**.
3. Sincroniza las dependencias de Gradle.
4. Ejecuta Build > Make Project o lanza directamente el modo Run en tu emulador/dispositivo Android.

---

## 📄 Licencia
Este proyecto ha sido diseñado e implementado por **ZortVibes**. Todo el código fuente está sujeto a derechos de autor (Copyright © 2026 ZortVibes. Todos los derechos reservados).
