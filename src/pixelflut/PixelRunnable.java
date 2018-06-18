package pixelflut;

import java.util.SplittableRandom;

public class PixelRunnable
{
	static String lineEnding = "\n"; // or "\\n"
	static SplittableRandom randomInstance;

	public static String pixelToString(int x, int y, int r, int g, int b)
	{
		StringBuilder str = new StringBuilder();
		str.append("PX ");
		str.append(x);
		str.append(' ');
		str.append(y);
		str.append(' ');
		str.append(toHex(r));
		str.append(toHex(g));
		str.append(toHex(b));
		str.append(lineEnding);
		return str.toString();
	}

	public static String toHex(int n)
	{
		String value = Integer.toHexString(n);
		if (value.length() < 2)
		{
			value = "0" + value;
		}
		return value;
	}

	public static int randomInt(int to)
	{
		return randomInstance.nextInt(to);
	}

	public static int randomInt(int from, int to)
	{
		return randomInt(to - from) + from;
	}
}
