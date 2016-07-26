<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$eventID = $_POST['EventID'];
if (isset($eventID)) {
    $getPoints = $connectionObject->prepare("CALL GetEventCheckpoints(?)");
    $getPoints->bindParam(1, $eventID, PDO::PARAM_INT);
    $getPoints->execute();
    if ($getPoints->rowCount() > 0) {
        $result = $getPoints->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($result);
    } else {
        echo "Error200";
    }
} else {
    echo "Error100";
}