package gachon.mpclass.brainstorm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Draw extends View {
    private Paint paint = new Paint();

    private Mindmap activity;

    public Draw(Mindmap context) {
        super(context);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        activity = context;
    }


    //x좌표 구하기
    private static int getRelative_x(View view) {

        if (view.getParent() == view.getRootView())
            return view.getLeft();
        else
            return view.getLeft() + getRelative_x((View) view.getParent());
    }

    //y좌표 구하기
    private static int getRelative_y(View view) {
        if (view.getParent() == view.getRootView())
            return view.getTop();
        else
            return view.getTop() + getRelative_y((View) view.getParent());
    }

    //노드 선으로 연결하기
    private void connect_node(Canvas canvas, NodeFragment fragment) {

        int barHeight = activity.getBarsHeight();

        View parentView = fragment.getView();
        if (parentView == null) {
            return;
        }

        parentView = parentView.findViewById(R.id.node_img);
        if (parentView == null) {
            return;
        }

        if (fragment.onAddToLayout != null) {

            fragment.onAddToLayout.run();
            fragment.onAddToLayout = null;

        }

        int[] parentLocation = new int[]{getRelative_x(parentView), getRelative_y(parentView)};

        parentLocation[0] += parentView.getWidth() / 2;
        parentLocation[1] += parentView.getHeight() / 2;

        for (Node child : fragment.node.child_node) {
            View childView = child.fragment.getView();
            if (childView == null) {
                continue;
            }

            childView = childView.findViewById(R.id.node_img);
            if (childView == null) {
                continue;
            }

            int[] childLocation = new int[]{getRelative_x(childView), getRelative_y(childView)};
            childLocation[0] += childView.getWidth() / 2;
            childLocation[1] += childView.getHeight() / 2;

            canvas.drawLine(parentLocation[0], parentLocation[1] - barHeight, childLocation[0], childLocation[1] - barHeight, paint);

            connect_node(canvas, child.fragment);

        }
    }

    public void onDraw(Canvas canvas) {
        if (activity.getNodeFragments().size() > 0) {

            connect_node(canvas, activity.getNodeFragments().get(0));
        }
        invalidate();
    }

}
