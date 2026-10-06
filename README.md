# GestorInformeSumPerdidasImpl

## Ejecutar el test

```
mvn -Dtest=GestorInformeSumPerdidasImplTest test
```

Genera `target/informe-control.html` con las tablas de control.

`stubs/java` contiene versiones mínimas de las clases del proyecto TLG que no están en este
repo (beans, DAOs y gestores), solo para poder compilar y ejecutar el test aquí. En el
proyecto real no se copian: allí se usan las clases de verdad.
