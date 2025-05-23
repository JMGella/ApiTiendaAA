# **API Tienda**

## **Descripción**
Este proyecto es una API  para la gestión de usuarios, pedidos, productos, y categorías en una tienda. Permite realizar operaciones CRUD y manejar relaciones entre las diferentes entidades.


---

### **Actualización**

1.**Se han añadido test unitarios y de integración.
2.**Se ha añadido una versión mock de la api con WireMock.
3.**Se ha creado una colección de Postman para la api mock.

---

## **Requisitos Previos**

### **Software Necesario**
1. **Java Development Kit (JDK)**: Versión 17 o superior.
2. **Maven**
3. **Base de Datos**: H2 (o cualquier otra base de datos compatible).
4. **Postman (Opcional)**: Para probar la API de forma manual.

---

## **Configuración del Proyecto**

### **Clonar el Repositorio**
```bash
git clone https://github.com/JMGella/api-tienda.git
cd api-tienda
```

## Configurar el Archivo de Propiedades
Accede al archivo application.properties ubicado en src/main/resources/.

Configura las propiedades de la base de datos:

```bash
spring.application.name=ApiTiendaAA

# Configuracion para el acceso a la Base de Datos
spring.jpa.hibernate.ddl-auto=update

# Puerto donde escucha el servidor una vez se inicie
server.port=8080

# Datos de conexion con la base de datos H2
spring.datasource.url=jdbc:h2:file:~/apitienda.db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

logging.level.org.springframework=INFO
logging.level.org.hibernate=INFO
```


### Instalar Dependencias
Ejecuta el siguiente comando para descargar las dependencias del proyecto:

```bash
mvn clean install
```

## Ejecutar la Aplicación

### Desde la Línea de Comandos
Usa Maven para compilar y ejecutar el proyecto:

```bash

mvn spring-boot:run
```

## Desde un IDE
1. Abre el proyecto en tu IDE (IntelliJ IDEA, Eclipse, VS Code, etc.).
2. Asegúrate de que el entorno JDK esté configurado correctamente.
3. Ejecuta la clase principal Application.java.
# Probar la API
## Colección de Postman
1. Importa el archivo apiTienda.postman_collection.json ubicado en la raíz del proyecto.
2. Prueba los endpoints disponibles con las configuraciones predefinidas.
## **Ejemplo de Endpoints**

### **Usuarios**
- **`POST /users`**: Crear un usuario.
- **`GET /users`**: Obtener todos los usuarios.
- **`GET /users/{id}`**: Obtener un usuario por ID.
- **`PUT /users/{id}`**: Actualizar un usuario.
- **`DELETE /users/{id}`**: Eliminar un usuario.

### **Pedidos**
- **`POST /users/{userId}/orders`**: Crear un pedido para un usuario.
- **`GET /users/{userId}/orders`**: Listar los pedidos de un usuario.


