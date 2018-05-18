import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.SplittableRandom;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

class Main

{
	static int xMax = 1024;
	static int yMax = 575;
	static String lineEnding = "\n"; // or "\\n"
	static PrintWriter out;
	static SplittableRandom randomInstance;

	public static void main(String[] args) throws IOException

	{
		@SuppressWarnings("resource")
		Socket socket = new Socket("192.168.0.20", 1337);
		out = new PrintWriter(socket.getOutputStream(), true);
		randomInstance = new SplittableRandom();
		int x;
		int y;

		int[][] image = readImage("/home/arne/workspace/pixelflut/kliemann.jpg");
		int imageScale = 1;
		int imageOffsetX = 0;
		int imageOffsetY = 0;

		while (true) {
//			x = randomInt(xMax);
//			y = randomInt(yMax);
//			double clock = x / (float) xMax * (Math.PI * 2);
//			int r = (int) ((Math.sin(clock)                   + 1) * 127);
//			int g = (int) ((Math.sin(clock + Math.PI * 0.666) + 1) * 127);
//			int b = (int) ((Math.sin(clock + Math.PI * 1.333) + 1) * 127);
//			pixel(x, y, r, g, b);
			
			for (x = 0; x < image.length; x++)
			{
				for (y = 0; y < image[0].length; y++)
				{
//			x = randomInt(image.length);
//			y = randomInt(image[0].length);
					for (int i = 1; i <= imageScale; i++) {
						for (int j = 1; j<= imageScale; j++) {
							pixel(imageOffsetX + x * imageScale + i, imageOffsetY + y * imageScale + j, image[x][y] >> 16 & 0xFF, image[x][y] >> 8 & 0xFF, image[x][y] >> 0 & 0xFF);
						}
					}
				}
			}
//			for (x = 0; x < xMax; x++) {
//				for (y = 0; y < yMax; y++) {
//					//pixel(randomInt(xMax), randomInt(yMax), 246, 188, 180);
//					//out.println ("PX " + (int) (Math.random() * xMax) + " " + (int)
//					//(Math.random() * yMax) + " 00ff00\\n");
//					pixel(x, y, 0, 0, 0);
//				}
//			}

		}

		// socket.close ();

	}

	public static void pixel(int x, int y, int r, int g, int b)
	{
		StringBuilder str = new StringBuilder();
		str.append("PX");
		str.append(' ');
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
	
	public static int[][] readImage(String path)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(path));
			int width;
			int height;
			boolean hasAlphaChannel;
			int pixelLength;
			byte[] pixels;
			
			pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
	        width = img.getWidth();
	        height = img.getHeight();
	        hasAlphaChannel = img.getAlphaRaster() != null;
	        pixelLength = 3;
	        if (hasAlphaChannel)
	        {
	            pixelLength = 4;
	        }
	        
	        int imgData[][] = new int[width][height];
	        for (int x = 0; x < width; x++)
	        {
				for (int y = 0; y < height; y++)
				{
			        int pos = (y * pixelLength * width) + (x * pixelLength);
		
			        int argb = -16777216; // 255 alpha
			        if (hasAlphaChannel)
			        {
			            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
			        }
		
			        argb += ((int) pixels[pos++] & 0xff); // blue
			        argb += (((int) pixels[pos++] & 0xff) << 8); // green
			        argb += (((int) pixels[pos++] & 0xff) << 16); // red
			        imgData[x][y] = argb;
				}
	        }
	        return imgData;
		}
		catch (IOException e)
		{
			System.err.println("Image could not be loaded");
			e.printStackTrace();
			System.exit(42);
		}
		return null;
	}

}
