package farmItem;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem;

class PricingVisitor implements NodeVisitor {
	private float netPrice;
	private double totalPrice;

//	
//	public PricingVisitor() {
//		netPrice = 0;
//
//	}
//	
//	public float netPrice() {
//		return netPrice;
//	}
//	
//	
//	public void visit(Item item) {
//		netPrice += item.netPrice();
//	}
//	
//	public void visit(ItemContainer itemContainer) {
//		netPrice += itemContainer.netPrice();
//	}
//
//	@Override
//	public void visit(Component comp) {
//		// TODO Auto-generated method stub
//		 netPrice += comp.getPrice();
//		
//	}
	
	
	public double getTotalPrice(List<TreeItem<Component>> children) {
		for (TreeItem<Component> child : children) {
			totalPrice += child.getValue().getPrice();
			List<TreeItem<Component>> newChildren = child.getChildren();
			if (newChildren != null) {
				getTotalPrice(newChildren);
			}
		}
		return totalPrice;
	}
	
	

	@Override
	public void visit(Component comp) {
		// TODO Auto-generated method stub
		totalPrice += comp.getPrice();
	}
}
