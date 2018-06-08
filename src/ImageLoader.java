import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader
{
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
		} catch (IOException e)
		{
			System.err.println("Image could not be loaded. Using rainbow mode instead");
		}
		return null;
	}
}
