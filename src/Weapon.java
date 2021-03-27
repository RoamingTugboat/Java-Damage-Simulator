/**
 * A Container for the various attributes of a weapon in Counter-Strike:Global Offensive, e.g. damage,
 * range fall-off, fire rate etc.
 * @author void
 *
 */
public class Weapon {

	//please make sure all fields have the same names as the attribute entries in weapon_<weaponname>.txt, else WeaponLibrary.getWeaponScriptFileAsMap() won't work 
	private String weaponName;

	private int MaxPlayerSpeed;
	private String WeaponType;

	private int WeaponPrice;
	private double WeaponArmorRatio;
	private String Team;
	
	private int Penetration;
	private int Damage;
	private int Range;
	private double RangeModifier;
	private int Bullets;
	private double CycleTime;

	private double RecoveryTimeCrouch;
	private double RecoveryTimeStand;
	
	private int clip_size;
		
	
	public Weapon() {
		
	}


	public String getWeaponName() {
		return weaponName;
	}


	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}


	public int getMaxPlayerSpeed() {
		return MaxPlayerSpeed;
	}


	public void setMaxPlayerSpeed(int maxPlayerSpeed) {
		MaxPlayerSpeed = maxPlayerSpeed;
	}


	public String getWeaponType() {
		return WeaponType;
	}


	public void setWeaponType(String weaponType) {
		WeaponType = weaponType;
	}


	public int getWeaponPrice() {
		return WeaponPrice;
	}


	public void setWeaponPrice(int weaponPrice) {
		WeaponPrice = weaponPrice;
	}


	public double getWeaponArmorRatio() {
		return WeaponArmorRatio;
	}


	public void setWeaponArmorRatio(double weaponArmorRatio) {
		WeaponArmorRatio = weaponArmorRatio;
	}


	public String getTeam() {
		return Team;
	}


	public void setTeam(String team) {
		Team = team;
	}


	public int getPenetration() {
		return Penetration;
	}


	public void setPenetration(int penetration) {
		Penetration = penetration;
	}


	public int getDamage() {
		return Damage;
	}


	public void setDamage(int damage) {
		Damage = damage;
	}


	public int getRange() {
		return Range;
	}


	public void setRange(int range) {
		Range = range;
	}


	public double getRangeModifier() {
		return RangeModifier;
	}


	public void setRangeModifier(double rangeModifier) {
		RangeModifier = rangeModifier;
	}


	public int getBullets() {
		return Bullets;
	}


	public void setBullets(int bullets) {
		Bullets = bullets;
	}


	public double getCycleTime() {
		return CycleTime;
	}


	public void setCycleTime(double cycleTime) {
		CycleTime = cycleTime;
	}


	public double getRecoveryTimeCrouch() {
		return RecoveryTimeCrouch;
	}


	public void setRecoveryTimeCrouch(double recoveryTimeCrouch) {
		RecoveryTimeCrouch = recoveryTimeCrouch;
	}


	public double getRecoveryTimeStand() {
		return RecoveryTimeStand;
	}


	public void setRecoveryTimeStand(double recoveryTimeStand) {
		RecoveryTimeStand = recoveryTimeStand;
	}


	public int getClip_size() {
		return clip_size;
	}


	public void setClip_size(int clip_size) {
		this.clip_size = clip_size;
	}
	
	

	

	
	

}
