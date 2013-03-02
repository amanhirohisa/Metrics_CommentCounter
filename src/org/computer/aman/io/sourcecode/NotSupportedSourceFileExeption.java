package org.computer.aman.io.sourcecode;

/**
 * 対象外のソースファイルであることを示す例外
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class NotSupportedSourceFileExeption extends Exception
{
    public NotSupportedSourceFileExeption()
    {
        super();
    }

    public NotSupportedSourceFileExeption(final String aMessage)
    {
        super(aMessage);
    }

    private static final long serialVersionUID = 2008061502L;
}
