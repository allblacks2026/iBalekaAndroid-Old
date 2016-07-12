<?php

$host = "mysql:host=localhost;dbname=iBaleka";
$username = "koeks525";
$password = "Okuhle*1994";

$connectionObject = new PDO($host, $username, $password);
$connectionObject->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

