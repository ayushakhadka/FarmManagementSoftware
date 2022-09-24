package farmItem;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class DrawAnimation implements DroneAnimation{
	private Component drone;
	private ImageView drone1;
	
	public DrawAnimation(Component drone,ImageView drone1 ) {
		this.drone = drone;
		this.drone1 = drone1;
	}


	@Override
	public void droneToItem(double xPos, double yPos, double width, double lenght) {
		if (drone1 == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("No drone exists. Add a drone to the command center and try again.");
			alert.showAndWait();
		} else {
			// Calculation the coordinates of the middle of the item or item container
			double xDestination = xPos + (0.5 * lenght);
			
			double yDestination = yPos + (0.5 * width);
			
			
			// JavaFX animation
			TranslateTransition visitItem = new TranslateTransition();
			visitItem.setNode(drone1);
			visitItem.setDuration(Duration.seconds(2));
			// Accounting for the starting location of the drone and the size of the 72x72
			// drone picture
			visitItem.setByX(xDestination - drone.getXPosition() - 18);
			visitItem.setByY(yDestination - drone.getYPosition() - 36);
			// Ensuring the drone returns to its starting position
			visitItem.setCycleCount(2);
			visitItem.setAutoReverse(true);
			visitItem.play();
		}
		 
	}
	
	
	
	public void FarmScan(int farmWidth, int farmLength) {
		if (drone1 == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("No drone exists. Add a drone to the command center and try again.");
			alert.showAndWait();
		} else {
			 //TODO: add a way to adjust farm size so any sized farm can be scanned with 1 function

			// Move to corner of the Farm
			TranslateTransition moveToCorner = new TranslateTransition();
			moveToCorner.setNode(drone1);
			moveToCorner.setDuration(Duration.seconds(2));
			moveToCorner.setByX(10 + drone.getXPosition() * -1);
			moveToCorner.setByY(10 + drone.getYPosition() * -1);
			moveToCorner.play();

			// Scan the whole farm moving downward and going left to right in 4 sweeps
			PathTransition scanFarm = new PathTransition();
			Path route = new Path();
			
			MoveTo moveTo = new MoveTo();
			moveTo.setX(25);
			moveTo.setY(25);
			LineTo line1 = new LineTo();
			line1.setX(25);
			line1.setY(farmLength - 25);
			LineTo line2 = new LineTo();
			line2.setX(farmWidth / 3);
			line2.setY(farmLength - 25);
			LineTo line3 = new LineTo();
			line3.setX(farmWidth / 3);
			line3.setY(25);
			LineTo line4 = new LineTo();
			line4.setX(2 * farmWidth / 3);
			line4.setY(25);
			LineTo line5 = new LineTo();
			line5.setX(2 * farmWidth / 3);
			line5.setY(farmLength - 25);
			LineTo line6 = new LineTo();
			line6.setX(farmWidth - 25);
			line6.setY(farmLength - 25);
			LineTo line7 = new LineTo();
			line7.setX(farmWidth - 25);
			line7.setY(25);
			LineTo line8 = new LineTo();
			line8.setX(25);
			line8.setY(25);
			route.getElements().add(moveTo);
			route.getElements().add(line1);
			route.getElements().add(line2);
			route.getElements().add(line3);
			route.getElements().add(line4);
			route.getElements().add(line5);
			route.getElements().add(line6);
			route.getElements().add(line7);
			route.getElements().add(line8);
			
			//Play the transition
			scanFarm.setDelay(Duration.seconds(2));
			scanFarm.setNode(drone1);
			scanFarm.setDuration(Duration.seconds(15));
			scanFarm.setPath(route);
			scanFarm.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
			scanFarm.play();

			// Return to command center
			TranslateTransition returnToCC = new TranslateTransition();
			returnToCC.setDelay(Duration.seconds(17));
			returnToCC.setNode(drone1);
			returnToCC.setDuration(Duration.seconds(2));
			returnToCC.setByX(1);
			returnToCC.setByY(1);
			returnToCC.play();
		}
	}
	

}
