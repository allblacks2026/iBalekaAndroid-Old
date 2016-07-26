<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$emailAddress = $_POST['EmailAddress'];
if (isset($emailAddress)){

    $getAthleteExtended = $connectionObject->prepare("CALL GetAthleteProfileExtended(?)");
    $getAthleteExtended->bindParam(1, $emailAddress, PDO::PARAM_STR);
    $getAthleteExtended->execute();
        $result = $getAthleteExtended->fetch(PDO::FETCH_ASSOC);
        echo json_encode($result);
} else {
    echo "Error300";
}