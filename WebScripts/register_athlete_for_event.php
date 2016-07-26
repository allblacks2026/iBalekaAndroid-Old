<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");

$emailAddress = $_POST['EmailAddress'];
$eventID = $_POST['EventID'];
$currDate = date("Y-m-d");

if (isset($emailAddress, $eventID)) {
    $getAthleteID = $connectionObject->prepare("CALL GetAthleteById(?)");
    $getAthleteID->bindParam(1, $emailAddress, PDO::PARAM_STR);
    $getAthleteID->execute();

    if ($getAthleteID->rowCount() > 0) {
        $resultAthlete = $getAthleteID->fetch(PDO::FETCH_ASSOC);
        $athleteID = $resultAthlete['UserID'];
        $getAthleteID->closeCursor();
            $checkReg = $connectionObject->prepare("CALL CheckAthleteEventRegistration(?,?)");
            $checkReg->bindParam(1, $athleteID, PDO::PARAM_INT);
            $checkReg->bindParam(2, $eventID, PDO::PARAM_INT);
            $checkReg->execute();

        if ($checkReg->rowCount() == 0) {
            $checkReg->closeCursor();

            $registerAthlete = $connectionObject->prepare("CALL RegisterAthleteToEvent(?,?,?)");
            $registerAthlete->bindParam(1, $athleteID, PDO::PARAM_INT);
            $registerAthlete->bindParam(2, $eventID, PDO::PARAM_INT);
            $registerAthlete->bindParam(3, $currDate, PDO::PARAM_STR);

            $registerAthlete->execute();
            if ($registerAthlete->rowCount() > 0) {
                echo "Success";
            } else {
                echo "Error400";
            }
        } else {
            echo "Error300";
        }

    } else {
        echo "Error200";
    }
} else {
    echo "Error100";
}