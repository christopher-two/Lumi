# Qr App

Esta es una aplicación de escáner y generador de códigos QR para Android, creada con Jetpack Compose y que utiliza la API de Gemini para la generación avanzada de códigos QR.

## Características

*   **Escáner de códigos QR:** Escanea códigos QR con la cámara de tu dispositivo. La aplicación utiliza el ML Kit de Google para un escaneo de códigos de barras rápido y preciso.
*   **Generador de códigos QR:** Genera códigos QR personalizados a partir de una indicación de texto. Puedes especificar el contenido, los colores y el estilo del código QR utilizando lenguaje natural.
*   **Integración con la API de Gemini:** La aplicación aprovecha la API de Gemini para comprender tus indicaciones de texto y crear códigos QR personalizados.
*   **Interfaz de usuario moderna:** La aplicación está creada con Jetpack Compose, lo que proporciona una interfaz de usuario moderna y receptiva.
*   **Arquitectura limpia:** El código base sigue un patrón de arquitectura limpia, lo que facilita su mantenimiento y escalado.

## Cómo compilar y ejecutar

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/christopher-two/Qr.git
    ```
2.  **Obtén una clave de API de Gemini:**
    *   Ve a [Google AI Studio](https://aistudio.google.com/) y crea una nueva clave de API.
    *   Abre la aplicación y se te pedirá que introduzcas tu clave de API de Gemini.
3.  **Abre en Android Studio:**
    *   Abre Android Studio y selecciona "Abrir un proyecto existente".
    *   Navega hasta el directorio del proyecto y haz clic en "Aceptar".
4.  **Ejecuta la aplicación:**
    *   Conecta tu dispositivo Android o inicia un emulador.
    *   Haz clic en el botón "Ejecutar" en Android Studio.

## Cómo funciona

### Generación de códigos QR

La función de generación de códigos QR utiliza la API de Gemini para interpretar tus indicaciones de texto. Cuando introduces una indicación como "un código QR para mi sitio web, con puntos azules", la aplicación envía esta indicación a la API de Gemini. A continuación, la API devuelve un objeto JSON con instrucciones sobre cómo generar el código QR, incluido el contenido, los colores y el estilo.

Luego, la aplicación utiliza la biblioteca `qrose` para generar el código QR basándose en las instrucciones de la API de Gemini.

### Escaneo de códigos QR

La función de escaneo de códigos QR utiliza la cámara del dispositivo y el ML Kit de Google para detectar y decodificar códigos QR. La aplicación muestra una vista previa de la cámara con un área de escaneo y, cuando se detecta un código QR, la aplicación muestra el contenido del código QR.

## Dependencias

*   [Jetpack Compose](https://developer.android.com/jetpack/compose): Para crear la interfaz de usuario.
*   [Koin](https://insert-koin.io/): Para la inyección de dependencias.
*   [ML Kit](https://developers.google.com/ml-kit): Para el escaneo de códigos de barras.
*   [qrose](https://github.com/alexzhirkevich/qrose): Para generar códigos QR.
*   [Accompanist](https://google.github.io/accompanist/): Para el manejo de permisos.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo [LICENSE](LICENSE) para más detalles.