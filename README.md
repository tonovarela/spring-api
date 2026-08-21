# API Gestión de Eventos

API REST en Spring Boot para gestión de eventos con autenticación JWT y control de roles.

## Stack

- Java 21
- Spring Boot 4.0.6 (Web MVC, Data JPA, Validation, Security)
- JWT (jjwt 0.13.0)
- MapStruct + Lombok
- H2 (runtime/dev) y PostgreSQL (runtime/prod)
- Maven

## Arquitectura

```
controller  -> endpoints REST
service     -> lógica de negocio (interfaces + implementation)
repository  -> Spring Data JPA
domain      -> entidades (Event, User, Role)
dto         -> objetos de request/response
mapper      -> MapStruct (Event, User)
security    -> JWT filter, SecurityConfig, entry point
exception   -> manejo global de excepciones
```

## Configuración

Los datos sensibles (base de datos y clave JWT) no se guardan en los `.properties`.
Se leen de variables de entorno. Para desarrollo local, copiar la plantilla:

```bash
cp .env.example .env
```

y completar los valores en `.env` (archivo ignorado por git):

```properties
DB_URL=jdbc:postgresql://localhost:5432/gestioneventosdb
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=...
```

`application.properties` importa ese archivo con
`spring.config.import=optional:file:.env[.properties]`, y los perfiles
`application-dev.properties` / `application-prod.properties` referencian las variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

> En producción no se usa `.env`: definir las variables como variables de entorno del sistema.
> Generar una clave JWT nueva con `openssl rand -base64 64` (HS512 requiere al menos 64 caracteres).

## Ejecutar

```bash
./mvnw spring-boot:run
```

Tests:

```bash
./mvnw test
```

## Seguridad

- Stateless (sin sesión), JWT por header `Authorization: Bearer <token>`.
- `/api/v1/auth/**` público.
- Resto de endpoints requieren autenticación; algunos además usan `@PreAuthorize` con roles `ADMIN`/`USER`.

## Endpoints

### Auth (`/api/v1/auth`) — público

| Método | Ruta | Body | Descripción |
|---|---|---|---|
| POST | `/login` | `LoginDTO {username, password}` | Devuelve `JwtAuthResponseDTO {accessToken}` |
| POST | `/register` | `RegisterDTO {username, password, email, name}` | Registra usuario con rol `ROLE_USER` |

### Eventos (`/api/v1/events`) — autenticado

| Método | Ruta | Rol | Body | Descripción |
|---|---|---|---|---|
| GET | `/` | ADMIN, USER | - | Lista eventos |
| GET | `/{id}` | ADMIN, USER | - | Detalle de evento |
| POST | `/` | ADMIN | `EventRequestDTO {name, date, local}` | Crea evento |
| PUT | `/{id}` | ADMIN | `EventRequestDTO` | Actualiza evento |
| DELETE | `/{id}` | ADMIN | - | Elimina evento |

Respuesta: `EventResponseDTO {id, name, date, local}`.

### Usuarios (`/api/v1/users`) — autenticado

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Lista usuarios |

### Roles (`/api/v1/roles`)

Sin endpoints implementados aún.

## Manejo de errores

`GlobalExceptionHandler` centraliza respuestas de error (incluye `ResouceNotFoundException`).
