package com.main;

import com.tools.Show;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class Control implements Initializable {
    @FXML
    private Tab split;
    @FXML
    private Tab merge;
    @FXML
    private Hyperlink gplHyperlink;
    @FXML
    private Hyperlink gitLabHyperlink;
    @FXML
    private Hyperlink gitHubHyperlink;
    @FXML
    private Hyperlink blogHyperlink;
    
    @Override
    public void initialize ( URL location, ResourceBundle resources ) {
        Pane layout1;
        try {
            layout1 = FXMLLoader.load ( requireNonNull ( com.split.Control.class.getResource ( "layout.fxml" ) ) );
        }
        catch ( IOException e ) {
            Show.error ( "找不到布局文件 com.split.layout.fxml，请尝试卸载本工具后重新安装", false, null );
            Stage stage = ( Stage ) split.getTabPane ( ).getScene ( ).getWindow ( );
            stage.close ( );
            return;
        }
        split.setContent ( layout1 );
        Pane layout2;
        try {
            layout2 = FXMLLoader.load ( requireNonNull ( com.merge.Control.class.getResource ( "layout.fxml" ) ) );
        }
        catch ( IOException e ) {
            Show.error ( "找不到布局文件 com.merge.layout.fxml，请尝试卸载本工具后重新安装", false, null );
            Stage stage = ( Stage ) merge.getTabPane ( ).getScene ( ).getWindow ( );
            stage.close ( );
            return;
        }
        merge.setContent ( layout2 );
        gplHyperlink.setOnAction ( run -> new Main ( ).getHostServices ( ).showDocument ( gplHyperlink.getText ( ) ) );
        gitLabHyperlink.setOnAction (
                run -> new Main ( ).getHostServices ( )
                                   .showDocument ( "https://gitlab.com/fstyou/file-split-and-merge-tool" ) );
        gitHubHyperlink.setOnAction (
                run -> new Main ( ).getHostServices ( )
                                   .showDocument ( "https://github.com/fstyou/file-split-and-merge-tool" ) );
        blogHyperlink.setOnAction (
                run -> new Main ( ).getHostServices ( ).showDocument ( blogHyperlink.getText ( ) ) );
    }
}
