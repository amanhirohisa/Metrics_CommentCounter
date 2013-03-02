package org.computer.aman.metrics.util.java;

import org.computer.aman.metrics.util.CodeLineMap;

/**
 * Java ソースコード一行分のコードマップを管理するクラスである．
 * <p>
 * コードマップとは，ソースコードの内容である各文字を<br>
 * (1)コードの一部，(2)空白類，(3)コメント文の一部，<br>
 * のいずれかに分類し，あらかじめ定められた整数値でその配列を表したものである．
 * <p>
 * (1) は定数 CODE，(2) は定数 BLANK でそれぞれ表される．<br>
 * (3) については，// から行末までのコメントが定数 EOL_COMMENT で，
 * C と同じ形式のコメントが定数 TRADITIONAL_COMMENT で，
 * Javadoc 形式のコメントが定数 JAVADOC_COMMENTでそれぞれ表される．
 * また，実行コードをコメントアウトしたと思われる場合は，その形式にあわせて
 * EOL_COMMENT_OUT または TRADITIONAL_COMMENT_OUT で表される．
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeLineMapForJava 
extends CodeLineMap
{

    /** マップ上で，その文字が // 形式のコメントであることを表す値 */
    public static final String EOL_COMMENT = "2";
    
    /** マップ上で，その文字が // 形式のコメントであり，なおかつ，実行コードのコメントアウトであることを表す値 */
    public static final String EOL_COMMENT_OUT = "5";
    
    /** マップ上で，その文字が Javadoc コメントであることを表す値 */
    public static final String JAVADOC_COMMENT = "4";

    /** マップ上で，その文字が C と同じ形式のコメントであることを表す値 */
    public static final String TRADITIONAL_COMMENT = "3";
    
    /** マップ上で，その文字が C と同じ形式のコメントであり，なおかつ，実行コードのコメントアウトであることを表す値 */
    public static final String TRADITIONAL_COMMENT_OUT = "6";
    
    /**
     * 一行分のコードマップを設定する． 
     * @param aMap 一行分のコードマップ
     */
    public CodeLineMapForJava(final String aMap)
    {
        super(aMap);
        
        for (int i = 0; i < aMap.length(); i++ ){
            String ithChar = Character.toString(aMap.charAt(i));
            if ( EOL_COMMENT.equals(ithChar) ){
                eolCommentCount++;
            }
            else if ( TRADITIONAL_COMMENT.equals(ithChar) ){
                traditionalCommentCount++;
            }
            else if ( JAVADOC_COMMENT.equals(ithChar) ){
                javadocCommentCount++;
            }
            else if ( EOL_COMMENT_OUT.equals(ithChar) ){
                eolCommentOutCount++;
            }
            else if ( TRADITIONAL_COMMENT_OUT.equals(ithChar) ){
                traditionalCommentOutCount++;
            }
        }
    }

    /**
     * EOL 形式のコメントの文字数を返す．
     *
     * @return EOL 形式のコメントの文字数
     */
    public int getEolCommentCount()
    {
        return eolCommentCount;
    }
    
    /**
     * EOL 形式のコメントアウトの文字数を返す．
     *
     * @return EOL 形式のコメントアウトの文字数
     */
    public int getEolCommentOutCount()
    {
        return eolCommentOutCount;
    }

    /**
     * Javadoc 形式のコメントの文字数を返す．
     *
     * @return Javadoc 形式のコメントの文字数 
     */
    public int getJavadocCommentCount()
    {
        return javadocCommentCount;
    }

    /**
     * C 形式のコメントの文字数を返す．
     *
     * @return C 形式のコメントの文字数
     */
    public int getTraditionalCommentCount()
    {
        return traditionalCommentCount;
    }

    /**
     * C 形式のコメントアウトの文字数を返す．
     *
     * @return C 形式のコメントアウトの文字数
     */
    public int getTraditionalCommentOutCount()
    {
        return traditionalCommentOutCount;
    }

    /**
     * 一行分のコードマップの内容（内訳のカウント付き）を文字列にして返す．
     * <br>
     * 内容は (b?,c?,#?:t?,e?,j?,ec?,tc?) ....... 
     * という形式になっており，
     * それぞれ順番に空白類の文字数，コード文字数，コメント文字数，Traditional コメント文字数，EOL コメント文字数，Javadoc 文字数，
     * EOL コメントアウト文字数，Traditional コメントアウト文字数が ? に入り，その後にコードマップの内容が続く．
     * <br>
     * なお，コメント文字数は，すべてのタイプのコメントの総文字数である．
     * @return 一行分のコードマップの内容
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();        
        buf.append("(b" + getBlankCount() + ",c" + getCodeCount() + ",#" + getCommentCount() + ":");
        buf.append("t" + getTraditionalCommentCount() + ",");
        buf.append("e" + getEolCommentCount() + ",");
        buf.append("j" + getJavadocCommentCount() + ",");
        buf.append("ec" + getEolCommentOutCount() + ",");
        buf.append("tc" + getTraditionalCommentOutCount() + ")");
        buf.append(" ");
        buf.append(getMap());
        
        return new String(buf);
    }

    /** EOL 形式のコメントの文字数 */
    private int eolCommentCount;
    
    /** EOL 形式のコメントアウトの文字数 */
    private int eolCommentOutCount;

    /** Javadoc 形式のコメントの文字数 */
    private int javadocCommentCount;

    /** C 形式のコメントの文字数 */
    private int traditionalCommentCount;

    /** C 形式のコメントアウトの文字数 */
    private int traditionalCommentOutCount;
}
