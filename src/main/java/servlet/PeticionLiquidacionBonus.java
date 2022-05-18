package servlet;

public class PeticionLiquidacionBonus {
	private UserBean userbean;
	private float bonus;
	private String sala;
	
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	public UserBean getUserbean() {
		return userbean;
	}
	public void setUserbean(UserBean userbean) {
		this.userbean = userbean;
	}
	public float getBonus() {
		return bonus;
	}
	public void setBonus(float cantidad) {

		this.bonus = cantidad;
	}
}
