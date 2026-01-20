<div style="text-align: justify; line-height: 1.6;">

# Tasks API · Spring Boot REST (Hexagonal)

<div style="text-align: center;">
  <img src="images/tecnologias-tasks-api-rest.png" width="300">
</div>

API REST desarrollada con **Spring Boot** para la gestión de tareas (*tasks*), diseñada siguiendo una **arquitectura hexagonal (ports & adapters)**, con separación clara entre dominio, aplicación, infraestructura y adaptadores.

Incluye persistencia en base de datos, mapeo automático con **MapStruct**, documentación mediante **Swagger / OpenAPI** y tests unitarios con **JUnit + Mockito**.

---

## 🎯 Objetivo del proyecto

- Construir una **API REST limpia y mantenible**
- Aplicar **arquitectura hexagonal / clean architecture**
- Separar **dominio, casos de uso y adaptadores**
- Uso de **Spring Data JPA** desacoplado del dominio
- Persistencia en **H2 (en memoria)**
- Uso de **MapStruct** para mapeo entre capas
- Tests unitarios con **Mockito**
- Documentación automática con **Swagger / OpenAPI 3**
- Proyecto base ideal para prácticas avanzadas y ampliaciones

---

## 🛠️ Tecnologías utilizadas

- ☕ **Java 17**
- 🌱 **Spring Boot**
- 🌐 **Spring Web**
- 🗄️ **Spring Data JPA**
- 🧪 **H2 Database**
- 🧭 **Arquitectura Hexagonal (Ports & Adapters)**
- 🔄 **MapStruct**
- 🧪 **JUnit 5 + Mockito**
- 📄 **Swagger / OpenAPI 3 (springdoc)**
- 🚀 **Tomcat embebido**

---

## 📂 Estructura del proyecto

```text
src/main/java
└── com.jpexposito.tasks
    ├── TasksApplication.java
    │
    ├── domain
    │   └── model
    │       └── Task.java
    │
    ├──  business
    │    ├── TaskService.java
    │    └── TaskServicePort.java
    │
    ├── adapters
    │   ├── in
    │   │   ├── controller
    │   │   │   └── TaskController.java
    │   │   └── api
    │   │       ├── TaskRequest.java
    │   │       └── TaskResponse.java
    │   │
    │   ├── mapper
    │   │   └── TaskMapper.java
    │   │
    │   └── out
    │       └── persistence
    │           ├── TaskJpaEntity.java
    │           ├── TaskRepositoryRepository.java
    │           └── TaskPersistenceAdapter.java
    │
    └── infrastructure
        └── (configuración futura)
```

### 🧠 Descripción de capas

- **Domain**  
  Contiene el modelo de dominio puro (`Task`), sin dependencias de frameworks.

- **Application / Business**  
  Implementa los **casos de uso** y reglas de aplicación (`TaskService`), dependiente solo de **puertos**.

- **Adapters In**  
  Entrada al sistema (REST):
  - Controladores
  - DTOs de entrada y salida

- **Adapters Out**  
  Infraestructura externa:
  - Persistencia JPA
  - Implementación del puerto de repositorio

- **Mapper**  
  Conversión automática entre:
  - API ↔ Dominio
  - Dominio ↔ JPA

---

## 📘 Endpoints disponibles

### 📝 Tasks API

| Método | Endpoint | Descripción |
|------|---------|------------|
| GET | `/api/v1/tasks` | Obtener todas las tareas |
| GET | `/api/v1/tasks/{id}` | Obtener una tarea por id |
| POST | `/api/v1/tasks` | Crear una nueva tarea |
| PATCH | `/api/v1/tasks/{id}` | Actualizar parcialmente una tarea |
| DELETE | `/api/v1/tasks/{id}` | Eliminar una tarea |

---

## 📄 Modelo de dominio

### Task (Dominio)

```json
{
  "id": 1,
  "title": "Aprender Spring Boot",
  "description": "Aplicar arquitectura hexagonal",
  "completed": false
}
```

---

## 🔄 Flujo de una petición

```text
HTTP Request
   ↓
TaskController (Adapter In)
   ↓
TaskServicePort
   ↓
ITaskService (Application)
   ↓
ITaskPersistenceAdapter (Adapter Out)
   ↓
Base de datos (TaskRepositoryRepository H2)
```

---

## 📖 Documentación Swagger

### Swagger UI
```text
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON
```text
http://localhost:8080/v3/api-docs
```

---

## 🗄️ Base de datos H2

### Consola
```text
http://localhost:8080/h2-console
```

**JDBC URL**
```text
jdbc:h2:mem:tasksdb
```

---

## ▶️ Ejecución

```bash
mvn clean spring-boot:run

---

## 🔐 Seguridad con JWT (Swagger con candado)

Esta versión incorpora **Spring Security + JWT**. Los endpoints de `/api/v1/tasks/**` están protegidos y en Swagger UI verás el **candado** y el botón **Authorize**.

### Usuarios de ejemplo

| Usuario | Password | Rol | Permisos |
|---|---|---|---|
| `user` | `user123` | USER | `GET /api/v1/tasks` y `GET /api/v1/tasks/{id}` |
| `admin` | `admin123` | ADMIN | Todo (GET/POST/PATCH/DELETE) |

### 1) Obtener token

`POST /api/v1/auth/login`

```json
{ "username": "admin", "password": "admin123" }
```

Respuesta (ejemplo):

```json
{ "tokenType": "Bearer", "token": "<JWT>", "roles": ["ROLE_ADMIN"] }
```

### 2) Usar token en Swagger (Authorize)

1. Abre Swagger UI: `http://localhost:8080/swagger-ui/index.html`
2. Pulsa **Authorize**
3. Pega: `Bearer <JWT>`
4. Ya puedes ejecutar endpoints protegidos (aparecen con candado).

### 3) Uso con curl

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | jq -r .token)

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/tasks
```
```

---

## 🧪 Tests

- Tests unitarios con **JUnit 5**
- Mocks con **Mockito**
- Cobertura típica de:
  - Servicio de aplicación
  - Controlador (unitario)
  - Adaptador de persistencia (unitario)

> *Una buena arquitectura no es la más compleja, sino la que permite evolucionar sin dolor.*

</div>
