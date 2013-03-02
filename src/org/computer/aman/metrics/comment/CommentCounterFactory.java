package org.computer.aman.metrics.comment;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;

/**
 * 指定されたソースファイルを測定対象とした CommentCounter オブジェクトを生成するファクトリ
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CommentCounterFactory
{
    /**
     * 指定されたソースファイルを測定対象とした CommentCounter オブジェクトを生成する
     * 
     * @param aFilePath 測定対象のソースファイルパス
     */
    public static CommentCounter create(final String aFilePath) 
    throws SecurityException, NotSupportedSourceFileExeption, IOException
    {
        SourceFile file = new SourceFile(aFilePath);
        
        if ( file.isJavaFile() ){
            return new CommentCounterForJava(file);
        }
        if ( file.isCFile() ){
            return new CommentCounterForC(file);
        }
        
        throw new NotSupportedSourceFileExeption("not supported file type: " + aFilePath);    
    }
    
}
