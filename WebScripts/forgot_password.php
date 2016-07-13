<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$emailAddress = $_POST['EmailAddress'];

if (isset($emailAddress)) {

    $getUser = $connectionObject->prepare("CALL sp_ForgotPassword(?)");
    $getUser->bindParam(1, $emailAddress, PDO::PARAM_STR);

    $getUser->execute();
    if ($getUser->rowCount() == 0) {
        $errorMsg = array('error' => array('message', 'No account was found with the supplied email address. Please enter a valid email'));
        echo json_encode($errorMsg);
        $getUser->closeCursor();
    } else {
        $result = $getUser->fetchAll();
        $username = $result['Username'];
        $password = $result['Password'];
        $name = $result['Name'];
        $surname = $result['Surname'];

        $mailObject = new PHPMailer();
    }
} else {
    $errorMsg = array('error' => array('message', 'Please ensure you have sent a valid email address'));
    echo json_encode($errorMsg);
}