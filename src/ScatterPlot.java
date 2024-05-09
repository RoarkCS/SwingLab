public class ScatterPlot extends GraphPanel{


    public ScatterPlot(int[] X, int[] Y) {
        super(X, Y);
    }

    public ScatterPlot(int[] X, int[] Y, int camX, int camY) {
        super(X, Y, camX, camY);
    }

    public ScatterPlot(int[] X, int[] Y, int camX, int camY, int scaleX, int scaleY) {
        super(X, Y, camX, camY, scaleX, scaleY);
    }
}
