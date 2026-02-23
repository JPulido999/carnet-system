🚀 Guía de Instalación y Configuración del Entorno (Spring Boot + Java + MySQL + Maven)

Este documento explica cómo preparar el entorno para correr un proyecto Spring Boot con Java 17, Maven y MySQL en Windows.

✅ 1. Instalar Java 17 (JDK)

Descargar Eclipse Temurin (Adoptium) JDK 17 desde:
👉 https://adoptium.net/temurin/releases/

Seleccionar:

Operating System: Windows

Architecture: x64

Version: 17 (LTS)

Instalar con el .msi y marcar la opción:

Set JAVA_HOME variable


Verificar instalación:

java -version


Debe mostrar algo como:

openjdk version "17.0.xx"

✅ 2. Instalar Maven

Descargar Apache Maven desde:
👉 https://maven.apache.org/download.cgi

Descomprimir en:

C:\Program Files\Apache\Maven


Configurar variables de entorno:

MAVEN_HOME → C:\Program Files\Apache\Maven

Agregar a PATH:

%MAVEN_HOME%\bin


Verificar instalación:

mvn -v


Debe mostrar la versión de Maven y de Java.

⚠️ Nota: Si no instalas Maven globalmente, también puedes usar el wrapper (mvnw) que viene en el proyecto.

✅ 3. Instalar MySQL

Descargar el instalador de MySQL Community Server:
👉 https://dev.mysql.com/downloads/mysql/

Durante la instalación:

Elegir Developer Default.

Configurar usuario root y una contraseña segura.

Anotar el puerto (por defecto 3306).

Verificar instalación:

mysql -u root -p


Crear una base de datos para el proyecto:

CREATE DATABASE carnetdb;

✅ 4. Configuración en el proyecto

En src/main/resources/application.properties, agregar:

spring.datasource.url=jdbc:mysql://localhost:3306/carnetdb
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect


⚠️ Reemplaza TU_PASSWORD por la contraseña que configuraste en MySQL.

✅ 5. Ejecutar el proyecto

Desde la terminal, en la carpeta del proyecto:

./mvnw spring-boot:run


o si tienes Maven instalado globalmente:

mvn spring-boot:run


Si todo está correcto, la app se levantará en:
👉 http://localhost:9000

✅ 6. Extensiones recomendadas en VS Code

Extension Pack for Java

Spring Boot Extension Pack

Lombok Annotations Support

Maven for Java

SQLTools + MySQL/MariaDB Driver

REST Client o Thunder Client (para probar APIs)

🎯 Resultado esperado

Con estos pasos tendrás configurado:

Java 17

Maven

MySQL

Spring Boot listo para correr tu backend