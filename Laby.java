import java.util.ArrayList;

public class Laby {

	public static void main(String[] args) {
		Grid a = makeLaby(4, 4);
		a.showGrid();
	}

	public static Grid makeLaby(int n, int m) {
		Grid grid = new Grid(n, m);
		while (!find(grid)) {
			int[] permutation = permutation(n * m);
			for (int i = 0; i < permutation.length; i++) {
				Cellule c = grid.cell[permutation[i] / grid.columns][permutation[i] % grid.columns];
				ArrayList<Integer> directionVoisin = auMoinsUnVoisinNonConnecte(c);
				if (directionVoisin.size() != 0) {
					int direction = directionVoisin.get((int) (Math.random() * directionVoisin.size()));
					union(c, direction, grid);
				}
			}
		} /* la grille a deja le mur de l'entree et de la sortie casse */
		return grid;
	}

	public static Grid makeLabyA(int n, int m) {
		Grid grid = new Grid(n, m);
		while (!find(grid)) {
			for (int i = 0; i < grid.rows; i++) {
				for (int j = 0; j < grid.columns; j++) {
					Cellule c = grid.cell[i][j];
					ArrayList<Integer> directionVoisin = auMoinsUnVoisinNonConnecte(c);
					if (directionVoisin.size() != 0) {
						int direction = directionVoisin.get((int) (Math.random() * directionVoisin.size()));
						union(c, direction, grid);
					}
				}
			}
		} /* la grille a deja le mur de l'entree et de la sortie casse */
		return grid;
	}

	public static int[] permutation(int n) {
		int p[] = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		for (int i = 0; i < n; i++) {
			int j = (int) (Math.random() * (n - i) + i);
			int tmp = p[i];
			p[i] = p[j];
			p[j] = tmp;
		}
		return p;
	}

	public static boolean find(Grid alpha) {
		/*
		 * savoir si il y a qu'un groupe connexe
		 */
		for (int i = 0; i < alpha.rows; i++) {
			for (int j = 0; j < alpha.columns; j++) {
				if (alpha.cell[i][j].getId() != alpha.cell[0][0].getId()) {
					return false;
				}
			}
		}
		return true;
	}

	public static ArrayList<Integer> auMoinsUnVoisinNonConnecte(Cellule c) {
		ArrayList<Integer> voisin = new ArrayList<Integer>();
		for (int v = 0; v < 6; v++) {// il y a que 6 voisins
			if (c.hasNeighbor(v) && c.getNeighbor(v).isWall() && (c.getId() != c.getNeighborId(v))) {
				/*
				 * si il y a une cellule est un voisin, si il y a un mur entre
				 * les deux et si ils sont pas dans le meme groupe connexe
				 */
				voisin.add(v);
			}
		}
		return voisin;
	}

	public static void union(Cellule c, int direction, Grid alpha) {
		c.breakWallWith(direction);
		changeIdGroupe(c, alpha, new ArrayList<Cellule>());
	}
	
	/*Changer l'id de tous les groupes connexes*/
	public static void changeIdGroupe(Cellule c, Grid alpha, ArrayList<Cellule> a) {
		if (c.getId() < 0 || a.contains(c)) {
			return;
		}
		a.add(c);
		for (int v = 0; v < 6; v++) {
			if (c.hasNeighbor(v) && !c.getNeighbor(v).isWall()) {
				c.getNeighbor(v).getCell().setId(c.getId());
				changeIdGroupe(c.getNeighbor(v).getCell(), alpha, a);
			}
		}
	}

}
