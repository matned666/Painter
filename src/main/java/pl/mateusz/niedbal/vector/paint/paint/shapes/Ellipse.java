package pl.mateusz.niedbal.vector.paint.paint.shapes;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse  extends Shape{

    private  double startX;
    private  double startY;
    private  double endX;
    private  double endY;

    public Ellipse(double startX, double startY, double endX, double endY) {
        this.startX = Math.min(startX,endX);
        this.startY = Math.min(startY,endY);
        this.endX = Math.abs(endX-startX);
        this.endY = Math.abs(endY-startY);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.fillOval(startX,startY,endX,endY);
        context.strokeOval(startX,startY,endX,endY);
    }

    @Override
    public String getData() {
        StringBuilder builder = new StringBuilder();
        builder.append("Ellipse;");
        builder.append(startX).append(";");
        builder.append(startY).append(";");
        builder.append(endX).append(";");
        builder.append(endY).append(";");
        builder.append(fillColor).append(";");
        builder.append(strokeColor).append(";");
        return builder.toString();
    }
}
