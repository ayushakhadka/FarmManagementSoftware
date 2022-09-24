package farmItem;


import main.java.surelyhuman.jdrone.control.physical.tello.TelloDrone;

public class TelloAdapter implements DroneAnimation{
	private TelloDrone td;
	
	// Found this conversion on Stack overflow (please double check to make sure this is correct)
//	There are 2.54 centimeters per inch; if it is sufficient 
	//to assume 96 pixels per inch, the formula is rather simple:
//		centimeters = pixels * 2.54 / 96
	
	
	
	public TelloAdapter(TelloDrone td) {
		this.td = td;
	}

	@Override
	public void droneToItem(double xPos, double yPos, double width, double length) {
	
		// TODO Auto-generated method stub
		// convert all values from pixels to feet to cm
		// then use the functions provided by Tellodrone object (td) to 
		// mirror the actions of our animation using the arguments passed to function 
		// take a look at the TelloDrone class to familrize yourself with what functionalites it has
		
		// example:
		// start with gotoMissionPadXY (x,y of command center)
		// then gotoxyz() visit item
		// then go back to command center i.e. gotoMissionPadXYZ
		
	}

	@Override
	public void FarmScan(int farmWidth, int farmLength) {
		// TODO Auto-generated method stub
		// convert all values from pixels to feet to cm
		// then use the functions provided by Tellodrone object (td) to 
		// mirror the actions of our animation using the arguments passed to function
		// take a look at the TelloDrone class to familrize yourself with what functionalites it has
		
		
	}

}
