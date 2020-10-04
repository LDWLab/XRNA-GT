package ssview;

import java.awt.Graphics;
import java.awt.print.*;

public abstract class
ComplexPrintable
implements Printable
{

public int
print(Graphics g, PageFormat pf, int pageIndex)
{
	if (pageIndex != 0)
		return (NO_SUCH_PAGE);

	runPrintCmd(g);
	return (PAGE_EXISTS);
}

public void
runPrintCmd(Graphics g)
{
}


}
