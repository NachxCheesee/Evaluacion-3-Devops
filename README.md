# 🌊 MarFuego - Microservicio de Gestión de Locales y Mesas (`ms-locales`)

Este proyecto es el microservicio encargado de centralizar, controlar y administrar toda la infraestructura física de la cadena de restaurantes **MarFuego**. Su objetivo principal es gestionar de manera eficiente los diferentes **Locales** (sucursales) y las **Mesas** asociadas a cada uno de ellos.

El servicio provee una API REST robusta que permite al sistema interactuar en tiempo real con la distribución física de los restaurantes, sirviendo como base fundamental para los módulos de reservas, atención al cliente y gestión de pedidos.

---

## 🛠️ ¿Qué hace el Proyecto? (Funcionalidades Principales)

* **Gestión de Locales:** Permite registrar, actualizar, listar y dar de baja las diferentes sucursales del restaurante MarFuego, almacenando datos clave como su dirección, capacidad máxima y estado de operación.
* **Control de Mesas por Local:** Administra el inventario de mesas de cada restaurante de forma independiente. Registra la numeración de la mesa, la cantidad de asientos disponibles (capacidad) y su ubicación dentro del local (ej: terraza, salón principal, VIP).
* **Trazabilidad mediante Logs:** Monitorea de forma activa cada petición HTTP entrante (`GET`, `POST`, `PUT`, `DELETE`). Cada acción del backend genera logs detallados para facilitar la auditoría y el mantenimiento del sistema.
* **Documentación Interactiva (Swagger/OpenAPI):** Expone una interfaz web viva donde se pueden visualizar todos los endpoints disponibles, los modelos de datos (DTOs) requeridos y simular peticiones de prueba directamente desde el navegador.
* **Manejo Global de Errores:** Cuenta con un controlador de excepciones centralizado que intercepta solicitudes mal formadas (Errores 400) o intentos de consultar locales/mesas inexistentes (Errores 404), respondiendo siempre con una estructura de datos limpia y estandarizada.

---

## 📂 Componentes del Código del Microservicio

El diseño interno está estructurado bajo una arquitectura limpia y ordenada por capas:

* **Controller:** Define los endpoints HTTP de la API, maneja la seguridad, los mensajes de Swagger y gatilla los logs informativos de cada operación.
* **Service:** Contiene las reglas y la lógica de negocio del restaurante (validaciones antes de registrar una mesa, asignación de mesas a un local específico, etc.).
* **Repository:** Capa encargada de la comunicación directa con la base de datos mediante Spring Data JPA para guardar y consultar la información de los locales.
* **Model / Entity:** Representa las tablas de la base de datos relacional (`Local` y `Mesa`), mapeando sus atributos y relaciones (un local tiene muchas mesas).
* **DTO (Data Transfer Objects):** Objetos encargados de recibir la información desde el exterior, aplicando validaciones estrictas (como asegurar que el nombre de un local no viaje vacío o que la capacidad de una mesa sea mayor a cero).
* **Exception (GlobalExceptionHandler):** El escudo protector del sistema que unifica los formatos de respuesta ante cualquier fallo o dato no encontrado.

---

## 🚀 Preparado para DevOps y Despliegue

La estructura de este proyecto fue diseñada pensando en la automatización y la observabilidad:
* **Contenedorización:** Cuenta con un `Dockerfile` optimizado para empaquetar el microservicio y ejecutarlo en cualquier entorno mediante contenedores independientes.
* **Preparado para CI/CD:** Listo para integrarse con pipelines automatizados (como GitHub Actions) para compilar, testear y subir la imagen final a registros en la nube (AWS ECR).
* **Monitoreo:** Expone las métricas e información necesaria para enlazarse de forma nativa con herramientas de monitoreo como Prometheus (para métricas de rendimiento) y Loki (para la recolección centralizada de sus logs).

---
*Desarrollado para el ecosistema de microservicios de MarFuego.*