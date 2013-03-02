package org.computer.aman.metrics.comment;

/**
 * コメント文の測定の結果集合（Java 用）
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CountResultForJava 
extends CountResult
{

    /**
     * // 形式のコメントの数を返す．
     *
     * @return // 形式のコメントの数
     */
    public int getEolCommentCount()
    {
        return eolCommentCount;
    }
    
    /**
     * // 形式のコメント（ファイルの先頭に記述）の数を返す．
     *
     * @return // 形式のコメント（ファイルの先頭に記述）の数
     */
    public int getEolCommentCountInHead()
    {
        return eolCommentCountInHead;
    }
    
    /**
     * // 形式のコメントアウトの数を返す．
     *
     * @return // 形式のコメントアウトの数
     */
    public int getEolCommentOutCount()
    {
        return eolCommentOutCount;
    }

    /**
     * Javadoc 形式のコメントの数を返す．
     *
     * @return Javadoc 形式のコメントの数
     */
    public int getJavadocCommentCount()
    {
        return javadocCommentCount;
    }

    /**
     * C と同じ形式のコメントの数を返す．
     *
     * @return C と同じ形式のコメントの数
     */
    public int getTraditionalCommentCount()
    {
        return traditionalCommentCount;
    }

    /**
     * C と同じ形式のコメント（ファイルの先頭に記述）の数を返す．
     *
     * @return C と同じ形式のコメント（ファイルの先頭に記述）の数
     */
    public int getTraditionalCommentCountInHead()
    {
        return traditionalCommentCountInHead;
    }

    /**
     * C と同じ形式のコメントアウトの数を返す．
     *
     * @return C と同じ形式のコメントアウトの数
     */
    public int getTraditionalCommentOutCount()
    {
        return traditionalCommentOutCount;
    }

    /**
     * // 形式のコメントの数を +1 する．
     */
    public void incrementEolCommentCount()
    {
        eolCommentCount++;
    }

    /**
     * // 形式のコメント（ファイルの先頭に記述）の数を +1 する．
     */
    public void incrementEolCommentCountInHead()
    {
        eolCommentCountInHead++;
    }

    /**
     * // 形式のコメントアウトの数を +1 する．
     */
    public void incrementEolCommentOutCount()
    {
        eolCommentOutCount++;
    }
    
    /**
     * Javadoc 形式のコメントの数を +1 する．
     */
    public void incrementJavadocCommentCount()
    {
        javadocCommentCount++;
    }

    /**
     * C と同じ形式のコメントの数を +1 する．
     */
    public void incrementTraditionalCommentCount()
    {
        traditionalCommentCount++;
    }
    
    /**
     * C と同じ形式のコメント（ファイルの先頭に記述）の数を +1 する．
     */
    public void incrementTraditionalCommentCountInHead()
    {
        traditionalCommentCountInHead++;
    }
    
    /**
     * C と同じ形式のコメントアウトの数を +1 する．
     */
    public void incrementTraditionalCommentOutCount()
    {
        traditionalCommentOutCount++;
    }

    /**
     * コメント文の測定結果を文字列のかたちで返す．
     * 内容は，
     *   EOL コメント，Traditional コメント，Javadoc コメント, （測定部分の）直前の EOL コメント，（測定部分の）直前の Traditional コメント，
     *   EOL 形式のコメントアウト，Traditional 形式のコメントアウト
     * をこの順番にタブ区切りで表したもの．
     * 
     * @return コメント文の測定結果
     */
    public String toString()
    {
        return eolCommentCount + "\t" + traditionalCommentCount + "\t" + javadocCommentCount + "\t" +
               eolCommentCountInHead + "\t" + traditionalCommentCountInHead + "\t" + 
               eolCommentOutCount + "\t" + traditionalCommentOutCount;
    }
    
    /** // 形式のコメントの数 */
    private int eolCommentCount;

    /** // 形式のコメント（ファイルの先頭に記述）の数 */
    private int eolCommentCountInHead;

    /** // 形式のコメントアウトの数 */
    private int eolCommentOutCount;

    /** Javadoc 形式のコメントの数 */
    private int javadocCommentCount;

    /** C と同じ形式のコメントの数 */
    private int traditionalCommentCount;

    /** C と同じ形式のコメント（ファイルの先頭に記述）の数 */
    private int traditionalCommentCountInHead;

    /** C と同じ形式のコメントアウトの数 */
    private int traditionalCommentOutCount;
}
