import javax.swing.JButton;

public class FloorLabel extends JButton{
	protected int elevatorNum;
	protected int floor;
	FloorLabel(int elevatorNum, int floor){
		this.elevatorNum = elevatorNum;
		this.floor = floor;
		setText(String.valueOf(floor));
	}
}
