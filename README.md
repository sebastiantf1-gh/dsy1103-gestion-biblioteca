### Arquitectura de Microservicios — DSY1103 Desarrollo FullStack

---

## Integrantes del Equipo

| Nombre | Microservicios a cargo |
|--------|----------------------|
| Benjamín Vásquez | usuarios, reseñas, géneros |
| Sebastian Toro | libros, autores, categorías, prestamos |
| Camilo Vera | reservas, multas, autenticación |

---

## Descripción del Proyecto

Sistema de gestión de biblioteca desarrollado bajo arquitectura de microservicios independientes. Cada microservicio gestiona un dominio específico del negocio, con su propia base de datos, endpoints REST y lógica de negocio. Los microservicios se comunican entre sí a través de WebClient para operaciones que requieren datos de otros dominios.

---

## Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                     Sistema de Gestion de Biblioteca        │
├──────────────────────────┬──────────────────────────────────┤
│   autenticacion :8088    │   usuarios :8081                 │
│   generos       :8083    │   libros   :8085                 │
│   autores       :8084    │   categorias :8086               │
│   resennas      :8082    │   prestamos :8089                │
│   multas        :8087    │   reservas  : 8090               │
└──────────────────────────┴──────────────────────────────────┘
```

### Comunicación entre microservicios

```
resennas-microservice
    → llama a usuarios-microservice  (verificar que el usuario existe)
    → llama a libros-microservice    (verificar que el libro existe)

prestamos-microservice
    → llama a usuarios-microservice  (verificar que el usuario existe)
    → llama a libros-microservice    (verificar disponibilidad del libro)

multas-microservice
    → llama a usuarios-microservice  (verificar que el usuario existe)
    → llama a prestamos-microservice (verificar que el préstamo existe)
```
reservas-microservice
     → llama a usuarios-microservice  (verificar que el usuario existe)
     → llama a libros-microservice    (verificar disponibilidad del libro)

---

## Microservicios

### 1. usuarios-microservice — Puerto 8081
Gestiona los usuarios registrados en la biblioteca.

**Base de datos:** `db_usuarios`

**Endpoints:**
```
GET    /api/usuarios          → listar todos los usuarios
GET    /api/usuarios/{id}     → buscar usuario por id
POST   /api/usuarios          → crear nuevo usuario
PUT    /api/usuarios/{id}     → modificar usuario
DELETE /api/usuarios/{id}     → eliminar usuario
```

---

### 2. resennas-microservice — Puerto 8082
Gestiona las reseñas de libros realizadas por los usuarios. Un usuario solo puede reseñar el mismo libro una vez.

**Base de datos:** `db_resennas`

**Endpoints:**
```
GET    /api/resennas          → listar todas las reseñas
GET    /api/resennas/{id}     → buscar reseña por id
POST   /api/resennas          → crear nueva reseña
PUT    /api/resennas/{id}     → modificar reseña
DELETE /api/resennas/{id}     → eliminar reseña
```

**Reglas de negocio:**
- Un usuario no puede reseñar el mismo libro dos veces
- La calificación debe ser entre 1 y 5
- La fecha de registro la asigna el sistema automáticamente

---

### 3. generos-microservice — Puerto 8083
Gestiona los géneros literarios disponibles en la biblioteca.

**Base de datos:** `db_generos`

**Endpoints:**
```
GET    /api/generos           → listar todos los géneros
GET    /api/generos/{id}      → buscar género por id
POST   /api/generos           → agregar nuevo género
PUT    /api/generos/{id}      → modificar género
DELETE /api/generos/{id}      → eliminar género
```

---

### 4. autores-microservice — Puerto 8084
Gestiona los autores de los libros del catálogo.

**Base de datos:** `db_autores`

**Endpoints:**
```
GET    /api/autores           → listar todos los autores
GET    /api/autores/{id}      → buscar autor por id
POST   /api/autores           → crear nuevo autor
PUT    /api/autores/{id}      → modificar autor
DELETE /api/autores/{id}      → eliminar autor
```

---

### 5. libros-microservice — Puerto 8085
Gestiona el catálogo completo de libros de la biblioteca.

**Base de datos:** `db_libros`

**Endpoints:**
```
GET    /api/libros                        → listar todos los libros
GET    /api/libros/{id}                   → buscar libro por id
GET    /api/libros/autor/{idAutor}        → buscar libros por autor
GET    /api/libros/categoria/{idCategoria}→ buscar libros por categoría
GET    /api/libros/genero/{idGenero}      → buscar libros por género
POST   /api/libros                        → crear nuevo libro
PUT    /api/libros/{id}                   → modificar libro
DELETE /api/libros/{id}                   → eliminar libro
```

---

### 6. categorias-microservice — Puerto 8086
Gestiona las categorías de los libros del catálogo.

**Base de datos:** `db_categorias`

**Endpoints:**
```
GET    /api/categorias        → listar todas las categorías
GET    /api/categorias/{id}   → buscar categoría por id
POST   /api/categorias        → crear nueva categoría
PUT    /api/categorias/{id}   → modificar categoría
DELETE /api/categorias/{id}   → eliminar categoría
```

---

### 7. multas-microservice — Puerto 8087
Gestiona las multas generadas por préstamos vencidos.

**Base de datos:** `db_multas`

**Endpoints:**
```
GET    /api/multas            → listar todas las multas
GET    /api/multas/{id}       → buscar multa por id
POST   /api/multas            → crear nueva multa
PUT    /api/multas/{id}       → modificar multa
DELETE /api/multas/{id}       → eliminar multa
```

---

### 8. autenticacion-microservice — Puerto 8088
Gestiona la autenticación y autorización de los usuarios del sistema mediante JWT.

**Base de datos:** `db_autenticacion`

**Endpoints:**
```
POST   /api/auth/login        → iniciar sesión, devuelve token JWT
POST   /api/auth/register     → registrar nuevo usuario del sistema
```

---

### 9. prestamos-microservice — Puerto 8089
Gestiona los préstamos de libros realizados por los usuarios.

**Base de datos:** `db_prestamos`

**Endpoints:**
```
GET    /api/prestamos                     → listar todos los préstamos
GET    /api/prestamos/{id}                → buscar préstamo por id
GET    /api/prestamos/usuario/{idUsuario} → préstamos de un usuario
GET    /api/prestamos/libro/{idLibro}     → préstamos de un libro
GET    /api/prestamos/estado/{estado}     → préstamos por estado
POST   /api/prestamos                     → crear nuevo préstamo
```

---

### 10. reservas-microservice — Puerto 8090

## Tecnologías Utilizadas

```
Java 17
Spring Boot 3.4.5
Spring Data JPA + Hibernate
Spring Security
Spring WebFlux (WebClient)
MySQL 8
Flyway
Lombok
Maven
```

---

## Bases de Datos

Cada microservicio tiene su propia base de datos independiente:

```
db_usuarios
db_resennas
db_generos
db_autores
db_libros
db_categorias
db_multas
db_autenticacion
db_prestamos
db_reservas
```

---

## Requisitos Previos

Antes de ejecutar el proyecto necesitas tener instalado:

```
Java 17 o superior
Maven 3.8 o superior
MySQL 8 corriendo en localhost:3306
IntelliJ IDEA (recomendado)
Postman (para probar los endpoints)
```

---

## Pasos para Ejecutar

### 1. Clonar el repositorio

```bash
git clone https://github.com/[usuario]/dsy1103-gestion-biblioteca.git
cd dsy1103-gestion-biblioteca
```

### 2. Crear las bases de datos en MySQL

```sql
CREATE DATABASE IF NOT EXISTS db_usuarios;
CREATE DATABASE IF NOT EXISTS db_resennas;
CREATE DATABASE IF NOT EXISTS db_generos;
CREATE DATABASE IF NOT EXISTS db_autores;
CREATE DATABASE IF NOT EXISTS db_libros;
CREATE DATABASE IF NOT EXISTS db_categorias;
CREATE DATABASE IF NOT EXISTS db_multas;
CREATE DATABASE db_autenticacion;
CREATE DATABASE IF NOT EXISTS db_prestamos;
CREATE DATABASE IF NOT EXISTS db_reservas;

