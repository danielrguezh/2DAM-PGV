# 🔐 Guía Completa: Seguridad JWT en Spring Boot 3.x - Repaso para Examen

> **Proyecto de referencia:** `tasks-api`  
> **Paquete base:** `com.docencia.*`  
> **Tecnologías:** Spring Boot 3.4.0 + Spring Security 6 + JWT + Swagger/OpenAPI

---

## 📋 Índice

1. [Objetivo del Sistema](#objetivo-del-sistema)
2. [Dependencias Maven](#dependencias-maven)
3. [Configuración (application.properties)](#configuración-applicationproperties)
4. [Arquitectura General](#arquitectura-general)
5. [Componentes Principales](#componentes-principales)
6. [Flujos de Autenticación](#flujos-de-autenticación)
7. [Estructura de Paquetes](#estructura-de-paquetes)
8. [Código Clave Explicado](#código-clave-explicado)
9. [Testing](#testing)
10. [Errores Comunes](#errores-comunes)
11. [Comandos Útiles](#comandos-útiles)

---

## 🎯 Objetivo del Sistema

### ¿Qué queremos conseguir?

1. ✅ **Endpoint público de autenticación:** `POST /api/auth/login`
2. ✅ **Proteger el resto del API:** `/api/**` requiere token JWT
3. ✅ **Autenticación con Bearer Token:** `Authorization: Bearer <TOKEN>`
4. ✅ **CORS habilitado** para llamadas desde frontend
5. ✅ **Swagger UI con botón "Authorize"** 🔒
6. ✅ **Sistema de roles** (USER, ADMIN) para autorización

---

## 📦 Dependencias Maven

### 1. Seguridad + Web

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. JWT (jjwt)

```xml
<properties>
  <jjwt.version>0.12.5</jjwt.version>
</properties>

<dependencies>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>${jjwt.version}</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>${jjwt.version}</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

### 3. Swagger/OpenAPI

```xml
<properties>
  <openapi.version>2.6.0</openapi.version>
</properties>

<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>${openapi.version}</version>
</dependency>
```

---

## ⚙️ Configuración (application.properties)

```properties
# JWT Configuration
app.jwt.secret=${APP_JWT_SECRET:Kraj8AxPPe5XdByv9wN4o4cwhW8ExUoxH3kGIG9oY3MobGgN7zbPmmG2aomaZ7RP6EH17Le6RdX6+k0DPxqbfQ==}
app.jwt.expiration-minutes=60

# CORS: URLs permitidas (frontends)
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

### 📝 ¿Qué hace cada propiedad?

| Propiedad | Descripción |
|-----------|-------------|
| `app.jwt.secret` | Clave secreta para firmar/verificar JWT (HS256). **Mínimo 32 caracteres**. Si cambia, los tokens anteriores dejan de ser válidos. |
| `app.jwt.expiration-minutes` | Tiempo de vida del token. Al expirar → `401 Unauthorized` |
| `app.cors.allowed-origins` | Lista de frontends permitidos. Si falta el origen, el navegador bloquea la petición. |
| `springdoc.swagger-ui.path` | Ruta donde se publica Swagger UI. **Debe estar permitida en SecurityConfig**. |

---

## 🏗️ Arquitectura General

### Idea General

```
┌─────────────┐
│   Cliente   │
│  (Frontend) │
└──────┬──────┘
       │
       │ 1. POST /api/auth/login
       │    {username, password}
       ▼
┌──────────────────┐
│ AuthController   │ ──► AuthService (valida credenciales)
└────────┬─────────┘      │
         │                ▼
         │           JwtService (genera token)
         │                │
         │                ▼
         │           { token: "eyJhbG..." }
         │
         │ 2. GET /api/tasks
         │    Authorization: Bearer <token>
         ▼
┌─────────────────────────┐
│ JwtAuthenticationFilter │ ──► JwtService (valida token)
└────────┬────────────────┘      │
         │                       ▼
         │                  SecurityContext
         │                  (usuario autenticado)
         ▼
┌──────────────────┐
│ TaskController   │
└──────────────────┘
```

### Responsabilidades

| Componente | Responsabilidad |
|------------|-----------------|
| **Controller** | Expone endpoints HTTP (ej: login, CRUD tareas) |
| **Service** | Lógica de negocio reutilizable (ej: crear/validar JWT) |
| **Filter** | Intercepta peticiones **antes** de llegar al controller |
| **SecurityConfig** | Define rutas públicas/protegidas, filtros, sesiones |
| **OpenApiConfig** | Configura Swagger para probar endpoints con "Authorize" |

---

## 🧩 Componentes Principales

### 1. AuthController (Capa Web)

**¿Qué hace?**
- Expone `POST /api/auth/login`
- Recibe credenciales (username/password)
- Valida credenciales con `AuthService`
- Genera JWT con `JwtService`
- Devuelve token al cliente

**¿Qué NO hace?**
- ❌ No valida JWT en cada request (eso es el filtro)
- ❌ No decide reglas globales de seguridad (eso es SecurityConfig)

**Ubicación:** `com.docencia.tasks.adapters.in.controller.AuthController`

---

### 2. AuthService

**¿Qué hace?**
- Valida credenciales de usuario
- En este proyecto: validación simple (hardcoded)
- En producción: consulta a base de datos

**Código de ejemplo:**

```java
@Service
public class AuthService {
  public boolean validateCredentials(String username, String password) {
    return "user".equals(username) && "pass".equals(password);
  }
}
```

**Ubicación:** `com.docencia.tasks.business.AuthService`

---

### 3. JwtService

**¿Qué hace?**
- ✅ Genera tokens JWT: `generateToken(username)`
- ✅ Extrae información del token: `extractUsername(token)`
- ✅ Valida token: `isValid(token)` o `isTokenValid(token, userDetails)`

**¿Por qué es un Service?**
- Se reutiliza en:
  - Login (para emitir token)
  - Filtro (para validar token en cada request)
- Mantiene el código limpio y desacoplado

**Métodos clave:**

```java
@Service
public class JwtService {
  
  // Genera un token JWT
  public String generateToken(String username) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expirationMinutes * 60);
    
    return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(key)  // Firma con clave secreta
        .compact();
  }
  
  // Extrae el username del token
  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }
  
  // Valida el token
  public boolean isValid(String token) {
    try {
      Claims c = parseClaims(token);
      return c.getExpiration().after(new Date());
    } catch (Exception ex) {
      return false;
    }
  }
  
  // Parsea el token y obtiene los claims
  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
```

**Ubicación:** `com.docencia.tasks.infrastructure.security.JwtService`

---

### 4. JwtAuthenticationFilter

**¿Qué hace?**
- 🚪 **Portero del sistema**: intercepta **todas** las peticiones
- 📖 Lee la cabecera: `Authorization: Bearer <token>`
- ✅ Si hay token:
  1. Lo valida con `JwtService`
  2. Obtiene el usuario (vía `UserDetailsService`)
  3. Crea un `Authentication` y lo guarda en `SecurityContext`
- ❌ Si no hay token o es inválido:
  - No autentica (petición queda como anónima)
  - El acceso final depende de las reglas de `SecurityConfig`

**Flujo del filtro:**

```
Request → JwtAuthenticationFilter
            │
            ├─ ¿Tiene cabecera "Authorization: Bearer ..."?
            │   NO → Continuar sin autenticar
            │   SÍ ↓
            │
            ├─ Extraer token
            ├─ ¿Token válido?
            │   NO → Continuar sin autenticar
            │   SÍ ↓
            │
            ├─ Extraer username del token
            ├─ Cargar UserDetails
            ├─ Crear Authentication
            └─ Guardar en SecurityContext
            
→ Continuar al Controller
```

**Código clave:**

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
    
    // 1. Obtener cabecera Authorization
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 2. Extraer token
    String token = authHeader.substring("Bearer ".length()).trim();
    
    // 3. Validar y autenticar
    try {
      String username = jwtService.extractUsername(token);
      UserDetails user = userDetailsService.loadUserByUsername(username);
      
      if (jwtService.isTokenValid(token, user)) {
        // 4. Crear autenticación
        UsernamePasswordAuthenticationToken auth = 
            new UsernamePasswordAuthenticationToken(
                user, 
                null, 
                user.getAuthorities()
            );
        
        // 5. Guardar en SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (Exception ignored) {
      // Token inválido → continuar sin autenticación
    }

    filterChain.doFilter(request, response);
  }
}
```

**Ubicación:** `com.docencia.tasks.infrastructure.security.JwtAuthenticationFilter`

---

### 5. SecurityConfig

**¿Qué hace?**
- 🔧 **Configuración central** de Spring Security
- Define el `SecurityFilterChain`:
  - Rutas `permitAll()` (públicas)
  - Rutas `authenticated()` (protegidas)
  - Gestión de sesiones (`STATELESS` para JWT)
  - Registro del `JwtAuthenticationFilter`
- Expone beans:
  - `PasswordEncoder`
  - `AuthenticationManager`
  - `UserDetailsService`

**¿Por qué es importante?**

Sin esta clase, Spring Security no sabe:
- ❓ Qué endpoints son públicos
- ❓ Qué endpoints requieren token
- ❓ Cuándo se ejecuta el filtro JWT

**Código clave:**

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                                 JwtAuthenticationFilter jwtFilter) throws Exception {
    http
        // Deshabilitar CSRF (común en APIs REST)
        .csrf(csrf -> csrf.disable())
        
        // Permitir H2 Console en iframe
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        
        // Sesiones STATELESS (sin sesión en servidor)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // Reglas de autorización
        .authorizeHttpRequests(auth -> auth
            // Rutas PÚBLICAS
            .requestMatchers(
                "/api/auth/**",      // Login
                "/swagger-ui/**",    // Swagger UI
                "/v3/api-docs/**",   // OpenAPI docs
                "/h2-console/**"     // H2 Console
            ).permitAll()
            
            // Rutas PROTEGIDAS con roles
            .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks/**").hasRole("ADMIN")
            
            // Cualquier otra ruta requiere autenticación
            .anyRequest().authenticated()
        )
        
        // Deshabilitar HTTP Basic
        .httpBasic(httpBasic -> httpBasic.disable());

    // Agregar filtro JWT ANTES del filtro de usuario/contraseña
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
```

**Ubicación:** `com.docencia.tasks.infrastructure.security.SecurityConfig`

---

### 6. OpenApiConfig

**¿Qué hace?**
- 📄 Configura Swagger/OpenAPI
- Define un `SecurityScheme` tipo HTTP Bearer (JWT)
- Habilita el botón **"Authorize"** 🔒 en Swagger UI
- Permite probar endpoints protegidos pegando el token

**Idea clave:**
- Swagger **NO protege** nada por sí solo
- Solo **documenta** y facilita pruebas
- La seguridad real está en `SecurityConfig` + `JwtAuthenticationFilter`

**Código:**

```java
@Configuration
public class OpenApiConfig {

  public static final String SECURITY_SCHEME = "bearerAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
        .components(new Components()
            .addSecuritySchemes(SECURITY_SCHEME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        );
  }
}
```

**Ubicación:** `com.docencia.tasks.infrastructure.openapi.OpenApiConfig`

---

## 🔄 Flujos de Autenticación

### Flujo 1: Login (Obtener JWT)

```
┌─────────┐
│ Cliente │
└────┬────┘
     │
     │ POST /api/auth/login
     │ { "username": "user", "password": "pass" }
     ▼
┌──────────────────┐
│ AuthController   │
└────────┬─────────┘
         │
         │ 1. Validar credenciales
         ▼
┌──────────────────┐
│   AuthService    │
└────────┬─────────┘
         │
         │ 2. ¿Credenciales válidas?
         │    SÍ ↓
         ▼
┌──────────────────┐
│    JwtService    │
└────────┬─────────┘
         │
         │ 3. Generar token JWT
         ▼
┌──────────────────┐
│ AuthController   │
└────────┬─────────┘
         │
         │ 4. Responder con token
         ▼
┌─────────────────────────────┐
│ { "token": "eyJhbGci..." }  │
└─────────────────────────────┘
```

**Paso a paso:**

1. Cliente envía `POST /api/auth/login` con credenciales
2. `AuthController` llama a `AuthService.validateCredentials()`
3. Si son correctas → `JwtService.generateToken(username)`
4. `AuthController` responde: `{ "token": "..." }`

---

### Flujo 2: Acceder a Endpoint Protegido (con JWT)

```
┌─────────┐
│ Cliente │
└────┬────┘
     │
     │ GET /api/v1/tasks
     │ Authorization: Bearer <token>
     ▼
┌─────────────────────────┐
│ JwtAuthenticationFilter │
└────────┬────────────────┘
         │
         │ 1. Extraer token de cabecera
         ▼
┌──────────────────┐
│    JwtService    │
└────────┬─────────┘
         │
         │ 2. Validar token
         │    ¿Válido? SÍ ↓
         ▼
┌─────────────────────────┐
│ JwtAuthenticationFilter │
└────────┬────────────────┘
         │
         │ 3. Extraer username
         │ 4. Cargar UserDetails
         │ 5. Crear Authentication
         │ 6. Guardar en SecurityContext
         ▼
┌──────────────────┐
│  SecurityConfig  │
└────────┬─────────┘
         │
         │ 7. Evaluar reglas de autorización
         │    ¿Tiene permisos? SÍ ↓
         ▼
┌──────────────────┐
│ TaskController   │
└────────┬─────────┘
         │
         │ 8. Ejecutar lógica de negocio
         ▼
┌─────────────────────┐
│ Respuesta: 200 OK   │
└─────────────────────┘
```

**Paso a paso:**

1. Cliente llama a endpoint protegido (ej: `GET /api/v1/tasks`)
2. Envía cabecera: `Authorization: Bearer <token>`
3. `JwtAuthenticationFilter` intercepta:
   - Extrae token
   - Valida con `JwtService`
   - Si OK → coloca autenticación en `SecurityContext`
4. Spring Security evalúa reglas de `SecurityConfig`:
   - Si requiere `authenticated()` → petición pasa
   - Si no hay autenticación → responde `401`
5. Controller ejecuta lógica y responde

---

## 📁 Estructura de Paquetes

```
src/main/java/com/docencia/tasks/
│
├── TasksApplication.java                    # Clase principal
│
├── adapters/
│   ├── in/
│   │   ├── api/                             # DTOs (Request/Response)
│   │   │   ├── LoginRequest.java
│   │   │   ├── TokenResponse.java
│   │   │   ├── TaskRequest.java
│   │   │   └── TaskResponse.java
│   │   │
│   │   └── controller/                      # Controladores REST
│   │       ├── AuthController.java          # POST /api/auth/login
│   │       └── TaskController.java          # CRUD /api/v1/tasks
│   │
│   ├── mapper/
│   │   └── TaskMapper.java                  # MapStruct
│   │
│   └── out/
│       └── persistence/                     # Persistencia JPA
│           ├── ITaskPersistenceAdapter.java
│           ├── TaskJpaEntity.java
│           ├── TaskPersistenceAdapter.java
│           └── TaskRepository.java
│
├── business/                                # Lógica de negocio
│   ├── AuthService.java                    # Validación de credenciales
│   ├── ITaskService.java
│   └── TaskService.java                    # CRUD de tareas
│
├── domain/
│   └── model/
│       └── Task.java                        # Modelo de dominio puro
│
└── infrastructure/                          # Configuración
    ├── openapi/
    │   └── OpenApiConfig.java              # Swagger/OpenAPI
    │
    └── security/
        ├── JwtAuthenticationFilter.java    # Filtro JWT
        ├── JwtService.java                 # Generación/validación JWT
        └── SecurityConfig.java             # Configuración Spring Security
```

---

## 💻 Código Clave Explicado

### AuthController

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  public AuthController(AuthService authService, JwtService jwtService) {
    this.authService = authService;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest req) {
    // 1. Validar credenciales
    if (!authService.validateCredentials(req.username(), req.password())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
    
    // 2. Generar token
    String token = jwtService.generateToken(req.username());
    
    // 3. Devolver token
    return new TokenResponse(token);
  }
}
```

**Puntos clave:**
- ✅ Endpoint público: `/api/auth/login`
- ✅ Valida credenciales con `AuthService`
- ✅ Genera token con `JwtService`
- ✅ Devuelve `TokenResponse` con el token

---

### JwtService - Generar Token

```java
public String generateToken(String username) {
  Instant now = Instant.now();
  Instant exp = now.plusSeconds(expirationMinutes * 60);

  return Jwts.builder()
      .subject(username)                    // Usuario
      .issuedAt(Date.from(now))            // Fecha de emisión
      .expiration(Date.from(exp))          // Fecha de expiración
      .signWith(key)                       // Firma con clave secreta
      .compact();
}
```

**Puntos clave:**
- ✅ `subject`: username del usuario
- ✅ `issuedAt`: cuándo se creó el token
- ✅ `expiration`: cuándo expira (configurable)
- ✅ `signWith`: firma con clave secreta (HS256)

---

### JwtService - Validar Token

```java
public boolean isValid(String token) {
  try {
    Claims c = parseClaims(token);
    return c.getExpiration().after(new Date());  // ¿No ha expirado?
  } catch (Exception ex) {
    return false;  // Token inválido (firma incorrecta, malformado, etc.)
  }
}

private Claims parseClaims(String token) {
  return Jwts.parser()
      .verifyWith(key)           // Verifica firma con clave secreta
      .build()
      .parseSignedClaims(token)  // Parsea el token
      .getPayload();             // Obtiene los claims
}
```

**Puntos clave:**
- ✅ Verifica la firma del token
- ✅ Comprueba que no haya expirado
- ✅ Si algo falla → `false`

---

### JwtAuthenticationFilter - Proceso Completo

```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                                HttpServletResponse response, 
                                FilterChain filterChain) throws ServletException, IOException {

  // 1. Obtener cabecera Authorization
  String authHeader = request.getHeader("Authorization");
  if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);
    return;
  }

  // 2. Extraer token (quitar "Bearer ")
  String token = authHeader.substring("Bearer ".length()).trim();
  if (token.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null) {
    filterChain.doFilter(request, response);
    return;
  }

  try {
    // 3. Extraer username del token
    String username = jwtService.extractUsername(token);
    if (username == null || username.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    // 4. Cargar detalles del usuario
    UserDetails user = userDetailsService.loadUserByUsername(username);
    
    // 5. Validar token
    if (jwtService.isTokenValid(token, user)) {
      // 6. Crear autenticación
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          user,
          null,
          user.getAuthorities()  // Roles: ROLE_USER, ROLE_ADMIN
      );
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      
      // 7. Guardar en SecurityContext
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
  } catch (Exception ignored) {
    // Token inválido → continuar sin autenticación
  }

  // 8. Continuar con la cadena de filtros
  filterChain.doFilter(request, response);
}
```

**Puntos clave:**
- ✅ Intercepta **todas** las peticiones
- ✅ Extrae token de cabecera `Authorization: Bearer <token>`
- ✅ Valida token con `JwtService`
- ✅ Carga usuario con `UserDetailsService`
- ✅ Crea `Authentication` con roles
- ✅ Guarda en `SecurityContext`
- ✅ Si falla → continúa sin autenticar (SecurityConfig decide si bloquea)

---

### SecurityConfig - Reglas de Autorización

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                               JwtAuthenticationFilter jwtFilter) throws Exception {
  http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
          // PÚBLICAS
          .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
          
          // PROTEGIDAS CON ROLES
          .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAnyRole("USER", "ADMIN")
          .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")
          .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/**").hasRole("ADMIN")
          .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks/**").hasRole("ADMIN")
          
          // RESTO REQUIERE AUTENTICACIÓN
          .anyRequest().authenticated()
      );

  // Agregar filtro JWT
  http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  
  return http.build();
}
```

**Puntos clave:**
- ✅ CSRF deshabilitado (común en APIs REST)
- ✅ Sesiones `STATELESS` (sin sesión en servidor)
- ✅ Rutas públicas: login, swagger
- ✅ Rutas protegidas por roles:
  - `GET` → USER o ADMIN
  - `POST/PATCH/DELETE` → solo ADMIN
- ✅ Filtro JWT se ejecuta **antes** del filtro de usuario/contraseña

---

## 🧪 Testing

### Tests Implementados

El proyecto `tasks-api` incluye **15 tests unitarios**:

```
src/test/java/com/docencia/tasks/
│
├── TasksApplicationTest.java
│
├── adapters/
│   ├── in/
│   │   ├── api/
│   │   │   ├── LoginRequestTest.java
│   │   │   └── TokenResponseTest.java
│   │   │
│   │   └── controller/
│   │       ├── AuthControllerTest.java
│   │       └── TaskControllerTest.java
│   │
│   ├── mapper/
│   │   └── TaskMapperTest.java
│   │
│   └── out/
│       └── persistence/
│           ├── TaskJpaEntityTest.java
│           ├── TaskPersistenceAdapterTest.java
│           └── TaskRepositoryTest.java
│
├── business/
│   ├── AuthServiceTest.java
│   └── TaskServiceTest.java
│
├── domain/
│   └── model/
│       └── TaskTest.java
│
└── infrastructure/
    └── security/
        ├── JwtAuthenticationFilterTest.java
        ├── JwtServiceTest.java
        └── SecurityConfigTest.java
```

### Ejemplo: Test de JwtService

```java
@Test
void generateToken_shouldReturnValidToken() {
  // Given
  String username = "testuser";
  
  // When
  String token = jwtService.generateToken(username);
  
  // Then
  assertNotNull(token);
  assertTrue(token.length() > 0);
  assertEquals(username, jwtService.extractUsername(token));
}

@Test
void isValid_shouldReturnTrueForValidToken() {
  // Given
  String username = "testuser";
  String token = jwtService.generateToken(username);
  
  // When
  boolean isValid = jwtService.isValid(token);
  
  // Then
  assertTrue(isValid);
}

@Test
void isValid_shouldReturnFalseForExpiredToken() {
  // Given
  String expiredToken = "eyJhbGciOiJIUzI1NiJ9..."; // Token expirado
  
  // When
  boolean isValid = jwtService.isValid(expiredToken);
  
  // Then
  assertFalse(isValid);
}
```

### Cobertura de Código

El proyecto usa **JaCoCo** para medir cobertura:

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.12</version>
  <executions>
    <execution>
      <id>check</id>
      <goals>
        <goal>check</goal>
      </goals>
      <configuration>
        <rules>
          <rule>
            <element>BUNDLE</element>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.70</minimum>  <!-- Mínimo 70% -->
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

**Ejecutar tests:**

```bash
mvn test
```

**Ver reporte de cobertura:**

```bash
mvn test
# Abrir: target/site/jacoco/index.html
```

---

## ❌ Errores Comunes

### 1. El token no se lee

**Síntoma:** Siempre recibo `401 Unauthorized` aunque envíe el token

**Causas:**
- ❌ Cabecera incorrecta: debe ser `Authorization: Bearer <token>`
- ❌ Falta el prefijo `Bearer ` (con espacio)
- ❌ Token con espacios extra o saltos de línea

**Solución:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzA2...
```

---

### 2. El filtro no se ejecuta

**Síntoma:** El filtro JWT nunca intercepta las peticiones

**Causas:**
- ❌ El filtro no está registrado en `SecurityConfig`
- ❌ El filtro está registrado después del filtro incorrecto

**Solución:**
```java
http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
```

---

### 3. Swagger no muestra "Authorize"

**Síntoma:** No aparece el botón 🔒 en Swagger UI

**Causas:**
- ❌ `OpenApiConfig` no está configurado
- ❌ Falta el `SecurityScheme` en la configuración

**Solución:**
```java
@Bean
public OpenAPI openAPI() {
  return new OpenAPI()
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .components(new Components()
          .addSecuritySchemes("bearerAuth",
              new SecurityScheme()
                  .type(SecurityScheme.Type.HTTP)
                  .scheme("bearer")
                  .bearerFormat("JWT")
          )
      );
}
```

---

### 4. 401 aunque hay token

**Síntoma:** Envío token válido pero recibo `401 Unauthorized`

**Causas:**
- ❌ Token expirado (supera `app.jwt.expiration-minutes`)
- ❌ Firma incorrecta (cambió `app.jwt.secret`)
- ❌ Usuario no encontrado en `UserDetailsService`
- ❌ Token malformado

**Solución:**
1. Verificar que el token no haya expirado
2. Verificar que `app.jwt.secret` sea el mismo que cuando se generó
3. Verificar que el usuario existe en el sistema

---

### 5. 403 Forbidden

**Síntoma:** Estoy autenticado pero recibo `403 Forbidden`

**Causas:**
- ❌ Usuario autenticado pero **sin el rol requerido**
- ❌ Endpoint requiere `ROLE_ADMIN` pero usuario tiene `ROLE_USER`

**Ejemplo:**
```java
.requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")
```

Si el usuario tiene `ROLE_USER` → `403 Forbidden`

**Solución:**
- Verificar los roles del usuario
- Ajustar las reglas de autorización en `SecurityConfig`

---

### 6. CORS bloqueado

**Síntoma:** El navegador bloquea la petición desde el frontend

**Error en consola:**
```
Access to XMLHttpRequest at 'http://localhost:8080/api/tasks' from origin 
'http://localhost:3000' has been blocked by CORS policy
```

**Causas:**
- ❌ `app.cors.allowed-origins` no incluye el origen del frontend
- ❌ CORS no está habilitado en `SecurityConfig`

**Solución:**
```properties
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200
```

```java
http.cors(Customizer.withDefaults())
```

---

## 🧪 Cómo Probar en Swagger

### Paso 1: Arrancar la aplicación

```bash
mvn spring-boot:run
```

### Paso 2: Abrir Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### Paso 3: Hacer login

1. Buscar endpoint: `POST /api/auth/login`
2. Click en **"Try it out"**
3. Introducir credenciales:
   ```json
   {
     "username": "user",
     "password": "pass"
   }
   ```
4. Click en **"Execute"**
5. **Copiar el token** de la respuesta:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzA2..."
   }
   ```

### Paso 4: Autorizar en Swagger

1. Click en el botón **"Authorize"** 🔒 (arriba a la derecha)
2. Pegar el token (solo el token, sin `Bearer`)
3. Click en **"Authorize"**
4. Click en **"Close"**

### Paso 5: Probar endpoints protegidos

1. Buscar endpoint: `GET /api/v1/tasks`
2. Click en **"Try it out"**
3. Click en **"Execute"**
4. ✅ Deberías recibir `200 OK` con la lista de tareas

### Paso 6: Probar sin token (opcional)

1. Click en **"Authorize"** 🔒
2. Click en **"Logout"**
3. Intentar `GET /api/v1/tasks`
4. ❌ Deberías recibir `401 Unauthorized`

---

## 📚 Comandos Útiles

### Compilar el proyecto

```bash
mvn clean compile
```

### Ejecutar tests

```bash
mvn test
```

### Ejecutar tests con cobertura

```bash
mvn clean test
# Ver reporte: target/site/jacoco/index.html
```

### Arrancar la aplicación

```bash
mvn spring-boot:run
```

### Empaquetar (JAR)

```bash
mvn clean package
```

### Ejecutar JAR

```bash
java -jar target/tasks-backend-0.0.1-SNAPSHOT.jar
```

### Ver dependencias

```bash
mvn dependency:tree
```

---

## 🎓 Resumen

### Conceptos Clave

| Concepto | Descripción |
|----------|-------------|
| **JWT** | JSON Web Token - Token firmado que contiene información del usuario |
| **Bearer Token** | Esquema de autenticación: `Authorization: Bearer <token>` |
| **STATELESS** | Sin sesión en servidor - toda la info viaja en el token |
| **SecurityFilterChain** | Cadena de filtros de Spring Security |
| **UserDetailsService** | Servicio para cargar detalles del usuario |
| **Authentication** | Objeto que representa al usuario autenticado |
| **SecurityContext** | Contexto donde se guarda la autenticación actual |

### Flujo Completo

1. **Login:**
   - Cliente → `POST /api/auth/login` → AuthController
   - AuthController → AuthService (valida) → JwtService (genera token)
   - Respuesta: `{ "token": "..." }`

2. **Request Protegido:**
   - Cliente → `GET /api/tasks` + `Authorization: Bearer <token>`
   - JwtAuthenticationFilter → JwtService (valida) → SecurityContext
   - SecurityConfig → evalúa reglas → Controller
   - Respuesta: `200 OK` o `401/403`

### Componentes Obligatorios

- ✅ `JwtService` - Genera y valida tokens
- ✅ `JwtAuthenticationFilter` - Intercepta requests
- ✅ `SecurityConfig` - Define reglas de seguridad
- ✅ `AuthController` - Endpoint de login
- ✅ `OpenApiConfig` - Configuración de Swagger

### Dependencias Obligatorias

- ✅ `spring-boot-starter-security`
- ✅ `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (0.12.5)
- ✅ `springdoc-openapi-starter-webmvc-ui` (2.6.0)

### Configuración Obligatoria

```properties
app.jwt.secret=<mínimo 32 caracteres>
app.jwt.expiration-minutes=60
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 🎯 Checklist Final

Antes del examen, asegúrate de entender estas preguntas clave:

<details>
<summary><strong>❓ ¿Qué es JWT y cómo funciona?</strong></summary>

### Respuesta:

**JWT (JSON Web Token)** es un estándar abierto (RFC 7519) que define un formato compacto y autónomo para transmitir información de forma segura entre dos partes como un objeto JSON.

**Estructura de un JWT:**

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzA2...
│                     │                                    │
│     HEADER          │        PAYLOAD                     │  SIGNATURE
```

1. **Header** (Cabecera):
   ```json
   {
     "alg": "HS256",
     "typ": "JWT"
   }
   ```
   - Define el algoritmo de firma (HS256, RS256, etc.)

2. **Payload** (Carga útil):
   ```json
   {
     "sub": "user",           // Subject (usuario)
     "iat": 1706000000,       // Issued At (fecha de emisión)
     "exp": 1706003600        // Expiration (fecha de expiración)
   }
   ```
   - Contiene los "claims" (afirmaciones) sobre el usuario

3. **Signature** (Firma):
   ```
   HMACSHA256(
     base64UrlEncode(header) + "." + base64UrlEncode(payload),
     secret
   )
   ```
   - Garantiza que el token no ha sido modificado

**¿Cómo funciona?**

1. Usuario hace login → Servidor valida credenciales
2. Servidor genera JWT firmado con clave secreta
3. Cliente guarda el token (localStorage, cookie, etc.)
4. Cliente envía token en cada petición: `Authorization: Bearer <token>`
5. Servidor valida la firma y extrae información del usuario
6. Si es válido → permite acceso; si no → `401 Unauthorized`

**Ventajas:**
- ✅ **Stateless**: No requiere sesión en servidor
- ✅ **Escalable**: Funciona en arquitecturas distribuidas
- ✅ **Autónomo**: Contiene toda la información necesaria
- ✅ **Seguro**: Firmado criptográficamente

</details>

<details>
<summary><strong>❓ ¿Qué hace JwtService?</strong></summary>

### Respuesta:

`JwtService` es el **servicio encargado de toda la lógica relacionada con JWT**. Es el cerebro del sistema de tokens.

**Responsabilidades principales:**

1. **Generar tokens JWT**
   ```java
   public String generateToken(String username) {
     return Jwts.builder()
         .subject(username)
         .issuedAt(Date.from(now))
         .expiration(Date.from(exp))
         .signWith(key)
         .compact();
   }
   ```

2. **Extraer información del token**
   ```java
   public String extractUsername(String token) {
     return parseClaims(token).getSubject();
   }
   ```

3. **Validar tokens**
   ```java
   public boolean isValid(String token) {
     try {
       Claims c = parseClaims(token);
       return c.getExpiration().after(new Date());
     } catch (Exception ex) {
       return false;
     }
   }
   ```

**¿Por qué es un Service?**

- Se reutiliza en múltiples lugares:
  - `AuthController` → para generar token en login
  - `JwtAuthenticationFilter` → para validar token en cada request
- Mantiene la lógica JWT centralizada y desacoplada
- Facilita el testing con mocks

**Ubicación:** `com.docencia.tasks.infrastructure.security.JwtService`

</details>

<details>
<summary><strong>❓ ¿Qué hace JwtAuthenticationFilter?</strong></summary>

### Respuesta:

`JwtAuthenticationFilter` es el **portero del sistema**. Intercepta **todas** las peticiones HTTP antes de que lleguen a los controllers.

**Responsabilidades:**

1. **Interceptar peticiones**
   - Hereda de `OncePerRequestFilter` (se ejecuta una vez por request)

2. **Extraer token de la cabecera**
   ```java
   String authHeader = request.getHeader("Authorization");
   String token = authHeader.substring("Bearer ".length());
   ```

3. **Validar token con JwtService**
   ```java
   String username = jwtService.extractUsername(token);
   if (jwtService.isTokenValid(token, user)) {
     // Token válido
   }
   ```

4. **Cargar detalles del usuario**
   ```java
   UserDetails user = userDetailsService.loadUserByUsername(username);
   ```

5. **Crear autenticación y guardarla en SecurityContext**
   ```java
   UsernamePasswordAuthenticationToken auth = 
       new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
   SecurityContextHolder.getContext().setAuthentication(auth);
   ```

**Flujo:**
```
Request → JwtAuthenticationFilter
            ↓
         ¿Tiene token?
            ↓ SÍ
         ¿Es válido?
            ↓ SÍ
    Autenticar usuario
            ↓
    Guardar en SecurityContext
            ↓
    Continuar al Controller
```

**Ubicación:** `com.docencia.tasks.infrastructure.security.JwtAuthenticationFilter`

</details>

<details>
<summary><strong>❓ ¿Qué hace SecurityConfig?</strong></summary>

### Respuesta:

`SecurityConfig` es la **configuración central de Spring Security**. Define todas las reglas de seguridad de la aplicación.

**Responsabilidades principales:**

1. **Definir rutas públicas y protegidas**
   ```java
   .authorizeHttpRequests(auth -> auth
       .requestMatchers("/api/auth/**").permitAll()      // Público
       .requestMatchers("/api/**").authenticated()       // Protegido
   )
   ```

2. **Configurar sesiones STATELESS**
   ```java
   .sessionManagement(sm -> 
       sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
   )
   ```

3. **Registrar el filtro JWT**
   ```java
   http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
   ```

4. **Deshabilitar CSRF** (común en APIs REST)
   ```java
   .csrf(csrf -> csrf.disable())
   ```

5. **Configurar CORS**
   ```java
   .cors(Customizer.withDefaults())
   ```

6. **Definir autorización por roles**
   ```java
   .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")
   .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**").hasAnyRole("USER", "ADMIN")
   ```

**Beans que expone:**
- `SecurityFilterChain` - Cadena de filtros de seguridad
- `PasswordEncoder` - Codificador de contraseñas (BCrypt)
- `AuthenticationManager` - Gestor de autenticación

**Ubicación:** `com.docencia.tasks.infrastructure.security.SecurityConfig`

</details>

<details>
<summary><strong>❓ ¿Cómo se genera un token?</strong></summary>

### Respuesta:

Un token JWT se genera en el **login exitoso** usando `JwtService.generateToken()`.

**Proceso paso a paso:**

1. **Usuario hace login**
   ```java
   POST /api/auth/login
   {
     "username": "user",
     "password": "pass"
   }
   ```

2. **AuthController valida credenciales**
   ```java
   if (!authService.validateCredentials(req.username(), req.password())) {
     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
   }
   ```

3. **JwtService genera el token**
   ```java
   public String generateToken(String username) {
     Instant now = Instant.now();
     Instant exp = now.plusSeconds(expirationMinutes * 60);
     
     return Jwts.builder()
         .subject(username)                    // Usuario
         .issuedAt(Date.from(now))            // Fecha de emisión
         .expiration(Date.from(exp))          // Fecha de expiración
         .signWith(key)                       // Firma con clave secreta
         .compact();                          // Genera el string JWT
   }
   ```

4. **Se devuelve al cliente**
   ```java
   return new TokenResponse(token);
   ```
   
   Respuesta:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzA2..."
   }
   ```

**Elementos del token generado:**
- `subject`: username del usuario
- `issuedAt`: timestamp de creación
- `expiration`: timestamp de expiración (configurable en `app.jwt.expiration-minutes`)
- `signature`: firma HMAC-SHA256 con la clave secreta

</details>

<details>
<summary><strong>❓ ¿Cómo se valida un token?</strong></summary>

### Respuesta:

La validación de un token ocurre en **cada petición protegida** a través del `JwtAuthenticationFilter`.

**Proceso de validación:**

1. **Extraer token de la cabecera**
   ```java
   String authHeader = request.getHeader("Authorization");
   // Ejemplo: "Bearer eyJhbGciOiJIUzI1NiJ9..."
   
   String token = authHeader.substring("Bearer ".length()).trim();
   ```

2. **Parsear y verificar firma**
   ```java
   private Claims parseClaims(String token) {
     return Jwts.parser()
         .verifyWith(key)              // Verifica firma con clave secreta
         .build()
         .parseSignedClaims(token)     // Parsea el token
         .getPayload();                // Obtiene los claims
   }
   ```
   
   Si la firma no coincide → `Exception` → token inválido

3. **Verificar expiración**
   ```java
   public boolean isValid(String token) {
     try {
       Claims c = parseClaims(token);
       return c.getExpiration().after(new Date());  // ¿No ha expirado?
     } catch (Exception ex) {
       return false;  // Token inválido
     }
   }
   ```

4. **Validar contra usuario (opcional)**
   ```java
   public boolean isTokenValid(String token, UserDetails userDetails) {
     String username = extractUsername(token);
     return username.equals(userDetails.getUsername()) && isValid(token);
   }
   ```

**Casos de invalidación:**
- ❌ Firma incorrecta (clave secreta diferente)
- ❌ Token expirado
- ❌ Token malformado
- ❌ Usuario no coincide

</details>

<details>
<summary><strong>❓ ¿Dónde se define qué rutas son públicas?</strong></summary>

### Respuesta:

Las rutas públicas se definen en **`SecurityConfig`** dentro del método `securityFilterChain()`.

**Ubicación exacta:**

```java
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, ...) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // ✅ RUTAS PÚBLICAS (permitAll)
            .requestMatchers(
                "/api/auth/**",      // Login y autenticación
                "/swagger-ui/**",    // Swagger UI
                "/v3/api-docs/**",   // OpenAPI docs
                "/h2-console/**"     // H2 Console (solo desarrollo)
            ).permitAll()
            
            // Resto de rutas...
        );
    
    return http.build();
  }
}
```

**¿Qué significa `permitAll()`?**
- No requiere autenticación
- Accesible sin token JWT
- Cualquier usuario puede acceder

**Rutas públicas típicas:**
- `/api/auth/login` - Para obtener el token
- `/swagger-ui/**` - Para documentación
- `/v3/api-docs/**` - Para especificación OpenAPI
- `/h2-console/**` - Para consola de base de datos (solo desarrollo)
- `/public/**` - Recursos públicos (imágenes, CSS, etc.)

**Archivo:** `com.docencia.tasks.infrastructure.security.SecurityConfig`

</details>

<details>
<summary><strong>❓ ¿Dónde se define qué rutas requieren autenticación?</strong></summary>

### Respuesta:

Las rutas protegidas también se definen en **`SecurityConfig`** dentro del método `securityFilterChain()`.

**Ubicación exacta:**

```java
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, ...) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // Rutas públicas
            .requestMatchers("/api/auth/**").permitAll()
            
            // ✅ RUTAS PROTEGIDAS (authenticated)
            .requestMatchers("/api/**").authenticated()
            
            // O más específico:
            .requestMatchers("/api/v1/tasks/**").authenticated()
            
            // Cualquier otra ruta
            .anyRequest().authenticated()
        );
    
    return http.build();
  }
}
```

**¿Qué significa `authenticated()`?**
- Requiere autenticación válida
- Usuario debe enviar token JWT válido
- Si no hay token → `401 Unauthorized`

**Ejemplos de configuración:**

1. **Todas las rutas bajo `/api/` protegidas:**
   ```java
   .requestMatchers("/api/**").authenticated()
   ```

2. **Rutas específicas protegidas:**
   ```java
   .requestMatchers("/api/v1/tasks/**").authenticated()
   .requestMatchers("/api/v1/users/**").authenticated()
   ```

3. **Por defecto todo protegido:**
   ```java
   .anyRequest().authenticated()
   ```

**Archivo:** `com.docencia.tasks.infrastructure.security.SecurityConfig`

</details>

<details>
<summary><strong>❓ ¿Cómo se configuran roles (USER, ADMIN)?</strong></summary>

### Respuesta:

Los roles se configuran en **dos lugares**:

### 1. Definir roles en `SecurityConfig`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, ...) throws Exception {
  http
      .authorizeHttpRequests(auth -> auth
          // Rutas públicas
          .requestMatchers("/api/auth/**").permitAll()
          
          // ✅ ROLES: USER o ADMIN pueden leer
          .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**")
              .hasAnyRole("USER", "ADMIN")
          
          // ✅ ROLES: Solo ADMIN puede crear
          .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**")
              .hasRole("ADMIN")
          
          // ✅ ROLES: Solo ADMIN puede actualizar
          .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/**")
              .hasRole("ADMIN")
          
          // ✅ ROLES: Solo ADMIN puede eliminar
          .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks/**")
              .hasRole("ADMIN")
          
          .anyRequest().authenticated()
      );
  
  return http.build();
}
```

### 2. Asignar roles al usuario

**Opción A: En memoria (para desarrollo/testing)**

```java
@Bean
public UserDetailsService userDetailsService() {
  UserDetails user = User.builder()
      .username("user")
      .password(passwordEncoder().encode("pass"))
      .roles("USER")  // ✅ Rol USER
      .build();
  
  UserDetails admin = User.builder()
      .username("admin")
      .password(passwordEncoder().encode("admin"))
      .roles("ADMIN")  // ✅ Rol ADMIN
      .build();
  
  return new InMemoryUserDetailsManager(user, admin);
}
```

**Opción B: En el filtro JWT (desde el token)**

```java
// En JwtAuthenticationFilter
String username = jwtService.extractUsername(token);

// Cargar roles desde base de datos o del token
List<SimpleGrantedAuthority> authorities = List.of(
    new SimpleGrantedAuthority("ROLE_USER"),
    new SimpleGrantedAuthority("ROLE_ADMIN")
);

UsernamePasswordAuthenticationToken auth = 
    new UsernamePasswordAuthenticationToken(
        username, 
        null, 
        authorities  // ✅ Asignar roles
    );
```

**Métodos de autorización:**

| Método | Descripción | Ejemplo |
|--------|-------------|---------|
| `hasRole("ADMIN")` | Requiere rol específico | Solo ADMIN |
| `hasAnyRole("USER", "ADMIN")` | Requiere al menos uno de los roles | USER o ADMIN |
| `hasAuthority("ROLE_ADMIN")` | Requiere autoridad específica | ROLE_ADMIN |
| `authenticated()` | Solo requiere estar autenticado | Cualquier usuario |

**Nota importante:** Spring Security añade automáticamente el prefijo `ROLE_` a los roles. Por eso usamos:
- `hasRole("ADMIN")` → busca `ROLE_ADMIN`
- `hasAuthority("ROLE_ADMIN")` → busca exactamente `ROLE_ADMIN`

</details>

<details>
<summary><strong>❓ ¿Cómo se prueba en Swagger?</strong></summary>

### Respuesta:

**Proceso completo para probar endpoints protegidos en Swagger:**

### Paso 1: Arrancar la aplicación

```bash
mvn spring-boot:run
```

### Paso 2: Abrir Swagger UI

Navegar a:
```
http://localhost:8080/swagger-ui/index.html
```

### Paso 3: Hacer login y obtener token

1. Buscar el endpoint: **`POST /api/auth/login`**
2. Click en **"Try it out"**
3. Introducir credenciales en el Request body:
   ```json
   {
     "username": "user",
     "password": "pass"
   }
   ```
4. Click en **"Execute"**
5. En la respuesta, **copiar el token**:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzA2..."
   }
   ```

### Paso 4: Autorizar en Swagger

1. Click en el botón **"Authorize"** 🔒 (arriba a la derecha)
2. En el modal que aparece:
   - Pegar **solo el token** (sin `Bearer`)
   - Ejemplo: `eyJhbGciOiJIUzI1NiJ9...`
3. Click en **"Authorize"**
4. Click en **"Close"**

### Paso 5: Probar endpoints protegidos

1. Buscar un endpoint protegido: **`GET /api/v1/tasks`**
2. Click en **"Try it out"**
3. Click en **"Execute"**
4. ✅ Deberías recibir **`200 OK`** con los datos

### Paso 6: Verificar que funciona la protección

1. Click en **"Authorize"** 🔒
2. Click en **"Logout"** (para quitar el token)
3. Intentar nuevamente **`GET /api/v1/tasks`**
4. ❌ Deberías recibir **`401 Unauthorized`**

**Indicadores visuales en Swagger:**
- 🔒 **Candado cerrado** = endpoint protegido
- 🔓 **Candado abierto** = endpoint público
- ✅ **Candado con check** = token configurado

</details>

<details>
<summary><strong>❓ ¿Qué errores comunes pueden ocurrir?</strong></summary>

### Respuesta:

### 1. **401 Unauthorized (No autorizado)**

**Síntomas:**
- Envío token pero recibo `401`

**Causas posibles:**
- ❌ Token expirado (supera `app.jwt.expiration-minutes`)
- ❌ Firma incorrecta (cambió `app.jwt.secret`)
- ❌ Token malformado
- ❌ Cabecera incorrecta (falta `Bearer ` o tiene espacios extra)
- ❌ Usuario no encontrado en `UserDetailsService`

**Solución:**
```java
// Verificar cabecera
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
//              ↑ Espacio importante

// Verificar expiración
app.jwt.expiration-minutes=60

// Verificar secret (debe ser el mismo)
app.jwt.secret=Kraj8AxPPe5XdByv9wN4o4cwhW8ExUoxH3k...
```

---

### 2. **403 Forbidden (Prohibido)**

**Síntomas:**
- Estoy autenticado pero recibo `403`

**Causas posibles:**
- ❌ Usuario autenticado pero **sin el rol requerido**
- ❌ Endpoint requiere `ROLE_ADMIN` pero usuario tiene `ROLE_USER`

**Ejemplo:**
```java
// SecurityConfig requiere ADMIN
.requestMatchers(HttpMethod.POST, "/api/v1/tasks/**").hasRole("ADMIN")

// Usuario tiene ROLE_USER → 403 Forbidden
```

**Solución:**
- Verificar roles del usuario
- Ajustar reglas en `SecurityConfig`
- Asignar roles correctos al usuario

---

### 3. **El token no se lee**

**Síntomas:**
- Siempre `401` aunque envíe token

**Causas posibles:**
- ❌ Cabecera incorrecta
- ❌ Falta prefijo `Bearer `
- ❌ Token con espacios o saltos de línea

**Solución:**
```http
✅ CORRECTO:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIi...

❌ INCORRECTO:
Authorization: eyJhbGciOiJIUzI1NiJ9...  (falta Bearer)
Authorization:Bearer eyJhbGci...        (falta espacio)
Authorization: Bearer  eyJhbGci...      (doble espacio)
```

---

### 4. **El filtro no se ejecuta**

**Síntomas:**
- El filtro JWT nunca intercepta peticiones

**Causas posibles:**
- ❌ Filtro no registrado en `SecurityConfig`
- ❌ Filtro registrado en orden incorrecto

**Solución:**
```java
// En SecurityConfig
http.addFilterBefore(
    jwtFilter, 
    UsernamePasswordAuthenticationFilter.class  // ← ANTES de este
);
```

---

### 5. **Swagger no muestra "Authorize"**

**Síntomas:**
- No aparece botón 🔒 en Swagger UI

**Causas posibles:**
- ❌ `OpenApiConfig` no configurado
- ❌ Falta `SecurityScheme`

**Solución:**
```java
@Bean
public OpenAPI openAPI() {
  return new OpenAPI()
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .components(new Components()
          .addSecuritySchemes("bearerAuth",
              new SecurityScheme()
                  .type(SecurityScheme.Type.HTTP)
                  .scheme("bearer")
                  .bearerFormat("JWT")
          )
      );
}
```

---

### 6. **CORS bloqueado**

**Síntomas:**
- Navegador bloquea peticiones desde frontend

**Error en consola:**
```
Access to XMLHttpRequest at 'http://localhost:8080/api/tasks' 
from origin 'http://localhost:3000' has been blocked by CORS policy
```

**Causas posibles:**
- ❌ `app.cors.allowed-origins` no incluye origen del frontend
- ❌ CORS no habilitado en `SecurityConfig`

**Solución:**
```properties
# application.properties
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200
```

```java
// SecurityConfig
http.cors(Customizer.withDefaults())
```

---

### 7. **Token expira muy rápido**

**Síntomas:**
- Token deja de funcionar después de poco tiempo

**Causa:**
- ❌ `app.jwt.expiration-minutes` muy bajo

**Solución:**
```properties
# Aumentar tiempo de expiración (en minutos)
app.jwt.expiration-minutes=60  # 1 hora
app.jwt.expiration-minutes=1440  # 24 horas
```

---

### 8. **H2 Console no accesible**

**Síntomas:**
- No puedo acceder a `/h2-console`

**Causas posibles:**
- ❌ No está en rutas públicas
- ❌ Frame options bloqueando iframe

**Solución:**
```java
http
    .headers(headers -> 
        headers.frameOptions(frame -> frame.sameOrigin())
    )
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()
        // ...
    );
```

</details>

---

---

## 📖 Referencias

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - Decodificador de tokens
- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [SpringDoc OpenAPI](https://springdoc.org/)

---

**¡Buena suerte en el examen! 🚀**
