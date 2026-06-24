# 📚 Sistema de Gestión de Biblioteca — DSY1103

Sistema de gestión de biblioteca desarrollado con arquitectura de **microservicios** en **Spring Boot**, comunicación entre servicios vía **WebClient**, seguridad basada en **JWT**, migraciones de base de datos con **Flyway** y orquestación completa mediante **Docker Compose**.

---

## 🏗️ Arquitectura

El sistema está compuesto por un **API Gateway** central que enruta todas las peticiones entrantes hacia los microservicios correspondientes, cada uno con su propia base de datos en MySQL.

```
Cliente
   │
   ▼
API Gateway (:8080)
   ├── /api/autores/**          → autores-microservice       (:8084)
   ├── /api/libros/**           → libros-microservice        (:8085)
   ├── /api/categorias/**       → categorias-microservice    (:8086)
   ├── /api/generos/**          → generos-microservice       (:8083)
   ├── /api/usuarios/**         → usuarios-microservice      (:8081)
   ├── /api/resennas/**         → resennas-microservice      (:8082)
   ├── /api/prestamos/**        → prestamos-microservice     (:8089)
   ├── /api/v1/reservas/**      → reservas-microservice      (:8090)
   ├── /api/v1/multas/**        → multas-microservice        (:8087)
   └── /api/v1/auth/**          → autenticacion-microservice (:8088)
```

---

## 🧩 Microservicios

| Microservicio              | Puerto | Descripción                                                               |
|----------------------------|--------|---------------------------------------------------------------------------|
| `api-gateway`              | 8080   | Enrutador central (Spring Cloud Gateway)                                  |
| `autenticacion-microservice` | 8088 | Emisión de tokens JWT, login y registro de personal                      |
| `usuarios-microservice`    | 8081   | CRUD de usuarios de la biblioteca (lectores)                              |
| `autores-microservice`     | 8084   | CRUD de autores literarios                                                |
| `libros-microservice`      | 8085   | Catálogo de libros; consume autores, categorías y géneros vía WebClient  |
| `categorias-microservice`  | 8086   | CRUD de categorías de libros                                              |
| `generos-microservice`     | 8083   | CRUD de géneros literarios                                                |
| `prestamos-microservice`   | 8089   | Gestión de préstamos; consume usuarios y libros vía WebClient             |
| `reservas-microservice`    | 8090   | Gestión de reservas; consume usuarios y libros vía WebClient              |
| `resennas-microservice`    | 8082   | Reseñas de libros por usuario; consume usuarios y libros vía WebClient   |
| `multas-microservice`      | 8087   | Gestión de multas; consume préstamos y usuarios vía WebClient             |

---

## 🛠️ Stack Tecnológico

- **Java 17**
- **Spring Boot 4.0.6**
    - Spring Web MVC
    - Spring Data JPA
    - Spring Security
    - Spring WebClient
    - Spring Cloud Gateway
    - Spring Actuator
    - Spring Validation
- **JWT** — `jjwt` 0.13.0
- **MySQL 8.4**
- **Flyway** — migraciones de base de datos
- **Lombok**
- **Springdoc OpenAPI / Swagger UI** — documentación automática
- **Docker & Docker Compose**

---

## ✅ Requisitos Previos

