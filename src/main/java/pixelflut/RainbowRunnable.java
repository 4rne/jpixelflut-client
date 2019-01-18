package pixelflut;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.SplittableRandom;

public class RainbowRunnable extends PixelRunnable implements Runnable
{
	int xPos;
	int yPos;
	int xMax;
	int yMax;
	int threadNumber;
	int threads;
	static PrintWriter out;

	public RainbowRunnable(int threadNumber, int threads, int xPos, int yPos,
			int xMax, int yMax, String ip, int port) throws UnknownHostException, IOException
	{
		this.threads = threads;
		this.threadNumber = threadNumber;
		this.xPos = xPos;
		this.yPos = yPos;
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
			int p = randomInt(xMax * yMax / threads) * threads + threadNumber;
			x = p % xMax;
			y = p / xMax;

			double clock = x / (float) xMax * (Math.PI * 2);
			int r = (int) ((Math.sin(clock) + 1) * 127);
			int g = (int) ((Math.sin(clock + Math.PI * 0.666) + 1) * 127);
			int b = (int) ((Math.sin(clock + Math.PI * 1.333) + 1) * 127);
			String pixel = pixelToString(x + xPos, y + yPos, r, g, b);
			out.write(pixel);
		}
	}
}
