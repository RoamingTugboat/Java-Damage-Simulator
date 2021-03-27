
public class AttributeExplanations {

	public String labelStWeaponName() {
		return " ";
	}
	
	public String getPrice() {
		return "How much money the weapon costs.";
	}
	
	public String getRunspeed() {
		return "How fast a player can run while holding the selected weapon.\n\n" +
				"The fastest possible run speed is 250 inches/s and is only attainable while holding a knife.";
	}
	
	public String getRangeFalloff() {
		return "Damage penalty incurred by bullets fired from this weapon, per 500 inches.";
	}
	
	public String getArmorpen() {
		return "Percentage of damage left after hitting an armored body part.";
	}
	
	
	
	
	public String getFirerate() {
		return "Maximum amount of rounds fired per second.";
	}
	
	public String getShotinterval() {
		return "Mimimum time interval between two shots.";
	}
	
	public String getCapacity() {
		return "Round capacity of one magazine.";
	}
	
	public String getEmptyAfter() {
		return "The time it takes to empty a full magazine when shooting at maximum fire rate.";
	}
	
	

	public String getRecoilResetStand() {
		return "Unknown";
	}
	
	public String getRecoilResetCrouch() {
		return "Unknown";
	}

	public String getPlayerpen() {
		return "Amount of players the same shot may hit in a row.";
	}
	
	
	
	
	public String getOHKUnarm() {
		return "Range at which an unarmored target can be killed in one headshot.";
	}
	
	public String getOHKArm() {
		return "Range at which a helmeted target can be killed in one headshot.";
	}
	
	public String get3SKUnarm() {
		return "Range at which 3 hits to the stomach or chest hitboxes are guaranteed to kill an unarmored target. Useful when other team is on eco.";
	}
		
	public String get4SKArm() {
		return "Range at which 4 hits to the stomach or chest hitboxes are guaranteed to kill an armored target.";
	}
	
	public String get5SKArm() {
		return "Range at which 5 hits to the stomach or chest hitboxes are guaranteed to kill an armored target.";
	}

	
	
	
	public String getDisplayRangeHint() {
		return "All measurements are taken in inches.\n\n" +
				"With your knife out, you'll run 250 inches in one second - use that to measure distances ingame.\n\n" +
				"E.g.: 4s running = 1000\'";
	}
	
	public String getHitbox() {
		return "This is the Hitbox panel.\n" +
				"Check how much damage the selected weapon does against which hitbox.\n\n" +
				"Use the slider to modify the distance from the target. Ticking the box enables kevlar/helmet.";
	}
	

}
