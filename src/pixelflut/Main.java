package pixelflut;

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
			// System.err.println(
			// "Need to specify at least -ip and -port. -mode [random, linear] -dim x,y -img src -scale [0..n] -threads [1..n]");

			System.out
					.println("usage: java -jar pixelfluter.jar [IP] [PORT] [X] [Y] [IMG] [THREADS]");
			System.out
					.println("\t-ip IP\t\tconnect to the Pixelflut-Server at IP");
			System.out
					.println("\t-port PORT\tuse the PORT to connect to the Pixelflut-Server");
			System.out
					.println("\t-img SRC\tload an image from the location SRC, if no image is specified it uses the rainbow mode");
			System.out.println("\t-threads N\trun the program with N threads");
			System.out
					.println("\t-scale N\tonly useful with -img, scales the image n times. Only integers possible");
			System.out.println("\t-x N\tOffset on the x-axis, in pixels");
			System.out.println("\t-y N\tOffset on the y-axis, in pixels");
			System.out.println("\t-x-max N\timage width for rainbow");
			System.out.println("\t-y-max N\timage heigth for rainbow");
			System.exit(1);
		}
		if (!arguments.containsKey("port") || !arguments.containsKey("ip"))
		{
			System.err.println("IP and/or port broken.");
			System.exit(1);
		}
		@SuppressWarnings("resource")
		Socket socket = new Socket(arguments.get("ip"),
				Integer.parseInt(arguments.get("port")));
		out = new PrintWriter(socket.getOutputStream(), true);
		randomInstance = new SplittableRandom();

		int[][] image = ImageLoader.readImage(arguments.get("img"));
		int imageScale = 1;
		if (arguments.containsKey("scale"))
		{
			imageScale = Integer.parseInt(arguments.get("scale"));
		}

		int imageOffsetX = 0;
		int imageOffsetY = 0;
		if (arguments.containsKey("X"))
		{
			imageOffsetX = Integer.parseInt(arguments.get("X"));
		}
		if (arguments.containsKey("Y"))
		{
			imageOffsetY = Integer.parseInt(arguments.get("Y"));
		}

		int threads = Integer.parseInt(arguments.get("threads"));

		if (image == null)
		{
			for (int i = 0; i < threads; i++)
			{
				Runnable r = new RainbowRunnable(i, threads,
						Integer.parseInt(arguments.get("x-max")),
						Integer.parseInt(arguments.get("y-max")),
						arguments.get("ip"), Integer.parseInt(arguments
								.get("port")));
				Thread t = new Thread(r);
				t.start();
			}
		}
		else
		{
			for (int i = 0; i < threads; i++)
			{
				Runnable r = new ImageRunnable(i, threads, image, imageScale,
						imageOffsetX, imageOffsetY, arguments.get("ip"),
						Integer.parseInt(arguments.get("port")));
				Thread t = new Thread(r);
				t.start();
			}
		}
	}
}
