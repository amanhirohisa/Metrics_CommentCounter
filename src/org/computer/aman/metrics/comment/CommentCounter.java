package org.computer.aman.metrics.comment;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeMap;
import org.computer.aman.metrics.util.CodeMapFactory;

/**
 * メソッド単位でのコメント計上を行う測定器
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public abstract class CommentCounter
{
    /**
     * 指定されたソースファイルを測定対象とした MethodCommentCounter オブジェクトを生成する
     * （このクラスは抽象クラスであるため，このコンストラクタはサブクラスのコンストラクタからのみ呼び出される）
     * 
     * @param aSourceFile 測定対象のソースファイル
     * @throws IOException 
     * @throws NotSupportedSourceFileExeption 
     */
    public CommentCounter (final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        sourceFile = aSourceFile;
        codeMap = CodeMapFactory.create(aSourceFile);
    }
    
    /**
     * 測定対象ソースファイルのパスを返す
     * 
     * @return 測定対象ソースファイルのパス
     */
    public String getFilePath()
    {
        return sourceFile.getPath();
    }
    
    /**
     * コメント文の測定を実行し，結果を CommentCountResultSet インスタンスとして返す．
     * 
     * @param aBeginLineNumber 測定の開始行
     * @param anEndLineNumber 測定の終了行
     * @return コメント文測定の結果
     */
    public abstract CountResult measure(final int aBeginLineNumber, final int anEndLineNumber)
    throws IOException, NotSupportedSourceFileExeption ;
    
    
    /**
     * 対象としているソースコードのコードマップを返す（サブクラス向け）
     * 
     * @return 対象としているソースコードのコードマップ
     */
    protected CodeMap getCodeMap()
    {
        return codeMap;
    }
    
    /** 測定対象ソースファイルのコードマップ */
    private CodeMap codeMap;
    
    /** 測定対象のソースファイル */
    private SourceFile sourceFile;

}