```

### 3. Configurar credenciales de MySQL

En cada microservicio abre el archivo `src/main/resources/application.properties` y ajusta:

```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Ejecutar los microservicios

Se recomienda ejecutar en este orden para respetar las dependencias:

```
1. usuarios-microservice      (puerto 8081)
2. generos-microservice       (puerto 8083)
3. autores-microservice       (puerto 8084)
4. libros-microservice        (puerto 8085)
5. categorias-microservice    (puerto 8086)
6. autenticacion-microservice (puerto 8088)
7. resennas-microservice      (puerto 8082)
8. prestamos-microservice     (puerto 8089)
9. multas-microservice        (puerto 8087)
10. reservas-microservice     (puerto 8090)
```

En IntelliJ: abrir cada proyecto → botón Run o `Shift+F10`.

Flyway ejecutará automáticamente las migraciones SQL al arrancar cada microservicio.

### 5. Verificar que funcionan

```
GET http://localhost:8081/api/usuarios     → debe devolver lista de usuarios
GET http://localhost:8083/api/generos      → debe devolver lista de géneros
GET http://localhost:8085/api/libros       → debe devolver lista de libros
```

---

## Ejemplo de Uso con Postman

### Crear una reseña

```
POST http://localhost:8082/api/resennas
Content-Type: application/json

{
    "calificacion": 5,
    "descripcion": "Excelente libro, muy recomendado",
    "idUsuario": 1,
    "idLibro": 1
}
```

### Crear un usuario

```
POST http://localhost:8081/api/usuarios
Content-Type: application/json

{
    "nombreCompleto": "Juan Pérez",
    "email": "juan@email.com",
    "telefono": "+56912345678"
}
```

### Crear un préstamo

```
POST http://localhost:8089/api/prestamos
Content-Type: application/json

{
    "idUsuario": 1,
    "idLibro": 1,
    "fechaDevolucion": "2026-06-01T10:00:00"
}
```

---

## Estructura de cada Microservicio

```
microservicio/
├── src/main/java/cl/duoc/dsy1103/
│   └── [nombre]_microservice/
│       ├── client/          → comunicación con otros microservicios
│       ├── config/          → SecurityConfig, WebClientConfig
│       ├── controller/      → endpoints REST
│       ├── dto/             → Request, Response, UpdateRequest
│       ├── exception/       → excepciones personalizadas y GlobalHandlerException
│       ├── mapper/          → conversión entre entidad y dto
│       ├── model/           → entidad JPA
│       ├── repository/      → interface JpaRepository
│       └── service/         → lógica de negocio
└── src/main/resources/
    ├── application.properties
    └── db/migration/
        ├── V1__create_table.sql
        └── V2__initial_data.sql
```

---

## Notas Importantes

- Flyway gestiona las migraciones automáticamente, no modificar las tablas manualmente
- Los ids de usuarios y libros en `db_resennas` y `db_prestamos` deben existir en sus respectivas BDs
- No modificar el repositorio después de la fecha de entrega.
