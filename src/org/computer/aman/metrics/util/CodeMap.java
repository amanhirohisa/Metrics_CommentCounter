package org.computer.aman.metrics.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ソースコードの内容を表したコードマップ
 * <p>
 * コードマップは，ソースコードの内容を文字単位で表現したマップである．<br>
 * 例えば，空白類を 0，何らかのコードを 1, コメントを 2 として，<br>
 * <pre>   public int x; // 座標</pre>
 * は
 * <pre>0001111110111011022022</pre>
 * と表現される．
 * <p>
 * 本クラスのインスタンスでは，
 * 一行ごとのコードマップオブジェクト（CodeLineMap インスタンス）をリストとして保持する．
 * <p>
 * 具体的にどういった値がマップに使われるのかは，CodeLineMap のサブクラス（言語ごとに特化されたクラスが作られる）に依存する．
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public abstract class CodeMap
{    
    /**
     * 指定された文字列が実行コードの一部であるかどうかを判定
     * 
     * @param aLine 判定対象文字列
     * @return コードの一部ならば true さもなくば false
     */
    public abstract boolean isCommentOut(final String aLine);
    
    /**
     * 行ごとのコードマップをイテレータとして返す
     * 
     * @return 行ごとのコードマップに対するイテレータ
     */
    public Iterator<CodeLineMap> iterator()
    {
        return lines.iterator();
    }
    
    /**
     * コードマップの内容を文字列のかたちで返す．
     * 
     * @return 文字列化されたコードマップ
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        int lineNumber = 1;
        for (Iterator<CodeLineMap> iterator = lines.iterator(); iterator.hasNext();) {
            CodeLineMap element = iterator.next();
            buf.append(lineNumber++);
            buf.append(element.toString());
            buf.append("\n");
        }
        
        return new String(buf);
    }
    /**
     * CodeLineMap インスタンスをコードマップの末尾に追加する
     * 
     * @param aLineMap コードマップの末尾に追加される CodeLineMap インスタンス
     */
    protected void add(final CodeLineMap aLineMap)
    {
        if ( lines == null ){
            lines = new ArrayList<CodeLineMap>();
        }
        lines.add(aLineMap);
    }
      
    /** 
     * コードマップの内容:
     * 行単位でのコードマップに相当する CodeLineMap オブジェクトを
     * 順序付きリストとして保持
     */
    private ArrayList<CodeLineMap> lines;
}
