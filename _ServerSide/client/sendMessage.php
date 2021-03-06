<?php
	header('Content-type: text/html');
    require_once 'includes/xen_api.php';
    require_once 'includes/database.php';
    require_once 'includes/config.php';
    require_once 'includes/utils.php';

    $GROUP = 'dark_taigachat';
    $PERM = 'post'; //MyLoli
    $params = $_POST;
    $xenAPI = new XenAPI($apiUrl, $apiKey);
    $err = 0;
    $errMsg = "Success!";
    try {
        $message = $params['msg'];  
        $login = $params['l'];
        $hash = $params['hash'];
        if (checkAuthHash($login, $hash, $xenAPI)){
            $xfDir = dirname('../../');
            require_once($xfDir . '/library/XenForo/Autoloader.php');
            XenForo_Autoloader::getInstance()->setupAutoloader($xfDir. '/library');
            XenForo_Application::initialize($xfDir . '/library', $xfDir);
            XenForo_Application::set('page_start_time', microtime(TRUE));
            $uid = getUserID($DBH, $login);
            //print($uid);
            $user = XenForo_Visitor::setup($uid);
            //var_dump($user, true);
            if ($user -> hasPermission($GROUP, $PERM)){
                $time = time();
                //uid - 1
                //login - 2
                //time - 3,4
                //msg - 5
                $q = "INSERT INTO dark_taigachat (user_id, room_id, username, date, last_update, message, activity) VALUES (? 1, '?', ?, ?, '?', 0) ";
                $STH = $DBH->prepare($q);
                $STH->bindParam(1, $uid);  
                $STH->bindParam(2, $login);  
                $STH->bindParam(3, $time); 
                $STH->bindParam(4, $time);  
                $STH->bindParam(5, $message); 
                $STH->execute(); 
                //print($q);
                //mysqli_query($link,$q);
            } else {
                $errMsg = 'You are not permitted to write in chat.';
                $err = 1337;
            }
        } else{
            $errMsg = 'You must be logged in.';
            $err = 1488;
        }
    } catch (Exception $e) {
        if ($e->getCode() == 400) {
            $error = json_decode($e->getMessage(), TRUE);
            $errMsg = 'API call failed: API ERROR CODE=' . $error['error'] . ' & API ERROR MESSAGE=' . $error['message'];
            $err = $error['error'];
        } else {
            $errMsg = 'API call failed: HTTP RESPONSE=' . $e->getMessage() . ' & HTTP STATUS CODE=' . $e->getCode();
            $err = $e -> getCode();
        }
    }
    die(json_encode(array('error' => $err, 'errorMsg' => $errMsg),128));
?>