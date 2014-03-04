import java.io.File;
import java.io.FileNotFoundException;
import java.math.*;
import java.util.Scanner;

/*
 * Octree.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log$
 */

/**
 * Implementation of Octree.
 *
 * @author Siddesh Pillai
 * 
 */


public class Octree {

	public Point root;
	public Octree childNodes[];
	boolean parent;
	public int depth;

	public double dimensions;

	/**
	 * Parameterized Constructor.
	 * 
	 * @param x
	 * @param Point.
	 * 
	 */
	public Octree(double x, Point p) {
		this.root = p;
		this.dimensions = x;
	}

	/**
	 * Default.
	 */
	public Octree() {
		root = null;
	}

	public int i = 0;

	/**
	 * Add the points to the octree.
	 * 
	 * @param x
	 * @param y
	 * @param z           
	 * 
	 * @return boolean indicating whether the point was added or not.
	 */

	public boolean add_nodes(double x, double y, double z) {

		boolean success = false;

		if (this.i < 8) {
			if (compare(x, y, z)) {
				if (root.childPoints == null) {
					System.out.println("Root is null" + x + " " + y + " " + z);
				}

				this.root.childPoints[i] = new Point(x, y, z);
				this.i++;
				success = true;
			}
		}

		else if (this.childNodes == null) {

			this.create_nodes_of_nodes(x, y, z);

			for (int j = 0; j < 8; j++) {
				double tempx = this.root.x - root.childPoints[j].x;
				double tempy = this.root.y - root.childPoints[j].y;
				double tempz = this.root.z - root.childPoints[j].z;

				int checker = compareValues(tempx, tempy, tempz);

				this.childNodes[checker].add_nodes(root.childPoints[j].x,
						root.childPoints[j].y, root.childPoints[j].z);

			}
			root.childPoints = null;

			double tempx = this.root.x - x;
			double tempy = this.root.y - y;
			double tempz = this.root.z - z;
			int checker = compareValues(tempx, tempy, tempz);
			this.childNodes[checker].add_nodes(x, y, z);
			// this.i=0;
		}

		else {

			if (childNodes != null) {
				int checker = compareValues(x, y, z);
				childNodes[checker].add_nodes(x, y, z);

			}
		}

		return success;
	}

	/**
	 * Compare the values for which we need to add to the octant.
	 * 
	 * @param x
	 * @param y
	 * @param z           
	 * 
	 * @return the index(0-7) to which we need to insert
	 * the point in the octant
	 */
	public int compareValues(double x, double y, double z) {

		if (x > 0 && y > 0 && z > 0)
			return 0;
		else if (x > 0 && y > 0 && z < 0)
			return 1;
		else if (x > 0 && y < 0 && z > 0)
			return 2;
		else if (x > 0 && y < 0 && z < 0)
			return 3;
		else if (x < 0 && y > 0 && z > 0)
			return 4;
		else if (x < 0 && y > 0 && z < 0)
			return 5;
		else if (x < 0 && y < 0 && z > 0)
			return 6;
		else
			return 7;

	}

	/**
	 * Compare the values for range check -2.0 to 2.0.
	 * 
	 * @param x
	 * @param y
	 * @param z           
	 * 
	 * @return boolean indicating the bound check.
	 */
	public boolean compare(double x, double y, double z) {
		if (x <= 2.0 && x >= -2.0) {
			if (y <= 2.0 && y >= -2.0) {
				if (z <= 2.0 && z >= -2.0) {
					return true;
				}
			}
		}
		return true;
	}

	// dimension/2 power depth

	public double makeDimensions(double dimensions) {
		return (this.dimensions) / (2 ^ depth);
	}

	/**
	 * Create nodes of nodes.
	 * 
	 * @param x
	 * @param y
	 * @param z           
	 * 
	 * @return the boolean indicating success 
	 */
	public boolean create_nodes_of_nodes(double x, double y, double z) {

		this.childNodes = new Octree[8];
		boolean success = false;

		this.childNodes[0] = new Octree(this.dimensions / 2, new Point(
				this.root.x + dimensions / 2, this.root.y + dimensions / 2,
				this.root.z + dimensions / 2));

		this.childNodes[1] = new Octree(this.dimensions / 2, new Point(
				this.root.x + dimensions / 2, this.root.y + dimensions / 2,
				this.root.z - dimensions / 2));

		this.childNodes[2] = new Octree(this.dimensions / 2, new Point(
				this.root.x + dimensions / 2, this.root.y - dimensions / 2,
				this.root.z + dimensions / 2));

		this.childNodes[3] = new Octree(this.dimensions / 2, new Point(
				this.root.x + dimensions / 2, this.root.y - dimensions / 2,
				this.root.z - dimensions / 2));

		this.childNodes[4] = new Octree(this.dimensions / 2, new Point(
				this.root.x - dimensions / 2, this.root.y + dimensions / 2,
				this.root.z + dimensions / 2));

		this.childNodes[5] = new Octree(this.dimensions / 2, new Point(
				this.root.x - dimensions / 2, this.root.y + dimensions / 2,
				this.root.z - dimensions / 2));

		this.childNodes[6] = new Octree(this.dimensions / 2, new Point(
				this.root.x - dimensions / 2, this.root.y - dimensions / 2,
				this.root.z + dimensions / 2));

		this.childNodes[7] = new Octree(this.dimensions / 2, new Point(
				this.root.x - dimensions / 2, this.root.y - dimensions / 2,
				this.root.z - dimensions / 2));

		return success;

	}

	/**
	 * Printing the tree using breadth first search.
	 */
	public void print_tree() {

		int depth = (int) (Math.log(2 / dimensions) / Math.log(2));

		for (int k = 0; k < depth; k++) {
			System.out.print("   ");
		}

		System.out.println("(" + root.x + ", " + root.y + ", " + root.z + ")"
				+ "  -- " + dimensions);

		if (this.childNodes == null) {
			for (int j = 0; j < i; j++) {
				for (int k = 0; k < depth; k++) {
					System.out.print("   ");
				}
				System.out.println("  *  (" + this.root.childPoints[j].x + ", "
						+ this.root.childPoints[j].y + ", "
						+ this.root.childPoints[j].z + ")");
			}
		} else {
			for (int j = 0; j < i; j++) {
				this.childNodes[j].print_tree();
			}
		}
	}

	/*
	 * The main method
	 */
	public static void main(String args[]) throws FileNotFoundException {
		/*
		 * Octree object initially we set the root as (0,0,0)
		 */
		Octree a = new Octree(2d, new Point(0d, 0d, 0d));

		File file = new File(args[0]); // Read from command line
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			double x, y, z;
			x = Double.parseDouble(sc.next());
			y = Double.parseDouble(sc.next());
			z = Double.parseDouble(sc.next());
			a.add_nodes(x, y, z);
		}

		sc.close();
		a.print_tree(); //Print the tree

	}
}
