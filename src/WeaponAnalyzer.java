/**
 * Provides various methods to compile statistics of Weapons and returning them as Strings.
 */
public class WeaponAnalyzer {
	
	
	/**
	 * Returns how much damage this weapon would do under specific circumstances.
	 * Reminder: damage = baseDmg * rangeModifier^(distance/500f) * hitboxModifier * weaponArmorRatio/2f
	 * @param hitboxModifier
	 * @param distance
	 * @param hasArmor
	 * @return
	 */
	public int getShotDamage(Weapon w, float hitboxModifier, int distance, boolean hasArmor) {
		double shotDamage = w.getDamage() * Math.pow(w.getRangeModifier(), (distance/500f)) * hitboxModifier; //DON'T FORGET: DIVIDE BY FLOAT IF YOU DON'T WANT AN INT AS RESULT
		if(hasArmor)
			shotDamage *= w.getWeaponArmorRatio()/2f;
		
		if(shotDamage>100)
			return 100;
		else
			return (int)shotDamage;
	}
	
	public String getPrice(Weapon w) {
		return "$"+w.getWeaponPrice();
	}
	
	public String getRunSpeed(Weapon w) {
		return String.format("%.0f", ((float)w.getMaxPlayerSpeed()))+" inch/s";
	}
	
	public String getArmorPenetration(Weapon w) {
		return String.format("%.0f", (w.getWeaponArmorRatio()/2f)*100)+"%";
	}
	
	public String getPlayerPenetration(Weapon w) {
		return w.getPenetration()+"";
	}
	
	public String getShotInterval(Weapon w) {
		return String.format("%.0f", 1000*w.getCycleTime())+" ms";
	}
	
	public String getFireRate(Weapon w) {
		return String.format("%.1f", 1f/w.getCycleTime())+" shots/s";
	}
	
	public String getCapacity(Weapon w) {
		return w.getClip_size()+" rounds";
	}
	
	public String getEmptyAfter(Weapon w) {
		return String.format("%.1f", (w.getClip_size()*w.getCycleTime()))+" s";
	}
	
	public String getFalloff(Weapon w) {
		return String.format("%.0f", 100*(1-w.getRangeModifier()))+"%";
	}
	
	public String getKillRange(Weapon w, boolean targetHasArmor, int shotAmount, float hitboxModifier) {
		int killRange = getKillRangeAsInt(w,targetHasArmor,shotAmount,hitboxModifier);
		if(killRange < 32)
			return "N/A";
		else if(killRange > 4096)
			//return "\u221E"; //infinity symbol
			return "All Ranges";
		else
			return killRange+"\'";
	}
	
	public int getKillRangeAsInt(Weapon w, boolean targetHasArmor, int shotAmount, float hitboxModifier) {
		double weaponArmorRatio;
		//If armor is to be factored in, set armorratio to the weapon's value
		if(targetHasArmor && (hitboxModifier > 0.75f))
			weaponArmorRatio = w.getWeaponArmorRatio();
		else //if not, negate armor by setting armorratio to 2
			weaponArmorRatio = 2; 
		int killRange = (int) ((500*Math.log((float)(200f/shotAmount)/(w.getDamage() * weaponArmorRatio * hitboxModifier))) / Math.log(w.getRangeModifier()));	
		return killRange;
	}
	
	public String getRecoilResetStand(Weapon w) {
		//if lower than cycle time, return cycle time instead
		if(w.getCycleTime() > w.getRecoveryTimeStand())
			return String.format("%.0f",w.getCycleTime()*1000)+" ms";
		return String.format("%.0f",w.getRecoveryTimeStand()*1000)+" ms";
	}
	
	public String getRecoilResetCrouch(Weapon w) {
		//if lower than cycle time, return cycle time instead
		if(w.getCycleTime() > w.getRecoveryTimeCrouch())
			return String.format("%.0f",w.getCycleTime()*1000)+" ms";
		return String.format("%.0f",w.getRecoveryTimeCrouch()*1000)+" ms";
	}
	

	
	
}
