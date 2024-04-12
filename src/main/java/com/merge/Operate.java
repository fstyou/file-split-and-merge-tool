package com.merge;

import com.tools.FileUtil;
import com.tools.Show;
import javafx.application.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Operate implements Runnable {
    private String path;
    private String paths;
    private boolean doing = false;
    private boolean exit;
    private boolean hasError;
    
    void path ( String path ) {
        this.path = path;
    }
    
    void paths ( String paths ) {
        this.paths = paths;
    }
    
    boolean doing ( ) {
        return doing;
    }
    
    void exit ( ) {
        exit = true;
    }
    
    boolean hasError ( ) {
        return hasError;
    }
    
    @Override
    public void run ( ) {
        doing = true;
        exit = false;
        hasError = false;
        String fileName = FileUtil.firstName ( path );
        File file1 = new File ( this.path );
        File files = new File ( this.paths );
        if ( ! file1.exists ( ) || ! files.exists ( ) ) {
            Platform.runLater ( ( ) -> Show.error ( "以下列出的内容中，有一个或几个不存在，请检查它们是否存在", true,
                                                    "分割出的第一个文件，该文件的上级目录，合并文件的保存目录" ) );
            doing = false;
            hasError = true;
            return;
            
        }
        try ( FileOutputStream outPut = new FileOutputStream ( paths + File.separator + fileName );
              FileChannel write = outPut.getChannel ( ) ) {
            String mergeFile;
            for ( int i = 0; ; ++ i ) {
                if ( exit ) {
                    outPut.close ( );
                    write.close ( );
                    Platform.runLater ( ( ) -> Show.info ( "已停止，您现在可将未完成合并的残缺文件删除", false, null ) );
                    exit = true;
                    doing = false;
                    hasError = true;
                    return;
                }
                mergeFile = String.format ( "%s.%d", FileUtil.firstPath ( path ) + fileName, i + 1 );
                File file = new File ( mergeFile );
                if ( ! file.exists ( ) ) {
                    Platform.runLater ( ( ) -> Show.info ( "文件合并完成（如满足以下要求，则文件处于可用状态）", true,
                                                           "待合并文件完整，未缺失" ) );
                    outPut.close ( );
                    write.close ( );
                    doing = false;
                    hasError = false;
                    return;
                }
                if ( ! file.canRead ( ) ) {
                    Platform.runLater ( ( ) -> Show.error ( file.getName ( ) + "无法读取", false, null ) );
                    doing = false;
                    hasError = true;
                    return;
                }
                try ( FileInputStream inPut = new FileInputStream ( mergeFile );
                      FileChannel read = inPut.getChannel ( ) ) {
                    read.transferTo ( 0, read.size ( ), write );
                }
                catch ( Exception e ) {
                    Platform.runLater ( ( ) -> Show.error ( "合并时出错，请仔细检查路径等是否正确", false, null ) );
                    doing = false;
                    hasError = true;
                    return;
                }
            }
        }
        catch ( Exception e ) {
            Platform.runLater ( ( ) -> Show.error ( "发生错误，请仔细检查路径等是否正确", false, null ) );
            doing = false;
            hasError = true;
        }
    }
}
