# 🚀 Guía de Instalación y Configuración del Entorno  
(Spring Boot + Java + MySQL + Maven)

Este documento explica cómo preparar el entorno para correr un proyecto **Spring Boot con Java 17, Maven y MySQL** en Windows.  

---
## ✅ 1. Spring boot 3.5.3

## ✅ 1. Instalar Java 17 (JDK)

1. Descargar **Eclipse Temurin (Adoptium) JDK 17 (Temurin 17.0.16) desde:  
   👉 [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)  

2. Seleccionar:
   - **Operating System**: Windows  
   - **Architecture**: x64  
   - **Version**: 17 (LTS)  

3. Instalar con el `.msi` y marcar la opción:  
   ```
   Set JAVA_HOME variable
   ```

4. Verificar instalación:  
   ```bash
   java -version
   ```
   Debe mostrar algo como:  
   ```
   openjdk version "17 (Temurin 17.0.16)"
   ```

---

## ✅ 2. Instalar Maven

1. Descargar Apache Maven desde:  
   👉 [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)  

2. Descomprimir en:  
   ```
   C:\Program Files\Apache\Maven
   ```

3. Configurar variables de entorno:  
   - **MAVEN_HOME** → `C:\Program Files\Apache\Maven`  
   - Agregar a **PATH**:  
     ```
     %MAVEN_HOME%\bin
     ```

4. Verificar instalación:  
   ```bash
   mvn -v
   ```
   Debe mostrar la versión de Maven y de Java.

⚠️ Nota: Si no instalas Maven globalmente, también puedes usar el **wrapper (`mvnw`)** que viene en el proyecto.

---

## ✅ 3. Instalar PostgreeSQL

1. Descargar el instalador de Postgree Server version 16.10:  
   👉 [https://www.enterprisedb.com/downloads/postgres-postgresql-downloads](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)  

2. Durante la instalación:  
   - Elegir **Developer Default**.  
   - Configurar usuario `root` y una contraseña segura.  
   - Anotar el puerto (por defecto **3306**).  

3. Verificar instalación:  
   ```bash
   mysql -u root -p
   ```

4. Crear una base de datos para el proyecto:  
   ```sql
   CREATE DATABASE carnetdb;
   ```

---

## ✅ 4. Configuración en el proyecto

En `src/main/resources/application.properties`, agregar:  

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/carnetdb
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

⚠️ Reemplaza `TU_PASSWORD` por la contraseña que configuraste en MySQL.

---

## ✅ 5. Ejecutar el proyecto

1. Desde la terminal, en la carpeta del proyecto:  
   ```bash
   ./mvnw spring-boot:run
   ```
   o si tienes Maven instalado globalmente:  
   ```bash
   mvn spring-boot:run
   ```

2. Si todo está correcto, la app se levantará en:  
   👉 [http://localhost:9000](http://localhost:9000)  

---

## ✅ 6. Extensiones recomendadas en VS Code

- **Extension Pack for Java**  
- **Spring Boot Extension Pack**  
- **Lombok Annotations Support**  
- **Maven for Java**  
- **SQLTools + MySQL/MariaDB Driver**  
- **REST Client** o **Thunder Client** (para probar APIs)  

---

## 🎯 Resultado esperado
Con estos pasos tendrás configurado:  
- **Java 17**  
- **Maven**  
- **MySQL**  
- **Spring Boot listo para correr tu backend**  
