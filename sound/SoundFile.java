package sound;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 12, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class SoundFile
{
	private AudioClip	ac;
	/**
	 * loads a soundfile specified by the filename
	 * 
	 * @author capn_midnight
	 * @param filename
	 */
	public SoundFile(URL filename)
	{
		try
		{
			ac = Applet.newAudioClip(filename);
		}
		catch (Exception e)
		{
			//do nothing
		}
	}
	/**
	 * plays the sound through to completion
	 *  
	 */
	public void play()
	{
		ac.stop();
		ac.play();
	}
	/**
	 * stops a sound midstream
	 *  
	 */
	public void stop()
	{
		ac.stop();
	}
	/**
	 * loops the sound file, replaying it once it has finished
	 */
	public void loop()
	{
		ac.loop();
	}
}