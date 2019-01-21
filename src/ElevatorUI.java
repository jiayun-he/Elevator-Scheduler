import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.*;

public class ElevatorUI extends JFrame implements Runnable{
	protected final int numberOfElevator = 5;
	protected final int numberOfFloor = 20;
	protected boolean[] isEmergency = new boolean[numberOfElevator+1];
	protected elevator[] elevatorArr = new elevator[numberOfElevator];
	protected Stack<exRequest> externalRequest = new Stack<exRequest>();
	
	protected JPanel storeyDiv;
	protected JPanel screenDiv;
	protected JPanel floorControl;
	protected JPanel outside;
	protected JPanel[] liftCar = new JPanel[numberOfElevator];
	protected FloorButton[] floorButton = new FloorButton[numberOfFloor*2];
	protected JFrame ControlPanel = new JFrame("Control Panel");
	public ElevatorUI() {
		
	}
	
	public void Demo(){
		init();
		this.setLayout(new BorderLayout());
		this.setSize(1520,750);
		this.setVisible(true);
	}
	
	public void init(){
		outside = new JPanel();
		outside.setLayout(new GridLayout(1,6,5,5));
		outside.setSize(1500,700);
		outside.setBackground(new Color(184, 236, 215));
		outside.setVisible(true);
		
		int index = 0;
		setLayout(null);
		ControlPanel.setLayout(new GridLayout(1, 5, 2, 2));
		ControlPanel.setSize(1300, 350);
		ControlPanel.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		floorControl = new JPanel();
		floorControl.setLayout(new GridLayout(numberOfFloor, 2, 2, 2));
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		for(int i = 0; i < numberOfElevator; i++){
			//初始化电梯
			elevatorArr[i] = new elevator(i);
			//初始化电梯紧急按钮状态
			isEmergency[i] = false;
			//初始化电梯内部显示屏
			elevatorArr[i].floor = new JLabel(String.valueOf(elevatorArr[i].currentFloor));
			elevatorArr[i].floor.setFont(new Font("Microsoft Yahei",0,50));
			
			elevatorArr[i].direction = new JLabel();
			elevatorArr[i].setDirectionLabel();
			elevatorArr[i].direction.setFont(new Font("宋体",0,40));
			
			elevatorArr[i].doorStatus = new JLabel(elevatorArr[i].doorStatusStr);
			elevatorArr[i].doorStatus.setFont(new Font("Microsoft Yahei",0,20));
			
			screenDiv = new JPanel();
			screenDiv.setLayout(new GridLayout(1, 3, 5, 5));
			screenDiv.setPreferredSize(new Dimension(200, 100));
			screenDiv.setBackground(Color.white);
			screenDiv.add(elevatorArr[i].floor);
//			screenDiv.add(elevatorArr[i].doorStatus);
			screenDiv.add(elevatorArr[i].direction);
			
			screenDiv.setVisible(true);
			
			//初始化电梯动画的5*20
			GridLayout liftCarLayout = new GridLayout(numberOfFloor, 1, 1, 2);
			liftCar[i] = new JPanel();
			liftCar[i].setLayout(liftCarLayout);
			liftCar[i].setBackground(new Color(184, 236, 215));
			liftCar[i].setVisible(true);
			
			storeyDiv = new JPanel(new GridLayout(7, 3, 1, 1));
			storeyDiv.setSize(200,100);
			storeyDiv.setBackground(Color.white);
			storeyDiv.setVisible(true);
			
			elevatorArr[i].elevatorPanel = new JPanel(new FlowLayout());
			elevatorArr[i].elevatorPanel.setPreferredSize(new Dimension(200,100));
			elevatorArr[i].elevatorPanel.setBackground(Color.gray);
			elevatorArr[i].elevatorPanel.setVisible(true);
			index = numberOfFloor;
			for(int j = 0; j < 20; j++){
				//电梯内楼层按钮
				elevatorArr[i].buttonInsideElevator[j] = new elevatorButton();
				elevatorArr[i].buttonInsideElevator[j].floorIndex = j+1;
				elevatorArr[i].buttonInsideElevator[j].setElBtn(i+1);
				elevatorArr[i].buttonInsideElevator[j].setBackground(new Color(126, 138, 162));
				elevatorArr[i].buttonInsideElevator[j].addActionListener(new elevatorButtonListener());
				
				//电梯内Emergency按钮
				elevatorArr[i].emergency = new elevatorButton("Emergency");
				elevatorArr[i].emergency.setElNum(i+1);
				elevatorArr[i].emergency.setBackground(Color.orange);
				elevatorArr[i].emergency.addActionListener(new emergencyButtonListener());
				
				storeyDiv.add(elevatorArr[i].buttonInsideElevator[j]);
				
				elevatorArr[i].floorLabel[j] = new FloorLabel(i,j);
				elevatorArr[i].floorLabel[j].setBackground(Color.white);
				elevatorArr[i].floorLabel[j].setEnabled(false);
				elevatorArr[i].floorLabel[j].setText(Integer.toString(index--));
				elevatorArr[i].floorLabel[j].setSize(10,100);
				elevatorArr[i].floorLabel[j].setFont(new Font("Microsoft Yahei", 0, 20));

				liftCar[i].add(elevatorArr[i].floorLabel[j]);
			}//楼层循环结束
			
			//添加电梯控制面板
			storeyDiv.add(elevatorArr[i].emergency);
			elevatorArr[i].elevatorPanel.add(storeyDiv);
			elevatorArr[i].elevatorPanel.add(screenDiv);
			//设置第一层
			elevatorArr[i].floorLabel[19].setBackground(Color.orange);
			//添加电梯动画面板
			outside.add(liftCar[i]);
			
			ControlPanel.setVisible(true);
			ControlPanel.add(elevatorArr[i].elevatorPanel);
		}//电梯个数循环结束
		
		//添加楼层控制面板↑按钮
		int upIndex = numberOfFloor;
		int downIndex = numberOfFloor;
		int k = 0;
		while(k < numberOfFloor*2){
			//设置楼层事件监听
			if(k%2 == 0){
				floorButton[k] = new FloorButton(upIndex--);
				floorButton[k].setButton("↑");
				floorButton[k].direction = "Up";
			}
			else{
				floorButton[k] = new FloorButton(downIndex--);
				floorButton[k].setButton("↓");
				floorButton[k].direction = "Down";
			}
				
			floorButton[k].setBackground(Color.white);
			floorButton[k].setForeground(Color.black);
			floorButton[k].addActionListener(new floorButtonListener());
			floorControl.add(floorButton[k]);
			k++;
		}
		
		outside.add(floorControl);
		this.add(outside,BorderLayout.SOUTH);
		
		for(int i = 0; i < numberOfElevator; i++)
			new Thread(elevatorArr[i]).start();
	}
	
