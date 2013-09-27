import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * A class that represents complex numbers. A complex number is represented by a
 * Real and Imaginary part. The class allows separate obtaining and modifying
 * those parts, in addition to certain operations, such as getting the square of
 * the complex number, the square of the modulus, and an add operation.
 * 
 * @author Zacharias Markakis
 * 
 */
public class Complex {
	private double real, imaginary;

	/**
	 * Default constructor. Initialises to 0.
	 */
	public Complex() {
		real = 0.0;
		imaginary = 0.0;
	}

	/**
	 * Constructor that initialises to given complex number.
	 * 
	 * @param real
	 *            Initialises the real part to this number.
	 * @param imaginary
	 *            Initialises the imaginary part to this number.
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Changes the real part of the complex number.
	 * 
	 * @param real
	 *            The new value of the real part.
	 */
	public void setReal(double real) {
		this.real = real;
	}

	/**
	 * Changes the imaginary part of the complex number.
	 * 
	 * @param imaginary
	 *            The new value of the imaginary part.
	 */
	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}

	/**
	 * Returns the real part of the complex number.
	 * 
	 * @return The real part of the complex number.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Returns the imaginary part of the complex number.
	 * 
	 * @return The imaginary part of the complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * Calculates the square of the complex number and returns it as a new
	 * object.
	 * 
	 * @return A new Complex object containing the square of the complex number.
	 */
	public Complex square() {
		return new Complex(real * real - imaginary * imaginary, imaginary
				* real + real * imaginary);
	}

	/**
	 * Returns the square of the modulus of the complex number.
	 * 
	 * @return The square of the modulus of the complex number.
	 */
	public double modulusSquared() {
		return (real * real + imaginary * imaginary);
	}

	/**
	 * Method to add two complex numbers.
	 * 
	 * @param c
	 *            First complex number to be added.
	 * @param d
	 *            Second complex number to be added.
	 * @return The result of the addition in a new Complex object.
	 */
	public static Complex add(Complex c, Complex d) {
		return new Complex(c.getReal() + d.getReal(), c.getImaginary()
				+ d.getImaginary());
	}

	public static void main(String[] args) {
		FractalFrame test = new FractalFrame("Hello");
		test.init();
	}
}

class FractalFrame extends JFrame {
	private JTextField iterationsField, minXField, maxXField, minYField, maxYField;
	private JLabel userPoint = new JLabel("0 + 0i");
	private Complex userSelectedPoint;
	private double stepX;
	private double stepY;
    private JuliaPanel juliaImage;
    
	public FractalFrame(String title) {
		super(title);
		iterationsField = new JTextField("100", 20);
		minXField = new JTextField("-2", 20);
		maxXField = new JTextField("2", 20);
		minYField = new JTextField("-1.6", 20);
		maxYField = new JTextField("1.6", 20);
		userSelectedPoint = new Complex();
	}

	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(850, 750);

		MandelbrotPanel mandelbrotImage = new MandelbrotPanel();
		juliaImage = new JuliaPanel();
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		JPanel dataInput = new JPanel();
		dataInput.setLayout(new GridLayout(1, 2));
		JPanel rangeInput = new JPanel();
		rangeInput.setLayout(new GridLayout(4, 4));
		// JLabel testLabel = new JLabel("Testing");
		rangeInput.add(new JLabel("Min X Axis Plot"));
		rangeInput.add(minXField);
		rangeInput.add(new JLabel("Max X Axis Plot"));
		rangeInput.add(maxXField);
		rangeInput.add(new JLabel("Min Y Axis Plot"));
		rangeInput.add(minYField);
		rangeInput.add(new JLabel("Max Y Axis Plot"));
		rangeInput.add(maxYField);
		JPanel iterationsInput = new JPanel();
		iterationsInput.setLayout(new GridLayout(2, 2));
		iterationsInput.add(new JLabel("Max Iterations"));
		// iterationsInput.add(new JLabel(""));
		iterationsInput.add(iterationsField);
		iterationsInput.add(new JLabel("User Selecter Point"));
		iterationsInput.add(userPoint);
		dataInput.add(rangeInput);
		dataInput.add(iterationsInput);
		dataPanel.add(dataInput, BorderLayout.CENTER);
		JButton draw = new JButton("Draw");
		draw.addActionListener(mandelbrotImage);
		iterationsField.addActionListener(mandelbrotImage);
		mandelbrotImage.addMouseListener(mandelbrotImage);
		dataPanel.add(draw, BorderLayout.SOUTH);
		Container p = getContentPane();
		p.setLayout(new BorderLayout());
		p.add(dataPanel, BorderLayout.SOUTH);
		JPanel panels = new JPanel(new GridLayout(1,2));
		
		Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
		
		mandelbrotImage.setBorder(border);
		juliaImage.setBorder(border);
		
		panels.add(mandelbrotImage);
		panels.add(juliaImage);
		
