/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 15, 2004
 */
package sound;

import java.util.HashMap;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class SoundManager
{
	private static HashMap<String, SoundFile>	playlist;
	private static boolean	initialized	= false;
	/**
	 * a singleton for the playlist
	 * 
	 * @author capn_midnight
	 *  
	 */
	public SoundManager()
	{
		if (!initialized)
		{
			playlist = new HashMap<String, SoundFile>();
			add("shot", "gunshot.wav");
			add("miss", "click.wav");
			add("song1", "song.mp3");
			initialized = true;
		}
	}
	public void add(String name, String filename)
	{
		playlist
				.put(name, new SoundFile(this.getClass().getResource(filename)));
	}
	public void play(String name)
	{
		playlist.get(name).play();
	}
	public void loop(String name)
	{
		playlist.get(name).loop();
	}
	public void stop(String name)
	{
		playlist.get(name).stop();
	}
}