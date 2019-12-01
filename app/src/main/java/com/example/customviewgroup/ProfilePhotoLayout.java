package com.example.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Based on this <a href="https://sriramramani.wordpress.com/2015/05/06/custom-viewgroups/">tutorial</a>.
 */
public class ProfilePhotoLayout extends ViewGroup {

  private View photo;
  private View menu;
  private TextView title;
  private TextView subtitle;

  public ProfilePhotoLayout(Context context) {
    this(context, null);
  }

  public ProfilePhotoLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    inflate(context, R.layout.profile_photo, this);

    photo = findViewById(R.id.photo);
    title = findViewById(R.id.title);
    subtitle = findViewById(R.id.subtitle);
    menu = findViewById(R.id.menu);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 1. Setup initial constraints.
    int widthUsed = getPaddingLeft() + getPaddingRight();
    int heightUsed = getPaddingTop() + getPaddingBottom();
    int width = 0;
    int height = 0;

    // 2. Measure the photo view
    measureChildWithMargins(
        photo,
        widthMeasureSpec,
        widthUsed,
        heightMeasureSpec,
        heightUsed);

    // 3. Update the constraints with photos view.
    widthUsed += getMeasuredWidthWithMargins(photo);
    width += getMeasuredWidthWithMargins(photo);
    height = Math.max(getMeasuredHeightWithMargins(photo), height);

    // 4. Measure the menu view
    measureChildWithMargins(
        menu,
        widthMeasureSpec,
        widthUsed,
        heightMeasureSpec,
        heightUsed);

    // 5. Update the constraints with menu view.
    widthUsed += getMeasuredWidthWithMargins(menu);
    width += getMeasuredWidthWithMargins(menu);
    height = Math.max(getMeasuredHeightWithMargins(menu), height);

    // 6. Prepare the MeasureSpec for the title area
    int titleAreaWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
        MeasureSpec.getSize(widthMeasureSpec) - widthUsed,
        MeasureSpec.getMode(widthMeasureSpec));
    int titleAreaHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
        MeasureSpec.getSize(heightMeasureSpec) - heightUsed,
        MeasureSpec.getMode(heightMeasureSpec));

    // 7. Measure the Title.
    measureChildWithMargins(
        title,
        titleAreaWidthMeasureSpec,
        0,
        titleAreaHeightMeasureSpec,
        0);

    // 8. Measure the Subtitle.
    measureChildWithMargins(
        subtitle,
        titleAreaWidthMeasureSpec,
        0,
        titleAreaHeightMeasureSpec,
        getMeasuredHeightWithMargins(title));

    // 9. Update the sizes.
    width += Math.max(getMeasuredWidthWithMargins(title), getMeasuredWidthWithMargins(subtitle));
    height = Math
        .max(getMeasuredHeightWithMargins(title) + getMeasuredHeightWithMargins(subtitle), height);

    // 10. Set the dimension for this ViewGroup.
    setMeasuredDimension(
        resolveSize(width, widthMeasureSpec),
        resolveSize(height, heightMeasureSpec));
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(), attrs);
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int height = b - t;
    final int width = r - l;

    final MarginLayoutParams photoLp = getLayoutParams(photo);
    layoutChild(photo, getPaddingLeft() + photoLp.leftMargin,
        getPaddingTop() + height / 2 - photo.getMeasuredHeight() / 2);

    final int titleLeft = photo.getRight() + photoLp.rightMargin;
    final MarginLayoutParams titleLp = getLayoutParams(title);
    layoutChild(title, titleLeft, getPaddingTop() + titleLp.topMargin);
    final MarginLayoutParams subtitleLp = getLayoutParams(subtitle);
    layoutChild(subtitle, titleLeft,
        title.getBottom() + titleLp.bottomMargin + subtitleLp.topMargin);

    final MarginLayoutParams menuLp = getLayoutParams(menu);
    layoutChild(menu, width - menu.getMeasuredWidth() - menuLp.leftMargin,
        getPaddingTop() + height / 2 - menu.getMeasuredHeight() / 2);

  }

  void layoutChild(View view, int left, int top) {
    view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
  }

  private int getMeasuredWidthWithMargins(View view) {
    final MarginLayoutParams lp = getLayoutParams(view);
    return view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
  }

  private int getMeasuredHeightWithMargins(View view) {
    final MarginLayoutParams lp = getLayoutParams(view);
    return view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
  }

  private static MarginLayoutParams getLayoutParams(View view) {
    return (MarginLayoutParams) view.getLayoutParams();
  }
}
