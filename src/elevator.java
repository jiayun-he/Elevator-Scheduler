import java.awt.Color;
import java.util.Stack;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class elevator implements Runnable{
	public final int numberOfFloor = 20;
	protected int elevatorNum;
	protected int currentFloor;
	private int tempRequest;
	protected String currentStatus; // Up, Down and Stop
	protected elevatorButton buttonInsideElevator[] = new elevatorButton[numberOfFloor];
	protected FloorLabel[] floorLabel = new FloorLabel[numberOfFloor];
	protected JPanel elevatorPanel;
	protected elevatorButton emergency;
	protected JLabel floor; //��Ļ�ϵ�����
	protected JLabel direction;//��Ļ�ϵ�����
	protected JLabel doorStatus;//������״̬����ʾ����Ļ��
	protected FloorButton tempFloorBtn; //���ڸı�¥�㰴ť����ɫ
	protected String doorStatusStr;
	protected Stack<Integer> internalRequest = new Stack<Integer>();
	protected Stack<exRequest> externalRequest = new Stack<exRequest>();
	
	
	elevator(){
		currentFloor = 1;
		currentStatus = "Stop";
		doorStatusStr = "Close";
	};
	
	elevator(int elevatorNum) {
		this.elevatorNum = elevatorNum;
		currentFloor = 1;
		currentStatus = "Stop";
		doorStatusStr = "Close";
	}
	
	public void run()
	{
//		System.out.println("elevator" + (elevatorNum+1) + " is running");
		while(true){
			direction.setText("-");
			while(!internalRequest.isEmpty() || !externalRequest.isEmpty()){
//				System.out.println("EL: " + elevatorNum + "current floor:" + currentFloor);
				//����������
				if(!internalRequest.isEmpty())
					tempRequest = internalRequest.pop();
				else if(!externalRequest.isEmpty())
					tempRequest = externalRequest.pop().requestFloor;
				
				directionFeedback(tempRequest);
				setDirectionLabel();
				//ͣ��ĳ��¥���ʱ��
				try{
					Thread.sleep(200);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
				//Ŀ��¥����ʼ��¥��ľ���
				int tempDistance = distance(tempRequest);

				//����
				if(currentStatus == "Up"){
//					System.out.println("tempRequest:" + tempRequest + " tempDistance" + tempDistance);
					for(int i = 0; i < tempDistance ; i++){
						if(!externalRequest.isEmpty()){
//							System.out.println("exRequest:" + externalRequest.peek().requestFloor);
							if(externalRequest.peek().requestFloor < tempRequest 
									&& currentFloor == externalRequest.peek().requestFloor
									&& externalRequest.peek().direction == currentStatus){
								externalRequest.pop();
								try{
									Thread.sleep(1000);
								}
								catch (InterruptedException e2){
									e2.printStackTrace();
								}
								System.out.println("elevator " + (elevatorNum+1) + " Stop at " + currentFloor);
							}
						}
						goingUpward();
					}
					System.out.println("elevator " + (elevatorNum+1) + " Stop at " + currentFloor);
					buttonInsideElevator[tempRequest-1].setBackground(new Color(126, 138, 162));
				}
				
				//����
				else if(currentStatus == "Down"){
					for(int i = 0; i < tempDistance ; i++){
						if(!externalRequest.isEmpty()){
//							System.out.println("exRequest:" + externalRequest.peek().requestFloor);
							if(externalRequest.peek().requestFloor > tempRequest 
									&& currentFloor == externalRequest.peek().requestFloor
									&& externalRequest.peek().direction == currentStatus){
								externalRequest.pop();
								try{
									Thread.sleep(1000);
								}
								catch (InterruptedException e2){
									e2.printStackTrace();
								}
								System.out.println("elevator " + elevatorNum + " Stop at " + currentFloor);
							}
						}
						goingDownward();
					}
						
					buttonInsideElevator[tempRequest-1].setBackground(new Color(126, 138, 162));
				}
//				tempFloorBtn.setBackground(Color.white);
			}
		}
	}
	
	//the floorRequest is the floor that push the button
	public int distance(int floorRequest){
		return Math.abs(floorRequest - currentFloor);
	}
	
	public void goingUpward(){
//		System.out.println("Floor Request up:" + floorRequest);
		//��䵱ǰ¥��Ϊ��ɫ
		floorLabel[numberOfFloor - currentFloor].setBackground(Color.white);
		//�����һ¥��Ϊ��ɫ
		floorLabel[numberOfFloor - currentFloor - 1].setBackground(Color.orange);
		floor.setText(String.valueOf(++currentFloor));
		try{
			Thread.sleep(800);
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void goingDownward(){
//		System.out.println("Floor Request down:" + floorRequest);
		//��䵱ǰ¥��Ϊ��ɫ
		floorLabel[numberOfFloor - currentFloor].setBackground(Color.white);
		//�����һ¥��Ϊ��ɫ
		floorLabel[numberOfFloor - currentFloor + 1].setBackground(Color.orange);
		floor.setText(String.valueOf(--currentFloor));
		try{
			Thread.sleep(800);
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void directionFeedback(int targetFloor){
		if(currentFloor - targetFloor == 0)
			currentStatus = "Stop";
		else if(currentFloor - targetFloor < 0)
			currentStatus = "Up";
		else 
			currentStatus = "Down";
	}
	
	public void setDirectionLabel(){
		if(currentStatus == "Up")
			direction.setText("��");
		else if(currentStatus == "Down")
			direction.setText("��");
		else if(currentStatus == "Stop")
			direction.setText("-");
	}
	
	public void printElevator(){
		System.out.println("elevator Num: " + elevatorNum);
		System.out.println("status: " + currentStatus);
		System.out.println("current floor: " + currentFloor);
	}
}
