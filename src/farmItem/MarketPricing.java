package farmItem;

import java.util.List;

import javafx.scene.control.TreeItem;

public class MarketPricing implements NodeVisitor{
	private double total = 0;
//	private float netValue;
//	
//	public MarketPricing() {
//		netValue = 0;
//	}
//	
//	public float netValue() {
//		
//		return netValue;
//	}
//	
////	public void visit(Item item) {
////		netValue += item.netValue();
////		System.out.println("in: " + netValue);
////	}
////	
////	public void visit(ItemContainer itemContainer) {
////		netValue += itemContainer.netValue();
////	}
//
//	@Override
//	public void visit(Component comp) {
//		// TODO Auto-generated method stub
//		netValue += comp.getPrice();
//		System.out.println("in: " + netValue);
//		
//	}
	
	public double getTotalMarketPrice(List<TreeItem<Component>> children) {
		for (TreeItem<Component> child : children) {
			  total+= child.getValue().getMarketValue();
			List<TreeItem<Component>> newChildren = child.getChildren();
			if (newChildren != null) getTotalMarketPrice(newChildren);

		}
		return total;
	}
	
	
	

	@Override
	public void visit(Component comp) {
		// TODO Auto-generated method stub
		 total += comp.getMarketValue();
		
		
	} 
}