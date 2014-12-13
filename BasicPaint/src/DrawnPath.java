import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;

public class DrawnPath extends Path2D.Double {
	private static final long serialVersionUID = 1L;
	protected Color color;
	protected Stroke stroke;

	public DrawnPath(Color color, Stroke stroke) {
		super();
		this.color = color;
		this.stroke = stroke;
	}
	
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.setStroke(stroke);
		g.draw(this);
	}
}