- [Docker](https://www.docker.com/) y Docker Compose instalados
- Puerto **8080** disponible (y opcionalmente los puertos 8081–8090 para acceso directo)

---

## 🚀 Levantar el proyecto

Desde la raíz del repositorio, ejecutar:

```bash
docker compose up --build
```

Docker Compose:
1. Levanta una instancia de **MySQL 8.4** y ejecuta `init-db.sql` para crear las 10 bases de datos.
2. Construye y lanza cada microservicio una vez que MySQL está saludable.
3. Levanta el **API Gateway** una vez que todos los servicios estén en pie.

Para detener y eliminar los contenedores:

```bash
docker compose down
```

Para eliminar también los volúmenes de datos:

```bash
docker compose down -v
```

---

## 🔐 Autenticación

Todos los endpoints (excepto login y registro) requieren un **token JWT Bearer**.

### Obtener token

```http
POST http://localhost:8080/api/v1/auth
Content-Type: application/json

{
  "nombreUsuario": "admin",
  "password": "admin123"
}
```

### Registrar nuevo usuario de personal

```http
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "nombreUsuario": "nuevo_usuario",
  "email": "usuario@biblioteca.cl",
  "password": "contraseña_segura"
}
```

Una vez obtenido el token, incluirlo en todas las peticiones:

```
Authorization: Bearer <token>
```

---

## 📖 Documentación de la API (Swagger UI)

Cada microservicio expone su propia documentación interactiva. Acceder directamente al servicio (sin pasar por el gateway):

| Microservicio          | Swagger UI                                      |
|------------------------|-------------------------------------------------|
| Autores                | http://localhost:8084/swagger-ui.html           |
| Libros                 | http://localhost:8085/swagger-ui.html           |
| Categorías             | http://localhost:8086/swagger-ui.html           |
| Prestamos              | http://localhost:8089/swagger-ui.html           |
| Multas                 | http://localhost:8087/swagger-ui.html           |
| Autenticación          | http://localhost:8088/swagger-ui.html           |

---

## 🗄️ Base de Datos

El archivo `init-db.sql` crea automáticamente las siguientes bases de datos en MySQL al primer inicio:

| Base de datos       | Microservicio asociado       |
|---------------------|------------------------------|
| `db_autores`        | autores-microservice         |
| `db_libros`         | libros-microservice          |
| `db_categorias`     | categorias-microservice      |
| `db_generos`        | generos-microservice         |
| `db_usuarios`       | usuarios-microservice        |
| `db_prestamos`      | prestamos-microservice       |
| `db_resennas`       | resennas-microservice        |
| `db_reservas`       | reservas-microservice        |
| `db_autenticacion`  | autenticacion-microservice   |
| `db_multas`         | multas-microservice          |

Las migraciones de esquema e datos iniciales de cada base son gestionadas por **Flyway** al arrancar cada microservicio.

---

## 📂 Estructura del Repositorio

```
dsy1103-gestion-biblioteca/
├── compose.yaml
├── init-db.sql
├── api-gateway/
├── autenticacion-microservice/
├── usuarios-microservice/
├── autores-microservice/
├── libros-microservice/
├── categorias-microservice/
├── generos-microservice/
├── prestamos-microservice/
├── reservas-microservice/
├── resennas-microservice/
└── multas-microservice/
```

Cada microservicio sigue la misma estructura interna:

```
<microservicio>/
├── Dockerfile
├── pom.xml
└── src/
    ├── main/
    │   ├── java/.../
    │   │   ├── config/       # SecurityConfig, SwaggerConfig, WebClientConfig
    │   │   ├── controller/   # Controladores REST
    │   │   ├── dto/          # Request y Response DTOs
    │   │   ├── exception/    # Excepciones y GlobalExceptionHandler
    │   │   ├── mapper/       # Mappers entidad ↔ DTO
    │   │   ├── model/        # Entidades JPA
    │   │   ├── repository/   # Repositorios Spring Data
    │   │   ├── security/     # JwtAuthenticationFilter
    │   │   └── service/      # Lógica de negocio
    │   └── resources/
    │       ├── application.yaml
    │       └── db/migration/ # Scripts Flyway (V1__, V2__, ...)
    └── test/
        └── java/.../         # Tests unitarios de servicios
```

---

## 🔄 Comunicación entre Microservicios

Los microservicios que necesitan datos de otros los consultan de forma **síncrona** mediante **Spring WebClient**:

- **libros-microservice** → consulta autores, categorías y géneros
- **prestamos-microservice** → consulta usuarios y libros
- **reservas-microservice** → consulta usuarios y libros
- **resennas-microservice** → consulta usuarios y libros
- **multas-microservice** → consulta préstamos y usuarios

---

## 🧪 Tests

Cada microservicio incluye tests unitarios sobre la capa de servicio. Para ejecutarlos en un microservicio específico:

```bash
cd <microservicio>
./mvnw test
```
