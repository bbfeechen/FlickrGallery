package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import kailianc.andrew.cmu.edu.flickrgallery.R;


/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class GraphView extends View {
	private static final int HISTOGRAM_DATA_SIZE = 128;
	private static final float HISTOGRAM_DISPLAY_PERCENTAGE = 1.0f;
	private static final int H_BORDER = 1;
	private static final int V_BORDER = 2;
	private static final int OPAQUE_BLACK = 0xFF000000;
	private static final int HISTOGRAM_RESIZE_MAX_VALUE = 2000;
	
	private static final float HISTOGRAM_DISPLAY_MAX_VALUE =
			(HISTOGRAM_RESIZE_MAX_VALUE * 2 * HISTOGRAM_DISPLAY_PERCENTAGE);

	private int mColor;
	private int[] mData;
	private Paint paint;

	/**
	 * Constructor with paramters {@code Context} and {@code AttributeSet}<br>
	 * 
	 * @param context   interface to application specific resources<br>
	 * @param attrs     attribute set of custom view<p>
	 */
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.GraphView);
		mColor = attr.getColor(R.styleable.GraphView_histocolor, OPAQUE_BLACK);
		paint = new Paint();
		paint.setColor(mColor);
	}

	@Override
	protected void onDetachedFromWindow(){
		mData = null;
		super.onDetachedFromWindow();
	}
	
	/**
	 * Receive the histogram data and refresh the display.<br>
	 * 
	 * @param y    histogram data obtained from Platform.<p>
	 */
	public void setHistogram(int[] y) {
		mData = y;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if(mData != null) {
			int width = getWidth();
			int height = getHeight();
			int graphheight = height - 2 * V_BORDER;
			int pinWidth = width / HISTOGRAM_DATA_SIZE;
			
			int l = H_BORDER;
			int b = height - V_BORDER;
			int index, workData;
			float h;
			for (int i = 0; i < HISTOGRAM_DATA_SIZE; i++) {
				index = i << 1;
				workData = mData[index] + mData[index + 1];
				if (workData > HISTOGRAM_DISPLAY_MAX_VALUE) {
					workData = (int)HISTOGRAM_DISPLAY_MAX_VALUE;
				}
	
				h = (float) (graphheight * workData / HISTOGRAM_DISPLAY_MAX_VALUE);
				if(h > graphheight) {
					h = graphheight;
				}
	
				canvas.drawRect(
						l, 
						b - h, 
						l + pinWidth, 
						b, 
						paint);
				l += pinWidth;
			}
		}
	}
}
