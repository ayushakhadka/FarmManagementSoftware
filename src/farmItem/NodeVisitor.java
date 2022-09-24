package farmItem;
//visitor
interface NodeVisitor {
//	public abstract void visit(Item item);
//	public abstract void visit(ItemContainer itemContainer);
	
	public abstract void visit(Component comp);
}
