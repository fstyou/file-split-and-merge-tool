package com.main;

import com.tools.Show;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main ( String[] startValue ) {
        System.out.println ( "\n----------------------------------------------\n" +
                             "文件分割与合并工具\n" +
                             "这是本工具的控制台，请勿关闭此窗口，否则可能引起文件损坏。\n" );
        launch ( startValue );
    }
    
    @Override
    public void start ( Stage stage ) {
        stage.setTitle ( "文件分割与合并工具" );
        try {
            stage.getIcons ( ).add ( new Image ( "/icon.png" ) );
        }
        catch ( Exception e ) {
            Show.error ( "找不到图标 icon.png，请尝试卸载本工具后重新安装", false, null );
            return;
        }
        Parent layout;
        try {
            layout =
                    FXMLLoader.load ( Objects.requireNonNull ( getClass ( ).getResource ( "layout.fxml" ) ) );
        }
        catch ( Exception e ) {
            Show.error ( "找不到布局文件 com.main.layout.fxml，请尝试卸载本工具后重新安装", false, null );
            return;
        }
        Scene scene = new Scene ( layout );
        stage.setScene ( scene );
        stage.setResizable ( false );
        stage.show ( );
    }
}
