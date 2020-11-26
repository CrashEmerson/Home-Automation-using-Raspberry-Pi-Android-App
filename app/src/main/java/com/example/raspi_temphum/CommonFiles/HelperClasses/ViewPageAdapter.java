package com.example.raspi_temphum.CommonFiles.HelperClasses;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.raspi_temphum.R;

public class ViewPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    int[] image ={R.drawable.onboardpic01,R.drawable.onboardpic02,R.drawable.onboardpic03};
    int[] heading = {R.string.onboardpic01_heading1,R.string.onboardpic01_heading2,R.string.onboardpic01_heading3};
    int[] description = {R.string.onboarding_dummyText,R.string.onboarding_dummyText,R.string.onboarding_dummyText};

    public ViewPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_onboarding, container, false);

        ImageView onboardImage = view.findViewById(R.id.onBoardingImage);
        TextView onboardHeading = view.findViewById(R.id.onBoardingHeading);
        TextView onboardDescription = view.findViewById(R.id.onBoardingDescription);

        onboardImage.setImageResource(image[position]);
        onboardHeading.setText(heading[position]);
        onboardDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