		p.add(panels, BorderLayout.CENTER);
		setVisible(true);
		//test.draw();

	}

	class MandelbrotPanel extends JPanel implements ActionListener,
			MouseListener {
		private int x = 0, y = 0, width = 0, height = 0;
		private double minX, minY, maxX, maxY;
		private Color color;
		BufferedImage image = null;
		Graphics graphics;

		public MandelbrotPanel() {
			super();
			/*
			 * minX = -2.0; minY = -1.6; maxX = 2.0; maxY = 1.6;
			 */
		}

		public void paintComponent(Graphics g) {
			g.setColor(Color.black);
			if (image == null)
				g.fillRect(0, 0, getWidth(), getHeight());		
			else
				g.drawImage(image, 0, 0, null);
		}

		public int mandelbrot(int maxIterations, Complex c) {
			Complex d = new Complex(c.getReal(), c.getImaginary());
			int noOfIterations;

			for (noOfIterations = 0; (d.modulusSquared() < 4)
					&& (noOfIterations < maxIterations); noOfIterations++)
				d = Complex.add(d.square(), c);

			return noOfIterations;
		}

		public void draw() {
			
			final int maxIterations = Integer
					.parseInt(iterationsField.getText());
			final Complex c = new Complex(minX, maxY);
			minX = Double.parseDouble(minXField.getText());
			minY = Double.parseDouble(minYField.getText());
			maxX = Double.parseDouble(maxXField.getText());
			maxY = Double.parseDouble(maxYField.getText());
			width = getWidth();
			height = getHeight();

			stepX = Math.abs(maxX - minX) / width;
			stepY = Math.abs(maxY - minY) / height;
			//width = (int) Math.ceil((Math.abs(maxX - minX) / step));
			//height = (int) Math.ceil((Math.abs(maxY - minY) / step));
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			graphics = image.getGraphics();
			

			class ImageDrawer extends SwingWorker<BufferedImage, Void> {
				int iterations = 0;
				@Override
				protected BufferedImage doInBackground() throws Exception {
					for (double curX = minX; curX <= maxX; curX += stepX) {
						for (double curY = maxY; curY >= minY; curY -= stepY) {
							c.setReal(curX);
							c.setImaginary(curY);
							iterations = mandelbrot(maxIterations, c);
							color = Color.getHSBColor((float) iterations
									/ maxIterations, 1F, 1F);
							if (iterations == maxIterations) {
								color = Color.black;
							}
							graphics.setColor(color);
							graphics.drawRect(x, y, 0, 0);
							y++;
						}
						x++;
						y = 0;
					}
					return null;
				}
				
				@Override
				protected void done() {
					repaint();
				}
				
			}
			
			(new ImageDrawer()).execute();
			
			/*
			for (double curX = minX; curX <= maxX; curX += stepX) {
				for (double curY = maxY; curY >= minY; curY -= stepY) {
					c.setReal(curX);
					c.setImaginary(curY);
					iterations = mandelbrot(maxIterations, c);
					color = Color.getHSBColor((float) iterations
							/ maxIterations, 1F, 1F);
					if (iterations == maxIterations) {
						color = Color.black;
					}
					graphics.setColor(color);
					graphics.drawRect(x, y, 0, 0);
					y++;
				}
				x++;
				y = 0;
			}
			repaint();
			*/
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			draw();
			x = 0;
			y = 0;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// realPoint = minX + arg0.getX()*step;
			// imaginaryPoint = maxY-arg0.getY()*step;
			userSelectedPoint.setReal(minX + arg0.getX() * stepX);
			userSelectedPoint.setImaginary(maxY - arg0.getY() * stepY);

			if (image != null) {
				if (userSelectedPoint.getImaginary() >= 0.0)
					userPoint.setText(userSelectedPoint.getReal() + " + "
							+ userSelectedPoint.getImaginary() + "i");
				else
					userPoint.setText(userSelectedPoint.getReal() + " "
							+ userSelectedPoint.getImaginary() + "i");
				juliaImage.draw(userSelectedPoint);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	class JuliaPanel extends JPanel {
		
		private int x = 0, y = 0, width = 0, height = 0;
		private double minX, minY, maxX, maxY;
		private Color color;
		BufferedImage image = null;
		Graphics graphics;
		
		public JuliaPanel()
		{
			super();
			minX = -2.0;
			minY = -1.6;
			maxX = 2.0;
			maxY = 1.6;
			image = null;
		}
		
		public void paintComponent(Graphics g) {
			g.setColor(Color.blue);
			if (image == null)
				g.fillRect(0, 0, 1000, 1000);
			else
				g.drawImage(image, 0, 0, null);
		}
		
		public int julia(int maxIterations, Complex c, Complex fixedPoint) {
			Complex d = new Complex(c.getReal(), c.getImaginary());
			int noOfIterations;

			for (noOfIterations = 0; (d.modulusSquared() < 4)
					&& (noOfIterations < maxIterations); noOfIterations++)
				d = Complex.add(d.square(), fixedPoint);

			return noOfIterations;
		}
		
		public void draw(Complex fixedPoint) {
			int iterations = 0, maxIterations = Integer
					.parseInt(iterationsField.getText());
			Complex c = new Complex(minX, maxY);
			width = getWidth();
			height = getHeight();

			stepX = Math.abs(maxX - minX) / width;
			stepY = Math.abs(maxY - minY) / height;
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);

			for (double curX = minX; curX <= maxX; curX += stepX) {
				for (double curY = maxY; curY >= minY; curY -= stepY) {
					c.setReal(curX);
					c.setImaginary(curY);
					iterations = julia(maxIterations, c, fixedPoint);
					graphics = image.getGraphics();
					color = Color.getHSBColor((float) iterations
							/ maxIterations, 1F, 1F);
					if (iterations == maxIterations) {
						color = Color.black;
					}
					graphics.setColor(color);
					graphics.drawRect(x, y, 0, 0);
					y++;
				}
				x++;
				y = 0;
			}
			repaint();
			x=0;
			y=0;
		}
	}
}
