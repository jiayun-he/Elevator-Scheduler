import java.awt.Color;

import javax.swing.JButton;

public class FloorButton extends JButton{
	protected int index;
	protected String direction;
	public FloorButton() {
		index = -1;
		direction = "none";
	}
	public FloorButton(int index){
		this.index = index;
		setBackground(Color.white);
	}
	public void setButton(){
		setText(Integer.toString(index));
	}
	public void setButton(String s){
		setText(s);
	}
}
