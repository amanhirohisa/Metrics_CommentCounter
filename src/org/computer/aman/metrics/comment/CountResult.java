package org.computer.aman.metrics.comment;

/**
 * コメント文の測定の結果集合
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CountResult
{
    /**
     * コメント文の登場する行数を返す．
     *
     * @return コメント文の登場する行数
     */
    public int getCommentCount()
    {
        return commentCount;
    }
    
    /**
     * コメント文の登場する行数を +1 する．
     */
    public void incrementCommentCount()
    {
        commentCount++;
    }

    /** コメント文の登場する行数 */
    private int commentCount;
}
