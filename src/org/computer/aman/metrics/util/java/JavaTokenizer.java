package org.computer.aman.metrics.util.java;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JavaTokenizer
{
    public JavaTokenizer(final String aCodeFragment)
    {
        codeFragment = aCodeFragment;
        if ( codeFragment == null || codeFragment.equals("") ){
            tokenList = new LinkedList<String>();
        }
    }
        
    public List<String> getTokenList()
    {
        if ( tokenList == null ){
            tokenList = makeTokenList(codeFragment);
        }        
        return tokenList;
    }
        
    private LinkedList<String> makeTokenList(final String aCodeFragment)
    {
        // まず，コード断片から文字列リテラル，文字リテラルを見つけ出し，
        // 文字列（文字）リテラルとそれ以外というカテゴリで分割する
        LinkedList<String> tmpList = splitLiterals(aCodeFragment);
        
        // 文字列（文字）リテラルはそのまま tokenList へ追加し，
        // それ以外はさらにトークン分けしてから tokenList へ追加する
        tokenList = new LinkedList<String>();
        for (Iterator<String> iterator = tmpList.iterator(); iterator.hasNext();) {
            String element = iterator.next();
            if ( element.startsWith("\"") || element.startsWith("'") ){
                tokenList.add(element);
            }
            else{
                tokenList.addAll(makeSubTokenList(element));
            }
        }
        
        return tokenList;
    }
    
    /**
     * 文字リテラル／文字列リテラル以外の文字列に対してトークン分けを行い，
     * 結果を LinkedList として返す．
     * 
     * @param anElement トークン分けの対象（文字リテラル／文字列リテラル以外の文字列）
     * @return トークン分けの結果として得られるトークンリスト
     */
    private LinkedList<String> makeSubTokenList(final String anElement)
    {
        // まず，スペースで分断して仮のトークンリストを作り，
        // それぞれの仮トークンの中で演算子が登場している場合は，さらにその仮トークンを分断する．
        // そのような分断をすべてに対して施す．
        // 一つの仮トークンの中に複数の演算子が登場することもあるので，繰り返し施す必要がある．
        String[] tmpTokenArray = anElement.split("\\s");
        LinkedList<String> tmpTokenList = new LinkedList<String>();
        for (int i = 0; i < tmpTokenArray.length; i++) {
            if ( tmpTokenArray[i].length() == 0 ){
                continue;
            }
            tmpTokenList.add(tmpTokenArray[i]);
        }
        int index = 0;
        while ( index < tmpTokenList.size() ){
            // 演算子（またはセパレータ）で分断する（splitWithSymbol）
            //  --> 結果は，{ 演算子より前，演算子，演算子より後 } という長さ 3 の配列
            //  なお，演算子が見つからない場合は後ろの 2 要素が null となる
            String[] subTokens = splitWithSymbol(tmpTokenList.get(index));
            if ( subTokens[1] == null ){
                index++;
            }
            else{
                tmpTokenList.remove(index);
                if ( subTokens[0].length() > 0 ){
                    tmpTokenList.add(index, subTokens[0]);
                    index++;
                }
                tmpTokenList.add(index, subTokens[1]);
                index++;
                if ( subTokens[2].length() > 0 ){
                    tmpTokenList.add(index, subTokens[2]);
                }
            }
        }
        
        // 浮動小数点数リテラルが複数のトークンへ分断されている可能性もあるため
        // リストを順番にチェックし，必要ならば融合させる
        // ただし，厳密な構文チェックでは無く，ピリオドが登場した場合に，
        // その前のトークンが数字で始まっていたらピリオドとくっつける．
        // 同様にピリオドの後ろが数字で始まっていたらそちらともくっつける．
        // 
        // さらに特殊なパターンとして 1.2e-3f といった指数表記もある．
        // この場合，上の処理が終わった後でも，まだ - で区切られている状態なので
        // 符号(+-)の前のトークンが「数字またはピリオドで始まっていて，かつ e で終わる」であり，
        // 後ろのトークンが「数字で始まる」ならば三者をくっつける
        index = 0;
        while ( index < tmpTokenList.size() ){
            boolean isReplaced = false;
            if ( tmpTokenList.get(index).equals(".") ){
                String replace = ".";
                boolean removeLeft = false;
                boolean removeRight = false;
                if ( index > 0 && Character.isDigit(tmpTokenList.get(index-1).charAt(0)) ){
                    replace = tmpTokenList.get(index-1) + ".";
                    removeLeft = true;
                }
                if ( index+1 < tmpTokenList.size() && Character.isDigit(tmpTokenList.get(index+1).charAt(0)) ){
                    replace += tmpTokenList.get(index+1);
                    removeRight = true;
                }
                
                if ( removeLeft && removeRight ){
                    tmpTokenList.remove(index+1);
                    tmpTokenList.remove(index);
                    tmpTokenList.remove(index-1);
                    tmpTokenList.add(index-1, replace);
                    isReplaced = true;
                }
                else if ( removeLeft ){
                    tmpTokenList.remove(index);
                    tmpTokenList.remove(index-1);
                    tmpTokenList.add(index-1, replace);                            
                    isReplaced = true;
                }
                else if ( removeRight ){
                    tmpTokenList.remove(index+1);
                    tmpTokenList.remove(index);
                    tmpTokenList.add(index, replace);                            
                    index++;
                    isReplaced = true;
                }
            }
            if ( !isReplaced ){
                index++;
            }
        }
        
        index = 0;
        while ( index < tmpTokenList.size() ){
            boolean isReplaced = false;
            if ( tmpTokenList.get(index).equals("+") || tmpTokenList.get(index).equals("-") ){
                if ( index > 0 && index+1 < tmpTokenList.size() ){
                    char headLeft = tmpTokenList.get(index-1).charAt(0);
                    char tailLeft = tmpTokenList.get(index-1).charAt(tmpTokenList.get(index-1).length()-1);
                    char headRight = tmpTokenList.get(index+1).charAt(0);
                    if ( (Character.isDigit(headLeft) || headRight == '.') 
                            && (tailLeft == 'e' || tailLeft == 'E')
                            && Character.isDigit(headRight) ){
                        String replace = tmpTokenList.get(index-1) + tmpTokenList.get(index) + tmpTokenList.get(index+1);
                        tmpTokenList.remove(index+1);
                        tmpTokenList.remove(index);
                        tmpTokenList.remove(index-1);
                        tmpTokenList.add(index-1, replace);
                        isReplaced = true;
                    }
                }
            }
            if ( !isReplaced ){
                index++;
            }
        }
        
        return tmpTokenList;
    }
    
    
    /**
     * コード断片を演算子（または区切り文字）で分断する
     * 
     * @param aCodeFragment 分断対象のコード断片
     * @return String 配列 { 演算子より左，演算子，演算子より右 }
     */
    private String[] splitWithSymbol(final String aCodeFragment)
    {
        final String[] SYMBOLS
            = { ">>>=", 
                ">>>", ">>=", "<<=",
                "==", "+=", "<=", "-=", ">=", "*=", "!=", "/=", "&&", "&=", "||", "|=", "++", "^=", "--", "%=", "<<", ">>",
                "=", ">", "<", "!", "~", "?", ":", "+", "-", "*", "/", "&", "|", "^", "%",
                "(", ")", "{", "}", "[", "]", ";", ",", "." };
        
        // 最も左に登場する演算子・区切り文字で分断する
        int cutPoint = aCodeFragment.length();
        int symbol = -1;
        for ( int i = 0; i < SYMBOLS.length; i++ ){
            int index = aCodeFragment.indexOf(SYMBOLS[i]);
            if ( index != -1 ){
                if ( index < cutPoint ){
                    cutPoint = index;
                    symbol = i;
                }
            }
        }
        
        if ( symbol != -1 ){
            return new String[]{ aCodeFragment.substring(0, cutPoint), SYMBOLS[symbol], aCodeFragment.substring(cutPoint+SYMBOLS[symbol].length()) };
        }
        
        return new String[]{ aCodeFragment, null, null };
    }
    
    /**
     * コード断片から文字列リテラル，文字リテラルを見つけ出し，
     * 文字列（文字）リテラルとそれ以外というカテゴリで分割する
     * 
     * @return 分断後のコード断片を格納したリスト
     */
    private LinkedList<String> splitLiterals(final String aCodeFragment)
    {
        // codeFragment の先頭から一文字ずつチェックして，状態遷移を確認しながら
        // コード断片を分断していく（分断後のコード断片を tmpList に入れていく）
        // [状態] status
        // 0 : 初期状態
        // 1 : 文字列リテラルの中
        // 11 : 文字列リテラル中で \ によるエスケープ文字を読込んだ直後の状態
        // 2 : 文字リテラルの中
        // 21 : 文字リテラル中で \ によるエスケープ文字を読込んだ直後の状態
        // 3 : 文字列リテラルでも文字リテラルでもない状態
        // 31 : \ によるエスケープ文字を読込んだ直後の状態
        LinkedList<String> tmpList = new LinkedList<String>();
        int status = 0;
        StringBuilder tmpString = new StringBuilder();
        for ( int i = 0; i < aCodeFragment.length(); i++ ){
            char ch = aCodeFragment.charAt(i);
            switch ( status ){
            case 0 : // 0 : 初期状態
                tmpString.append(ch);
                if ( ch == '"' ){
                    status = 1;
                }
                else if ( ch == '\''){
                    status = 2;
                }
                else{
                    status = 3;
                }
                break;
            case 1 : // 1 : 文字列リテラルの中
                tmpString.append(ch);
                if ( ch == '\\' ){
                    status = 11;
                }
                else if ( ch == '"' ){
                    status = 0;
                    tmpList.add(new String(tmpString));
                    tmpString.delete(0, tmpString.length());
                }
                break;
            case 11 : // 11 : 文字列リテラル中で \ によるエスケープ文字を読込んだ直後の状態
                tmpString.append(ch);
                status = 1;
                break;
            case 2 : // 2 : 文字リテラルの中
                tmpString.append(ch);
                if ( ch == '\\' ){
                    status = 21;
                }
                else if ( ch == '\'' ){
                    status = 0;
                    tmpList.add(new String(tmpString));
                    tmpString.delete(0, tmpString.length());
                }
                break;
            case 21 : // 21 : 文字リテラル中で \ によるエスケープ文字を読込んだ直後の状態
                tmpString.append(ch);
                status = 2;
                break;
            case 3 : // 3 : 文字列リテラルでも文字リテラルでもない状態
                if ( ch == '"' ){
                    status = 1;
                    tmpList.add(new String(tmpString));
                    tmpString.delete(0, tmpString.length());
                    tmpString.append(ch);
                }
                else if ( ch == '\'' ){
                    status = 2;
                    tmpList.add(new String(tmpString));
                    tmpString.delete(0, tmpString.length());
                    tmpString.append(ch);
                }
                else if ( ch == '\\' ){
                    status = 31;
                    tmpString.append(ch);
                }
                else{
                    tmpString.append(ch);
                }
                break;
            case 31 : // 31 : \ によるエスケープ文字を読込んだ直後の状態
                tmpString.append(ch);
                status = 3;
                break;                
            }
        }
        if ( tmpString.length() > 0 ){
            if ( status == 1 || status == 2 ){
                // 文字リテラルまたは文字列リテラルの状態のまま
                // コード断片の終端に到達している場合，不完全なリテラルという
                // ことになるため，先頭の一文字だけをトークンとして，
                // 二文字目以降はこのメソッドを再帰的に利用する
                tmpList.add(tmpString.substring(0,1));
                tmpString.delete(0, 1);
                tmpList.addAll(splitLiterals(new String(tmpString)));
            }
            else{                
                tmpList.add(new String(tmpString));
            }
        }
        
        return tmpList;
    }
    
    /** トークン分け対象の文字列 */
    private String codeFragment;
    
    /** トークン分けの結果 */
    private LinkedList<String> tokenList;
}
