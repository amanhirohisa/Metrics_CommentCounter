package org.computer.aman.metrics.comment;

import java.io.IOException;
import java.util.Iterator;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.c.CodeLineMapForC;

/**
 * C ソースファイル中のメソッドについてコメント文の測定とそれに関連する機能を提供
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CommentCounterForC 
extends CommentCounter
{

    /**
     * 指定されたソースファイルを測定対象とした CommentCounterForC オブジェクトを生成する
     * 
     * @param aSourceFile 測定対象のソースファイル
     */
    public CommentCounterForC(final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        super(aSourceFile);
    }
    
    /**
     * コメント文の測定を実行し，結果を CommentCountResultSet インスタンスとして返す．
     * 
     * @param aBeginLineNumber 測定の開始行
     * @param anEndLineNumber 測定の終了行
     * @return コメント文測定の結果
     */
    public CountResult measure(final int aBeginLineNumber, final int anEndLineNumber) 
    throws IOException, NotSupportedSourceFileExeption 
    {
        CountResultForC results = new CountResultForC();

        // (1) 測定対象メソッドの直前に書かれたコメントを測定する
        // (1-1) 測定対象行よりも前に登場する実効コード行の中で一番後(predecessorLine)を見つける
        // (1-2) predecessorLine+1 から aBeginLineNumber-1 までの行に登場するコメントをカウントする        
        Iterator<CodeLineMap> itr = getCodeMap().iterator();
        
        int predecessorLine = -1;
        for ( int i = 1; i < aBeginLineNumber; i++ ){
            if ( itr.next().getCodeCount() > 0 ){
                predecessorLine = i;
            }
        }
        
        if ( predecessorLine != -1 ){
            itr = getCodeMap().iterator();
            for ( int i = 1; i < aBeginLineNumber; i++ ){
                CodeLineMapForC lineMap = (CodeLineMapForC)itr.next();
                if ( i <= predecessorLine || lineMap.getCommentCount() == 0 ){
                    continue;
                }
                else if ( lineMap.getEolCommentCount() > 0 ){
                    results.incrementEolCommentCountInHead();
                }
                else{
                    results.incrementTraditionalCommentCountInHead();
                }
            }
        }
        
        // (2) (1) の続きから一行ずつマップを見ていく
        for ( int i = aBeginLineNumber; i <= anEndLineNumber; i++ ){
            CodeLineMapForC lineMap = (CodeLineMapForC)itr.next();                        
            if ( lineMap.getCommentCount() == 0 ){
                continue;
            }
            results.incrementCommentCount();
            // コメントの内訳のカウント
            // 異なるタイプのコメント文が一行に混在する場合は EOLコメントアウト，Traditional コメントアウト，EOL, Traditional の優先順でカウントする 
            if ( lineMap.getTraditionalCommentOutCount() > 0 ){
                results.incrementTraditionalCommentOutCount();
            }
            else if ( lineMap.getEolCommentOutCount() > 0 ){
                results.incrementEolCommentOutCount();
            }
            else if ( lineMap.getEolCommentCount() > 0 ){
                results.incrementEolCommentCount();
            }
            else{
                results.incrementTraditionalCommentCount();
            }
        }
        
        return results;
    }
}
