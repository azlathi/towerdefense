/**
 * @author: Arjun Lathi, Brandon Erickson, Jon Musser, Zhaoyi Yang
 * FILE: Constants.java
 * PURPOSE: Holds constants for the Screen dimensions and Strings for file paths
 */

package constants;

public class Constants {

	public static final int SCREEN_HEIGHT = 600;
	public static final int SCREEN_WIDTH = 900;

	//Enemies
	public static final String KNIGHT = "/image/KNIGHT.png";
	public static final String ASSASSIN = "/image/ASSASSIN.png";
	public static final String BRUTE = "/image/ORC.png";
	public static final String GIANT = "/image/Giant.png";
	public static final String CONQUISTADOR = "/image/Conquistador.png";
	public static final String TANKENEMY = "/image/TankEnemy.png";

	//Towers
	public static final String MACHINEGUN = "/image/machineguntower.png";
	public static final String BASIC = "/image/basictower.png";
	public static final String ICE = "/image/icetower.png";
	public static final String SNIPER = "/image/snipertower.png";
	public static final String PITFALL = "/image/pitfalltower.png";
	public static final String TANK = "/image/tanktower.png";

	public static final String BLOCKED = "/image/blocked.png";
	public static final String GROUND = "/image/grass.png";
	public static final String PATH = "/image/path.jpg";
	public static final String BASE = "/image/base.png";
	public static final String ENEMYBASE = "/image/enemybase.png";
	public static final String CANCEL = "/image/CANCEL.png";
	//Tower Names
	public static final String TOWER1 = "MACHINEGUN";
	public static final String TOWER2 = "BASIC";
	public static final String TOWER3 = "ICE";
	public static final String TOWER4 = "SNIPER";
	public static final String TOWER5 = "PITFALL";
	public static final String TOWER6 = "TANK";
	
	public static final String STAGE1 = "/image/Stage1.png";
	public static final String STAGE2 = "/image/Stage2.png";
	public static final String STAGE3 = "/image/Stage3.png";
	public static final String STAGE4 = "/image/Stage4.png";
	
	public static final String BACKGROUND = "/image/Background.jpg";
	public static final String TITLE = "/image/Title.png";
	public static final String ENEMYDIE = "/image/break01.png";
	public static final String COIN = "/image/cointower.png";
	public static final String HEART = "/image/heart.png";
	public static final String SLASH = "/image/damagingbase.png";
	public static final String GAMEMUSIC = "src/image/gamemusic.wav";
	public static final String MENUMUSIC = "src/image/menumusic.wav";
	//Bullets
	public static final String BASICBULLET = "/image/basicbullet.PNG";
	public static final String ICEBULLET = "/image/icebullet.PNG";
	public static final String SNIPERBULLET = "/image/sniperbullet.png";
	public static final String TANKBULLET = "/image/tankbullet.PNG";


	public static final String[] ENEMYWAVES =
			{"KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT",
			"ASSASSIN",
			"KNIGHT", "KNIGHT", "KNIGHT", "ASSASSIN", "ASSASSIN",
			"KNIGHT", "KNIGHT", "BRUTE", "ASSASSIN", "KNIGHT", "KNIGHT", "BRUTE", "BRUTE",
			"BRUTE", "BRUTE", "BRUTE", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "ASSASSIN", "KNIGHT", "ASSASSIN", "BRUTE",
			"GIANT", "GIANT", "ASSASSIN", "BRUTE", "KNIGHT", "ASSASSIN", "BRUTE", "ASSASSIN", "BRUTE", "GIANT",
			"GIANT","KNIGHT","GIANT", "GIANT","GIANT", "GIANT", "GIANT", "GIANT", "ASSASSIN", "ASSASSIN", "BRUTE", "KNIGHT",
			"CONQUISTADOR", "KNIGHT", "KNIGHT", "ASSASSIN", "GIANT", "GIANT", "CONQUISTADOR", "BRUTE", "BRUTE", "KNIGHT", "ASSASSIN", "GIANT", "CONQUISTADOR",
			"TANK",
			"KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "KNIGHT", "TANK", "TANK", "CONQUISTADOR", "GIANT", "BRUTE", "ASSASSIN", "KNIGHT"
			};

	public static final int[] SPAWNTIMES =
			{45, 60, 75, 110, 120,
			300,
			450, 475, 500, 540, 550,
			700, 710, 730, 735, 740, 750, 780, 790,
			1000, 1010, 1020, 1055, 1065, 1075, 1100, 1110, 1125, 1130,
			1300, 1320, 1320, 1330, 1330, 1330, 1350, 1360, 1375, 1380,
			1630, 1640, 1645, 1650, 1655, 1660, 1665, 1670, 1800, 1820, 1830, 1840,
			2100, 2150, 2175, 2195, 2200, 2220, 2225, 2230, 2230, 2235, 2245, 2245, 2255, 2270,
			2800,
			2950, 2950, 2950, 2950, 2950, 2950, 2950, 2950, 2950, 3000, 3030, 3045, 3060, 3075, 3090, 4005
			};
}
