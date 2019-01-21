import javax.swing.JButton;

public class elevatorButton extends JButton{
	protected int floorIndex;
	protected int elevatorNum;
	public elevatorButton() {
		floorIndex = -1;
		elevatorNum = -1;
	}
	public elevatorButton(String s){
		floorIndex = -1;
		elevatorNum = -1;
		setText(s);
	}
	public void setElBtn(int elevatorNum){
		setText(Integer.toString(floorIndex));
		this.elevatorNum = elevatorNum;
	}
	public void setElNum(int elevatorNum){
		this.elevatorNum = elevatorNum;	
	}
}
