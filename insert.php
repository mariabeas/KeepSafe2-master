<?php 
	$hostname_localhost="localhost:8889";
	$database_localhost="bdhmis";
	$username_localhost="root";
	$password_localhost="";


	//CONECTAR CON EL SERVIDOR
	$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
	or
	trigger_error(mysql_error(),E_USER_ERROR);


	//CONECTAR A LA BD
	mysql_select_db($database_localhost,$localhost);

	//PASARLE LAS VARIABLES NECESARIAS EN LA BD PARA ALMACENARLAS
	$telefono = $_POST['telefono'];
	$mensaje=$_POST['mensaje'];
	$ubicacion=$_POST['ubicacion'];
	$direccion=$_POST['direccion'];

	//INSERTAR LAS VARIABLES
	$query_search="insert into mensajes (telefono,mensaje,ubicacion,direccion) values ('".$telefono"','".$mensaje"','".$ubicacion"','".$direccion"')";


	//COMPROBAR SI LA CONEXION AL SERVIDOR ES CORRECTA
	$query_exec =mysql_query($query_search) or die (mysql_error());

	//CERRAR LA CONEXION A LA BD
	mysql_close($localhost);


?>

