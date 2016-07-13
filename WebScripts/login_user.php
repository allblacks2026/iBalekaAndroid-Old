<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$username = $_POST['Username'];
$password = $_POST['Password'];

if (isset($username, $password)) {
    $loginUserQuery = $connectionObject->prepare("CALL LoginAthlete(?,?)");
    $loginUserQuery->bindParam(1, $username, PDO::PARAM_STR);
    $loginUserQuery->bindParam(2, $password, PDO::PARAM_STR);
    
    $loginUserQuery->execute();
    if ($loginUserQuery->rowCount() != 1) {
        echo "Error100";
        $loginUserQuery->closeCursor();
    } else {
        $results = $loginUserQuery->fetch(PDO::FETCH_ASSOC);
        echo json_encode($results);
    }
} else {
    echo "Error200";
}