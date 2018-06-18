package pixelflut;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.SplittableRandom;

public class ImageRunnable extends PixelRunnable implements Runnable
{
	static String lineEnding = "\n"; // or "\\n"
	static SplittableRandom randomInstance;
	int thread_number;
	int threads;
	int[][] image;
	int imageScale;
	int imageOffsetX;
	int imageOffsetY;
	static PrintWriter out;

	public ImageRunnable(int thread_number, int threads, int[][] image, int imageScale, int xOffset, int yOffset, String ip, int port) throws UnknownHostException, IOException
	{
		this.image = image;
		this.threads = threads;
		this.thread_number = thread_number;
		this.imageScale = imageScale;
		this.imageOffsetX = xOffset;
		this.imageOffsetY = yOffset;
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
			// for (x = 0; x < image.length; x++)
			// {
			// for (y = 0; y < image[0].length; y++)
			// {
			x = randomInt(image.length);
			y = randomInt(image[0].length);
			int p = randomInt(image.length * image[0].length / threads) * threads + thread_number;
			x = p % image[0].length;
			y = p / image[0].length;
			for (int i = 1; i <= imageScale; i++)
			{
				for (int j = 1; j <= imageScale; j++)
				{
					String pixel = pixelToString(imageOffsetX + x * imageScale + i, imageOffsetY + y * imageScale + j,
							image[x][y] >> 16 & 0xFF, image[x][y] >> 8 & 0xFF, image[x][y] >> 0 & 0xFF);
					out.write(pixel);
				}
			}
		}
	}
}
