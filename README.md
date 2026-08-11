Catálogo CLI

Aplicación de consola en Java para la gestión de un catálogo de productos: alta de artículos, control de inventario y cálculo de precios con descuentos e impuestos.

Proyecto desarrollado como ejercicio de programación orientada a objetos idiomática en Java, con énfasis en modelado de dominio, inmutabilidad y manejo de errores.

Stack
Java 21
Maven — gestión de dependencias y build
Sin dependencias externas: solo biblioteca estándar
Funcionalidades
Registro de productos con validación de datos (SKU, precio y stock)
Listado ordenado alfabéticamente
Búsqueda por SKU
Entradas y salidas de inventario con control de disponibilidad
Cálculo de precio final aplicando descuentos encadenados e ITBIS (18%)
Filtrado por categoría
Cómo ejecutarlo

Requisitos: JDK 21 o superior y Maven 3.9+.

bash
git clone https://github.com/RafaelUrbaez/catalogo-cli-java.git
cd catalogo-cli-java
mvn clean package
java -jar target/catalogo-cli-1.0.0.jar

Alternativa durante el desarrollo:

bash
mvn compile exec:java -Dexec.mainClass="com.rafael.catalogo.Main"
Ejemplo de uso
=== CATÁLOGO DE PRODUCTOS ===
1. Agregar producto
2. Listar productos
3. Buscar por SKU
4. Registrar entrada de stock
5. Registrar salida de stock
6. Calcular precio con descuentos
0. Salir

Opción: 5
SKU: SKU-001
Cantidad a retirar: 50

Error: Stock insuficiente para SKU-001: se solicitaron 50, hay 12
Estructura del proyecto
src/main/java/com/rafael/catalogo
├── Main.java                  Punto de entrada
├── model/                     Entidades y objetos de valor del dominio
│   ├── Producto.java
│   ├── Categoria.java
│   └── PrecioConIVA.java
├── descuento/                 Estrategias de descuento
│   ├── Descuento.java
│   ├── DescuentoPorcentual.java
│   ├── DescuentoFijo.java
│   └── CalculadoraPrecio.java
├── service/                   Lógica de negocio
│   └── CatalogoService.java
├── exception/                 Excepciones de dominio
│   ├── ProductoNoEncontradoException.java
│   └── StockInsuficienteException.java
└── ui/                        Interacción por consola
    └── MenuConsola.java
Decisiones de diseño

Identidad basada en SKU. equals y hashCode de Producto se calculan solo sobre el SKU, que además es inmutable. Dos instancias con el mismo SKU representan el mismo artículo aunque difieran en precio o stock, y el objeto nunca cambia de posición dentro de colecciones basadas en hash.

BigDecimal para montos monetarios. double introduce errores de representación binaria inaceptables en cálculos de precios. Todos los montos se redondean a dos decimales con RoundingMode.HALF_UP.

Descuentos como estrategias. La interfaz Descuento permite agregar nuevos tipos sin modificar CalculadoraPrecio, y aplicarlos en cadena sobre un mismo precio base.

Excepciones de negocio no verificadas. StockInsuficienteException y ProductoNoEncontradoException extienden RuntimeException: obligar a cada llamador a capturarlas ensuciaría la cadena de llamadas sin aportar valor, y se traducen a mensajes de error en la capa de presentación.

Separación de responsabilidades. CatalogoService no imprime nada y MenuConsola no calcula nada. El servicio puede reutilizarse detrás de una API REST sin tocar una línea.

Documentación

El código está documentado con Javadoc. Para generar la documentación navegable:

bash
mvn javadoc:javadoc

Disponible luego en target/site/apidocs/index.html.

Flujo de trabajo

El repositorio sigue Git Flow:

main — versiones estables y etiquetadas
develop — rama de integración
feature/* — desarrollo de funcionalidades
release/* — preparación de versiones

Los mensajes de commit siguen la convención Conventional Commits.

Autor

Rafael Urbaez — Desarrollador Backend GitHub
