package biscotte.kana;

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       NONE
 * @return      NONE
 * @exception   NONE
 * @see         NONE
 **/
public class Kana
{
    private String  srcLine;
    private String  cnvLine;

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       NONEaa
 * @return      NONE
 * @exception   NONE
 * @see         NONE
 **/
    public Kana() {
        srcLine = "";
    }

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       String x : 変換前のローマ字文字列
 * @return      NONE
 * @exception   NONE
 * @see         NONE
 **/
    public void setLine(String s) {
        srcLine = s; //s.toLowerCase();
    }

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       NONE
 * @return      String : 変換後のかな文字列
 * @exception   NONE
 * @see         NONE
 **/
    public String getLine() {
        return cnvLine;
    }

    private String[][] r2kTable = {
        {   "", "あ","い","う","え","お" },             // 0.
        {  "k", "か","き","く","け","こ" },             // 1.
        {  "s", "さ","し","す","せ","そ" },             // 2.
        {  "t", "た","ち","つ","て","と" },             // 3.
        {  "n", "な","に","ぬ","ね","の" },             // 4.
        {  "h", "は","ひ","ふ","へ","ほ" },             // 5.
        {  "m", "ま","み","む","め","も" },             // 6.
        {  "y", "や","い","ゆ","いぇ","よ" },           // 7.
        {  "r", "ら","り","る","れ","ろ" },             // 8.
        {  "w", "わ","うぃ","う","うぇ","を" },         // 9.

        {  "g", "が","ぎ","ぐ","げ","ご" },             //10.
        {  "z", "ざ","じ","ず","ぜ","ぞ" },             //11.
        {  "j", "じゃ","じ","じゅ","じぇ","じょ" },     //12.
        {  "d", "だ","ぢ","づ","で","ど" },             //13.
        {  "b", "ば","び","ぶ","べ","ぼ" },             //14.
        {  "p", "ぱ","ぴ","ぷ","ぺ","ぽ" },             //15.
        { "gy", "ぎゃ","ぎぃ","ぎゅ","ぎぇ","ぎょ" },   //16.
        { "zy", "じゃ","じぃ","じゅ","じぇ","じょ" },   //17.
        { "jy", "じゃ","じぃ","じゅ","じぇ","じょ" },   //18.
        { "dh", "でゃ","でぃ","でゅ","でぇ","でょ" },   //19.
        { "dy", "ぢゃ","ぢぃ","ぢゅ","ぢぇ","ぢょ" },   //20.
        { "by", "びゃ","びぃ","びゅ","びぇ","びょ" },   //21.
        { "py", "ぴゃ","ぴぃ","ぴゅ","ぴぇ","ぴょ" },   //22.

        {  "l", "ぁ","ぃ","ぅ","ぇ","ぉ" },             //23.
        {  "v", "ヴぁ","ヴぃ","ヴ","ヴぇ","ヴぉ" },     //24.
        { "sh", "しゃ","し","しゅ","しぇ","しょ" },     //25.
        { "sy", "しゃ","し","しゅ","しぇ","しょ" },     //26.
        { "ch", "ちゃ","ち","ちゅ","ちぇ","ちょ" },     //27.
        { "cy", "ちゃ","ち","ちゅ","ちぇ","ちょ" },     //28.

        {  "f", "ふぁ","ふぃ","ふ","ふぇ","ふぉ" },     //29.
        {  "q", "くぁ","くぃ","く","くぇ","くぉ" },     //30.
        { "ky", "きゃ","きぃ","きゅ","きぇ","きょ" },   //31.
        { "ts", "つぁ","つぃ","つ","つぇ","つぉ" },     //32.
        { "ty", "ちゃ","ちぃ","ちゅ","ちぇ","ちょ" },   //33.
        { "ny", "にゃ","にぃ","にゅ","にぇ","にょ" },   //34.
        { "hy", "ひゃ","ひぃ","ひゅ","ひぇ","ひょ" },   //35.
        { "my", "みゃ","みぃ","みゅ","みぇ","みょ" },   //36.
        { "ry", "りゃ","りぃ","りゅ","りぇ","りょ" },   //37.
        { "ly", "ゃ","ぃ","ゅ","ぇ","ょ" },             //38.
        { "lt", "た","ち","っ","て","と" },             //39.
        { "xy", "ゃ","ぃ","ゅ","ぇ","ょ" },             //40.
        { "xt", "た","ち","っ","て","と" },             //41.
        {  "x", "ぁ","ぃ","ぅ","ぇ","ぉ" },             //42.
    };

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       String s : 子音の種類
 * @param       int    n : 母音の種類
 * @return      String : 変換後のかな文字列(一音分)
 * @exception   NONE
 * @see         NONE
 **/
    private String R2K(String s, int n) {

        if ( n<5 ) {
            for ( int i=0; i<r2kTable.length; i++ ) {
                if ( s.equals(r2kTable[i][0]) ) {
                    return r2kTable[i][n+1];
                }
            }
            return s+r2kTable[0][n+1];
        } else if (n==5) {
            return "ん";
        } else if (n==6) {
            return "っ";
        } else {
            return "ー";
        }
    }

/**
 * @author      WATANABE Mamoru
 * @version     Version 0.0  19 Feb.2000
 * @param       NONE
 * @return      NONE
 * @exception   NONE
 * @see         NONE
 **/
    public void convert() {
        String   buf;
        String   tmp;

        cnvLine = "";
        buf     = "";
        for ( int i=0; i<srcLine.length(); i++ ) {
            tmp = srcLine.substring(i,i+1);

            if      ( tmp.equals("a") ) { cnvLine=cnvLine+R2K(buf,0); buf = ""; }
            else if ( tmp.equals("i") ) { cnvLine=cnvLine+R2K(buf,1); buf = ""; }
            else if ( tmp.equals("u") ) { cnvLine=cnvLine+R2K(buf,2); buf = ""; }
            else if ( tmp.equals("e") ) { cnvLine=cnvLine+R2K(buf,3); buf = ""; }
            else if ( tmp.equals("o") ) { cnvLine=cnvLine+R2K(buf,4); buf = ""; }
            else {
                if ( buf.equals("n") ) {
                    if ( !(tmp.equals("y")) ) {         /* "ny" */
                        cnvLine=cnvLine+R2K(buf,5);
                        buf="";
                        if ( tmp.equals("n") ) continue; /* "nn" */
                    }
                }

                if ( java.lang.Character.isLetter(tmp.charAt(0)) ) {
                    if ( buf.equals(tmp) ) {
                        cnvLine=cnvLine+R2K(buf,6);     /* "っ" */
                        buf=tmp;
                    } else {
                        buf=buf+tmp;
                    }
                } else {
                    if (tmp.equals("-")) {
                        tmp = R2K(buf, 7);
                    }
                    cnvLine=cnvLine+buf+tmp;
                    buf="";
                }
            }

        //  System.out.println("buf,tmp:"+buf+","+tmp+","+cnvLine);
        }
        cnvLine=cnvLine+buf;
    }
}
