insert into clientes(nombre,apellido,email,fecha,foto) values('Elvis Aldair', 'Anaya Mendoza', 'aldaelvis@hotmail.com','2018-10-12','');
insert into clientes(nombre,apellido,email,fecha,foto) values('Karen', 'Huaman Quispe', 'karen233@hotmail.com','2018-10-12','');
insert into clientes(nombre,apellido,email,fecha,foto) values('Elezier', 'de Leon', 'ellezier123sd@gmail.com','2018-10-12','');

insert into productos(nombre,precio,fecha) values('Panasonic Pantalla LCD', 1000.50, NOW());
insert into productos(nombre,precio,fecha) values('Sony Camara Digital', 2500.10, NOW());
insert into productos(nombre,precio,fecha) values('Procesador Intel Core I3', 325.00, NOW());
insert into productos(nombre,precio,fecha) values('Mouse Genius', 15.30, NOW());


insert into facturas(descripcion,observacion,fecha, cliente_id) values('Factura de equipo de oficina',null,NOW(), 1);
insert into facturas_items(cantidad,factura_id,producto_id) values(1,1,1);
insert into facturas_items(cantidad,factura_id,producto_id) values(2,1,4);
insert into facturas_items(cantidad,factura_id,producto_id) values(2,1,3);