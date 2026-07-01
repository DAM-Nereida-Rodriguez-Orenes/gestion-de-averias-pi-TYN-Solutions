# Gestion-de-averias-TYN-Solutions
"Fixora", de TYN Solutions
Proyecto Intermodular de 2ºDAM, curso 2025/2026
IES Antonio José Cavanilles, Alicante (España)
Creado por: Yosué Navarro Molina, Thanya Tiffany Jecrois Savo y Nereida Rodríguez Orenes

## 1. Introducción

Este repositorio contiene la aplicación para escritorio del sistema Fixora, un software multiaplicación que ayuda a gestionar el mantenimiento correctivo de maquinaria pesada, como fresadoras, tornos o CNC en plantas de fabricación industrial. Esta idea surgió al visitar el taller de mecatrónica del instituto y entender que, si se rompe una máquina, los usuarios necesitan un arreglo eficiente y veloz para volver a sus tareas sin ocasionar pérdidas de tiempo o económicas. El sistema Fixora se compone de tres herramientas:

- Fixora Desktop: aplicación de escritorio que permite a los usuarios reportar averías, entre otras funciones (vea el punto 5 para más información). Se conecta directamente a la base de datos.
- Fixora Fieldtech: o Fixora Mobile. Aplicación para Android que permite a los mecánicos llevar un control de las averías que tienen asignadas, completadas y documentadas. Se conecta a la base de datos mediante la API.
- API REST: mediante endpoints hace de puente entre la aplicación móvil y la base de datos, utilizando JSON para el intercambio de información y JWT para mejorar la seguridad.

La API y la base de datos, MariaDB, se alojaron en un ordenador-servidor, mientras que las otras aplicaciones se conectaban a ellas desde otras máquinas físicas.

Académicamente, este proyecto une los conocimientos adquiridos en los diferentes módulos que componen el Grado Superior de Formación Profesional en Diseño de Aplicaciones Multiplataforma.

## 2. Fases del proyecto

En una primera fase, desarrollada en 1ºDAM, visitamos el taller y creamos una base documental con un diagrama del flujo de la información, historias de usuario, el diagrama entidad-relación de la base de datos, una página web y pequeños bocetos de funcionalidades con Java.

Al principio de 2ºDAM, desde mediados de septiembre a noviembre nos encargamos de acotar el alcance del proyecto, incorporar los nuevos módulos y generar un estudio de competencia (benchmarking). También empezamos a gestionar el trabajo como una empresa tipo start-up.

Desde noviembre hasta mediados de marzo trabajamos en el desarrollo de las distintas aplicaciones y su comunicación entre ellas. Para ello, nos acogimos a la filosofía Agile, utilizando Scrum y Odoo como ERP.

Por último, a finales de marzo redactamos la memoria del proyecto, otra documentación necesaria para las diferentes asaignaturas y presentamos el producto ante jurado.

## 3. División del trabajo

Al principio, con la intención académica de que todos practicáramos y entendieramos todo el proyecto, los tres integrantes del grupo hacíamos de todo, usando Git para controlar que el trabajo fuera eficiente y solucionar conflictos. Aun así, en enero, el tiempo apremiaba y decidimos especializarnos, quedando el equipo como:

- Thanya Jecrois: frontend desktop.
- Yosué Navarro: Kotlin app.
- Nereida Rodríguez: backend.

## 4. Tecnologías y herramientas utilizadas

Java, Java Maven, Java Swing, GitFlow, NetBeans, Kotlin, Android Studio, Grizzly, Scrum, Kanban, Odoo (Python), Hikari, MariaDB, XAMPP.

## 5. Usuarios del sistema Fixora y flujo

Fixora está basado en 3 tipos de usuarios, cada uno con necesidades diferentes pero interconectados entre sí:

- Operario: trabajador de la planta industrial que puede detectar una avería y reportarla mediante la aplicación de escritorio.
- Administrador: jefes, encargados o figuras coordinadoras. Además de la tarea del operario, también pueden editar la base de datos mediante CRUD, en la propia interfaz (GUI) de Fixora Desktop.
- Mecánico: trabajador que puede ser independiente a la empresa que tiene las máquinas. Con la aplicación móvil, gestiona su lista de averías, pudiendo modificarlas en la base de datos al cerrar una incidencia.

Así pues, el flujo normal de una avería dada sería ser reportada por un operador en la aplicación de escritorio, registrándola en la base de datos con una fecha que indique ese momento. A continuación, un administrador la asignaría a un técnico, que puede ser el recomendado por el propio Fixora Desktop o no, guardando esta fecha también en la base de datos. Después, el técnico recibiría la avería en la aplicación móvil como nueva y, al aceptarla, esta fecha también se registraría, junto al estado de la maquinaria, usando la API como intermediaria. Finalmente, el técnico registraría su intervención y cerraría la incidencia, registrándose este texto y fecha de nuevo en la base de datos. Como añadido, Fixora Desktop permite gestiones generales de maquinaria, usuarios y las tablas catálogo.

## 6. Descarga y uso

Debido a la falta de tiempo, no se creó un ejecutable de Fixora Desktop pero, como es un proyecto de Maven, se puede descargar, modificar la ruta de la base de datos y regenerar el JAR. Este archivo de puede ejecutar desde consola con Java.
