# Tarea3_INF331
## Working with TDD.

## Tarjeta de fidelidad gamificada
- Aplicación en Java 17.
- Pruebas en JUnit.
- Persistencia en memoria.
- Diseño modularizado.
- Uso de SnarQube.
- TDD en la construcción del código y lógica.
  



---

## 1. Contexto:
El sistema deja gestionar un programa de fidelidad de clientes para grandes tiendas.
Se incluye en las funcionalidades del sistema:
- **Administrar clientes** y su acumulación de puntos.
- **Registrar** compras y otorgar los puntos según las reglas de negocio.
- **Gestionar** niveles de fidelidad como BRONCE, PLATA, ORO y PLATINO.

**Servicio Cliente**

id, nombre, correo, puntos (0), nivel (BRONCE), streakDias (0) y el CRUD completo para este servicio. Se implementa la restricción de correo electrónico verificando un "@".

**Servicio Compras**

idCompra, idCliente, monto, fecha (uctNow). Cada $100 de compra se agrega un punto al idCliente. Se tienen multiplicadores de puntos según el nivel del cliente. (Bronce x 1, Plata x 1.2 , Oro x 1.5, Platino x 2). Si el usuario hace 3 compras seguidas en un día, se suman +10 puntos. CRUD completo para compras.

**Niveles de fidelidad**
| Nivel | Umbral (puntos totales) | Beneficio |
|-------|-------------------------|-----------|
| Bronce | 0 – 499 | — |
| Plata  | 500 – 1499 | +20 % puntos |
| Oro    | 1500 – 2999 | +50 % puntos |
| Platino| 3000 + | +100 % puntos |

**Diseño general**
