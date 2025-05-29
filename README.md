# 🛒 TiendaJava - Sistema de Gestión de Tienda

TiendaJava es una aplicación de escritorio desarrollada en **Java Swing**, que permite a **usuarios y administradores** interactuar con un sistema de tienda, donde se pueden **gestionar productos, usuarios, órdenes y reportes**. Se conecta con un **backend RESTful en Node.js** y utiliza `Gson` para el manejo de datos JSON.

---

## Aplicación ejecutable .jar

si no tienes maven puedes descargar el .jar y mirar la aplicación

### 🔗Get App [link](tiendajava-2.0-jar-with-dependencies.jar)

## Inicio 

Si eres un usuario normal puedes registrarte y acceder desde la primera vista.

Si quieres acceder como admistrador, las credenciales iniciales son las siguientes:

**Email:** Admin@tienda.com 

**Password:** 123

---

## 📌 Características

### 👤 Usuario Común
- Ver productos disponibles con imágenes, precio y descripción.
- Buscar productos por nombre.
- Editar su información personal y contraseña.

### 🛠️ Administrador
- Dashboard con reportes de productos, usuarios y ventas.
- Crear, editar y eliminar productos.
- Ver usuarios y administradores registrados.
- Cambiar roles de usuario.
- Eliminar cuentas.
- Ver productos fuera de stock y órdenes por estado.
- Crear nuevos administradores.

---

## 🧱 Estructura del Proyecto

```bash
src/
├── model/                 # Modelos de datos (Product, Order, User, etc.)
├── service/               # Servicios que interactúan con los repositorios
├── repository/            # Manejo de peticiones HTTP a la API
├── ui/
│   ├── components/        # Componentes reutilizables (Sidebar, Footer, Buttons, etc.)
│   ├── screens/           # Pantallas para Admin y Usuario
│   └── utils/             # Temas, fuentes, estilos, helpers
│   MainUI                 # Pantalla principal donde se crea el lienzo para las screens
│ Main.js                  #inica la app

```

## 🧑‍💻 Tecnologías Utilizadas
* Java 23

* Java Swing

* Maven 4.0

* Gson (manejo de JSON)

* HttpClient (Java HTTP API)

* Node.js + Express (Backend REST API)

## Information Backend
### 🔗Go to the repository [link](https://github.com/PinchaoRamiro/Tienda-Backend)

## 🚀 Cómo ejecutar la aplicación
Clonar el repositorio:

~~~bash
git clone https://github.com/PinchaoRamiro/TiendaJava.git
cd TiendaJava
~~~ 

Ejecutar con Maven:

~~~bash
mvn clean compile exec:java
~~~ 

Asegúrate de tener configurado el backend en http://localhost:5000/ o la URL que uses en BaseRepository.java.

Crear .jar

  ~~~bash
  mvn clean compile assembly:single
  ~~~ 

Y obtendrás un archivo como:

  ~~~pgsql
  target/tiendajava-1.0-SNAPSHOT-jar-with-dependencies.jar
  ~~~



## 🔐 Autenticación
El sistema usa JWT Tokens:

Los tokens se manejan en la clase Session después del login.

Los encabezados se configuran automáticamente en los repositorios que necesitan autenticación (Authorization: Bearer <token>).

## 🖼️ Gestión de Imágenes
Las imágenes de productos se suben desde CreateProductDialog y se almacenan en public/images/products en el backend.

Se cargan con rutas relativas que apuntan a http://localhost:5000/public/....

## 📊 Reportes
* Los administradores pueden visualizar:

* Productos agotados

* Órdenes por estado

* Resumen general del sistema (Dashboard)

### 🧑‍🏫 Desarrollador
### 👨‍💻 Ramiro Antonio Pinchao Chachinoy
### 📍 Colombia 
**Cel:** 3217840789
**Email:** rmrpinchao@gmai.com  

## 📄 Licencia
Este proyecto es de código abierto y libre de uso para propósitos educativos o personales.