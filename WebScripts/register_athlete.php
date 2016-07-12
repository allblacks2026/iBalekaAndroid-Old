<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$name = $_POST['Name'];
$surname = $_POST['Surname'];
$country = $_POST['Country'];
$dateOfBirth = $_POST['DateOfBirth'];
$emailAddress = $_POST['EmailAddress'];
$userName = $_POST['Username'];
$password = $_POST['Password'];
$securityQuestion = $_POST['SecurityQuestion'];
$securityAnswer = $_POST['SecurityAnswer'];
$type = "A";

if (isset($name, $surname, $country, $dateOfBirth, $emailAddress, $userName, $password, $securityQuestion, $securityAnswer)) {

    $checkAthlete = $connectionObject->prepare("CALL CheckAthlete(?)");
    $checkAthlete->bindParam(1, $emailAddress, PDO::PARAM_STR);

    $checkAthlete->execute();
    if ($checkAthlete->rowCount() > 0) {
        echo "Error200";
        $checkAthlete->closeCursor();
    } else {
        $checkAthlete->closeCursor();

        $checkAthleteUsername = $connectionObject->prepare("CALL CheckUsername(?)");
        $checkAthleteUsername->bindParam(1, $userName, PDO::PARAM_STR);

        $checkAthleteUsername->execute();
        if ($checkAthleteUsername->rowCount() > 0) {
            echo "Error300";
        } else {
            $checkAthleteUsername->closeCursor();
            $insertAthlete = $connectionObject->prepare("CALL InsertAthlete(?,?,?,?,?,?,?,?,?,?)");
            
            $insertAthlete->bindParam(1, $name, PDO::PARAM_STR);
            $insertAthlete->bindParam(2, $surname, PDO::PARAM_STR);
            $insertAthlete->bindParam(3, $emailAddress, PDO::PARAM_STR);
            $insertAthlete->bindParam(4, $country, PDO::PARAM_STR);
            $insertAthlete->bindParam(5, $type, PDO::PARAM_STR);
            $insertAthlete->bindParam(6, $dateOfBirth, PDO::PARAM_STR);
            $insertAthlete->bindParam(7, $userName, PDO::PARAM_STR);
            $insertAthlete->bindParam(8, $password, PDO::PARAM_STR);
            $insertAthlete->bindParam(9, $securityQuestion, PDO::PARAM_STR);
            $insertAthlete->bindParam(10, $securityAnswer, PDO::PARAM_STR);
            
            $insertAthlete->execute();
            if ($insertAthlete->rowCount() > 0) {
                echo "Success";
            } else {
                echo "Error400";
            }
        }
    }
} else {
        echo "Error400";
}

?>
