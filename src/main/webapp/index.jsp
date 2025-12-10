<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
<br>
<br>
<a href="${pageContext.request.contextPath}/tienda/usuarios/">usuarios</a>
<br>
<br>
<p>Voy a hacer una tienda online que venda impresiones 3d.
    la base de datos por ahora va así
    -- create database empresa;
    -- use empresa;

    CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) not null,
    rol VARCHAR(30) not null,
    password VARCHAR(256) not null
    );

    CREATE TABLE productos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) not null,
    precio DECIMAL(10,2) not null
    );

    CREATE TABLE pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT not null,
    importe DECIMAL(10,2) not null,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );

    CREATE TABLE detalle_pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT not null,
    producto_id INT not null,
    cantidad INT not null,
    precio_unitario DECIMAL(10,2) not null,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
    );
</p>
</body>
</html>