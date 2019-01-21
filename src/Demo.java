
public class Demo {
	public static void main(String[] args){
		ElevatorUI test = new ElevatorUI();
		Thread th1 = new Thread(test);
		th1.start();	
	}
}
