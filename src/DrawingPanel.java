import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseMotionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	private BufferedImage canvas;
	private int mouseX;
	private int mouseY;
	private int brushWidth = 10;
	private int brushWidthIncr = 2;
	private Color color = Color.BLACK;
	private int colorIncr = 10;
	private DrawnPath currentPath;
	final JFileChooser fc = new JFileChooser();

	public DrawingPanel() {
		this.setBackground(Color.WHITE);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		fc.setFileFilter(new ImageFilter());
	}

	private void newCanvas() {
		canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (canvas == null) {
			newCanvas();
			openFile();
		}
		g.drawImage(canvas, 0, 0, null);
		if (currentPath != null) {
			currentPath.paint((Graphics2D) g);
		}
		g.setColor(color);
		g.fillOval(mouseX - brushWidth / 2, mouseY - brushWidth / 2, brushWidth, brushWidth);
	}

	public void saveFile() {
		try {
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String path = file.getCanonicalPath();
				if (!path.endsWith(".png")) {
					file = new File(path + ".png");
				}
				writeImage(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeImage(File file) throws IOException {
		ImageIO.write(canvas, "PNG", file);
	}

	public void openFile() {
		try {
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				readImage(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readImage(File file) throws IOException {
		canvas = ImageIO.read(file);
		double w = getWidth();
		double h = getHeight();
		double iw = canvas.getWidth();
		double ih = canvas.getHeight();
		if (iw > w || ih > h) {
			BufferedImage scaled = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
			double scaledW = w, scaledH = h;
			if (iw - w > ih - h) {
				scaledH = w / iw * ih;
			} else {
				scaledW = h / ih * iw;
			}
			Graphics g = scaled.getGraphics();
			g.drawImage(canvas, 0, 0, (int) scaledW, (int) scaledH, null);
			canvas = scaled;
		}
		repaint();
	}

	private void updateMouseCoords(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (currentPath != null) {
			Graphics2D g2d = (Graphics2D) canvas.getGraphics();
			currentPath.paint(g2d);
			currentPath = null;
		}
		updateMouseCoords(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (currentPath == null) {
			Stroke stroke = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			currentPath = new DrawnPath(color, stroke);
			currentPath.moveTo(e.getX(), e.getY());
		} else {
			currentPath.lineTo(e.getX(), e.getY());
		}
		updateMouseCoords(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int r, g, b;
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) { // Clear drawing
			canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		if (currentPath == null) { // If the user isn't currently drawing
			switch (key) {
			case KeyEvent.VK_UP:
				brushWidth = brushWidth + brushWidthIncr > 1000 ? 1000 : brushWidth + brushWidthIncr;
				break;
			case KeyEvent.VK_DOWN:
				brushWidth = brushWidth - brushWidthIncr < 1 ? 1 : brushWidth - brushWidthIncr;
				break;
			case KeyEvent.VK_F:
				r = color.getRed() + 1;
				color = new Color(r > 255 ? 255 : r, color.getGreen(), color.getBlue());
				break;
			case KeyEvent.VK_G:
				g = color.getGreen() + colorIncr;
				color = new Color(color.getRed(), g > 255 ? 255 : g, color.getBlue());
				break;
			case KeyEvent.VK_H:
				b = color.getBlue() + colorIncr;
				color = new Color(color.getRed(), color.getGreen(), b > 255 ? 255 : b);
				break;
			case KeyEvent.VK_V:
				r = color.getRed() - colorIncr;
				color = new Color(r < 0 ? 0 : r, color.getGreen(), color.getBlue());
				break;
			case KeyEvent.VK_B:
				g = color.getGreen() - colorIncr;
				color = new Color(color.getRed(), g < 0 ? 0 : g, color.getBlue());
				break;
			case KeyEvent.VK_N:
				b = color.getBlue() - colorIncr;
				color = new Color(color.getRed(), color.getGreen(), b < 0 ? 0 : b);
				break;
			case KeyEvent.VK_O:
				openFile();
				break;
			case KeyEvent.VK_S:
				saveFile();
				break;
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
