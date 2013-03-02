package org.computer.aman.metrics.util.java;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.CodeMap;

/**
 * Java ソースコードの内容を表したコードマップ
 * <p>
 * コードマップは，ソースコードの内容を文字単位で表現したマップである．<br>
 * ここでは，
 * <ul>
 *  <li> 空白類: 0
 *  <li> 何らかのコード: 1
 *  <li> // コメント: 2
 *  <li> /* コメント: 3
 *  <li> Javadoc: 4
 * </ul>
 * が使われる．
 * <p>
 * 本クラスのインスタンスでは，
 * 一行ごとのコードマップオブジェクト（CodeLineMapForJava インスタンス）をリストとして保持する．
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CodeMapForJava 
extends CodeMap
{
    /**
     * Java ソースファイルに対応した CodeMap オブジェクトを生成する
     * 
     * @param aSourceFile ソースファイル
     * @throws NotSupportedSourceFileExeption ソースファイルが Java ソースでない場合
     * @throws IOException 入出力で何らかの例外が発生した場合
     */
    public CodeMapForJava(final SourceFile aSourceFile) 
    throws IOException
    {
        // 1 行ずつ読み込み，その内容について調べながら CodeLineMap を CodeMap へ追加していく
        //
        // status - コード解析の状態
        //   1 : 何らかのコード（非コメント，非文字（列）リテラル）を読み出し中
        //   12: Traditional コメント（/* ... ）
        //   13: Javadoc (/** ... )
        //   100: 文字列リテラルの内部（"..."）
        //   200: 文字リテラルの内部（'.'）
        final int CODE = 1;
        final int TRADITIONAL = 12;
        final int JAVADOC = 13;
        final int STRING_LITERAL = 100;
        final int CHAR_LITERAL = 200;
        int status = CODE;

        LineNumberReader reader = new LineNumberReader(new FileReader(aSourceFile));
        LinkedList<String> mapList = new LinkedList<String>();
        StringBuilder map = new StringBuilder(); // CodeLineMap を作り上げるための作業用 StringBuilder
        StringBuilder contents = new StringBuilder(); // Traditional コメント形式によるコメントアウトを判定するため，コメント内容の一時保存に使う
        
        String line = null;
        while ( (line = reader.readLine()) != null ){
            // ソースコード行から 1 文字ずつ取り出して調べ，状態を遷移させていく
            int idx = 0;
            while ( idx < line.length() ){    
                char ch = line.charAt(idx);
                
                if ( Character.isWhitespace(ch) ){ 
                    // 空白類の場合，そのことをマップに記録して以降はスキップする
                    // ただし，コメントの内部ならばコメントの一部として，文字列リテラルならばコードの一部として処理する
                    // なお，Traditional コメントに関しては，コメントアウトの可能性もあるため，contents にもためていく
                    if ( status == TRADITIONAL ){ // Traditional コメントの場合 
                        map.append(CodeLineMapForJava.TRADITIONAL_COMMENT);
                        contents.append(ch);
                    }
                    else if ( status == STRING_LITERAL || status == CHAR_LITERAL ){ // 文字（列）リテラルの場合
                        map.append(CodeLineMap.CODE);
                    }
                    else if ( status == JAVADOC ){ // Javadoc の場合
                        map.append(CodeLineMapForJava.JAVADOC_COMMENT);
                    }
                    else{                            
                        map.append(CodeLineMap.BLANK);
                    }
                    idx++;
                    continue;
                }

                ///////////////////////////////////////////////////////////////
                // 以下，取り出した文字 ch に応じて状態 status を変化させていく
                // また，必要に応じて文字位置 idx も更新していく
                
                // 通常のコード状態(CODE) の場合:
                if ( status == CODE ){ 
                    if ( ch == '/' ){ // コメント開始の可能性: 先読みして決める
                        // 次に続く文字が '/' なら EOL コメントに確定,
                        // '**' なら Javadoc, '*' なら Traditional コメント,
                        // いずれでもないならば通常のコードの一部
                        String remainedPart = line.substring(idx);
                        if ( remainedPart.startsWith("//") ){ // EOL コメントとして行末までを処理
                            // ただし，コードのコメントアウトの場合はそちらへ切り替える
                            idx += remainedPart.length();
                            appendCodeToMap(map, isCommentOut(trimComment(remainedPart)) ? CodeLineMapForJava.EOL_COMMENT_OUT : CodeLineMapForJava.EOL_COMMENT, remainedPart.length());
                        }
                        else if ( remainedPart.startsWith("/**") ){ // Javadoc として状態遷移
                            status = JAVADOC;
                            idx += "/**".length();
                            appendCodeToMap(map, CodeLineMapForJava.JAVADOC_COMMENT, "/**".length());
                        }
                        else if ( remainedPart.startsWith("/*") ){ // Traditional コメントとして状態遷移
                            status = TRADITIONAL;
                            idx += "/*".length();
                            appendCodeToMap(map, CodeLineMapForJava.TRADITIONAL_COMMENT, "/*".length());
                            contents.delete(0, contents.length());
                            contents.append("/*");
                        }
                        else{
                            idx++;
                            appendCodeToMap(map, CodeLineMapForJava.CODE, 1);
                        }
                    }
                    else{
                        // 文字（列）リテラルの場合のみ状態遷移を起こす
                        if ( ch == '\''){ // 文字リテラルの開始
                            status = CHAR_LITERAL;
                        }
                        else if ( ch == '"' ){ // 文字列リテラルの開始
                            status = STRING_LITERAL;
                        }                        
                        idx++;
                        appendCodeToMap(map, CodeLineMapForJava.CODE, 1);          
                    }
                    continue;
                }
                
                // Traditional コメント状態(TRADITIONAL) の場合:
                if ( status == TRADITIONAL ){
                    if ( ch == '*' && idx+1 < line.length() && line.charAt(idx+1) == '/' ){
                        idx += "*/".length();
                        appendCodeToMap(map, CodeLineMapForJava.TRADITIONAL_COMMENT, "*/".length());
                        contents.append("*/");
                        status = CODE;
                        if ( isCommentOut(trimComment(new String(contents))) ){
                            // マップを遡り，最後の連続した TRADITIONAL_COMMENT の列を 
                            // TRADITIONAL_COMMENT_OUT の列に塗り替る
                            int length = contents.length();
                            for ( int i = map.length()-1; i >= 0; i-- ){
                                map.replace(i, i+1, CodeLineMapForJava.TRADITIONAL_COMMENT_OUT);
                                length--;
                                if ( length == 0 ){
                                    break;
                                }
                            }
                            LinkedList<String> replacedMapList = new LinkedList<String>();
                            while ( length > 0 ){
                                StringBuilder buf = new StringBuilder(mapList.removeLast());
                                for ( int i = buf.length()-1; i >= 0; i-- ){
                                    buf.replace(i, i+1, CodeLineMapForJava.TRADITIONAL_COMMENT_OUT);
                                    length--;
                                    if ( length == 0 ){
                                        break;
                                    }
                                }
                                replacedMapList.addLast(new String(buf));
                            }
                            if ( replacedMapList.size() > 0 ){
                                mapList.addAll(replacedMapList);
                            }
                        }
                    }
                    else{
                        idx++;
                        appendCodeToMap(map, CodeLineMapForJava.TRADITIONAL_COMMENT, 1);
                        contents.append(ch);
                    }
                    continue;
                }
                
                // Javadoc 状態(JAVADOC) の場合:
                if ( status == JAVADOC ){
                    if ( ch == '*' && idx+1 < line.length() && line.charAt(idx+1) == '/' ){
                        idx += "*/".length();
                        appendCodeToMap(map, CodeLineMapForJava.JAVADOC_COMMENT, "*/".length());
                        status = CODE;
                    }
                    else{
                        idx++;
                        appendCodeToMap(map, CodeLineMapForJava.JAVADOC_COMMENT, 1);
                    }
                    continue;
                }
                
                // 文字リテラル状態(CHAR_LITERAL) の場合:
                if ( status == CHAR_LITERAL ){
                    idx++;
                    appendCodeToMap(map, CodeLineMapForJava.CODE, 1);
                    if ( ch == '\\' ){ // エスケープ文字の場合は無条件に次の文字をリテラルとする
                        idx++;
                        appendCodeToMap(map, CodeLineMapForJava.CODE, 1);
                    }
                    else if ( ch == '\'' ){
                        status = CODE;
                    }
                    continue;
                }
                
                // 文字列リテラル状態(STRING_LITERAL) の場合:
                if ( status == STRING_LITERAL ){
                    idx++;
                    appendCodeToMap(map, CodeLineMapForJava.CODE, 1);
                    if ( ch == '\\' ){ // エスケープ文字の場合は無条件に次の文字をリテラルとする
                        idx++;
                        appendCodeToMap(map, CodeLineMapForJava.CODE, 1);
                    }
                    else if ( ch == '"' ){
                        status = CODE;
                    }
                    continue;
                }
            }

            // 行の解析が終わったら，その結果を CodeLineMapForJava オブジェクトとして構築し，リストへ追加していく
            mapList.add(new String(map));
            map.delete(0, map.length());
        }

        for (Iterator<String> iterator = mapList.iterator(); iterator.hasNext();) {
            add(new CodeLineMapForJava(iterator.next()));            
        }

        reader.close();
    }    

    /**
     * 指定されたマップ（StringBuilder）の末尾を aContent で
     * aCount 文字分だけ塗る
     * 
     * @param aMap 対象マップ
     * @param aContent 塗る内容
     * @param aCount 塗る回数（文字数）
     */
    private void appendCodeToMap(StringBuilder aMap, final String aContent, final int aCount)
    {
        for ( int j = 0; j < aCount; j++ ){
            aMap.append(aContent);
        }
    }
    
    /**
     * 指定されたコメント文のコメント開始文字列と終了文字列を取り除いた文字列を返す
     * 
     * @param aComment コメント文
     * @return コメント開始文字列と終了文字列を取り除いた文字列
     */
    private String trimComment(final String aComment)
    {
        if ( aComment.startsWith("//") ){
            return aComment.substring(2);
        }
        if ( aComment.startsWith("/*") ){
            return aComment.substring(2, aComment.length()-2);
        }
        return null;
    }
    
    /**
     * 指定された文字列が実行コードの一部であるかどうかを判定
     * 
     * @param aFragment 判定対象文字列
     * @return コードの一部ならば true さもなくば false
     */
    public boolean isCommentOut(final String aFragment)
    {
        // 文字列をトークン分けし，その末尾トークンが {, } またはセミコロン
        // ならばコードの一部と見なす
        List<String> list = (new JavaTokenizer(aFragment)).getTokenList();
        if ( list.size() == 0 ){
            return false;
        }
        String tail = list.get(list.size()-1);
        if ( tail.equals("{") || tail.equals("}") || tail.equals(";") ){
            return true;
        }

        return false;
    }
}
