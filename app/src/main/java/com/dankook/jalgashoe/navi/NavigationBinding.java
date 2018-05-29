package com.dankook.jalgashoe.navi;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.BitmapUtil;

public class NavigationBinding {

    public static final int TURN_TYPE_DEPT = 200; // 출발지
    public static final int TURN_TYPE_DEST = 201; // 목적지

    public static final int TURN_TYPE_STRAIGHT = 11; // 직진

    public static final int TURN_TYPE_LEFT = 12; // 좌회전
    public static final int TURN_TYPE_LEFT_8 = 16;
    public static final int TURN_TYPE_LEFT_10 = 17;

    public static final int TURN_TYPE_RIGHT = 13; // 우회전
    public static final int TURN_TYPE_RIGHT_2 = 18;
    public static final int TURN_TYPE_RIGHT_4 = 19;

    public static final int TURN_TYPE_U = 14; // 유턴

    public static final int TURN_TYPE_BRIDGE = 125; // 육교
    public static final int TURN_TYPE_UNDERGROUND = 126; // 지하보도
    public static final int TURN_TYPE_STAIRS = 127; // 계단
    public static final int TURN_TYPE_SLOPE = 128; // 경사로

    public static final int TURN_TYPE_CROSS_WALK = 211; // 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_LEFT = 212; // 좌측 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_RIGHT = 213; // 우측 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_LEFT_8 = 214; // 8시방향 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_LEFT_10 = 215; // 10시방향 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_RIGHT_2 = 216; // 2시방향 횡단보도
    public static final int TURN_TYPE_CROSS_WALK_RIGHT_4 = 217; // 4시방향 횡단보도

    @BindingAdapter(value = {"turnType"})
    public static void bindTurnType(ImageView imageView, int turnType){
        switch (turnType){
            case TURN_TYPE_LEFT:
                imageView.setImageResource(R.drawable.ic_turn_left);
                break;
            case TURN_TYPE_LEFT_8:
                imageView.setImageResource(R.drawable.ic_turn_left_8);
                break;
            case TURN_TYPE_LEFT_10:
                imageView.setImageResource(R.drawable.ic_turn_left_10);
                break;
            case TURN_TYPE_RIGHT:
                imageView.setImageResource(R.drawable.ic_turn_right);
                break;
            case TURN_TYPE_RIGHT_2:
                imageView.setImageResource(R.drawable.ic_turn_right_2);
                break;
            case TURN_TYPE_RIGHT_4:
                imageView.setImageResource(R.drawable.ic_turn_right_2);
                break;
            case TURN_TYPE_STRAIGHT:
                imageView.setImageResource(R.drawable.ic_turn_straight);
                break;
            case TURN_TYPE_CROSS_WALK_LEFT:
                imageView.setImageResource(R.drawable.ic_cross_left);
                break;
            case TURN_TYPE_CROSS_WALK_LEFT_8:
                imageView.setImageResource(R.drawable.ic_cross_left_8);
                break;
            case TURN_TYPE_CROSS_WALK_LEFT_10:
                imageView.setImageResource(R.drawable.ic_cross_left_10);
                break;
            case TURN_TYPE_CROSS_WALK_RIGHT:
                imageView.setImageResource(R.drawable.ic_cross_right);
                break;
            case TURN_TYPE_CROSS_WALK_RIGHT_2:
                imageView.setImageResource(R.drawable.ic_cross_right_2);
                break;
            case TURN_TYPE_CROSS_WALK_RIGHT_4:
                imageView.setImageResource(R.drawable.ic_cross_right_2);
                break;
            case TURN_TYPE_CROSS_WALK:
                imageView.setImageResource(R.drawable.ic_cross_straight);
                break;
            case TURN_TYPE_U:
                imageView.setImageResource(R.drawable.ic_turn_u);
                break;
            case TURN_TYPE_DEPT:
                Bitmap startIcon = BitmapUtil.writeTextOnDrawable(imageView.getResources(), R.drawable.ic_location_active, 130, "출발");
                imageView.setImageBitmap(startIcon);
                break;
            case TURN_TYPE_DEST:
                Bitmap endIcon = BitmapUtil.writeTextOnDrawable(imageView.getResources(), R.drawable.ic_marker_destination, 130, "도착");
                imageView.setImageBitmap(endIcon);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_turn_straight);
                break;
        }
    }
}
