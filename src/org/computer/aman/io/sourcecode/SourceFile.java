package org.computer.aman.io.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * ソースファイルのモデル
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class SourceFile 
extends File
{
    /**
     * 指定されたパスのソースファイルに対応するモデルを生成する
     * 
     * @param aPathName ソースファイルのパス名
     * @throws FileNotFoundException ファイルが存在しない場合
     * @throws SecurityException ファイルが読み出せない場合
     */
    public SourceFile(final String aPathName) 
    throws FileNotFoundException, SecurityException
    {
        super(aPathName);
        if ( !isFile() ){
            throw new FileNotFoundException("File not found: " + aPathName);
        }
        if ( !canRead() ){
            throw new SecurityException("can't open file: " + aPathName);
        }
    }

    /**
     * ソースファイルの拡張子を返す
     * 
     * @return ソースファイルの拡張子
     */
    public String getExtension()
    {
        String fileName = getName().toLowerCase();
        int index = fileName.lastIndexOf('.');

        return fileName.substring(index+1);
    }

    /**
     * ソースファイルが Java ソースファイルかどうかを判定する．
     * （拡張子が java かどうかで判断する）
     * 
     * @return Java ソースファイルならば true さもなくば false
     */
    public boolean isJavaFile()
    {
        return getExtension().equalsIgnoreCase("java");
    }
    
    /**
     * ソースファイルが C/C++ ソースファイルかどうかを判定する．
     * （拡張子が c, cpp, cc, cxx, h, hpp, hxx かどうかで判断する）
     * 
     * @return Java ソースファイルならば true さもなくば false
     */
    public boolean isCFile()
    {
        return getExtension().equalsIgnoreCase("c") ||
                getExtension().equalsIgnoreCase("cpp") ||
                getExtension().equalsIgnoreCase("cc") ||
                getExtension().equalsIgnoreCase("cxx") ||
                getExtension().equalsIgnoreCase("h") ||
                getExtension().equalsIgnoreCase("hpp") ||
                getExtension().equalsIgnoreCase("hxx");
    }
    
    private static final long serialVersionUID = 2008061301L;
}
