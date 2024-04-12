package com.merge;

import com.tools.FileUtil;
import com.tools.Show;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Control implements Initializable {
    private static boolean isPath;
    private static boolean isPaths;
    @FXML
    private Button pathButton;
    @FXML
    private Label pathLabel;
    @FXML
    private Button pathsButton;
    @FXML
    private Label pathsLabel;
    @FXML
    private Button beginButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button exitButton;
    private boolean isExitLabel = true;
    private Operate operate;
    private Thread run;
    private Timer runDoingLabel;
    private Timer runExitLabel;
    
    @Override
    public void initialize ( URL location, ResourceBundle resources ) {
        operate = new Operate ( );
        isPath = false;
        isPaths = false;
        pathButton.setOnAction ( action -> {
            String getPath =
                    String.valueOf ( FileUtil.chooseOne ( ).showOpenDialog ( pathButton.getScene ( ).getWindow ( ) ) );
            java.io.File testPath = new java.io.File ( getPath );
            if ( testPath.isFile ( ) ) {
                if ( testPath.canRead ( ) ) {
                    pathLabel.setText ( testPath.getAbsolutePath ( ) );
                    isPath = true;
                    operate.path ( getPath );
                }
                else {
                    Show.info ( "待合并文件无法读取", false, null );
                }
            }
        } );
        pathsButton.setOnAction ( action -> {
            String getPaths =
                    String.valueOf ( FileUtil.chooseDir ( ).showDialog ( pathsButton.getScene ( ).getWindow ( ) ) );
            File testPaths = new File ( getPaths );
            pathsLabel.setText ( testPaths.getAbsolutePath ( ) );
            isPaths = true;
            operate.paths ( getPaths );
        } );
        beginButton.setOnAction ( action -> {
            if ( ! operate.doing ( ) ) {
                if ( isPath && isPaths ) {
                    Stage stage = ( Stage ) beginButton.getScene ( ).getWindow ( );
                    stage.setOnCloseRequest ( event -> {
                        Show.info ( "请在分割或合并动作完成后再关闭此工具，否则将损坏文件", false, null );
                        event.consume ( );
                    } );
                    pathButton.setDisable ( true );
                    pathsButton.setDisable ( true );
                    beginButton.setDisable ( true );
                    exitButton.setDisable ( false );
                    Show.info ( "准备合并文件（如遇窗口卡死，请耐心等待）", false, null );
                    runDoingLabel = new Timer ( );
                    runDoingLabel.schedule ( new doingLabelTask ( ), 0, 500 );
                    new Thread ( ( ) -> {
                        try {
                            run = new Thread ( operate );
                            run.start ( );
                            run.join ( );
                            runDoingLabel.cancel ( );
                            runDoingLabel.purge ( );
                            if ( runExitLabel != null ) {
                                runExitLabel.cancel ( );
                                runExitLabel.purge ( );
                            }
                            if ( operate.hasError ( ) ) {
                                Platform.runLater ( ( ) -> statusLabel.setText ( "合并已停止" ) );
                            }
                            else {
                                Platform.runLater ( ( ) -> statusLabel.setText ( "合并已完成" ) );
                            }
                        }
                        catch ( Exception e ) {
                            runDoingLabel.cancel ( );
                            runDoingLabel.purge ( );
                            if ( runExitLabel != null ) {
                                runExitLabel.cancel ( );
                                runExitLabel.purge ( );
                            }
                            Platform.runLater ( ( ) -> statusLabel.setText ( "合并已停止" ) );
                        }
                        isExitLabel = true;
                        pathButton.setDisable ( false );
                        pathsButton.setDisable ( false );
                        beginButton.setDisable ( false );
                        exitButton.setDisable ( true );
                        stage.setOnCloseRequest ( event -> {
                        } );
                    } ).start ( );
                }
                else {
                    Show.info ( "参数未填写完整", false, null );
                }
            }
        } );
        exitButton.setOnAction ( action -> {
            if ( operate.doing ( ) ) {
                try {
                    if ( isExitLabel ) {
                        runDoingLabel.cancel ( );
                        runDoingLabel.purge ( );
                        runExitLabel = new Timer ( );
                        runExitLabel.schedule ( new exitLabelTask ( ), 0, 500 );
                        isExitLabel = false;
                    }
                }
                catch ( Exception ignored ) {
                }
                operate.exit ( );
            }
        } );
    }
    
    private final class doingLabelTask extends TimerTask {
        private int step = 1;
        
        private String getText ( ) {
            String result = "正在合并文件" + " .".repeat ( Math.max ( 0, step ) );
            if ( step == 6 ) {
                step = 1;
            }
            else {
                ++ step;
            }
            return result;
        }
        
        @Override
        public void run ( ) {
            Platform.runLater ( ( ) -> statusLabel.setText ( getText ( ) ) );
        }
    }
    
    private final class exitLabelTask extends TimerTask {
        private int step = 1;
        
        private String getText ( ) {
            String result = "正在停止合并" + " .".repeat ( Math.max ( 0, step ) );
            if ( step == 6 ) {
                step = 1;
            }
            else {
                ++ step;
            }
            return result;
        }
        
        @Override
        public void run ( ) {
            Platform.runLater ( ( ) -> statusLabel.setText ( getText ( ) ) );
        }
    }
}
