import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

class Main
{
	static int xMax = 1024;
	static int yMax = 575;
	static String lineEnding = "\n"; // or "\\n"
	static PrintWriter out;
	static SplittableRandom randomInstance;
	private static Map<String, String> arguments = new HashMap<String, String>();

	public static void main(String[] args) throws IOException
	{
		if (args.length > 0 && args.length % 2 == 0)
		{
			for (int i = 0; i < args.length; i += 2)
			{
				arguments.put(args[i].substring(1), args[i + 1]);
			}
			System.out.println(arguments.toString());
		}
		if (arguments.isEmpty())
		{
			System.err.println(
					"Need to specify at least -ip and -port. -mode [random, linear] -dim x,y -img src -scale [0..n] -threads [1..n]");
			System.exit(1);
		}
		if (!arguments.containsKey("port") || !arguments.containsKey("ip"))
		{
			System.err.println("IP and/or port broken.");
			System.exit(1);
		}
		@SuppressWarnings("resource")
		Socket socket = new Socket(arguments.get("ip"), Integer.parseInt(arguments.get("port")));
		out = new PrintWriter(socket.getOutputStream(), true);
		randomInstance = new SplittableRandom();
		int x;
		int y;

		int[][] image = ImageLoader.readImage(arguments.get("img"));
		int imageScale = 1;
		int imageOffsetX = 0;
		int imageOffsetY = 0;
		int threads = Integer.parseInt(arguments.get("threads"));

		if (image == null)
		{
//			while (true)
//			{
//				x = randomInt(xMax);
//				y = randomInt(yMax);
//
//				double clock = x / (float) xMax * (Math.PI * 2);
//				int r = (int) ((Math.sin(clock) + 1) * 127);
//				int g = (int) ((Math.sin(clock + Math.PI * 0.666) + 1) * 127);
//				int b = (int) ((Math.sin(clock + Math.PI * 1.333) + 1) * 127);
//				pixel(x, y, r, g, b);
//			}
		}
		else
		{
			for (int i = 0; i < threads; i++)
			{
				Runnable r = new ImageRunnable(i, threads, image, imageScale, imageOffsetX, imageOffsetY, out);
				Thread t = new Thread(r);
				t.start();
			}
//			while (true)
//			{
//				x = randomInt(image.length);
//				y = randomInt(image[0].length);
//				int p = randomInt(image.length * image[0].length / 4) * 4;
//				x = p % image[0].length;
//				y = p / image[0].length;
//				for (int i = 1; i <= imageScale; i++)
//				{
//					for (int j = 1; j <= imageScale; j++)
//					{
//						pixel(imageOffsetX + x * imageScale + i, imageOffsetY + y * imageScale + j,
//								image[x][y] >> 16 & 0xFF, image[x][y] >> 8 & 0xFF, image[x][y] >> 0 & 0xFF);
//					}
//				}
//			}
		}
	}

	public static void pixel(int x, int y, int r, int g, int b)
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
		out.print(str);
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
