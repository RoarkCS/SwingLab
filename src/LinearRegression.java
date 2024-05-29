import java.util.ArrayList;

public class LinearRegression {
    private double slope;
    private double intercept;

    public void fit(ArrayList<Point> data){
        int n = data.size();

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (Point datum : data) {
            sumX += datum.getX();
            sumY += datum.getY();
            sumXY += datum.getX() * datum.getY();
            sumX2 += datum.getX() * datum.getX();
        }

        slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);

        intercept = (sumY - slope * sumX) / n;
    }

    public double predict(double x) {
        return slope * x + intercept;
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }
}
