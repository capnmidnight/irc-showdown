/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 3, 2004
 */
package graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class ImageLoader
{
    public static Image getImage (String filename)
    {
        Image temp = Toolkit.getDefaultToolkit ().getImage (
                new ImageLoader ().getClass ().getResource (filename));
        new ImageIcon (temp); // forces the image to load into memory
        return temp;
    }
}