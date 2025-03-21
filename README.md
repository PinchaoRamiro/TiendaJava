# TiendaJava

TiendaJava es una aplicación en **Java con Swing** que permite modelar una tienda en línea.   

## Funcionalidades:

 **Registro de usuarios**  
 **Inicio de sesión con autenticación JWT**  
 **Interfaz gráfica (Swing) con login y panel principal**  
 **Conexión con backend en Node.js y base de datos**  


## installation

# ⚙️ Instalación de TiendaJava

## 1. Clonar el repositorio
```sh
git clone https://github.com/PinchaoRamiro/TiendaJava.git
cd TiendaJava
```

## 2. ejecutar el proyecto

```sh
javac -d bin -cp lib/json-20210307.jar src/ui/*.java src/service/*.java src/model/*.java
java -cp lib/json-20210307.jar:bin ui.LoginGUI

```
### o si tienes windows:
```sh
javac -d bin -cp lib/json-20210307.jar src/ui/*.java src/service/*.java src/model/*.java
java -cp "lib/json-20210307.jar;bin" ui.LoginGUI


```

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
