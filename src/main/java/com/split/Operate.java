package com.split;

import com.tools.Check;
import com.tools.Show;
import javafx.application.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;

public class Operate implements Runnable {
    private String splitPath;
    private long splitNumber;
    private boolean isDoing = false;
    private boolean exit;
    private boolean hasError;
    
    void operatePath ( String operatePath ) {
        this.splitPath = operatePath;
    }
    
    void splitNumber ( long splitNumber ) {
        this.splitNumber = splitNumber;
    }
    
    boolean isDoing ( ) {
        return isDoing;
    }
    
    void exit ( ) {
        exit = true;
    }
    
    boolean hasError ( ) {
        return hasError;
    }
    
    @Override
    public void run ( ) {
        isDoing = true;
        exit = false;
        hasError = false;
        File file = new File ( this.splitPath );
        if ( ! file.exists ( ) ) {
            Platform.runLater ( ( ) -> Show.error ( "找不到待分割文件，请检查待分割文件是否存在", false, null ) );
            isDoing = false;
            hasError = true;
            return;
        }
        if ( ! file.canRead ( ) ) {
            Platform.runLater ( ( ) -> Show.error ( "待分割文件无法读取", false, null ) );
            isDoing = false;
            hasError = true;
            return;
        }
        try ( RandomAccessFile read = new RandomAccessFile ( this.splitPath, "r" ) ) {
            long length = read.length ( );
            BigInteger SingleLength;
            try {
                SingleLength = BigInteger.valueOf ( length / splitNumber + 1 );
            }
            catch ( Exception | Error e ) {
                read.close ( );
                Platform.runLater ( ( ) -> Show.error ( "分割出来的单个文件大小过大", false, null ) );
                isDoing = false;
                hasError = true;
                return;
            }
            int singleLength;
            if ( Check.isInteger ( SingleLength ) ) {
                singleLength = ( int ) ( length / splitNumber + 1 );
                if ( singleLength < 1_048_576 ) {
                    read.close ( );
                    Platform.runLater ( ( ) -> Show.error ( "分割出来的单个文件大小过小", false, null ) );
                    isDoing = false;
                    hasError = true;
                    return;
                }
            }
            else {
                read.close ( );
                Platform.runLater ( ( ) -> Show.error ( "再玩就玩坏了", false, null ) );
                isDoing = false;
                hasError = true;
                return;
            }
            for ( int i = 0; i < splitNumber; ++ i ) {
                if ( exit ) {
                    read.close ( );
                    Platform.runLater (
                            ( ) -> Show.info ( "已停止，您现在可删除未分割完成的文件（如*.1）", false, null ) );
                    isDoing = false;
                    hasError = true;
                    return;
                }
                read.seek ( i * splitNumber );
                String splitFile = String.format ( "%s.%d", this.splitPath, i + 1 );
                try ( FileOutputStream write = new FileOutputStream ( splitFile ) ) {
                    if ( i == splitNumber - 1 ) {
                        singleLength = ( int ) ( length - singleLength * ( splitNumber - 1 ) );
                    }
                    byte[] bytes = new byte[ singleLength ];
                    read.read ( bytes );
                    write.write ( bytes );
                }
                catch ( Exception e ) {
                    Platform.runLater ( ( ) -> Show.error ( "分割时出错，请仔细检查路径等是否正确", false, null ) );
                    isDoing = false;
                    hasError = true;
                    return;
                }
            }
            Platform.runLater (
                    ( ) -> Show.info ( "分割完成，分割出的文件已存放至被分割文件所在的文件夹内", false, null ) );
            hasError = false;
            isDoing = false;
        }
        catch ( Exception e ) {
            Platform.runLater ( ( ) -> Show.error ( "发生错误，请仔细检查路径等是否正确", false, null ) );
            hasError = false;
            isDoing = false;
        }
    }
}
