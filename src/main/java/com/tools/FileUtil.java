package com.tools;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.FileSystems;

public class FileUtil {
    public static FileChooser chooseFile ( ) {
        FileChooser choose = new FileChooser ( );
        choose.setTitle ( "选择文件" );
        choose.getExtensionFilters ( ).add ( new FileChooser.ExtensionFilter ( "所有文件", "*.*" ) );
        return choose;
    }
    
    public static FileChooser chooseOne ( ) {
        FileChooser choose1 = new FileChooser ( );
        choose1.setTitle ( "选择文件" );
        choose1.getExtensionFilters ( ).add ( new FileChooser.ExtensionFilter ( "1 文件", "*.1" ) );
        return choose1;
    }
    
    public static DirectoryChooser chooseDir ( ) {
        DirectoryChooser chooses = new DirectoryChooser ( );
        chooses.setTitle ( "选择文件夹" );
        return chooses;
    }
    
    public static String firstName ( String path ) {
        String fileName = path.substring ( path.lastIndexOf ( File.separator ) + 1 );
        fileName = fileName.substring ( 0, fileName.lastIndexOf ( "." ) );
        return fileName;
    }
    
    public static String firstPath ( String path ) {
        String fileName = path.substring ( path.lastIndexOf ( FileSystems.getDefault ( ).getSeparator ( ) ) + 1 );
        return path.substring ( 0, path.length ( ) - fileName.length ( ) );
    }
}
