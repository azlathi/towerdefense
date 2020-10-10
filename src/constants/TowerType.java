package constants;

public enum TowerType {

	//firingRange, firingRate, damage, cost, img, description
	MACHINEGUN(200,2,20,1000,Constants.MACHINEGUN,
			"($1000) Fires extremely fast, but has average damage and less than average range",4,4,0,0,75,75), // the fireingRate is faster when it is lower
	BASIC(250,10,50,500, Constants.BASIC,
			"($500) The averagest average tower. Fires average, has average damage, has " +
					"surprisingly good range",4,4,0,0,75,75), // basic tower, small but consistent damage
	ICE(100, 25, 10,750, Constants.ICE,
			"($750) Freezes enemies on hit. Above average damage, but slow firing rate and" +
					" extremely low range",4,4,0,0,75,75), // also freezes the enemy, low range
	SNIPER(400, 50, 750, 1500,Constants.SNIPER,
			"($1500) Insane range. Insane damage. Abysmal firing rate",4,4,0,0,75,75), // Long range, high damage, low fire rate
	PITFALL(30,1,Integer.MAX_VALUE, 1000, Constants.PITFALL,
			"($1000) One time use. Kills any enemy that touches it, no matter how tough, but gets removed" +
					" when triggered. Must be placed on the path",4,4,0,0,75,75), // 0 range and rate because must be on path, also gets removed on use
	TANK(150, 15, 500, 2500, Constants.TANK,
			"($2500) Insane damage. Good firing rate. Abysmal Range",4,4,0,0,75,75);

	private double firingRate;
	private double firingRange;
	private int damage;
	private int cost;
	private String image;
	private String descr;
	private int columns;
    private int count;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

	private TowerType(double firingRange, double firingRate, int damage, int cost, String img, String descr,int col,int count,int x, int y, int w, int h) {
		this.firingRange = firingRange;
		this.firingRate = firingRate;
		this.damage = damage;
		this.cost = cost;
		this.image = img;
		this.descr = descr;
		this.columns = col;
		this.count = count;
		this.offsetX = x;
		this.offsetY = y;
		this.width = w;
		this.height = h;
	}
	
	public int getColumns() {
		return this.columns;
	}
	public int getCount() {
		return this.count;
	}
	public int getOffsetX() {
		return this.offsetX;
	}
	public int getOffsetY() {
		return this.offsetY;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	public String getDescr() { return descr; }

	public int getDamage() {
		return damage;
	}

	public int getCost() {
		return cost;
	}

	public double getFiringRange() {
		return this.firingRange;
	}

	public double getFiringRate() {
		return this.firingRate;
	}

	public String getImage() {
		return this.image;
	}
	@Override
	public String toString() {
		return "Tower: " + this.name() + '\n' +
			   "Firing Range: " + this.firingRange + '\n' +
			   "Firing Rate: " + this.firingRate + '\n';
	}

}
