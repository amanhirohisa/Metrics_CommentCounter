package org.computer.aman.metrics.util;

/**
 * ソースコード一行分のコードマップを管理するクラスである．
 * <p>
 * コードマップとは，ソースコードの内容である各文字を<br>
 * (1)コードの一部，(2)空白類，(3)コメント文の一部，<br>
 * のいずれかに分類し，あらかじめ定められた整数値でその配列を表したものである．
 * <p>
 * (1) は定数 CODE，(2) は定数 BLANK でそれぞれ表される．<br>
 * (3) については，言語によって複数種類のコメント文を許す場合もあるため，
 * 本クラスを特化した子クラスで定義される．
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeLineMap
{
    /** マップ上で，その文字が空白類であることを表す値 */
    public static final String BLANK = "0";

    /** マップ上で，その文字がコードの一部であることを表す値 */
    public static final String CODE = "1";
    
    /**
     * 一行分のコードマップを設定する． 
     * @param aMap 一行分のコードマップ
     */
    public CodeLineMap(final String aMap)
    {
        map = new String(aMap != null ? aMap : "");

        blankCount = 0;
        codeCount = 0;
        commentCount = 0;
        for (int i = 0; i < map.length(); i++ ){
            String ithChar = Character.toString(map.charAt(i));
            if ( BLANK.equals(ithChar) ){
                blankCount++;
            }
            else if ( CODE.equals(ithChar) ){
                codeCount++;
            }
            else{
                commentCount++;
            }
        }
    }
    
    /**
     * 空白類の数を返す
     * @return 空白類の数
     */
    public int getBlankCount()
    {
        return blankCount;
    }

    /**
     * コード文字の数を返す
     * @return コード文字の数
     */
    public int getCodeCount()
    {
        return codeCount;
    }

    /**
     * コメント文字の数を返す
     * @return コメント文字の数
     */
    public int getCommentCount()
    {
        return commentCount;
    }
    
    /**
     * コードマップ（一行分）の内容を返す
     * @return コードマップ（一行分）の内容
     */
    public String getMap()
    {
        return map;
    }
    
    /**
     * 空行（空白類のみの行，または何も書いていない行）ならば true，さもなくば false を返す
     * 
     * @return 空行かどうか
     */
    public boolean isBlankLine()
    {
        return (codeCount == 0) && (commentCount == 0);
    }
    
    /** 空白類の数 */
    private int blankCount;
    
    /** コード文字の数 */
    private int codeCount;
    
    /** コメント文字の数 */
    private int commentCount;
    
    /** マップ:行の内容に対して各文字の種類に対応した数字で表したもの */
    private String map;
}
