package pixelflut;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.SplittableRandom;

public class RainbowRunnable extends PixelRunnable implements Runnable
{
	int xMax;
	int yMax;
	int thread_number;
	int threads;
	static PrintWriter out;

	public RainbowRunnable(int thread_number, int threads, int xMax, int yMax,
			String ip, int port) throws UnknownHostException, IOException
	{
		this.threads = threads;
		this.thread_number = thread_number;
		this.xMax = xMax;
		this.yMax = yMax;
		@SuppressWarnings("resource")
		Socket socket = new Socket(ip, port);
		out = new PrintWriter(socket.getOutputStream(), true);
		randomInstance = new SplittableRandom();
	}

	public void run()
	{
		int x;
		int y;
		while (true)
		{
			int p = randomInt(xMax * yMax / threads) * threads + thread_number;
			x = p % xMax;
			y = p / xMax;

			double clock = x / (float) xMax * (Math.PI * 2);
			int r = (int) ((Math.sin(clock) + 1) * 127);
			int g = (int) ((Math.sin(clock + Math.PI * 0.666) + 1) * 127);
			int b = (int) ((Math.sin(clock + Math.PI * 1.333) + 1) * 127);
			String pixel = pixelToString(x, y, r, g, b);
			out.write(pixel);
		}
	}
}
