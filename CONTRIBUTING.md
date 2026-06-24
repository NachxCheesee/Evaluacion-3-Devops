# 🔒 Manual de Ingeniería, Gobernanza y Contribución Interna - MarFuego

Este repositorio contiene el código fuente, los artefactos de configuración y los pipelines de infraestructura de propiedad privada y exclusiva para el ecosistema de microservicios de **MarFuego**. El acceso, modificación y distribución de este código está estrictamente restringido al personal de ingeniería de software y operaciones autorizado.

El propósito de este documento es establecer un marco normativo, técnico y procedimental obligatorio para cualquier desarrollador que forme parte del equipo. El cumplimiento estricto de estas de estas directrices garantiza la estabilidad de la plataforma gastronómica, la resiliencia de la infraestructura en la nube (**AWS**) y la continuidad operativa de nuestro pipeline de Integración y Despliegue Continuo (CI/CD).

---

## 🌿 1. Política de Gestión de Ramas y Git Workflow (GitFlow Corporativo)

Para mantener un historial de control de versiones limpio, auditable y libre de conflictos, queda estrictamente prohibido realizar commits, push o mezclas directas sobre la rama principal `main`. Todo desarrollo debe seguir el siguiente flujo de trabajo mandatorio:

### 1.1. Creación y Nomenclatura de Ramas
Antes de iniciar cualquier modificación, el desarrollador debe sincronizar su entorno local con el repositorio remoto y crear una rama de característica funcional (*Feature Branch*) utilizando una estructura clara basada en su nombre:
* **Formato permitido:** `git checkout -b NombreDesarrollador` (ej: `git checkout -b IgnacioCatalan`).

### 1.2. Ciclo de Commits
Los mensajes de los commits deben ser descriptivos, escritos en tiempo presente y explicar brevemente qué cambia y por qué (evitar mensajes ambiguos como "cambios", "fix" o "test").
* **Ejemplo correcto:** `git commit -m "Añade logs estructurados en endpoints de locales y mapeo en ExceptionHandler"`

### 1.3. Ciclo de Integración (Pull Requests)
1. **Push Remoto:** Una vez completada la funcionalidad en el entorno local de desarrollo, se debe subir la rama específica a GitHub (`git push origin NombreRama`).
2. **Apertura de Pull Request (PR):** Se debe abrir un *Pull Request* apuntando desde la rama de trabajo hacia `main`.
3. **Bloqueo Preventivo:** El pipeline automatizado de GitHub Actions se disparará inmediatamente sobre el PR de forma pasiva para validar la compilación y la generación de la imagen Docker.

---

## 🚀 2. Gobernanza del Pipeline CI/CD, Contenedores y AWS

El despliegue en la infraestructura de la nube es un proceso automatizado y crítico. Para asegurar que la orquestación funcione correctamente, se deben auditar los siguientes puntos de control:

### 2.1. Gestión de Credenciales Dinámicas (AWS Academy)
Dado que operamos en un entorno de laboratorio con credenciales de sesión que expiran periódicamente, es responsabilidad exclusiva del desarrollador a cargo del despliegue actualizar las variables secretas en la sección de **Repository Secrets** de GitHub antes de realizar el push definitivo a la rama integrada:
* `AWS_ACCESS_KEY_ID`
* `AWS_SECRET_ACCESS_KEY`
* `AWS_SESSION_TOKEN` *(Debe actualizarse obligatoriamente en cada inicio de ciclo en AWS)*

### 2.2. Estándar del Dockerfile y Empaquetado
Las imágenes de contenedor deben construirse utilizando el archivo `Dockerfile` homologado en la raíz de `ms-locales`. El pipeline de GitHub Actions compilará la aplicación con Maven, empaquetará el `.jar` resultante dentro de una imagen ligera basada en Linux Alpine, y realizará el push automático al registro de imágenes **AWS ECR (Elastic Container Registry)**.

### 2.3. Despliegue en Infraestructura (ECS / EC2)
Una vez que la nueva imagen está en ECR, el clúster de **AWS ECS (Elastic Container Service)** ejecutará la nueva definición de tarea sobre instancias **EC2** dentro de nuestra **VPC** privada, asegurando el aislamiento de red del microservicio.

---

## 📊 3. Estándar de Observabilidad (Métricas y Logs Corporativos)

Para que el microservicio se considere "Apto para Producción" dentro de MarFuego, debe integrarse perfectamente con la pila de telemetría y monitoreo centralizado:

### 3.1. Inyección de Logs Informativos y de Error
* **Flujo Feliz:** Todo inicio de procesamiento en los endpoints de `ms-locales` (guardar locales, asignación de mesas, consultas) debe documentarse con una traza informativa (`log.info()`).
* **Flujo de Excepciones:** Los bloques de captura de errores dentro del `GlobalExceptionHandler` (como errores 400 o 404) deben registrar un `log.error()`. Esto asegura que el recolector de logs **Loki** extraiga las cadenas de texto y las envíe en tiempo real a los tableros de **Grafana**.

### 3.2. Exposición de Métricas de Rendimiento
La URL de raspado `/actuator/prometheus` (provista por Spring Actuator y Micrometer) debe mantenerse activa de manera permanente en el puerto interno del contenedor. Esto permite que el servidor de **Prometheus** capture los consumos de CPU, memoria de la instancia y latencia para graficarlos en los dashboards corporativos.

---

## 📑 4. Criterios de Aceptación para el Cierre de Cambios

Un cambio de código se considerará finalizado y desplegado con éxito únicamente cuando cumpla los siguientes hitos en su totalidad:
1. El código compila localmente de forma limpia y sin errores.
2. La documentación OpenAPI/Swagger refleja con exactitud los contratos vigentes de la API.
3. El pipeline de GitHub Actions compila, genera la imagen Docker y realiza el push al repositorio de AWS ECR con un estado final exitoso (*Success*).
4. El nuevo contenedor es desplegado en el servicio correspondiente de AWS ECS dentro de la VPC y responde de forma saludable a las pruebas de humo iniciales.

---
*Cualquier acción fuera de estos estándares que comprometa la disponibilidad de los servicios en la nube o altere el correcto funcionamiento de los paneles de Grafana será reportada inmediatamente.*