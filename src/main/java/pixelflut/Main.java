package pixelflut;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

class Main
{
	static String lineEnding = "\n"; // or "\\n"
	static PrintWriter out;
	static SplittableRandom randomInstance;
	static int numThreads;
	private static Map<String, String> arguments = new HashMap<String, String>();
	static ArrayList<ImageRunnable> threads = new ArrayList<>();

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
		if (!arguments.containsKey("threads"))
		{
			System.out.println("No thread number given, using one thread.");
			numThreads = 1;
		}
		else
		{
			numThreads = Integer.parseInt(arguments.get("threads"));
		}
		@SuppressWarnings("resource")
		Socket socket = new Socket(arguments.get("ip"), Integer.parseInt(arguments.get("port")));
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
		if (arguments.containsKey("x"))
		{
			imageOffsetX = Integer.parseInt(arguments.get("x"));
		}
		if (arguments.containsKey("y"))
		{
			imageOffsetY = Integer.parseInt(arguments.get("y"));
		}

		if (image == null)
		{
			if (!arguments.containsKey("x-max") || !arguments.containsKey("y-max"))
			{
				System.err.println("No x-max or y-max specified. Exiting.");
				System.exit(1);
			}
			for (int i = 0; i < numThreads; i++)
			{
				Runnable r = new RainbowRunnable(i, numThreads, imageOffsetX, imageOffsetY,
						Integer.parseInt(arguments.get("x-max")), Integer.parseInt(arguments.get("y-max")),
						arguments.get("ip"), Integer.parseInt(arguments.get("port")));
				Thread t = new Thread(r);
				t.start();
				//threads.add(r);
			}
		}
		else
		{
			for (int i = 0; i < numThreads; i++)
			{
				Runnable r = new ImageRunnable(i, numThreads, image, imageScale, imageOffsetX, imageOffsetY,
						arguments.get("ip"), Integer.parseInt(arguments.get("port")));
				Thread t = new Thread(r);
				t.start();
				threads.add((ImageRunnable) r);
			}

			while(true)
			{
				try
				{
					Thread.sleep(15000);
				}
				catch (InterruptedException e)
				{
					
				}

				int x = PixelRunnable.randomInt(1024 - image.length);
				int y = PixelRunnable.randomInt(600 - image[0].length);
				for (ImageRunnable t : threads)
				{
					t.setPosition(x, y);
				}
			}
		}
	}
}
