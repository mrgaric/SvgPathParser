package com.igordubrovin.svgpathparser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class SvgView extends View {
    Path path;
    public Paint paint;
    int countColor;

    public SvgView(Context context, AttributeSet attributeSet) throws Exception {
        super(context, attributeSet);
        path = new Path();
        paint = new Paint();
        paint.setStrokeWidth(3);
        countColor = Color.RED;
      //  paint.setStyle(Paint.Style.STROKE);
      //  paint.setStyle(Paint.Style.FILL_AND_STROKE);
      //  String test = "M200,200A100,50,0,0,0,400,400";
      //  String test = "M58.375,259.344v-76h526v-66.667h114.667v66.667h194v76h9.333v98.667h-9.333v72H58.375v-72H45.708v-98.667H58.375z";
      //  parseStrSvg(test);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        countColor = paint.getColor() - 100000;
        if (countColor <= -16777216) countColor = Color.RED;
        paint.setColor(countColor);
        canvas.drawPath(path, paint);
    }

    public void parseStrSvg(String strSvg) throws Exception {
        List<String> itemSvgStr = new ArrayList<>();
        int countItemSvgStr = 0;
        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float currentPointX = 0;
        float currentPointY = 0;
        float intermediatePointX = 0;
        float intermediatePointY = 0;
        float angle;
        boolean largeArcFlag;
        boolean sweepFlag;
        path.reset();

        //разбор строки на состовляющие элементы
        int j = 0;
        if (strSvg.charAt(0) == 'M' || strSvg.charAt(0) == 'm') {
            for (int i = 0; i < strSvg.length(); i++) {
                if (checkItemSvg(strSvg.charAt(i))) {
                    if (i > j) {
                        itemSvgStr.add(strSvg.substring(j, i));
                    }
                    if (strSvg.charAt(i) != ',') itemSvgStr.add(strSvg.substring(i, i + 1));
                    j = i + 1;
                }
            }
            if (strSvg.charAt(strSvg.length() - 1) != 'z') {
                itemSvgStr.add(strSvg.substring(j));
            }
        }else throw new Exception();

        //добавление элемента в path
        while (countItemSvgStr < itemSvgStr.size()){
            switch (itemSvgStr.get(countItemSvgStr++)){
                case "M":
                    x1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    currentPointY = y1;
                    path.moveTo(x1, y1);
                    break;
                case "m":
                    x1 = 0 + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = 0 + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    currentPointY = y1;
                    path.moveTo(x1, y1);
                    break;
                case "L":
                    x1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    currentPointY = y1;
                    path.lineTo(x1,y1);
                    break;
                case "l":
                    x1 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = currentPointY + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    currentPointY = y1;
                    path.lineTo(x1,y1);
                    break;
                case "H":
                    x1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    path.lineTo(x1, currentPointY);
                    break;
                case "h":
                    x1 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x1;
                    path.lineTo(x1, currentPointY);
                    break;
                case "V":
                    y1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointY = y1;
                    path.lineTo(currentPointX, y1);
                    break;
                case "v":
                    y1 = currentPointY + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointY = y1;
                    path.lineTo(currentPointX, y1);
                    break;
                case "A":
                    x1 = currentPointX;
                    y1 = currentPointY;
                    x2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    angle = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    largeArcFlag = itemSvgStr.get(countItemSvgStr++).equals("1");
                    sweepFlag = itemSvgStr.get(countItemSvgStr++).equals("1");
                    x3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    arcTo(x1, y1, x2, y2, angle, largeArcFlag, sweepFlag, x3, y3);
                    break;
                case "a":
                    x1 = currentPointX;
                    y1 = currentPointY;
                    x2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    angle = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    largeArcFlag = itemSvgStr.get(countItemSvgStr++).equals("1");
                    sweepFlag = itemSvgStr.get(countItemSvgStr++).equals("1");
                    x3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    arcTo(x1, y1, x2, y2, angle, largeArcFlag, sweepFlag, x3, y3);
                    break;
                case "C":
                    x1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    intermediatePointX = x2;
                    intermediatePointY = y2;
                    path.cubicTo(x1, y1, x2, y2, x3, y3);
                    break;
                case "c":
                    x1 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = currentPointY + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    intermediatePointX = x2;
                    intermediatePointY = y2;
                    path.cubicTo(x1, y1, x2, y2, x3, y3);
                    break;
                case "S":
                    x1 = currentPointX + (currentPointX - intermediatePointX);
                    y1 = currentPointY + (currentPointY - intermediatePointY);
                    x2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    intermediatePointX = x2;
                    intermediatePointY = y2;
                    path.cubicTo(x1, y1, x2, y2, x3, y3);
                    break;
                case "s":
                    x1 = currentPointX + (currentPointX - intermediatePointX);
                    y1 = currentPointY + (currentPointY - intermediatePointY);
                    x2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y3 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x3;
                    currentPointY = y3;
                    intermediatePointX = x2;
                    intermediatePointY = y2;
                    path.cubicTo(x1, y1, x2, y2, x3, y3);
                    break;
                case "Q":
                    x1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x2;
                    currentPointY = y2;
                    intermediatePointX = x1;
                    intermediatePointY = y1;
                    path.quadTo(x1, y1, x2, y2);
                    break;
                case "q":
                    x1 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y1 = currentPointY + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    x2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x2;
                    currentPointY = y2;
                    intermediatePointX = x1;
                    intermediatePointY = y1;
                    path.quadTo(x1, y1, x2, y2);
                    break;
                case "T":
                    x1 = currentPointX + (currentPointX - intermediatePointX);
                    y1 = currentPointY + (currentPointY - intermediatePointY);
                    x2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x2;
                    currentPointY = y2;
                    intermediatePointX = x1;
                    intermediatePointY = y1;
                    path.quadTo(x1, y1, x2, y2);
                    break;
                case "t":
                    x1 = currentPointX + (currentPointX - intermediatePointX);
                    y1 = currentPointY + (currentPointY - intermediatePointY);
                    x2 = currentPointX + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    y2 = currentPointY + Float.valueOf(itemSvgStr.get(countItemSvgStr++));
                    currentPointX = x2;
                    currentPointY = y2;
                    intermediatePointX = x1;
                    intermediatePointY = y1;
                    path.quadTo(x1, y1, x2, y2);
                    break;
                case "z":
                    path.close();
                    break;
            }
            if (countItemSvgStr < itemSvgStr.size()) {
                if (!checkItemSvg(itemSvgStr.get(countItemSvgStr).charAt(0))){
                    path.reset();
                    throw new Exception();
                }
            }
            if(itemSvgStr.get(itemSvgStr.size()-1).equals("z")) paint.setStyle(Paint.Style.FILL);
            else paint.setStyle(Paint.Style.STROKE);
        }
    }

    private static boolean checkItemSvg(char itemSvg)
    {
        return  (itemSvg == ',' || itemSvg == 'M' || itemSvg == 'm' || itemSvg == 'L' || itemSvg == 'l' || itemSvg == 'H' || itemSvg == 'h'
                || itemSvg == 'V' || itemSvg == 'v' || itemSvg == 'A' || itemSvg == 'a' || itemSvg == 'C' || itemSvg == 'c'
                || itemSvg == 'S' || itemSvg == 's' || itemSvg == 'Q' || itemSvg == 'q' || itemSvg == 'T' || itemSvg == 't' || itemSvg == 'z');
    }

    public void arcTo(double x0, double y0, double rx, double ry, double angle, boolean largeArcFlag, boolean sweepFlag, double x, double y) {
        // Ensure radii are valid
        double dx2 = (x0 - x) / 2.0;
        double dy2 = (y0 - y) / 2.0;
        // Convert angle from degrees to radians
        angle = Math.toRadians(angle % 360.0);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        //
        // Step 1 : Compute (x1, y1)
        //
        double x1 = (cosAngle * dx2 + sinAngle * dy2);
        double y1 = (-sinAngle * dx2 + cosAngle * dy2);
        // Ensure radii are large enough
        rx = Math.abs(rx);
        ry = Math.abs(ry);
        double Prx = rx * rx;
        double Pry = ry * ry;
        double Px1 = x1 * x1;
        double Py1 = y1 * y1;
        // check that radii are large enough
        double radiiCheck = Px1/Prx + Py1/Pry;
        if (radiiCheck > 1) {
            rx = Math.sqrt(radiiCheck) * rx;
            ry = Math.sqrt(radiiCheck) * ry;
            Prx = rx * rx;
            Pry = ry * ry;
        }

        //
        // Step 2 : Compute (cx1, cy1)
        //
        double sign = (largeArcFlag == sweepFlag) ? -1 : 1;
        double sq = ((Prx*Pry)-(Prx*Py1)-(Pry*Px1)) / ((Prx*Py1)+(Pry*Px1));
        sq = (sq < 0) ? 0 : sq;
        double coef = (sign * Math.sqrt(sq));
        double cx1 = coef * ((rx * y1) / ry);
        double cy1 = coef * -((ry * x1) / rx);

        //
        // Step 3 : Compute (cx, cy) from (cx1, cy1)
        //
        double sx2 = (x0 + x) / 2.0;
        double sy2 = (y0 + y) / 2.0;
        double cx = sx2 + (cosAngle * cx1 - sinAngle * cy1);
        double cy = sy2 + (sinAngle * cx1 + cosAngle * cy1);

        //
        // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
        //
        double ux = (x1 - cx1) / rx;
        double uy = (y1 - cy1) / ry;
        double vx = (-x1 - cx1) / rx;
        double vy = (-y1 - cy1) / ry;
        double p, n;
        // Compute the angle start
        n = Math.sqrt((ux * ux) + (uy * uy));
        p = ux; // (1 * ux) + (0 * uy)
        sign = (uy < 0) ? -1.0 : 1.0;
        double angleStart = Math.toDegrees(sign * Math.acos(p / n));

        // Compute the angle extent
        n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
        p = ux * vx + uy * vy;
        sign = (ux * vy - uy * vx < 0) ? -1.0 : 1.0;
        double angleExtent = Math.toDegrees(sign * Math.acos(p / n));
        if(!sweepFlag && angleExtent > 0) {
            angleExtent -= 360f;
        } else if (sweepFlag && angleExtent < 0) {
            angleExtent += 360f;
        }
        angleExtent %= 360f;
        angleStart %= 360f;

        path.addArc((float) (cx-rx), (float) (cy-ry), (float)(cx+rx), (float)(cy+ry), (float)-angleStart, (float)-angleExtent);
    }
}
