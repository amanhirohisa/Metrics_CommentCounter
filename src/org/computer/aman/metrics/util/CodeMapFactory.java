package org.computer.aman.metrics.util;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.c.CodeMapForC;
import org.computer.aman.metrics.util.java.CodeMapForJava;

/**
 * ソースファイルの拡張子（プログラミング言語）に応じて適切な CodeMap オブジェクトを生成するファクトリ
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeMapFactory
{
    /**
     * ソースファイルの拡張子（プログラミング言語）に応じて適切な CodeMap オブジェクトを生成する
     * 
     * @param aSourceFile ソースファイル
     * @return 指定されたソースファイルの記述言語に対応した CodeMap オブジェクト
     * @throws NotSupportedSourceFileExeption 指定されたソースファイルが非対応の言語で書かれていた場合
     * @throws IOException 入出力で何らかの例外が発生した場合
     */
    public static CodeMap create(final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        if ( aSourceFile.isJavaFile() ){
            return new CodeMapForJava(aSourceFile);
        }
        if ( aSourceFile.isCFile() ){
            return new CodeMapForC(aSourceFile);
        }
        
        return null;
    }
}
