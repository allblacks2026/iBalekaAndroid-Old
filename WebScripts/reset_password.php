<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');
require_once("db_connection.php");
include_once("class.phpmailer.php");
require("PHPMailerAutoload.php");

$emailAddress = $_POST['EmailAddress'];
$securityAnswer = $_POST['SecurityAnswer'];
$newPassword = $_POST['NewPassword'];

if (isset($emailAddress, $newPassword, $securityAnswer)) {
    $checkAnswer = $connectionObject->prepare("CALL CheckSecurityAnswer(?,?)");
    $checkAnswer->bindParam(1, $emailAddress, PDO::PARAM_STR);
    $checkAnswer->bindParam(2, $securityAnswer, PDO::PARAM_STR);
    $checkAnswer->execute();
    if ($checkAnswer->rowCount() > 0) {
        $checkAnswer->closeCursor();

        $updatePassword = $connectionObject->prepare("CALL ResetPassword(?,?)");
        $updatePassword->bindParam(1, $emailAddress, PDO::PARAM_STR);
        $updatePassword->bindParam(2, $newPassword, PDO::PARAM_STR);

        $updatePassword->execute();
        if ($updatePassword->rowCount() > 0) {

            $updatePassword->closeCursor();
            echo "Success";
            /*
            $mailObject = new PHPMailer();
            $mailObject->isSMTP();
            $mailObject->SMTPDebug = 2;
            $mailObject->Host = 'smtp.gmail.com';
            $mailObject->Username = 'koeks525.okuhle@gmail.com';
            $mailObject->Password = '813sat08';
            $mailObject->SMTPSecure = 'tls';
            $mailObject->Port = 587;
            $mailObject->Debugoutput = 'html';

            $mailObject->setFrom('allblacks2026@gmail.com', 'iBaleka Administrator');
            $mailObject->addAddress($emailAddress);

            $mailObject->Subject = 'iBaleka Mobile Password Reset';
            $mailObject->Body = 'Hi iBaleka Athlete\n\nYou have succcessfully reset your iBaleka password! For reference sake, your new password is:\n\n'.$newPassword.'\nPlease keep this in a safe place.\n\nHappy Running!\n\niBaleka Admin';

            if ($mailObject->send()) {
               echo "Success";
            } else {
                echo "PartialSuccess";
            }

            */

        } else {
            $updatePassword->closeCursor();
            echo "Error300";
        }
    } else {
        echo "Error200";
    }
} else {
    echo "Error100";
}