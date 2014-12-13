import java.awt.Color;
import java.awt.Point;

public class DrawnPoint extends Point {
	private static final long serialVersionUID = 1L;
	Color color;
	int radius;

	public DrawnPoint(int x, int y, int r, Color c) {
		super(x, y);
		this.radius = r;
		this.color = c;
	}
}