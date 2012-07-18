package dpec.master.chart;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class CurveChartView extends View{

    private int backColor = Color.BLACK;
    private int gridColor = 0xFF339933;
    private int[] curveColor = null;
    private int defaCurveColor = Color.YELLOW;
    private int textColor = 0xeeeeeeee;
    private int popupColor = 0xaa33ff33;
    private int selectLineColor = 0xaaaaaaaa;
    private int minHarizonLineCount = 3;
    private int maxHarizonLineCount = 10;
    private boolean showTips = true;
    private float minValue = 0;
    private float maxValue = 0;
    private int maxCeil = 0;
    private int minCeil = 0;
    private DataQueue[] dataList = null;
    private int gridPaddingLeft = 3;
    private int gridPaddingRight = 3;
    private int gridPaddingTop = 3;
    private int gridPaddingBottom = 3;
    private int gridStart = gridPaddingLeft;
    private int pointGapDef = 2;
    private float pointGap = pointGapDef;
    private boolean isStatic = true;
    
    private float[][] staticData = null;
    private float[] pointX = null;
    private int selectPoint = -1;
    private PointF lastPoint = null;
    private boolean sizeHaveSet = false;
    private boolean showCalibration = false;
    private boolean onLeft = false;
    private boolean setScope = false;
    private Context context = null;
    
    public CurveChartView(Context context){
        super(context);
        init(context);
    }
    
    public CurveChartView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }
    
    public CurveChartView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }
    
    private void init(Context context){
        this.context = context;
        gridPaddingLeft = toPixel(context,gridPaddingLeft);
        gridPaddingTop = toPixel(context,gridPaddingTop);
        gridPaddingRight = toPixel(context,gridPaddingRight);
        gridPaddingBottom = toPixel(context,gridPaddingBottom);
        pointGapDef = toPixel(context,pointGapDef);
    }
  
    public void setCurveStatic(boolean b){
        isStatic = b;
    }
  
    public void setCurveCount(int c){
        if(isStatic){
            staticData = new float[c][];           
        }else{
            dataList = new DataQueue[c];
        }
        curveColor = new int[c];
        for(int i = 0; i < c; i++){
            curveColor[i] = defaCurveColor;
        }
    }
    public void setDataScope(int minv,int maxv){
        setScope = true;
        minCeil = minv;
        maxCeil = maxv;
    }
    

    public void setCalibrationLeft(boolean b){
        onLeft = b;
    }

    public void setCalibrationOn(boolean b){
        showCalibration = b;
        gridPaddingTop = toPixel(context,10);
        gridPaddingBottom = toPixel(context,isStatic ? 10 : 25);
    }
    

    public void setBackgroundColor(int color){
        backColor = color;   
    }
    

    public void setGridColor(int color){
        gridColor = color;
    }

    public void setMaxHarizonLineCount(int c)
    {
        maxHarizonLineCount = c;
    }
 
    public void setCurveColor(int index,int Color){
        if(curveColor == null || curveColor.length <= index)
            return;
        curveColor[index] = Color;
    }

    public void setCurveColor(int[] color){
        if(curveColor == null || color == null || curveColor.length != color.length)
            return;
        curveColor = color;
    }
 
    public void setCurveColor(int color){
        if((isStatic && curveColor == null) || (isStatic == false && dataList == null)){
            setCurveCount(1);
        }
        curveColor[0] = color;
    }
  
   
    public void setMaxCount(int size){
        if(isStatic || dataList == null)
            return;
        for(int i = 0; i < dataList.length; i++){
            if(dataList[i] == null)
                dataList[i] = new DataQueue();
            dataList[i].setSize(size);
        }
        pointGap = (getWidth() - getPaddingLeft() - getPaddingRight()) * 1.0f / size * 1.0f;
    }
    
  
    public void appendData(float data){
        if(isStatic)
            return;
        if(dataList == null){
            setCurveCount(1);
        }
        if(sizeHaveSet == false){
            int s = (getWidth() - getPaddingLeft() - getPaddingRight()) / pointGapDef;
            if(s > 1){
                for(int i = 0; i < dataList.length; i++){
                    if(dataList[i] == null)
                        dataList[i] = new DataQueue();
                    dataList[i].setSize(s);
                }
                sizeHaveSet = true;
                pointGap = pointGapDef;
            }
        }
        if(dataList[0] == null)
            dataList[0] = new DataQueue();
        if(maxValue < data)
            maxValue = data;
        if(minValue > data)
            minValue = data;
        dataList[0].add(new Float(data));
        if(setScope == false){
            maxCeil = (int)Math.ceil(maxValue);
            minCeil = (int)Math.floor(minValue);
        }
        invalidate();
    }
    
 
    public void appendData(float[] data){
        if(data == null || isStatic || dataList == null || dataList.length != data.length ){
            return;
        }
        if(sizeHaveSet == false){
            int s = (getWidth() - getPaddingLeft() - getPaddingRight()) / pointGapDef;
            if(s > 1){
                for(int i = 0; i < dataList.length; i++){
                    if(dataList[i] == null)
                        dataList[i] = new DataQueue();
                    dataList[i].setSize(s);
                }
                sizeHaveSet = true;
                pointGap = pointGapDef;
            }
        }
        for(int i = 0; i < data.length; i++){
            if(maxValue < data[i])
                maxValue = data[i];
            if(minValue > data[i])
                minValue = data[i];
            if(dataList[i] == null)
                dataList[i] = new DataQueue();
            dataList[i].add(new Float(data[i]));
        }
        if(setScope == false){
            maxCeil = (int)Math.ceil(maxValue);
            minCeil = (int)Math.floor(minValue);
        }
        invalidate();
    }
    
    

    public void setData(int index,float[] data){
        if(data == null || isStatic == false)
            return;
        if(staticData == null || staticData.length <= index)
            return;
        if(staticData[0] == null)
            index = 0;
        for(int i = 0; i < data.length; i++){
            if(maxValue < data[i]){
                maxValue = data[i];
            }
            if(minValue > data[i]){
                minValue = data[i];
            }
        }
        staticData[index] = data;
        if(setScope == false){
            maxCeil = (int)Math.ceil(maxValue);
            minCeil = (int)Math.floor(minValue);
        }
        pointX = null;
    }

    public void setData(float[] data){
        if(data == null || isStatic ==false)
            return;
        if(staticData != null && staticData.length > 1)
            return;
        
        if(staticData == null)
            setCurveCount(1);
        
        for(int i = 0; i < data.length; i++){
            if(maxValue < data[i]){
                maxValue = data[i];
            }
            if(minValue > data[i]){
                minValue = data[i];
            }
        }
        staticData[0] = data;
        if(setScope == false){
            maxCeil = (int)Math.ceil(maxValue);
            minCeil = (int)Math.floor(minValue);
        }
        pointX = null;
    }
 
    
    protected void onDraw(Canvas canvas) {
        
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        
        int height = getHeight() - paddingTop - paddingBottom;
        int width = getWidth() - paddingLeft - paddingRight;
        
        canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        canvas.clipRect(0,0,width,height);
        
        canvas.drawColor(backColor);
        
        Paint p = new Paint();
        float textw = 0;
        if(showCalibration){
            Rect rect = new Rect();
            String txt = String.valueOf(maxCeil)+"88";
            p.getTextBounds(txt, 0, txt.length(), rect);
            textw = rect.width();
            txt = String.valueOf(minCeil)+ "88";
            p.getTextBounds(txt, 0, txt.length(), rect);
            if(textw < rect.width())
                textw = rect.width();
            textw += 5;
        }
        
        float vsp = 1;
        
        int ceilValue = Math.abs(maxCeil - minCeil);
        if(ceilValue == 0)
            ceilValue = minHarizonLineCount;
        if(ceilValue > maxHarizonLineCount){
            vsp = (ceilValue * 1.0f) / maxHarizonLineCount;
            ceilValue = maxHarizonLineCount;
        }
        
        
        int vheight = height - gridPaddingTop - gridPaddingBottom;
        int vwidth = (int)(width - gridPaddingLeft - gridPaddingRight - textw);
        if(isStatic){
            if(staticData != null && staticData[0].length > 1)
                pointGap = (vwidth * 1.0f)/ (staticData[0].length - 1);
            else
                pointGap = pointGapDef;
        }else{
//            pointGap = pointGapDef;
        }
        
        
        float verticalSpace = (vheight * 1.0f) / (ceilValue * 1.0f);
        gridStart = gridPaddingLeft;
        

        Paint paint = new Paint();
        paint.setColor(textColor);
        FontMetrics fm = paint.getFontMetrics();
        if(onLeft){
            paint.setTextAlign(Paint.Align.RIGHT);
            gridStart += textw;
        }
        
        

        
        for(int i = 0; i <= ceilValue; i++){
            p.setColor(gridColor);
            canvas.drawLine(gridStart, gridPaddingTop + i * verticalSpace, gridStart + vwidth, gridPaddingTop + i * verticalSpace, p);
            if(showCalibration){
                if(i > 0 && i < ceilValue){
                    p.setColor(textColor);
                    String cs = String.format("%.1f", maxCeil - i * vsp);
                    if(cs.endsWith("0"))
                        cs = cs.substring(0,cs.length() - 2);
                    if(onLeft){
                        canvas.drawText(cs, gridStart, gridPaddingTop + i * verticalSpace - fm.ascent - toPixel(context,5), paint);
                    }else{
                        canvas.drawText(cs, gridPaddingLeft + vwidth + 1, gridPaddingTop + i * verticalSpace - fm.ascent- toPixel(context,5), paint);
                    }
                }
            }
        }
        p.setColor(gridColor);
        canvas.drawLine(gridStart, gridPaddingTop, gridStart, gridPaddingTop + vheight, p);
        canvas.drawLine(gridStart + vwidth, gridPaddingTop, gridStart + vwidth, gridPaddingTop + vheight, p);
        if(showCalibration){
            if(onLeft){
                canvas.drawText(String.valueOf(maxCeil), gridStart, gridPaddingTop -fm.ascent - toPixel(context,5), paint);
                canvas.drawText(String.valueOf(minCeil), gridStart, gridPaddingTop + vheight + toPixel(context,5) ,paint);
            }else{
                canvas.drawText(String.valueOf(maxCeil), gridPaddingLeft + vwidth + 1, gridPaddingTop -fm.ascent - toPixel(context,5),paint);
                canvas.drawText(String.valueOf(minCeil), gridPaddingLeft + vwidth + 1, gridPaddingTop + vheight + toPixel(context,5) ,paint);
            }
        }
        canvas.restore();
        if(isStatic)
            drawStaticCurve(canvas,vwidth,vheight,verticalSpace,vsp);
        else{
            drawDynamicCurve(canvas,vwidth,vheight,verticalSpace,vsp);
        }
        
    }
    
    private void drawDynamicCurve(Canvas canvas,int width,int height,float hsp,float hv){
        
        if(dataList != null && dataList[0] != null){
            canvas.save();
            canvas.translate(getPaddingLeft() + gridStart, getPaddingTop() + gridPaddingTop);
            canvas.clipRect(0,0,width,height + 1);
            Paint p = new Paint();
            int s = dataList[0].getListSize();
            float mx = minCeil;
            float mi = maxCeil;
            for(int i = 0; i < dataList.length; i++){
                if(dataList[i] == null)
                    continue;
                p.setColor(curveColor[i]);
                boolean flag = false;
                float px = 0; 
                float py = 0;
                float tmp = width - s * pointGap;
                if(tmp > 0){
                    for(int j = 0; j < s; j++){
                        float v = dataList[i].getData(j);
                        if(mx < v)
                            mx = v;
                        if(mi  > v)
                            mi = v;
                        float x =  width - j * pointGap;
                        float y =  height - ((minCeil - v ) * (-1.0f)* hsp)/ hv;
                        
                        if(flag){
                            canvas.drawLine(px, py, x, y, p);
                        }else{
                            flag = true;
                        }
                        px = x;
                        py = y; 
                    }
                }else{
                    int k = 0;
                    for(int j = s - 1; j >= 0; j--){
                        float v = dataList[i].getData(j);
                        if(mx < v)
                            mx = v;
                        if(mi  > v)
                            mi = v;
                        float x =  k * pointGap;
                        k++;
                        if(x > width)
                            break;
                        float y =  height - ((minCeil - v ) * (-1.0f)* hsp)/ hv;
                        
                        if(flag){
                            canvas.drawLine(px, py, x, y, p);
                        }else{
                            flag = true;
                        }
                        px = x;
                        py = y; 
                    }
                }
            }
            if(maxValue > mx)
                maxValue = mx;
            if(minValue < mi)
                minValue = mi;
            canvas.restore();
            FontMetrics fm = p.getFontMetrics();
            float tw = 0;
            float ty = gridPaddingTop + height + toPixel(context,15) - fm.ascent;
            float tx = gridPaddingLeft + toPixel(context,30);
            
            for(int i = 0; i < dataList.length; i++){
                if(dataList[i] == null)
                    continue;
                String tmp = String.valueOf(dataList[i].getData(s - 1));
                Rect r = new Rect();
                p.setColor(curveColor[i]);
                p.getTextBounds(tmp, 0, tmp.length(), r);
                tw += r.width();
                canvas.drawText(tmp, tx, ty, p);
                tx += tw + toPixel(context,10);
            }
        }
    }
    
    private void drawStaticCurve(Canvas canvas,int width,int height,float hsp,float hv){
        Paint p = new Paint();
        canvas.save();
        canvas.translate(getPaddingLeft() + gridStart, getPaddingTop() + gridPaddingTop);
        canvas.clipRect(0,0,width,height + 1);
        if(staticData != null && staticData[0] != null){  
            boolean po = false;
            int s = staticData[0].length;
            if(pointX == null){
                pointX = new float[s];
                po = true;
            }
            
            for(int k = 0; k < staticData.length; k++){
                p.setColor(curveColor[k]);   
                boolean flag = false;
                
                float px = 0;
                float py = 0;
                if(staticData[k] == null)
                    continue;
                for(int i = 0; i < s; i++){
                    float v = staticData[k][i];
                    float x = (i * pointGap);
                    float y =  height - ((minCeil - v ) * (-1.0f)* hsp)/ hv;
                    if(po)
                        pointX[i] = x;
                    
                    if(flag){
                        canvas.drawLine(px, py, x, y, p);
                    }else{
                        flag = true;
                    }
                    px = x;
                    py = y;  
                }
            }
        }
      
        
        if(showTips && selectPoint != -1){
            p.setColor(selectLineColor);
            canvas.drawLine(pointX[selectPoint], 0, pointX[selectPoint],  height, p);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setColor(popupColor);
            FontMetrics fm = paint.getFontMetrics();
            int bd = 2;
            float fms = fm.descent - fm.ascent;
            float x = width / 2 - 20;
            float y = gridPaddingTop + 10;
            Rect bounds = new Rect();
            for(int i = 0; i < staticData.length; i++){
                if(staticData[i] == null)
                    continue;
                String txt = String.valueOf(staticData[i][selectPoint]);
                paint.getTextBounds(txt, 0, txt.length(), bounds);  
                //canvas.drawRoundRect(rf, 3, 3, paint);
                p.setColor(curveColor[i]);
                canvas.drawText(txt, x, y - fm.ascent, p);
                y += fms + bd + 3;
            }
            selectPoint = -1;
        }
        canvas.restore();
    }
    
    
    
    private int getPointData(float x,float y){
        if(pointX != null && pointX.length > 2){
            float sp = pointX[1] - pointX[0];
            if(lastPoint != null){
                if(lastPoint.x > x){
                    for(int i = (int)lastPoint.y; i >=0; i--){
                        if(Math.abs(x - pointX[i]) <= sp){
                            lastPoint.x = x;
                            lastPoint.y = i;
                            return i;  
                        }
                    }
                }else if(lastPoint.x < x){
                    for(int i = (int)lastPoint.y; i < pointX.length; i++){
                        if(Math.abs(x - pointX[i]) <= sp){
                            lastPoint.x = x;
                            lastPoint.y = i;
                            return i;  
                        }
                    }
                }else{
                    return (int)lastPoint.y;
                }
            }else{
                for(int i = 0; i < pointX.length; i++){
                    if(Math.abs(x - pointX[i]) <= sp){
                        lastPoint = new PointF();
                        lastPoint.x = x;
                        lastPoint.y = i;
                        return i;  
                    }
                }
            }
            return -1;
        }else{
            return -1;
        }
    } 
    
    private boolean isDown = false;
    public boolean onTouchEvent(MotionEvent event) {
        if(showTips == false || isStatic == false)
            return false;
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(isDown){
                    handler.removeMessages(100); 
                    return true;
                }else{
                    isDown = true;
                }
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                selectPoint = getPointData(x,y);
                invalidate();
                isDown = false;
                return true;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(100, 5000);
                isDown = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                isDown = false;
                break;
        }
        return false;
    }
    
    private void reDraw(){
        invalidate();
    }
    
    private int toPixel(Context context, int dip) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dip, res.getDisplayMetrics());
        return (int)px;
    }
    
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            reDraw();
        }
    };
    
    private class DataQueue{
        private LinkedList<Float> list = new LinkedList<Float>();
        private int size = 1;
        
        public DataQueue(){}
    
        public void setSize(int s){
            if(s <= 1)
                return;
            if(size > s && list.size() > s){
                for(int i = 0; i < list.size() - s; i++){
                    list.poll();
                }
            }
            size = s;
        }
        
        public void add(Float f){
            if(list.size()>= size){
                list.poll();
            }
            list.add(f);
        }
        
        public Float getData(int index){
            return list.get(index);
        }
        
        public int getListSize(){
            return list.size();
        }
      
    }
}
