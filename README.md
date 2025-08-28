# MisMonedas

**MisMonedas** es un proyecto académico desarrollado como aplicación web dinámica para la gestión de presupuestos, ingresos y egresos personales.  
El sistema se encuentra desplegado en línea en:

👉 [https://mismonedas.duckdns.org/](https://mismonedas.duckdns.org/)

---

## 👨‍💻 Autores
- Carlos Diaz  
- Ricardo Portal  
- César Salirrosas  
- Miguel Centellas  

---

## 🚀 Tecnologías utilizadas
- **Lenguaje:** Java 23  
- **Servidor de aplicaciones:** Apache Tomcat 11.0.4  
- **Vistas:** JSP (Java Server Pages)  
- **Base de datos:** Servidor de base de datos separado (conexión vía JDBC / properties externos)  
- **Patrones aplicados:**
  - **MVC (Model-View-Controller):** separación de lógica de negocio, controladores y vistas JSP.  
  - **POO (Programación Orientada a Objetos):** clases organizadas por entidades, DAO y servicios.  

---

## 📂 Arquitectura del proyecto
- **Modelo (`model/`)**: Clases de negocio (Usuario, Ingreso, Egreso, Presupuesto, etc.).  
- **DAO (`dao/`, `interfaces/`)**: Acceso a datos con JDBC.  
- **Servicios (`service/`)**: Lógica intermedia entre DAO y controladores.  
- **Controladores (`controller/`)**: Servlets que coordinan peticiones/respuestas y aplican el patrón MVC.  
- **Vistas (`WebContent/` o `src/main/webapp/`)**: JSP, CSS, JS e imágenes.  

---

## 🌐 Despliegue
- El **servidor web (Tomcat)** y la **base de datos** se encuentran en servidores separados para mayor seguridad y escalabilidad.  
- Se utilizó **Nginx** como proxy inverso para exponer la aplicación en el dominio `mismonedas.duckdns.org` con **HTTPS (Let's Encrypt)**.  
- Archivos estáticos (CSS, JS, imágenes) se sirven directamente desde la aplicación web.  
- Configuración externa (`db.properties`) permite mantener credenciales fuera del código.  

---

## ⚙️ Requisitos para ejecutar localmente
1. **Instalar Java 23** (u otra versión compatible con Tomcat 11).  
2. **Instalar Tomcat 11.0.4** y configurarlo como runtime en Eclipse o entorno deseado.  
3. Configurar conexión a base de datos en `db.properties`.  
4. Exportar el proyecto como **WAR** e instalarlo en la carpeta `webapps/` de Tomcat.  

---

## ✨ Funcionalidades principales
- Registro y login de usuarios.  
- Gestión de **ingresos** y **egresos**.  
- Administración de **categorías** de gastos e ingresos.  
- **Presupuestos** y seguimiento de metas financieras.  
- Gráficos dinámicos con **Chart.js** y notificaciones con **SweetAlert**.  

---


## 📜 Licencia
Proyecto de carácter académico y demostrativo. Uso libre con fines educativos.
