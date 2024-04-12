package com.tools;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Show {
    public static void error ( String HeaderText, boolean setContentText, String ContentText ) {
        Alert error = new Alert ( Alert.AlertType.ERROR );
        error.setTitle ( "错误" );
        Stage stage = ( Stage ) error.getDialogPane ( ).getScene ( ).getWindow ( );
        try {
            stage.getIcons ( ).add ( new Image ( "/icon.png" ) );
        }
        catch ( Exception e ) {
            System.out.println ( "找不到图标 icon.png，请尝试卸载本工具后重新安装" );
        }
        error.setHeaderText ( HeaderText );
        if ( setContentText ) {
            error.setContentText ( ContentText );
        }
        error.showAndWait ( );
    }
    
    public static void info ( String HeaderText, boolean setContentText, String contentText ) {
        Alert information = new Alert ( Alert.AlertType.INFORMATION );
        information.setTitle ( "消息" );
        Stage stage = ( Stage ) information.getDialogPane ( ).getScene ( ).getWindow ( );
        try {
            stage.getIcons ( ).add ( new Image ( "/icon.png" ) );
        }
        catch ( Exception e ) {
            System.out.println ( "找不到图标 icon.png，请尝试卸载本工具后重新安装" );
        }
        information.setHeaderText ( HeaderText );
        if ( setContentText ) {
            information.setContentText ( contentText );
        }
        information.showAndWait ( );
    }
}
