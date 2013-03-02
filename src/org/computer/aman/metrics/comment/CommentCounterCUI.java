package org.computer.aman.metrics.comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;

public class CommentCounterCUI
{
    public static void main(String[] args) 
    throws SecurityException, NotSupportedSourceFileExeption, IOException
    {
        CommentCounterCUI ui = new CommentCounterCUI();

        ui.printCopyright();
        if ( args.length > 0 ){
            ui.printUsage();
            return;
        }
        printSeparator();
        
        // 標準入力から，「ファイルパス，開始行，終了行」の三つ組み（ただし，タブ区切り）を繰り返し読み出す
        // そして，当該ファイルに対応した CommentCounter オブジェクトを用意する．
        // ただし，通常は同じファイルパスがいくつか連続するので，ファイルパスが前の行と異なった時にのみ
        // オブジェクトを生成する．
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        CommentCounter counter = null;
        while ( (line = reader.readLine()) != null ){
            Scanner scanner = new Scanner(line).useDelimiter("\t");
            String path = scanner.next();
            int begin = scanner.nextInt();
            int end = scanner.nextInt();

            if ( counter == null || !path.equals(counter.getFilePath()) ){
                counter = CommentCounterFactory.create(path);
            }

            System.out.println(path + "\t" + begin + "\t" + end + "\t" + counter.measure(begin, end));
        }
        printSeparator();
    }
    
    /**
     * 区切り線を標準エラー出力へ表示する．
     */
    private static void printSeparator()
    {
        final int LENGTH = 64;
        for ( int i = 0; i < LENGTH; i++ ){
            System.err.print("-");
        }
        System.err.println();
    }
    
    /**
     * バージョン情報と Copyright を標準エラー出力へ表示する．
     */
    private void printCopyright()
    {
        try {
            printResource(COPYRIGHT);
        }
        catch (IOException e) {
            System.err.println("バージョン情報ファイルの読み出しでエラーが起こりました（無視して実行します）");
        }
    }
    
    /**
     * 指定されたリソース（テキストファイル）の内容を標準エラー出力へ表示する．
     * 
     * @param aFileName 対象となるテキストファイル
     * @throws IOException リソースの読み出しに失敗した場合
     */
    private void printResource(final String aFileName) 
    throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(aFileName)));
        String line = null;
        while ( (line = reader.readLine()) != null){
            System.err.println(line);
        }
        reader.close();
    }
    
    /**
     * 使い方を標準エラー出力へ出力する．
     */
    private void printUsage()
    {
        try {
            printResource(USAGE);
        }
        catch (IOException e) {
            System.err.println("使用方法の説明ファイルの読み出しでエラーが起こりました");
        }    
    }
    
    private final String COPYRIGHT = "copyright.txt";
    private final String USAGE = "usage.txt";
}
