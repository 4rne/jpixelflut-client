import java.io.PrintWriter;
import java.util.SplittableRandom;

public class ImageRunnable implements Runnable
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

	public ImageRunnable(int thread_number, int threads, int[][] image, int imageScale, int xOffset, int yOffset,
			PrintWriter out)
	{
		this.image = image;
		this.threads = threads;
		this.thread_number = thread_number;
		this.imageScale = imageScale;
		this.imageOffsetX = xOffset;
		this.imageOffsetY = yOffset;
		ImageRunnable.out = out;
		randomInstance = new SplittableRandom();
	}

	@Override
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
					pixel(imageOffsetX + x * imageScale + i, imageOffsetY + y * imageScale + j,
							image[x][y] >> 16 & 0xFF, image[x][y] >> 8 & 0xFF, image[x][y] >> 0 & 0xFF);
				}
			}
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
