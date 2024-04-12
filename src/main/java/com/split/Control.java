package com.split;

import com.tools.Check;
import com.tools.FileUtil;
import com.tools.Show;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Control implements Initializable {
    private static boolean isPath;
    private static boolean isSplitNumber;
    private static boolean isWrongSplitNumber = false;
    @FXML
    public Label statusLabel;
    @FXML
    private Button pathButton;
    @FXML
    private Label pathLabel;
    @FXML
    private TextField numberTextField;
    @FXML
    private Button beginButton;
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
        Control.isPath = false;
        Control.isSplitNumber = false;
        pathButton.setOnAction ( action -> {
            String getPath =
                    String.valueOf ( FileUtil.chooseFile ( ).showOpenDialog ( pathButton.getScene ( ).getWindow ( ) ) );
            java.io.File testPath = new java.io.File ( getPath );
            if ( testPath.isFile ( ) ) {
                if ( testPath.canRead ( ) ) {
                    if ( testPath.length ( ) < 1_048_576L ) {
                        Show.info ( "这么小的文件，用不着分割吧？", false, null );
                    }
                    else {
                        pathLabel.setText ( testPath.getAbsolutePath ( ) );
                        Control.isPath = true;
                        operate.operatePath ( getPath );
                    }
                }
                else {
                    Show.info ( "待分割文件无法读取", false, null );
                }
            }
        } );
        beginButton.setOnAction ( action -> {
            if ( ! operate.isDoing ( ) ) {
                long number;
                try {
                    BigInteger testNumber = new BigInteger ( numberTextField.getText ( ) );
                    if ( Check.isLong ( testNumber ) && testNumber.compareTo ( BigInteger.ONE ) > 0 ) {
                        number = Long.parseLong ( numberTextField.getText ( ) );
                        isSplitNumber = true;
                        operate.splitNumber ( number );
                    }
                    else {
                        Show.info ( "再玩就玩坏了", false, null );
                        isSplitNumber = false;
                        isWrongSplitNumber = true;
                    }
                }
                catch ( Exception e ) {
                    Show.info ( "再玩就玩坏了", false, null );
                    isSplitNumber = false;
                    isWrongSplitNumber = true;
                }
                if ( isPath && isSplitNumber ) {
                    Stage stage = ( Stage ) beginButton.getScene ( ).getWindow ( );
                    stage.setOnCloseRequest ( event -> {
                        Show.info ( "请在分割或合并动作完成后再关闭此工具", false, null );
                        event.consume ( );
                    } );
                    pathButton.setDisable ( true );
                    numberTextField.setEditable ( false );
                    beginButton.setDisable ( true );
                    exitButton.setDisable ( false );
                    Show.info ( "分割出的文件将存放至原文件所在的文件夹（此过程不会改动原文件；如遇窗口卡死，请耐心等待）",
                                false, null );
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
                                Platform.runLater ( ( ) -> statusLabel.setText ( "分割已停止" ) );
                            }
                            else {
                                Platform.runLater ( ( ) -> statusLabel.setText ( "分割已完成" ) );
                            }
                        }
                        catch ( Exception e ) {
                            runDoingLabel.cancel ( );
                            runDoingLabel.purge ( );
                            if ( runExitLabel != null ) {
                                runExitLabel.cancel ( );
                                runExitLabel.purge ( );
                            }
                            Platform.runLater ( ( ) -> statusLabel.setText ( "分割已停止" ) );
                        }
                        isExitLabel = true;
                        pathButton.setDisable ( false );
                        numberTextField.setEditable ( true );
                        beginButton.setDisable ( false );
                        exitButton.setDisable ( true );
                        stage.setOnCloseRequest ( event -> {
                        } );
                    } ).start ( );
                }
                else {
                    if ( ! isWrongSplitNumber ) {
                        Show.info ( "参数未填写完整", false, null );
                    }
                }
            }
        } );
        exitButton.setOnAction ( action -> {
            if ( operate.isDoing ( ) ) {
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
            String result = "正在分割文件" + " .".repeat ( Math.max ( 0, step ) );
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
            String result = "正在停止分割" + " .".repeat ( Math.max ( 0, step ) );
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
