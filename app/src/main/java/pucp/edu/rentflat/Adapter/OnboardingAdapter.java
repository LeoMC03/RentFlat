package pucp.edu.rentflat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import pucp.edu.rentflat.R;

public class OnboardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    int tittles[] = {
            R.string.tittle1,
            R.string.tittle2,
            R.string.tittle3,
    };

    int tittlesSub[] = {
            R.string.tittleSub1,
            R.string.tittleSub2,
            R.string.tittleSub3,
    };


    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return tittles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_onboarding, container,false);

        TextView textViewTitle = view.findViewById(R.id.tvTittle);
        TextView textViewSubTitle = view.findViewById(R.id.tvSubTittle);

        textViewTitle.setText(tittles[position]);
        textViewSubTitle.setText(tittlesSub[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
