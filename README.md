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

`src/main/resources/application.properties`:

```properties
spring.application.name=api
spring.jpa.show-sql=true
sping.jpa.hibernate.ddl-auto=update
jwt.secret=EsteEsUnSecretoMuySecretoPeronoSeLoDigasAAlguienPorqueEsMuySecretoMuySecreto
jwt.expiration=3600000
```

> Nota: cambiar `jwt.secret` en producción y no versionarlo en texto plano.

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