	//电梯内楼层按钮监听
	public class elevatorButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			elevatorButton tempElBtn = (elevatorButton)e.getSource();
			tempElBtn.setBackground(Color.white);
			int elNum = tempElBtn.elevatorNum - 1;
			int currentFloor = elevatorArr[elNum].currentFloor;
			int targetFloor = tempElBtn.floorIndex;
			System.out.print("Floor"+ tempElBtn.floorIndex);
			System.out.println(" request by Elevator No. " + tempElBtn.elevatorNum);
			
			elevatorArr[elNum].internalRequest.push(targetFloor);
		}
	}
	
	public class floorButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			FloorButton temp = (FloorButton)e.getSource();
//			temp.setBackground(Color.orange);
			int requestFloor = temp.index;
			int answerElNum = 0;
			String direction = null;
			String up = "↑";
			String down = "↓";
			if(temp.getText() == up)
				direction = "Up";
			else if(temp.getText() == down)
				direction = "Down";
			
//			elevatorArr[answerElNum].tempFloorBtn = temp;
			exRequest tempExRequest = new exRequest();
			tempExRequest.requestFloor = requestFloor;
			tempExRequest.direction = direction;
			answerElNum = locateElevator(temp);
			System.out.println("Floor " + requestFloor + "request " + direction);
//			System.out.println("Elevator No." + (locateElevator(temp)+1) + " takes the request");
			elevatorArr[answerElNum].externalRequest.push(tempExRequest);
		}
		
	}
	//电梯内紧急按钮监听
	public class emergencyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			elevatorButton temp = (elevatorButton)e.getSource();
			temp.setVisible(true);
			isEmergency[temp.elevatorNum] = !isEmergency[temp.elevatorNum];
			if(isEmergency[temp.elevatorNum]){
				temp.setBackground(Color.RED);
				System.out.println("Emergency request by No. " + temp.elevatorNum + " elevator");
			}
			else{
				temp.setBackground(Color.orange);
				System.out.println("Emergency cancelled by No. " + temp.elevatorNum + " elevator");
			}
		}
	}
	
	//返回最近且同向的电梯号
	public int locateElevator(FloorButton flBtn){
		int[] motionlessDistance = new int[numberOfElevator];
		int[] movingDistance = new int[numberOfElevator];
		int motionlessOptimal = 20, movingOptimal = 20;
		int motionlessIndex = 4, movingIndex = 4;
		for(int i = 0; i < numberOfElevator; i++){
			if(elevatorArr[i].currentStatus == "Stop"){
				motionlessDistance[i] = Math.abs(flBtn.index - elevatorArr[i].currentFloor);
//				System.out.printf("Motionless Distance[%d] = %d\n",i,motionlessDistance[i]);
				if(motionlessDistance[i] < motionlessOptimal){
					motionlessOptimal = motionlessDistance[i];
					motionlessIndex = i;
//					System.out.printf("motionlessOptimal: %d, motionlessIndex: %d\n", motionlessOptimal,motionlessIndex);
				}
			}
			else if(elevatorArr[i].currentStatus == flBtn.direction){
				movingDistance[i] = Math.abs(flBtn.index - elevatorArr[i].currentFloor);
				if(movingDistance[i] < movingOptimal){
					movingOptimal = movingDistance[i];
					movingIndex = i;
//					System.out.printf("movingOptimal: %d, movingIndex: %d\n",movingOptimal,movingIndex);
				}
			}
		}
		//如果相等，优先选择动的的电梯
		if(motionlessOptimal < movingOptimal)
			return motionlessIndex;
		else
			return movingIndex;
	}
	
	@Override
	public void run() {
		ElevatorUI test = new ElevatorUI();
		test.Demo();
	}
}
