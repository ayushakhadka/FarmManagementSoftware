package farmItem;

import java.io.IOException;
import java.util.List;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class View {
	private MainApp mainApp;
	public TreeView<Component> farmList;
	public TextField xPositionField;
	public TextField yPositionField;
	public TextField lengthField;
	public TextField heightField;
	public TextField widthField;
	public TextField nameField;
	public TextField priceField;
	public TextField marketValueField;
	public TextField netPriceField;
	public TextField netValueField;
	public Canvas cv;
	public Canvas cv2;
	public Pane dPane;
	public Pane pane;
	public Timeline timeline;
	public GraphicsContext drawShape;
	public GraphicsContext drawImage;
	public Image image;
	public String nameValue;
	public double xposValue;
	public double yposValue;
	public double heightValue;
	public double widthValue;
	public double lengthValue;
	public double priceValue;
	public double marketValue;
	public double netPrice;
	public double netValue;
	public static ImageView drone1;
	public Component drone;
	public static Component cc;
	public static double ccx;
	public static double ccy;
	public static double xd;
	public static double yd;
	public static double droneStartX;
	public static double droneStartY;
	public static TreeItem<Component> currentItem;
	public static double currentItemXPosition;
	public static double currentItemYPosition;
	public static double dx;
	public static double dy;
	public static double dl;
	public static double dw;

	private boolean isCommandCenter = false;
	private boolean isDrone = false;

	

	// Radio Buttons
	@FXML
	private RadioButton scanFarmRadioButton;
	@FXML
	private RadioButton scanItemRadioButton;

	private static View viewInstance;

	public View() {
	}

	public static View singletonDemo() {
		if (viewInstance == null) {
			viewInstance = new View();
		}
		return viewInstance;
	}

	private ToggleGroup scanOneToggleGroup;

	@FXML
	private void initialize() {
		farmList.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> updateDetails(newValue));

		scanOneToggleGroup = new ToggleGroup();
		this.scanFarmRadioButton.setToggleGroup(scanOneToggleGroup);
		this.scanItemRadioButton.setToggleGroup(scanOneToggleGroup);
	}
	// ---------------------------------------------------------

	/**
	 * When an item is selected, the fields are refreshed with the current data.
	 *
	 * @param newValue
	 */
	public void updateDetails(TreeItem<Component> newValue) {
		List<TreeItem<Component>> children = farmList.getSelectionModel().getSelectedItem().getChildren();
		if (newValue != null) {
			nameField.setText(newValue.getValue().getName());
			xPositionField.setText(Double.toString(newValue.getValue().getXPosition()));
			yPositionField.setText(Double.toString(newValue.getValue().getYPosition()));
			lengthField.setText(Double.toString(newValue.getValue().getLength()));
			widthField.setText(Double.toString(newValue.getValue().getWidth()));
			heightField.setText(Double.toString(newValue.getValue().getHeight()));
			priceField.setText(Double.toString(newValue.getValue().getPrice()));
			marketValueField.setText(Double.toString(newValue.getValue().getMarketValue()));
			
			PricingVisitor pv = new PricingVisitor();
			newValue.getValue().accept(pv);
			netPriceField.setText(Double.toString(pv.getTotalPrice(children)));
			
			MarketPricing mp = new MarketPricing();
			newValue.getValue().accept(mp);
			netValueField.setText(Double.toString(mp.getTotalMarketPrice(children)));
			
		} else {
			nameField.clear();
			xPositionField.clear();
			yPositionField.clear();
			lengthField.clear();
			widthField.clear();
			heightField.clear();
			priceField.clear();
			marketValueField.clear();
			netValueField.clear();
		}
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Creates the root of the tree
	 */
	public void setRoot() {
		if (farmList.getRoot() == null) {
			Component root = new ItemContainer();
			root.setKind("Container");
			root.setName("Root");
			root.setWidth(800);
			root.setLength(600);
			farmList.setRoot(new TreeItem<Component>(root));
			farmList.getSelectionModel().selectFirst();
			farmList.getRoot().setExpanded(true);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot add multiple root items");
			alert.showAndWait();
		}
	}

	/**
	 * Adds a container to the selected container
	 */
	public void addContainer() {
		TreeItem<Component> addContainer = farmList.getSelectionModel().getSelectedItem();
		if (farmList.getRoot() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first add a root");
			alert.showAndWait();
		} else if (farmList.getSelectionModel().isEmpty() == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first select a container to which to add your container");
			alert.showAndWait();
		} else if (addContainer.getValue().getKind() == "Container") {
			Component container = new ItemContainer();
			container.setKind("Container");
			container.setName("Container");
			
			container.setXPosition((addContainer.getValue().getXPosition()) + 20);
			container.setYPosition((addContainer.getValue().getYPosition()) + 25);
			container.setWidth((addContainer.getValue().getWidth()) / 5 );
			
			container.setHeight(20);
			container.setLength((addContainer.getValue().getLength()) / 4);
			
			
			container.setPrice(250);
			container.setMarketValue(0); // container should have market value of 0

			addContainer.getChildren().add(new TreeItem<>(container));
			addContainer.getValue().addChild(container);
			addContainer.setExpanded(true);
			Draw(container.getXPosition(), container.getYPosition(), container.getLength(), container.getWidth(),
					container.getName());
			farmList.getSelectionModel().select(addContainer);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot add a container to an item");
			alert.showAndWait();
		}
	}

	/**
	 * Adds an item to selected container
	 */
	public void addChild() {
		TreeItem<Component> addChild = farmList.getSelectionModel().getSelectedItem();
		if (farmList.getRoot() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first add a root");
			alert.showAndWait();
		} else if (farmList.getSelectionModel().isEmpty() == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first select a container to which to add your item");
			alert.showAndWait();
		} else if (addChild.getValue().getKind() == "Container") {
			Component leaf = new Item();
			leaf.setKind("Item");
			leaf.setName("Item");
			
//			System.out.println((addChild.getValue().getHeight()) / 2);
			
			leaf.setXPosition((addChild.getValue().getXPosition()) + 20);
			leaf.setYPosition((addChild.getValue().getYPosition()) + 20);
			leaf.setWidth((addChild.getValue().getWidth()) / 4);
			leaf.setHeight(40);
			leaf.setLength((addChild.getValue().getHeight()) / 2);
			
			leaf.setPrice(100);
            leaf.setMarketValue(100);
            
			addChild.getChildren().add(new TreeItem<>(leaf));
			addChild.getValue().addChild(leaf);
			
			Draw(leaf.getXPosition(), leaf.getYPosition(), leaf.getLength(), leaf.getWidth(), leaf.getName());

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot add an item to an item. An item must be in a container.");
			alert.showAndWait();
		}

	}

	/***
	 * Deletes the selected item or container. Warns root cannot be deleted.
	 */
	public void Delete() {
		TreeItem<Component> itemToDelete = farmList.getSelectionModel().getSelectedItem();

		Component delete = farmList.getSelectionModel().getSelectedItem().getValue();

		if (itemToDelete.getParent() == null) {

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("An Error has Occurred");
			alert.setContentText("You cannot delete the root element");
			alert.showAndWait();

		} else if (itemToDelete.getValue().getKind() == "Container"
				&& itemToDelete.getValue().getName().equals("Command Center")) {

			isCommandCenter = false;
			isDrone = false;
			ClearDrone();
			itemToDelete.getParent().getChildren().remove(itemToDelete);
			ClearDraw(delete.getXPosition(), delete.getYPosition(), delete.getLength(), delete.getWidth(),
					delete.getName());

		} else if (itemToDelete.getValue().getName().equals("Drone")) {

			isDrone = false;
			ClearDrone();
			itemToDelete.getParent().getChildren().remove(itemToDelete);

			ClearDraw(delete.getXPosition(), delete.getYPosition(), delete.getLength(), delete.getWidth(),
					delete.getName());

		} else if (itemToDelete.getValue().getKind() == "Item") {

			itemToDelete.getParent().getChildren().remove(itemToDelete);
			ClearDraw(delete.getXPosition(), delete.getYPosition(), delete.getLength(), delete.getWidth(),
					delete.getName());

		}

		else {

			itemToDelete.getParent().getChildren().remove(itemToDelete);
			// iterate through all children to delete all items in a container
			ClearDraw(delete.getXPosition(), delete.getYPosition(), delete.getLength(), delete.getWidth(),
					delete.getName());
			updateItem(itemToDelete.getChildren());
		}

		farmList.refresh();
		updateView(farmList.getRoot().getChildren());

	}

	public void updateItem(List<TreeItem<Component>> children) {
		for (TreeItem<Component> child : children) {
			ClearDraw(child.getValue().getXPosition(), child.getValue().getYPosition(), child.getValue().getLength(),
					child.getValue().getWidth(), child.getValue().getName());

			List<TreeItem<Component>> newChildren = child.getChildren();
			if (newChildren != null) {
				updateItem(newChildren);
			}
		}
	}

	/***
	 * Adds a command center to the Treeview.
	 */
	public void addCommandCenter() {
		TreeItem<Component> addContainer = farmList.getSelectionModel().getSelectedItem();
		if (farmList.getRoot() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first add a root");
			alert.showAndWait();

		} else if (isCommandCenter == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot have more than 1 command center");
			alert.showAndWait();

		} else if (addContainer.getValue().getKind() != "Container") {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot add a container to an item");
			alert.showAndWait();

		} else if (!addContainer.getValue().getName().equals("Root")
				&& addContainer.getValue().getKind() == "Container") {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must add command Center item container to the root");
			alert.showAndWait();

		} else if (addContainer.getValue().getKind() == "Container") {
			Component commandCenter = new ItemContainer();
			commandCenter.setKind("Container");
			commandCenter.setName("Command Center");
			isCommandCenter = true;

			commandCenter.setXPosition(300);
			commandCenter.setYPosition(300);
			commandCenter.setWidth(100);
			commandCenter.setHeight(40);
			commandCenter.setLength(100);
			commandCenter.setPrice(1000);

			addContainer.getChildren().add(new TreeItem<>(commandCenter));
			addContainer.getValue().addChild(commandCenter);
			addContainer.setExpanded(true);
			cc = commandCenter;
			Draw(commandCenter.getXPosition(), commandCenter.getYPosition(), commandCenter.getLength(),
					commandCenter.getWidth(), commandCenter.getName());
			farmList.getSelectionModel().select(addContainer);
		}

	}

	/**
	 * Add a Drone to the Command Center
	 */
	public void addDrone() {
		TreeItem<Component> addChild = farmList.getSelectionModel().getSelectedItem();
		if (farmList.getRoot() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first add a root");
			alert.showAndWait();
		} else if (farmList.getSelectionModel().isEmpty() == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must first select a container to which to add your item");
			alert.showAndWait();

		} else if (isDrone == true) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot have more than 1 drone item");
			alert.showAndWait();

		} else if (addChild.getValue().getName().equals("Command Center")
				&& addChild.getValue().getKind() == "Container") {
			drone = new Item();
			drone.setKind("Item");
			drone.setName("Drone");
			isDrone = true;

			drone.setXPosition(addChild.getValue().getXPosition() + 20);
			drone.setYPosition(addChild.getValue().getYPosition() + 20);
			drone.setWidth(40);
			drone.setHeight(15);
			drone.setLength(40);
			drone.setPrice(800);

			addChild.getChildren().add(new TreeItem<>(drone));
			addChild.getValue().addChild(drone);
			DrawDrone(drone.getXPosition(), drone.getYPosition(), drone.getLength(), drone.getWidth());
			addChild.setExpanded(true);
			farmList.getSelectionModel().getSelectedItem();

		} else if (addChild.getValue().getKind() == "Item") {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You cannot add an item to an item. An item must be in a container.");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("You must place drone item inside Command Center container.");
			alert.showAndWait();
		}

	}

	/**
	 * Draws the rectangle at position (X,Y) of l*b
	 * 
	 * @param x the X position of the upper left corner of the rectangle.
	 * @param y the Y position of the upper left corner of the rectangle.
	 * @param w the width of the rectangle.
	 * @param h the height of the rectangle.
	 */
	public void Draw(double x, double y, double l, double h, String nameValue) {
		drawShape = cv.getGraphicsContext2D();
		drawShape.setLineWidth(1.0);
		drawShape.setStroke(Color.BLACK);
		drawShape.strokeRect(x, y, l, h);
		drawShape.setLineWidth(.75);
		drawShape.strokeText(nameValue, x + 1, y - 3);

	}

	public ImageView DrawDrone(double x, double y, double l, double h) {
		ImageView drone1 = new ImageView();
		drone1.setImage(new Image("/images/drone.png"));
		drone1.setX(x);
		drone1.setY(y);
		drone1.setFitHeight(l);
		drone1.setFitWidth(h);
		dPane.getChildren().addAll(drone1);
		return drone1;
	}

	@FXML
	public void ScanFarm() throws IOException, InterruptedException {
		FarmScan();
	}

	
	public void LaunchSimulator() throws IOException, InterruptedException {
//
//    	if (this.scanOneToggleGroup.getSelectedToggle().equals(this.scanFarmRadioButton)) {
//    	      FarmScan ();
//    	}
//    	      
//    	if (this.scanOneToggleGroup.getSelectedToggle().equals(this.scanItemRadioButton)) {
//    		DroneToItem();
//    	}

	}

	public void LaunchDrone() throws IOException, InterruptedException {

	}

	@FXML
	public void FarmScan() {
		if (drone1 == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("No drone exists. Add a drone to the command center and try again.");
			alert.showAndWait();
		} else {
			 //TODO: add a way to adjust farm size so any sized farm can be scanned with 1 function
			int farmWidth = 600;
			int farmLength = 800;
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

	public void DroneToItem() {
		if (drone1 == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("No drone exists. Add a drone to the command center and try again.");
			alert.showAndWait();
		} else {
			// Calculation the coordinates of the middle of the item or item container
			double xDestination = Double.parseDouble(xPositionField.getText())
					+ (0.5 * Double.parseDouble(lengthField.getText()));
			double yDestination = Double.parseDouble(yPositionField.getText())
					+ (0.5 * Double.parseDouble(widthField.getText()));

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

	/**
	 * Removes the rectangle at position (X,Y) of l*b
	 * 
	 * @param x the X position of the upper left corner of the rectangle.
	 * @param y the Y position of the upper left corner of the rectangle.
	 * @param w the width of the rectangle.
	 * @param h the height of the rectangle.
	 */
	public void ClearDraw(double x, double y, double l, double h, String nameValue) {
		drawShape = cv.getGraphicsContext2D();
		drawShape.setLineWidth(1.5);
		drawShape.setStroke(Color.WHITE);
		drawShape.strokeRect(x, y, l, h);
		drawShape.setLineWidth(1.25);
		drawShape.strokeRect(x, y, l, h);
		drawShape.strokeText(nameValue, x + 1, y - 3);
	}

	/*
	 * Clears Drone from dPane
	 */
	public void ClearDrone() {
		dPane.getChildren().clear();
	}

	/**
	 * ----------------Reference for checking if digit is in
	 * string--------------------------
	 *
	 * https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
	 */
	public boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	// ----------------------------------------------------------------------------------------

	public boolean isTextFieldCorrect(String xPos, String yPos, String len, String height, String width, String name,
			String price, String netPrice, String netValue) {
		if (isNumeric(xPos) && isNumeric(yPos) && isNumeric(len) && isNumeric(height) && isNumeric(width)
				&& isNumeric(price) && isNumeric(netPrice) && isNumeric(netValue)) {
			return true;
		}
		return false;
	}

	/**
	 * Saves the given information
	 * 
	 * if update then will need to delete drawing (delete shape before changing
	 * values) then redraw it with updated values
	 */
	public void Save() {
		TreeItem<Component> itemSelected = farmList.getSelectionModel().getSelectedItem();
		Component resize = farmList.getSelectionModel().getSelectedItem().getValue();
		List<TreeItem<Component>> children = farmList.getSelectionModel().getSelectedItem().getChildren();

		double xDiff = Double.parseDouble(xPositionField.getText()) - itemSelected.getValue().getXPosition();
		double yDiff = Double.parseDouble(yPositionField.getText()) - itemSelected.getValue().getYPosition();

		String xPos = xPositionField.getText();
		String yPos = yPositionField.getText();
		String len = lengthField.getText();
		String height = heightField.getText();
		String width = widthField.getText();
		String name = nameField.getText();
		String price = priceField.getText();
		String netPrice = netPriceField.getText();
		String netValue = netValueField.getText();

		if (isTextFieldCorrect(xPos, yPos, len, height, width, name, price, netPrice, netValue)) {

			if (resize.getKind() == "Drone") {
				ClearDrone();
				nameValue = nameField.getText();
				xposValue = Double.parseDouble(xPositionField.getText());
				yposValue = Double.parseDouble(yPositionField.getText());
				heightValue = Double.parseDouble(heightField.getText());
				lengthValue = Double.parseDouble(lengthField.getText());
				priceValue = Double.parseDouble(priceField.getText());
				widthValue = Double.parseDouble(widthField.getText());
				marketValue = Double.parseDouble(marketValueField.getText());
				resize.setName(nameValue);
				resize.setXPosition(xposValue);
				resize.setYPosition(yposValue);
				resize.setHeight(heightValue);
				resize.setLength(lengthValue);
				resize.setWidth(widthValue);
				resize.setPrice(priceValue);
				resize.setMarketValue(marketValue);
			}

			else if (itemSelected.getValue().getName().equals("Root") && !name.equals("Root")
					|| itemSelected.getValue().getName().equals("Command Center") && !name.equals("Command Center")
					|| itemSelected.getValue().getName().equals("Drone") && !name.equals("Drone")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Cannot Perform That Action");
				alert.setContentText("You cannot change the name of " + itemSelected.getValue().getName());
				alert.showAndWait();
			}

			else {

				if (itemSelected.getValue().getName().equals("Command Center")) {
					ClearDrone();
					for (TreeItem<Component> drone : children) {
						drone.getValue().setXPosition((drone.getValue().getXPosition()) + xDiff);
						drone.getValue().setYPosition((drone.getValue().getYPosition()) + yDiff);
					}
				} else {
					updateChildren(children, xDiff, yDiff);
				}

				ClearDraw(resize.getXPosition(), resize.getYPosition(), resize.getLength(), resize.getWidth(),
						resize.getName());
				nameValue = nameField.getText();
				xposValue = Double.parseDouble(xPositionField.getText());
				yposValue = Double.parseDouble(yPositionField.getText());
				heightValue = Double.parseDouble(heightField.getText());
				lengthValue = Double.parseDouble(lengthField.getText());
				priceValue = Double.parseDouble(priceField.getText());
				widthValue = Double.parseDouble(widthField.getText());
				resize.setName(nameValue);
				resize.setXPosition(xposValue);
				resize.setYPosition(yposValue);
				resize.setHeight(heightValue);
				resize.setLength(lengthValue);
				resize.setWidth(widthValue);
				resize.setPrice(priceValue);
				resize.setMarketValue(marketValue);
			}

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Cannot Perform That Action");
			alert.setContentText("Please fill out all the text areas correctly");
			alert.showAndWait();
		}

		farmList.refresh();
		updateView(farmList.getRoot().getChildren());

	}

	/*
	 * Update the location (x,y) of a Containers children down to leafs
	 */
	public void updateChildren(List<TreeItem<Component>> children, double xDiff, double yDiff) {
		for (TreeItem<Component> child : children) {
			ClearDraw(child.getValue().getXPosition(), child.getValue().getYPosition(), child.getValue().getLength(),
					child.getValue().getWidth(), child.getValue().getName());
			child.getValue().setXPosition((child.getValue().getXPosition()) + xDiff);
			child.getValue().setYPosition((child.getValue().getYPosition()) + yDiff);
			List<TreeItem<Component>> newChildren = child.getChildren();
			if (newChildren != null) {
				updateChildren(newChildren, xDiff, yDiff);
			}
		}
	}

	/*
	 * Clear drawn items and redraw them
	 */
	public void updateView(List<TreeItem<Component>> itemList) {
		for (TreeItem<Component> child : itemList) {
			if (child.getValue().getName().equals("Drone")) {
				ClearDrone();
				DrawDrone(child.getValue().getXPosition(), child.getValue().getYPosition(),
						child.getValue().getLength(), child.getValue().getWidth());
			} else {
				ClearDraw(child.getValue().getXPosition(), child.getValue().getYPosition(),
						child.getValue().getLength(), child.getValue().getWidth(), child.getValue().getName());
				Draw((child.getValue().getXPosition()), (child.getValue().getYPosition()),
						(child.getValue().getLength()), (child.getValue().getWidth()), (child.getValue().getName()));
			}
			List<TreeItem<Component>> newChildren = child.getChildren();
			if (newChildren != null) {
				updateView(newChildren);
			}
		}

	}

}
