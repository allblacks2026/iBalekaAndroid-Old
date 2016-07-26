<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$emailAddress = $_POST['EmailAddress'];

if (isset($emailAddress)) {

    $getUser = $connectionObject->prepare("CALL ForgotPassword(?)");
    $getUser->bindParam(1, $emailAddress, PDO::PARAM_STR);

    $getUser->execute();
    if ($getUser->rowCount() == 0) {
        echo "Error200";
        $getUser->closeCursor();
    } else {
        $result = $getUser->fetch(PDO::FETCH_ASSOC);
        echo json_encode($result);
    }
} else {
    echo "Error100";
}