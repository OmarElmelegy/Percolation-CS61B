import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int[][] cells;
    private WeightedQuickUnionUF sets;
    private WeightedQuickUnionUF setsfull;
    private int numberOfOpenSites;
    private static final int VIRTUAL_TOP = 0;
    private int virtualBottom;

    public Percolation(int n) {
        this.n = n;

        this.cells = new int[n][n];
        this.numberOfOpenSites = 0;
        this.sets = new WeightedQuickUnionUF(n * n + 2);
        this.setsfull = new WeightedQuickUnionUF(n * n + 1);
        this.virtualBottom = n * n + 1;
    }

    private int matrixMapper(int row, int col) {
        validate(row, col);
        return (row * n + col + 1);
    }

    public void open(int row, int col) {

        validate(row, col);

        if (cells[row][col] == 1) {
            return;
        }

        cells[row][col] = 1;
        numberOfOpenSites++;

        int current = matrixMapper(row, col);

        // Connect to virtual top if in first row
        if (row == 0) {
            sets.union(current, VIRTUAL_TOP);
            setsfull.union(current, VIRTUAL_TOP);
        }

        // Connect to virtual bottom if in last row
        if (row == n - 1) {
            sets.union(current, virtualBottom);
            // NO setsFull — no virtual bottom in this UF
        }

        // Connect to open neighbors
        if (row > 0 && isOpen(row - 1, col)) {
            sets.union(current, matrixMapper(row - 1, col));
            setsfull.union(current, matrixMapper(row - 1, col));
        }
        if (row < n - 1 && isOpen(row + 1, col)) {
            sets.union(current, matrixMapper(row + 1, col));
            setsfull.union(current, matrixMapper(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            sets.union(current, matrixMapper(row, col - 1));
            setsfull.union(current, matrixMapper(row, col - 1));
        }
        if (col < n - 1 && isOpen(row, col + 1)) {
            sets.union(current, matrixMapper(row, col + 1));
            setsfull.union(current, matrixMapper(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return (cells[row][col] == 1);
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int cell = matrixMapper(row, col);

        return (isOpen(row, col) && (setsfull.find(cell) == setsfull.find(VIRTUAL_TOP)));
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return sets.find(VIRTUAL_TOP) == sets.find(virtualBottom);
    }

    private boolean validate(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new RuntimeException("Out of window size");
        }

        else {
            return true;
        }
    }
}
