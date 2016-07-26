<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$athleteEmail = $_POST['EmailAddress'];
if (isset($athleteEmail)) {

    $getEvents = $connectionObject->prepare("CALL GetAthleteRegisteredEvents(?)");
    $getEvents->bindParam(1, $athleteEmail, PDO::PARAM_STR);
    $getEvents->execute();
    if ($getEvents->rowCount() > 0) {
        $result = $getEvents->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($result);
    } else {
        echo "Error200";
    }
} else {
    echo "Error100";
}