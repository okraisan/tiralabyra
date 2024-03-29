package tiralabyra;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Represents a bitmap image of a labyrinth.
 */
public class Image {
  /**
   *  Color of the entry pixel in the image (AARRGGBB).
   */
  private final int entryPointColor  = 0xFFFF0000;

  /**
   * Color of the exit pixel in the image (AARRGGBB).
   */
  private final int exitPointColor   = 0xFF00FF00;

  /**
   * Color of walls, i.e. non-passable pixels (AARRGGBB).
   */
  private final int wallColor        = 0xFF000000;

  /**
   * Java internal representation of the image in memory.
   */
  private BufferedImage bufferedImage;

  /**
   * Read an image from a given file. This should be an RGB bitmap.
   * The entry should be colored with entryPointColor and the exit with
   * exitPointColor. Walls should be wallColor.
   * @param path Filesystem path to the input file.
   */
  public Image(String path) {
    try {
      bufferedImage = ImageIO.read(new File(path));
    } catch (IOException e) {
      System.out.println("Can't open input image");
      return;
    }
  }

  public int getWidth() {
    return bufferedImage.getWidth();
  }

  public int getHeight() {
    return bufferedImage.getHeight();
  }

  public int getNumberOfPixels() {
    return bufferedImage.getWidth() * bufferedImage.getHeight();
  }

  /**
   * Get a linear index for any pixel, starting from 0 at the top-left.
   * @param point XY position of the pixel on the image.
   * @return A linear index for the given pixel coordinates.
   */
  public int getIndexForPixel(Point point) {
    return point.getY() * bufferedImage.getWidth() + point.getX();
  }

  /**
   * Generate point objects for all pixels in this image.
   * @return A list of all possible pixels on the image.
   */
  public Point[] getPixelPositions() {
    Point[] result = new Point[getNumberOfPixels()];
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        result[y * getWidth() + x] = new Point(x, y);
      }
    }
    return result;
  }

  /**
   * Draw a filled square around the coordinates indicated by the given linear
   * pixel index. Wall pixels are left as is. Used to draw thicker lines
   * around a path for better clarity.
   * @param index Linear index pointing to the center pixel of the drawing
   *     action.
   * @param brushRadius Size of the drawing brush.
   * @param color Which color to use.
   */
  public void plotPathAroundIndex(int index, int brushRadius, int color) {
    Point centerPoint = new Point(index % bufferedImage.getWidth(),
                                  index / bufferedImage.getWidth());

    for (int deltaY = -brushRadius; deltaY <= brushRadius; deltaY++) {
      for (int deltaX = -brushRadius; deltaX <= brushRadius; deltaX++) {
        Point plotPoint = Point.add(centerPoint, new Point(deltaX, deltaY));
        if (containsPosition(plotPoint) && isTraversableAt(plotPoint)) {
          bufferedImage.setRGB(plotPoint.getX(), plotPoint.getY(), color);
        }
      }
    }
  }

  /**
   * This point is inside the image boundary.
   * @param point Coordinates of the requested pixel.
   * @return True if the given coordinates are inside the image boundary.
   */
  public boolean containsPosition(Point point) {
    return (point.getX() >= 0 && point.getY() >= 0
            && point.getX() < bufferedImage.getWidth()
            && point.getY() < bufferedImage.getHeight());
  }

  /**
   * There is a non-wall pixel at the given coordinates.
   * @param point Coordinates of the requested pixel.
   * @return True if there's a non-wall pixel at the given coordinates.
   */
  public boolean isTraversableAt(Point point) {
    return (containsPosition(point)
            && bufferedImage.getRGB(point.getX(), point.getY()) != wallColor);
  }

  /**
   * There is an entry pixel at the give coordinates.
   * @param point Coordinates of the requested pixel.
   * @return True if there's an entry point pixel at the given coordinates.
   */
  public boolean hasEntryPointAt(Point point) {
    return (containsPosition(point)
            && bufferedImage.getRGB(point.getX(), point.getY())
            == entryPointColor);
  }

  /**
   * There is an entry pixel at the give coordinates.
   * @param point Coordinates of the requested pixel.
   * @return True if there's an exit point pixel at the given coordinates.
   */
  public boolean hasExitPointAt(Point point) {
    return (containsPosition(point)
            && bufferedImage.getRGB(point.getX(), point.getY())
            == exitPointColor);
  }

  /**
   * Save this image as a file.
   * @param path Filesystem path of the image to be written.
   */
  public void save(String path) {
    try {
      ImageIO.write(bufferedImage, "png", new File(path));
    } catch (IOException e) {
      System.out.println("Can't open file for writing");
    }
  }

}
